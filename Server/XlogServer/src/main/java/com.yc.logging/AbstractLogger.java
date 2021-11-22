package com.yc.logging;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractLogger implements Logger {

    protected String mName;

    private HeaderType mHeaderType = HeaderType.SHORT;

    public AbstractLogger(final String name) {
        this.mName = name;
    }

    public AbstractLogger(final Class<?> clazz) {
        this.mName = clazz.getName();
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        if (str == null || str.length() == 0) {
            return;
        }
        this.mName = str;
    }

    public boolean isTraceEnabled() {
        return true;
    }

    public boolean isDebugEnabled() {
        return true;
    }

    public boolean isInfoEnabled() {
        return true;
    }

    public boolean isWarnEnabled() {
        return true;
    }

    public boolean isErrorEnabled() {
        return true;
    }

    @Deprecated
    public HeaderType getHeaderType() {
        return this.mHeaderType;
    }

    @Deprecated
    public void setHeaderType(final HeaderType type) {
        this.mHeaderType = type;
    }

    public void traceEvent(final String event, final Object... args) {
        traceEvent(event, toMap(args));
    }

    public void debugEvent(final String event, final Object... args) {
        debugEvent(event, toMap(args));
    }

    public void infoEvent(final String event, final Object... args) {
        infoEvent(event, toMap(args));
    }

    public void warnEvent(final String event, final Object... args) {
        warnEvent(event, toMap(args));
    }

    public void errorEvent(final String event, final Object... args) {
        errorEvent(event, toMap(args));
    }

    protected static Map<?, ?> toMap(final Object... args) {
        if (args == null) return null;

        final Map<Object, Object> map = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            map.put(args[i], args[i + 1]);
        }
        return map;
    }

    public String formatEvent(final Level level, final String event, final Map<?, ?> map) {
//        final HeaderType headerType = getHeaderType();
        final StringBuilder msg = new StringBuilder();

//        if (headerType == HeaderType.LONG) {
//            final StackTraceElement element = new Throwable().getStackTrace()[2];
//            msg.append("[").append(element.getFileName())
//                    .append(":").append(element.getLineNumber())
//                    .append(":").append(element.getClassName())
//                    .append(":").append(element.getMethodName())
//                    .append("]");
//        }

        msg.append(" ").append(null != event ? event : "_undef");

        if (map == null || map.isEmpty()) {
            msg.append("||_msg=null");
        } else {
            for (final Map.Entry<?, ?> entry : map.entrySet()) {
                msg.append("||").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        return msg.toString();
    }


    protected static Map<?, ?> mapCopy(Map<?, ?> from) {
        return from == null ? null : new HashMap<>(from);
    }
}
