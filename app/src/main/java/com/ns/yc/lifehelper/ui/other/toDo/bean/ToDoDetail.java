package com.ns.yc.lifehelper.ui.other.toDo.bean;

import java.util.List;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：时光日志内容【自定义，后期需要再添加】实体类
 * 修订历史：
 * ================================================
 */
public class ToDoDetail {

    private List<ToDo> list;

    public List<ToDo> getList() {
        return list;
    }

    public void setList(List<ToDo> list) {
        this.list = list;
    }

    public static class ToDo {

        private String title;               //标题
        private String content;             //内容
        private String time;                //时间
        private String dayOfWeek;           //周几？
        private int icon;                   //图片
        private String priority;            //重要级分类

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }
    }


}
