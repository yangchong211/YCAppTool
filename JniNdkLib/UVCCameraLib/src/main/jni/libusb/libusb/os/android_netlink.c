/* -*- Mode: C; c-basic-offset:8 ; indent-tabs-mode:t -*- */
/*
 * non-rooted Android usbfs backend for libusb
 * Copyright (C) 2007-2009 Daniel Drake <dsd@gentoo.org>
 * Copyright (c) 2001 Johannes Erdfelt <johannes@erdfelt.com>
 * Copyright (c) 2013 Nathan Hjelm <hjelmn@mac.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

#define LOG_TAG "libusb/netlink"
#if 0	// デバッグ情報を出さない時1
	#ifndef LOG_NDEBUG
		#define	LOG_NDEBUG		// LOGV/LOGD/MARKを出力しない時
		#endif
	#undef USE_LOGALL			// 指定したLOGxだけを出力
#else
	#define USE_LOGALL
	#undef LOG_NDEBUG
	#undef NDEBUG
	#define GET_RAW_DESCRIPTOR
#endif

#include "config.h"
#include "libusb.h"
#include "libusbi.h"
#include "android_usbfs.h"

#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <poll.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>

#ifdef HAVE_ASM_TYPES_H
#include <asm/types.h>
#endif

#ifdef HAVE_SYS_SOCKET_H
#include <sys/socket.h>
#endif

#include <arpa/inet.h>

#ifdef HAVE_LINUX_NETLINK_H
#include <linux/netlink.h>
#endif

#ifdef HAVE_LINUX_FILTER_H
#include <linux/filter.h>
#endif

#define KERNEL 1

static int android_netlink_socket = -1;
static int netlink_control_pipe[2] = { -1, -1 };
static pthread_t libusb_android_event_thread;

static void *android_netlink_event_thread_main(void *arg);

struct sockaddr_nl snl = { .nl_family=AF_NETLINK, .nl_groups=KERNEL };

static int set_fd_cloexec_nb (int fd)
{
	int flags;

#if defined(FD_CLOEXEC)
	flags = fcntl (android_netlink_socket, F_GETFD);
	if (0 > flags) {
		return -1;
	}

	if (!(flags & FD_CLOEXEC)) {
		fcntl (android_netlink_socket, F_SETFD, flags | FD_CLOEXEC);
	}
#endif

	flags = fcntl (android_netlink_socket, F_GETFL);
	if (0 > flags) {
		return -1;
	}

	if (!(flags & O_NONBLOCK)) {
		fcntl (android_netlink_socket, F_SETFL, flags | O_NONBLOCK);
	}

	return 0;
}

int android_netlink_start_event_monitor(void)
{
	ENTER();
	int socktype = SOCK_RAW;
	int ret;

	snl.nl_groups = KERNEL;

#if defined(SOCK_CLOEXEC)
	socktype |= SOCK_CLOEXEC;
#endif
#if defined(SOCK_NONBLOCK)
	socktype |= SOCK_NONBLOCK;
#endif

	android_netlink_socket = socket(PF_NETLINK, socktype, NETLINK_KOBJECT_UEVENT);
	if (-1 == android_netlink_socket && EINVAL == errno) {
		android_netlink_socket = socket(PF_NETLINK, SOCK_RAW, NETLINK_KOBJECT_UEVENT);
	}

	if (-1 == android_netlink_socket) {
		LOGE("failed to create android_netlink_socket:errno=%d", errno);	// 13:Permission deniedが返ってくる
		RETURN(LIBUSB_ERROR_OTHER, int);
	}

	ret = set_fd_cloexec_nb (android_netlink_socket);
	if (0 != ret) {
		close (android_netlink_socket);
		android_netlink_socket = -1;
		RETURN(LIBUSB_ERROR_OTHER, int);
	}

	ret = bind(android_netlink_socket, (struct sockaddr *) &snl, sizeof(snl));
	if (0 != ret) {
		close(android_netlink_socket);
		RETURN(LIBUSB_ERROR_OTHER, int);
	}

	/* TODO -- add authentication */
	/* setsockopt(android_netlink_socket, SOL_SOCKET, SO_PASSCRED, &one, sizeof(one)); */

	ret = usbi_pipe(netlink_control_pipe);
	if (ret) {
		LOGE("could not create netlink control pipe");
		usbi_err(NULL, "could not create netlink control pipe");
	        close(android_netlink_socket);
		RETURN(LIBUSB_ERROR_OTHER, int);
	}

	ret = pthread_create(&libusb_android_event_thread, NULL, android_netlink_event_thread_main, NULL);
	if (0 != ret) {
		close(netlink_control_pipe[0]);
        close(netlink_control_pipe[1]);
        close(android_netlink_socket);
		RETURN(LIBUSB_ERROR_OTHER, int);
	}

	RETURN(LIBUSB_SUCCESS, int);
}

int android_netlink_stop_event_monitor(void)
{
	int r;
	char dummy = 1;

	if (-1 == android_netlink_socket) {
		/* already closed. nothing to do */
		return LIBUSB_SUCCESS;
	}

	/* Write some dummy data to the control pipe and
	 * wait for the thread to exit */
	r = usbi_write(netlink_control_pipe[1], &dummy, sizeof(dummy));
	if (r <= 0) {
		usbi_warn(NULL, "netlink control pipe signal failed");
	}
	pthread_join(libusb_android_event_thread, NULL);

	close(android_netlink_socket);
	android_netlink_socket = -1;

	/* close and reset control pipe */
	close(netlink_control_pipe[0]);
	close(netlink_control_pipe[1]);
	netlink_control_pipe[0] = -1;
	netlink_control_pipe[1] = -1;

	return LIBUSB_SUCCESS;
}

