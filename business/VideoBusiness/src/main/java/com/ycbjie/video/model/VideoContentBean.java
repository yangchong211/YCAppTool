package com.ycbjie.video.model;

import java.util.List;


public class VideoContentBean {

    /**
     * data : {"status":10,"user_id":"toutiao","video_id":"f2aeddda2a894e53bb3f2cf98994aadb","big_thumbs":[{"img_num":16,"img_url":"https://p1.pstatp.com/origin/19cc000499f3a6986d59","img_x_size":160,"img_y_size":90,"img_x_len":1,"img_y_len":16}],"video_duration":84.8,"video_list":{"video_1":{"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNWE2YzA4ZmJjZWQxZmIzY2ZhMDdkZGE3Zjg0ZTNkZDUvNThkZGJlMjAvdmlkZW8vbS8xMTQ0YzNiMDAwMDBjMWU1MWIwMWQ1ZjIyMGE0MzMyMTUyNGQwOTQ0MjU5MDI5NmU1NDI3Yjc5NGNlLw==","bitrate":369405,"definition":"360p","main_url":"aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzExNDRjM2IwMDAwMGMxZTUxYjAxZDVmMjIwYTQzMzIxNTI0ZDA5NDQyNTkwMjk2ZTU0MjdiNzk0Y2UvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9WWNmakZDNnMxSHFhQ0NxeVZMd3ZkRWNlcXg0JTNE","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":4630326,"socket_buffer":221643000,"user_video_proxy":1,"vheight":360,"vtype":"mp4","vwidth":640},"video_2":{"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNTk0ZTZjNDZjMjliZDkxY2EwZWJhMTFkM2RlMGIxN2EvNThkZGJlMjAvdmlkZW8vbS8yMjBlNmU2MjY3Mjg4NDU0YzkwOTFjOGYyMTZlZThiOWEwMjExNDRiZjAwMDAwMDZmNTE3YjkxMTk1Lw==","bitrate":577524,"definition":"480p","main_url":"aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzIyMGU2ZTYyNjcyODg0NTRjOTA5MWM4ZjIxNmVlOGI5YTAyMTE0NGJmMDAwMDAwNmY1MTdiOTExOTUvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9JTJGbEtPMld4QVVtcUtqUGo4TWw5cFpxaFNSeEklM0Q=","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":6836189,"socket_buffer":346514400,"user_video_proxy":1,"vheight":480,"vtype":"mp4","vwidth":854},"video_3":{"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNzM4NTJiOTYxNDgyMzc0MDY2NWIyN2VkODZhNWVhMTEvNThkZGJlMjAvdmlkZW8vbS8yMjA0NTViMDUyMjFjYTg0MDAxOWFjZjkwZDNmZGFmZTNhZDExNDQ4ZGMwMDAwMjRkOTFkNmZkNzZhLw==","bitrate":1274484,"definition":"720p","main_url":"aHR0cDovL3YzLjM2NXlnLmNvbS83Mzg1MmI5NjE0ODIzNzQwNjY1YjI3ZWQ4NmE1ZWExMS81OGRkYmUyMC92aWRlby9tLzIyMDQ1NWIwNTIyMWNhODQwMDE5YWNmOTBkM2ZkYWZlM2FkMTE0NDhkYzAwMDAyNGQ5MWQ2ZmQ3NmEv","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":14223659,"socket_buffer":764690400,"user_video_proxy":1,"vheight":720,"vtype":"mp4","vwidth":1280}}}
     * message : success
     * code : 0
     * total : 3
     */

