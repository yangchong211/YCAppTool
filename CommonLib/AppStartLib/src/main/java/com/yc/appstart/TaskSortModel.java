package com.yc.appstart;

import java.io.Serializable;

public class TaskSortModel implements Serializable {

    private int in;

    public TaskSortModel(int in) {
        this.in = in;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

}