static const char *netlink_message_parse (const char *buffer, size_t len, const char *key)
{
	size_t keylen = strlen(key);
	size_t offset;

	for (offset = 0 ; offset < len && '\0' != buffer[offset] ; offset += strlen(buffer + offset) + 1) {
		if (0 == strncmp(buffer + offset, key, keylen) &&
		    '=' == buffer[offset + keylen]) {
			return buffer + offset + keylen + 1;
		}
	}

	return NULL;
}

/* parse parts of netlink message common to both libudev and the kernel */
static int android_netlink_parse(char *buffer, size_t len, int *detached, const char **sys_name,
			       uint8_t *busnum, uint8_t *devaddr) {
	const char *tmp;
	int i;

	errno = 0;

	*sys_name = NULL;
	*detached = 0;
	*busnum   = 0;
	*devaddr  = 0;

	tmp = netlink_message_parse((const char *) buffer, len, "ACTION");
	if (tmp == NULL)
		return -1;
	if (0 == strcmp(tmp, "remove")) {
		*detached = 1;
	} else if (0 == strcmp(tmp, "add")) {
		// pass through
	} else if (0 != strcmp(tmp, "change")) {
		usbi_dbg("unknown device action [%s]", tmp);
		return -1;
	}

	/* check that this is a usb message */
	tmp = netlink_message_parse(buffer, len, "SUBSYSTEM");
	if (NULL == tmp || 0 != strcmp(tmp, "usb")) {
		/* not usb. ignore */
		return -1;
	}

	tmp = netlink_message_parse(buffer, len, "BUSNUM");
	if (NULL == tmp) {
		/* no bus number. try "DEVICE" */
		tmp = netlink_message_parse(buffer, len, "DEVICE");
		if (NULL == tmp) {
			/* not usb. ignore */
			return -1;
		}
		
		/* Parse a device path such as /dev/bus/usb/003/004 */
		char *pLastSlash = (char*)strrchr(tmp,'/');
		if(NULL == pLastSlash) {
			return -1;
		}

		*devaddr = strtoul(pLastSlash + 1, NULL, 10);
		if (errno) {
			errno = 0;
			return -1;
		}
		
		*busnum = strtoul(pLastSlash - 3, NULL, 10);
		if (errno) {
			errno = 0;
			return -1;
		}
		
		return 0;
	}

	*busnum = (uint8_t)(strtoul(tmp, NULL, 10) & 0xff);
	if (errno) {
		errno = 0;
		return -1;
	}

	tmp = netlink_message_parse(buffer, len, "DEVNUM");
	if (NULL == tmp) {
		return -1;
	}

	*devaddr = (uint8_t)(strtoul(tmp, NULL, 10) & 0xff);
	if (errno) {
		errno = 0;
		return -1;
	}

	tmp = netlink_message_parse(buffer, len, "DEVPATH");
	if (NULL == tmp) {
		return -1;
	}

	for (i = strlen(tmp) - 1 ; i ; --i) {
		if ('/' ==tmp[i]) {
			*sys_name = tmp + i + 1;
			break;
		}
	}

	/* found a usb device */
	return 0;
}

static int android_netlink_read_message(void)
{
	char buffer[1024];	// XXX changed from unsigned char to char because the first argument of android_netlink_parse is char *
	struct iovec iov = {.iov_base = buffer, .iov_len = sizeof(buffer)};
	struct msghdr meh = { .msg_iov=&iov, .msg_iovlen=1,
			     .msg_name=&snl, .msg_namelen=sizeof(snl) };
	const char *sys_name = NULL;
	uint8_t busnum, devaddr;
	int detached, r;
	size_t len;

	/* read netlink message */
	memset(buffer, 0, sizeof(buffer));
	len = recvmsg(android_netlink_socket, &meh, 0);
	if (len < 32) {
		if (errno != EAGAIN)
			usbi_dbg("error recieving message from netlink");
		return -1;
	}

	/* TODO -- authenticate this message is from the kernel or udevd */

	r = android_netlink_parse(buffer, len, &detached, &sys_name, &busnum, &devaddr);
	if (r)
		return r;

	usbi_dbg("netlink hotplug found device busnum: %hhu, devaddr: %hhu, sys_name: %s, removed: %s",
		 busnum, devaddr, sys_name, detached ? "yes" : "no");

	/* signal device is available (or not) to all contexts */
	if (detached)
		android_device_disconnected(busnum, devaddr, sys_name);
	else
		android_hotplug_enumerate(busnum, devaddr, sys_name);

	return 0;
}

static void *android_netlink_event_thread_main(void *arg)
{
	char dummy;
	int r;
	struct pollfd fds[] = {
		{ .fd = netlink_control_pipe[0],
		  .events = POLLIN },
		{ .fd = android_netlink_socket,
		  .events = POLLIN },
	};

	/* silence compiler warning */
	(void) arg;

	while (poll(fds, 2, -1) >= 0) {
		if (fds[0].revents & POLLIN) {
			/* activity on control pipe, read the byte and exit */
			r = usbi_read(netlink_control_pipe[0], &dummy, sizeof(dummy));
			if (r <= 0) {
				usbi_warn(NULL, "netlink control pipe read failed");
			}
			break;
		}
		if (fds[1].revents & POLLIN) {
        		usbi_mutex_static_lock(&android_hotplug_lock);
	        	android_netlink_read_message();
	        	usbi_mutex_static_unlock(&android_hotplug_lock);
		}
	}

	return NULL;
}

void android_netlink_hotplug_poll(void)
{
	int r;

	usbi_mutex_static_lock(&android_hotplug_lock);
	do {
		r = android_netlink_read_message();
	} while (r == 0);
	usbi_mutex_static_unlock(&android_hotplug_lock);
}