    private DataBean data;
    private String message;
    private int code;
    private int total;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class DataBean {
        /**
         * status : 10
         * user_id : toutiao
         * video_id : f2aeddda2a894e53bb3f2cf98994aadb
         * big_thumbs : [{"img_num":16,"img_url":"https://p1.pstatp.com/origin/19cc000499f3a6986d59","img_x_size":160,"img_y_size":90,"img_x_len":1,"img_y_len":16}]
         * video_duration : 84.8
         * video_list : {"video_1":{"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNWE2YzA4ZmJjZWQxZmIzY2ZhMDdkZGE3Zjg0ZTNkZDUvNThkZGJlMjAvdmlkZW8vbS8xMTQ0YzNiMDAwMDBjMWU1MWIwMWQ1ZjIyMGE0MzMyMTUyNGQwOTQ0MjU5MDI5NmU1NDI3Yjc5NGNlLw==","bitrate":369405,"definition":"360p","main_url":"aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzExNDRjM2IwMDAwMGMxZTUxYjAxZDVmMjIwYTQzMzIxNTI0ZDA5NDQyNTkwMjk2ZTU0MjdiNzk0Y2UvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9WWNmakZDNnMxSHFhQ0NxeVZMd3ZkRWNlcXg0JTNE","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":4630326,"socket_buffer":221643000,"user_video_proxy":1,"vheight":360,"vtype":"mp4","vwidth":640},"video_2":{"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNTk0ZTZjNDZjMjliZDkxY2EwZWJhMTFkM2RlMGIxN2EvNThkZGJlMjAvdmlkZW8vbS8yMjBlNmU2MjY3Mjg4NDU0YzkwOTFjOGYyMTZlZThiOWEwMjExNDRiZjAwMDAwMDZmNTE3YjkxMTk1Lw==","bitrate":577524,"definition":"480p","main_url":"aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzIyMGU2ZTYyNjcyODg0NTRjOTA5MWM4ZjIxNmVlOGI5YTAyMTE0NGJmMDAwMDAwNmY1MTdiOTExOTUvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9JTJGbEtPMld4QVVtcUtqUGo4TWw5cFpxaFNSeEklM0Q=","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":6836189,"socket_buffer":346514400,"user_video_proxy":1,"vheight":480,"vtype":"mp4","vwidth":854},"video_3":{"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNzM4NTJiOTYxNDgyMzc0MDY2NWIyN2VkODZhNWVhMTEvNThkZGJlMjAvdmlkZW8vbS8yMjA0NTViMDUyMjFjYTg0MDAxOWFjZjkwZDNmZGFmZTNhZDExNDQ4ZGMwMDAwMjRkOTFkNmZkNzZhLw==","bitrate":1274484,"definition":"720p","main_url":"aHR0cDovL3YzLjM2NXlnLmNvbS83Mzg1MmI5NjE0ODIzNzQwNjY1YjI3ZWQ4NmE1ZWExMS81OGRkYmUyMC92aWRlby9tLzIyMDQ1NWIwNTIyMWNhODQwMDE5YWNmOTBkM2ZkYWZlM2FkMTE0NDhkYzAwMDAyNGQ5MWQ2ZmQ3NmEv","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":14223659,"socket_buffer":764690400,"user_video_proxy":1,"vheight":720,"vtype":"mp4","vwidth":1280}}
         */

        private int status;
        private String user_id;
        private String video_id;
        private double video_duration;
        private VideoListBean video_list;
        private List<BigThumbsBean> big_thumbs;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public double getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(double video_duration) {
            this.video_duration = video_duration;
        }

        public VideoListBean getVideo_list() {
            return video_list;
        }

        public void setVideo_list(VideoListBean video_list) {
            this.video_list = video_list;
        }

        public List<BigThumbsBean> getBig_thumbs() {
            return big_thumbs;
        }

        public void setBig_thumbs(List<BigThumbsBean> big_thumbs) {
            this.big_thumbs = big_thumbs;
        }

        public static class VideoListBean {
            /**
             * video_1 : {"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNWE2YzA4ZmJjZWQxZmIzY2ZhMDdkZGE3Zjg0ZTNkZDUvNThkZGJlMjAvdmlkZW8vbS8xMTQ0YzNiMDAwMDBjMWU1MWIwMWQ1ZjIyMGE0MzMyMTUyNGQwOTQ0MjU5MDI5NmU1NDI3Yjc5NGNlLw==","bitrate":369405,"definition":"360p","main_url":"aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzExNDRjM2IwMDAwMGMxZTUxYjAxZDVmMjIwYTQzMzIxNTI0ZDA5NDQyNTkwMjk2ZTU0MjdiNzk0Y2UvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9WWNmakZDNnMxSHFhQ0NxeVZMd3ZkRWNlcXg0JTNE","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":4630326,"socket_buffer":221643000,"user_video_proxy":1,"vheight":360,"vtype":"mp4","vwidth":640}
             * video_2 : {"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNTk0ZTZjNDZjMjliZDkxY2EwZWJhMTFkM2RlMGIxN2EvNThkZGJlMjAvdmlkZW8vbS8yMjBlNmU2MjY3Mjg4NDU0YzkwOTFjOGYyMTZlZThiOWEwMjExNDRiZjAwMDAwMDZmNTE3YjkxMTk1Lw==","bitrate":577524,"definition":"480p","main_url":"aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzIyMGU2ZTYyNjcyODg0NTRjOTA5MWM4ZjIxNmVlOGI5YTAyMTE0NGJmMDAwMDAwNmY1MTdiOTExOTUvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9JTJGbEtPMld4QVVtcUtqUGo4TWw5cFpxaFNSeEklM0Q=","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":6836189,"socket_buffer":346514400,"user_video_proxy":1,"vheight":480,"vtype":"mp4","vwidth":854}
             * video_3 : {"backup_url_1":"aHR0cDovL3Y3LnBzdGF0cC5jb20vNzM4NTJiOTYxNDgyMzc0MDY2NWIyN2VkODZhNWVhMTEvNThkZGJlMjAvdmlkZW8vbS8yMjA0NTViMDUyMjFjYTg0MDAxOWFjZjkwZDNmZGFmZTNhZDExNDQ4ZGMwMDAwMjRkOTFkNmZkNzZhLw==","bitrate":1274484,"definition":"720p","main_url":"aHR0cDovL3YzLjM2NXlnLmNvbS83Mzg1MmI5NjE0ODIzNzQwNjY1YjI3ZWQ4NmE1ZWExMS81OGRkYmUyMC92aWRlby9tLzIyMDQ1NWIwNTIyMWNhODQwMDE5YWNmOTBkM2ZkYWZlM2FkMTE0NDhkYzAwMDAyNGQ5MWQ2ZmQ3NmEv","preload_interval":25,"preload_max_step":10,"preload_min_step":5,"preload_size":327680,"size":14223659,"socket_buffer":764690400,"user_video_proxy":1,"vheight":720,"vtype":"mp4","vwidth":1280}
             */

            private Video1Bean video_1;
            private Video2Bean video_2;
            private Video3Bean video_3;

            public Video1Bean getVideo_1() {
                return video_1;
            }

            public void setVideo_1(Video1Bean video_1) {
                this.video_1 = video_1;
            }

            public Video2Bean getVideo_2() {
                return video_2;
            }

            public void setVideo_2(Video2Bean video_2) {
                this.video_2 = video_2;
            }

            public Video3Bean getVideo_3() {
                return video_3;
            }

            public void setVideo_3(Video3Bean video_3) {
                this.video_3 = video_3;
            }

            public static class Video1Bean {
                /**
                 * backup_url_1 : aHR0cDovL3Y3LnBzdGF0cC5jb20vNWE2YzA4ZmJjZWQxZmIzY2ZhMDdkZGE3Zjg0ZTNkZDUvNThkZGJlMjAvdmlkZW8vbS8xMTQ0YzNiMDAwMDBjMWU1MWIwMWQ1ZjIyMGE0MzMyMTUyNGQwOTQ0MjU5MDI5NmU1NDI3Yjc5NGNlLw==
                 * bitrate : 369405
                 * definition : 360p
                 * main_url : aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzExNDRjM2IwMDAwMGMxZTUxYjAxZDVmMjIwYTQzMzIxNTI0ZDA5NDQyNTkwMjk2ZTU0MjdiNzk0Y2UvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9WWNmakZDNnMxSHFhQ0NxeVZMd3ZkRWNlcXg0JTNE
                 * preload_interval : 25
                 * preload_max_step : 10
                 * preload_min_step : 5
                 * preload_size : 327680
                 * size : 4630326
                 * socket_buffer : 221643000
                 * user_video_proxy : 1
                 * vheight : 360
                 * vtype : mp4
                 * vwidth : 640
                 */

                private String backup_url_1;
                private int bitrate;
                private String definition;
                private String main_url;
                private int preload_interval;
                private int preload_max_step;
                private int preload_min_step;
                private int preload_size;
                private int size;
                private int socket_buffer;
                private int user_video_proxy;
                private int vheight;
                private String vtype;
                private int vwidth;

                public String getBackup_url_1() {
                    return backup_url_1;
                }

                public void setBackup_url_1(String backup_url_1) {
                    this.backup_url_1 = backup_url_1;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public void setBitrate(int bitrate) {
                    this.bitrate = bitrate;
                }

                public String getDefinition() {
                    return definition;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public String getMain_url() {
                    return main_url;
                }

                public void setMain_url(String main_url) {
                    this.main_url = main_url;
                }

                public int getPreload_interval() {
                    return preload_interval;
                }

                public void setPreload_interval(int preload_interval) {
                    this.preload_interval = preload_interval;
                }

                public int getPreload_max_step() {
                    return preload_max_step;
                }

                public void setPreload_max_step(int preload_max_step) {
                    this.preload_max_step = preload_max_step;
                }

                public int getPreload_min_step() {
                    return preload_min_step;
                }

                public void setPreload_min_step(int preload_min_step) {
                    this.preload_min_step = preload_min_step;
                }

                public int getPreload_size() {
                    return preload_size;
                }

                public void setPreload_size(int preload_size) {
                    this.preload_size = preload_size;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public int getSocket_buffer() {
                    return socket_buffer;
                }

                public void setSocket_buffer(int socket_buffer) {
                    this.socket_buffer = socket_buffer;
                }

                public int getUser_video_proxy() {
                    return user_video_proxy;
                }

                public void setUser_video_proxy(int user_video_proxy) {
                    this.user_video_proxy = user_video_proxy;
                }

                public int getVheight() {
                    return vheight;
                }

                public void setVheight(int vheight) {
                    this.vheight = vheight;
                }

                public String getVtype() {
                    return vtype;
                }

                public void setVtype(String vtype) {
                    this.vtype = vtype;
                }

                public int getVwidth() {
                    return vwidth;
                }

                public void setVwidth(int vwidth) {
                    this.vwidth = vwidth;
                }
            }

            public static class Video2Bean {
                /**
                 * backup_url_1 : aHR0cDovL3Y3LnBzdGF0cC5jb20vNTk0ZTZjNDZjMjliZDkxY2EwZWJhMTFkM2RlMGIxN2EvNThkZGJlMjAvdmlkZW8vbS8yMjBlNmU2MjY3Mjg4NDU0YzkwOTFjOGYyMTZlZThiOWEwMjExNDRiZjAwMDAwMDZmNTE3YjkxMTk1Lw==
                 * bitrate : 577524
                 * definition : 480p
                 * main_url : aHR0cDovL3Y2LjM2NXlnLmNvbS92aWRlby9tLzIyMGU2ZTYyNjcyODg0NTRjOTA5MWM4ZjIxNmVlOGI5YTAyMTE0NGJmMDAwMDAwNmY1MTdiOTExOTUvP0V4cGlyZXM9MTQ5MDkzMDczNiZBV1NBY2Nlc3NLZXlJZD1xaDBoOVRkY0VNb1Myb1BqN2FLWCZTaWduYXR1cmU9JTJGbEtPMld4QVVtcUtqUGo4TWw5cFpxaFNSeEklM0Q=
                 * preload_interval : 25
                 * preload_max_step : 10
                 * preload_min_step : 5
                 * preload_size : 327680
                 * size : 6836189
                 * socket_buffer : 346514400
                 * user_video_proxy : 1
                 * vheight : 480
                 * vtype : mp4
                 * vwidth : 854
                 */

                private String backup_url_1;
                private int bitrate;
                private String definition;
                private String main_url;
                private int preload_interval;
                private int preload_max_step;
                private int preload_min_step;
                private int preload_size;
                private int size;
                private int socket_buffer;
                private int user_video_proxy;
                private int vheight;
                private String vtype;
                private int vwidth;

                public String getBackup_url_1() {
                    return backup_url_1;
                }

                public void setBackup_url_1(String backup_url_1) {
                    this.backup_url_1 = backup_url_1;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public void setBitrate(int bitrate) {
                    this.bitrate = bitrate;
                }

                public String getDefinition() {
                    return definition;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public String getMain_url() {
                    return main_url;
                }

                public void setMain_url(String main_url) {
                    this.main_url = main_url;
                }

                public int getPreload_interval() {
                    return preload_interval;
                }

                public void setPreload_interval(int preload_interval) {
                    this.preload_interval = preload_interval;
                }

                public int getPreload_max_step() {
                    return preload_max_step;
                }

                public void setPreload_max_step(int preload_max_step) {
                    this.preload_max_step = preload_max_step;
                }

                public int getPreload_min_step() {
                    return preload_min_step;
                }

                public void setPreload_min_step(int preload_min_step) {
                    this.preload_min_step = preload_min_step;
                }

                public int getPreload_size() {
                    return preload_size;
                }

                public void setPreload_size(int preload_size) {
                    this.preload_size = preload_size;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public int getSocket_buffer() {
                    return socket_buffer;
                }

                public void setSocket_buffer(int socket_buffer) {
                    this.socket_buffer = socket_buffer;
                }

                public int getUser_video_proxy() {
                    return user_video_proxy;
                }

                public void setUser_video_proxy(int user_video_proxy) {
                    this.user_video_proxy = user_video_proxy;
                }

                public int getVheight() {
                    return vheight;
                }

                public void setVheight(int vheight) {
                    this.vheight = vheight;
                }

                public String getVtype() {
                    return vtype;
                }

                public void setVtype(String vtype) {
                    this.vtype = vtype;
                }

                public int getVwidth() {
                    return vwidth;
                }

                public void setVwidth(int vwidth) {
                    this.vwidth = vwidth;
                }
            }

            public static class Video3Bean {
                /**
                 * backup_url_1 : aHR0cDovL3Y3LnBzdGF0cC5jb20vNzM4NTJiOTYxNDgyMzc0MDY2NWIyN2VkODZhNWVhMTEvNThkZGJlMjAvdmlkZW8vbS8yMjA0NTViMDUyMjFjYTg0MDAxOWFjZjkwZDNmZGFmZTNhZDExNDQ4ZGMwMDAwMjRkOTFkNmZkNzZhLw==
                 * bitrate : 1274484
                 * definition : 720p
                 * main_url : aHR0cDovL3YzLjM2NXlnLmNvbS83Mzg1MmI5NjE0ODIzNzQwNjY1YjI3ZWQ4NmE1ZWExMS81OGRkYmUyMC92aWRlby9tLzIyMDQ1NWIwNTIyMWNhODQwMDE5YWNmOTBkM2ZkYWZlM2FkMTE0NDhkYzAwMDAyNGQ5MWQ2ZmQ3NmEv
                 * preload_interval : 25
                 * preload_max_step : 10
                 * preload_min_step : 5
                 * preload_size : 327680
                 * size : 14223659
                 * socket_buffer : 764690400
                 * user_video_proxy : 1
                 * vheight : 720
                 * vtype : mp4
                 * vwidth : 1280
                 */

                private String backup_url_1;
                private int bitrate;
                private String definition;
                private String main_url;
                private int preload_interval;
                private int preload_max_step;
                private int preload_min_step;
                private int preload_size;
                private int size;
                private int socket_buffer;
                private int user_video_proxy;
                private int vheight;
                private String vtype;
                private int vwidth;

                public String getBackup_url_1() {
                    return backup_url_1;
                }

                public void setBackup_url_1(String backup_url_1) {
                    this.backup_url_1 = backup_url_1;
                }

                public int getBitrate() {
                    return bitrate;
                }

                public void setBitrate(int bitrate) {
                    this.bitrate = bitrate;
                }

                public String getDefinition() {
                    return definition;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public String getMain_url() {
                    return main_url;
                }

                public void setMain_url(String main_url) {
                    this.main_url = main_url;
                }

                public int getPreload_interval() {
                    return preload_interval;
                }

                public void setPreload_interval(int preload_interval) {
                    this.preload_interval = preload_interval;
                }

                public int getPreload_max_step() {
                    return preload_max_step;
                }

                public void setPreload_max_step(int preload_max_step) {
                    this.preload_max_step = preload_max_step;
                }

                public int getPreload_min_step() {
                    return preload_min_step;
                }

                public void setPreload_min_step(int preload_min_step) {
                    this.preload_min_step = preload_min_step;
                }

                public int getPreload_size() {
                    return preload_size;
                }

                public void setPreload_size(int preload_size) {
                    this.preload_size = preload_size;
                }

                public int getSize() {
                    return size;
                }

                public void setSize(int size) {
                    this.size = size;
                }

                public int getSocket_buffer() {
                    return socket_buffer;
                }

                public void setSocket_buffer(int socket_buffer) {
                    this.socket_buffer = socket_buffer;
                }

                public int getUser_video_proxy() {
                    return user_video_proxy;
                }

                public void setUser_video_proxy(int user_video_proxy) {
                    this.user_video_proxy = user_video_proxy;
                }

                public int getVheight() {
                    return vheight;
                }

                public void setVheight(int vheight) {
                    this.vheight = vheight;
                }

                public String getVtype() {
                    return vtype;
                }

                public void setVtype(String vtype) {
                    this.vtype = vtype;
                }

                public int getVwidth() {
                    return vwidth;
                }

                public void setVwidth(int vwidth) {
                    this.vwidth = vwidth;
                }
            }
        }

        public static class BigThumbsBean {
            /**
             * img_num : 16
             * img_url : https://p1.pstatp.com/origin/19cc000499f3a6986d59
             * img_x_size : 160
             * img_y_size : 90
             * img_x_len : 1
             * img_y_len : 16
             */

            private int img_num;
            private String img_url;
            private int img_x_size;
            private int img_y_size;
            private int img_x_len;
            private int img_y_len;

            public int getImg_num() {
                return img_num;
            }

            public void setImg_num(int img_num) {
                this.img_num = img_num;
            }

            public String getImg_url() {
                return img_url;
            }

            public void setImg_url(String img_url) {
                this.img_url = img_url;
            }

            public int getImg_x_size() {
                return img_x_size;
            }

            public void setImg_x_size(int img_x_size) {
                this.img_x_size = img_x_size;
            }

            public int getImg_y_size() {
                return img_y_size;
            }

            public void setImg_y_size(int img_y_size) {
                this.img_y_size = img_y_size;
            }

            public int getImg_x_len() {
                return img_x_len;
            }

            public void setImg_x_len(int img_x_len) {
                this.img_x_len = img_x_len;
            }

            public int getImg_y_len() {
                return img_y_len;
            }

            public void setImg_y_len(int img_y_len) {
                this.img_y_len = img_y_len;
            }
        }
    }
}
