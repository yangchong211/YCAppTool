package com.ns.yc.lifehelper.ui.find.model.bean;

import java.util.List;

/**
 * Created by PC on 2017/8/31.
 * 作者：PC
 */

public class RiddleDetailBean {


    /**
     * status : 0
     * msg : ok
     * result : {"pagesize":"2","classid":"7","list":[{"content":"拆去图框 （打一节气）","answer":"冬至"},{"content":"公厕实行有偿服务 （打一成语）","answer":"令人费解"}]}
     */

    private String status;
    private String msg;
    /**
     * pagesize : 2
     * classid : 7
     * list : [{"content":"拆去图框 （打一节气）","answer":"冬至"},{"content":"公厕实行有偿服务 （打一成语）","answer":"令人费解"}]
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
        private String pagesize;
        private String classid;
        /**
         * content : 拆去图框 （打一节气）
         * answer : 冬至
         */

        private List<ListBean> list;

        public String getPagesize() {
            return pagesize;
        }

        public void setPagesize(String pagesize) {
            this.pagesize = pagesize;
        }

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String content;
            private String answer;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }
        }
    }
}
