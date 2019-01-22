package com.ycbjie.news.model;

import java.util.List;

/**
 * Created by PC on 2017/8/28.
 * 作者：PC
 */

public class TxNewsBean {


    /**
     * code : 200
     * msg : success
     * newslist : [{"ctime":"2017-08-09 00:31","title":"辟谣：九寨沟县地震部分谣言网上流传 请大家不信谣不传谣","description":"搜狐社会","picUrl":"http://photocdn.sohu.com/20170809/Img505992955_ss.jpeg","url":"http://news.sohu.com/20170809/n505992954.shtml"},{"ctime":"2017-06-07 10:12","title":"舍不得媳妇套不住流氓！警察带女友约会\u201c钓色魔\u201d","description":"搜狐社会","picUrl":"","url":"http://news.sohu.com/20170607/n495984947.shtml"},{"ctime":"2017-06-06 06:19","title":"专题 | 三分钟带你看遍苹果WWDC 2017亮点","description":"网易VR","picUrl":"http://img4.cache.netease.com/photo/0009/2017-06-06/s_CM7TROL26PGI0009.jpg","url":"http://tech.163.com/special/S1496632396292/"},{"ctime":"2017-06-06 07:41","title":"2400元的音箱贵不贵？苹果HomePod现场体验","description":"网易VR","picUrl":"http://img3.cache.netease.com/photo/0009/2017-06-06/s_CM7TRJ646PGI0009.jpg","url":"http://tech.163.com/photoview/6PGI0009/15180.html"},{"ctime":"2017-06-06 08:29","title":"苹果HomePod想占据你的家，但来得太迟了","description":"网易VR","picUrl":"http://cms-bucket.nosdn.127.net/b9bd4f9b5c6f4eee89ecaf71b641df7820170606083407.jpeg","url":"http://tech.163.com/17/0606/08/CM818M3600098GJ5.html"},{"ctime":"2017-06-02 09:43","title":"武汉理工大学两男生寝室斗殴 场面血腥(组图)","description":"搜狐社会","picUrl":"","url":"http://news.sohu.com/20170602/n495366391.shtml"},{"ctime":"2017-05-24 13:13","title":"费孝通民族研究的文化内涵","description":"搜狐社会","picUrl":"","url":"http://news.sohu.com/20170524/n494263638.shtml"},{"ctime":"2017-05-23 09:15","title":"机场：中国籍乘客回国如无身份证 须办临时证明","description":"搜狐社会","picUrl":"","url":"http://news.sohu.com/20170523/n494079084.shtml"},{"ctime":"2017-05-22 10:45","title":"为\u201c北京榜样\u201d圆梦","description":"搜狐社会","picUrl":"","url":"http://news.sohu.com/20170522/n493943677.shtml"},{"ctime":"2017-05-21 11:37","title":"南方都市报：英模表彰也是增进警民理解的一种方式","description":"搜狐社会","picUrl":"","url":"http://news.sohu.com/20170521/n493853366.shtml"}]
     */

    private int code;
    private String msg;
    /**
     * ctime : 2017-08-09 00:31
     * title : 辟谣：九寨沟县地震部分谣言网上流传 请大家不信谣不传谣
     * description : 搜狐社会
     * picUrl : http://photocdn.sohu.com/20170809/Img505992955_ss.jpeg
     * url : http://news.sohu.com/20170809/n505992954.shtml
     */

    private List<NewslistBean> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewslistBean> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistBean> newslist) {
        this.newslist = newslist;
    }

    public static class NewslistBean {
        private String ctime;
        private String title;
        private String description;
        private String picUrl;
        private String url;

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
