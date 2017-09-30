package com.ns.yc.lifehelper.ui.other.myTsSc.bean;

import java.util.List;

/**
 * Created by PC on 2017/8/29.
 * 作者：PC
 */
public class TangShiBean {

    /**
     * status : 0
     * msg : ok
     * result : {"total":"2","pagenum":"1","pagesize":"1","list":[{"title":"芙蓉楼送辛渐","type":"七言绝句","content":"寒雨连江夜入吴，平明送客楚山孤。<br />洛阳亲友如相问，一片冰心在玉壶。","explanation":"<p>１、芙蓉楼：原址在今江苏省镇江市西北。<br />２、楚山：古时吴、楚两地相接，镇江一带也称楚地，故其附近的山也可叫楚山。<\/p>","translation":"<p>迷蒙的烟雨，连夜洒遍吴地江天；<br />清晨送走你，孤对楚山离愁无限！<br />朋友呵，洛阳亲友若是问起我来；<br />就说我依然冰心玉壶，坚守信念！<\/p>","appreciation":"<p>这是一首送别诗。诗的构思新颖，淡写朋友的离情别绪，重写自己的高风亮节。首两句苍茫的江雨和孤峙的楚山，烘托送别时的孤寂之情；后两句自比冰壶，表达自己开郎胸怀和坚强性格。全诗即景生情，寓情于景，含蓄蕴藉，韵味无穷。 <\/p>","author":"王昌龄"}]}
     */

    private String status;
    private String msg;
    /**
     * total : 2
     * pagenum : 1
     * pagesize : 1
     * list : [{"title":"芙蓉楼送辛渐","type":"七言绝句","content":"寒雨连江夜入吴，平明送客楚山孤。<br />洛阳亲友如相问，一片冰心在玉壶。","explanation":"<p>１、芙蓉楼：原址在今江苏省镇江市西北。<br />２、楚山：古时吴、楚两地相接，镇江一带也称楚地，故其附近的山也可叫楚山。<\/p>","translation":"<p>迷蒙的烟雨，连夜洒遍吴地江天；<br />清晨送走你，孤对楚山离愁无限！<br />朋友呵，洛阳亲友若是问起我来；<br />就说我依然冰心玉壶，坚守信念！<\/p>","appreciation":"<p>这是一首送别诗。诗的构思新颖，淡写朋友的离情别绪，重写自己的高风亮节。首两句苍茫的江雨和孤峙的楚山，烘托送别时的孤寂之情；后两句自比冰壶，表达自己开郎胸怀和坚强性格。全诗即景生情，寓情于景，含蓄蕴藉，韵味无穷。 <\/p>","author":"王昌龄"}]
     */

    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private String total;
        private String pagenum;
        private String pagesize;
        /**
         * title : 芙蓉楼送辛渐
         * type : 七言绝句
         * content : 寒雨连江夜入吴，平明送客楚山孤。<br />洛阳亲友如相问，一片冰心在玉壶。
         * explanation : <p>１、芙蓉楼：原址在今江苏省镇江市西北。<br />２、楚山：古时吴、楚两地相接，镇江一带也称楚地，故其附近的山也可叫楚山。</p>
         * translation : <p>迷蒙的烟雨，连夜洒遍吴地江天；<br />清晨送走你，孤对楚山离愁无限！<br />朋友呵，洛阳亲友若是问起我来；<br />就说我依然冰心玉壶，坚守信念！</p>
         * appreciation : <p>这是一首送别诗。诗的构思新颖，淡写朋友的离情别绪，重写自己的高风亮节。首两句苍茫的江雨和孤峙的楚山，烘托送别时的孤寂之情；后两句自比冰壶，表达自己开郎胸怀和坚强性格。全诗即景生情，寓情于景，含蓄蕴藉，韵味无穷。 </p>
         * author : 王昌龄
         */

        private List<ListBean> list;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPagenum() {
            return pagenum;
        }

        public void setPagenum(String pagenum) {
            this.pagenum = pagenum;
        }

        public String getPagesize() {
            return pagesize;
        }

        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String title;
            private String type;
            private String content;
            private String explanation;
            private String translation;
            private String appreciation;
            private String author;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getExplanation() {
                return explanation;
            }

            public void setExplanation(String explanation) {
                this.explanation = explanation;
            }

            public String getTranslation() {
                return translation;
            }

            public void setTranslation(String translation) {
                this.translation = translation;
            }

            public String getAppreciation() {
                return appreciation;
            }

            public void setAppreciation(String appreciation) {
                this.appreciation = appreciation;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
