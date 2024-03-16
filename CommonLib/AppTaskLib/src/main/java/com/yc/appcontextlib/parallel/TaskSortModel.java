package com.yc.appcontextlib.parallel;

import java.io.Serializable;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @time : 2019/03/15
 * @desc : task数据实体
 * @revise :
 * GitHub ：https://github.com/yangchong211/YCEfficient
 */
public final class TaskSortModel implements Serializable {

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
