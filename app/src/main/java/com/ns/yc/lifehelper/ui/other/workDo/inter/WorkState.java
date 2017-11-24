package com.ns.yc.lifehelper.ui.other.workDo.inter;


import android.content.Intent;

import com.ns.yc.lifehelper.ui.other.toDo.bean.TaskDetailEntity;

public interface WorkState {

    void initView(Intent intent);

    void onComplete(TaskDetailEntity entity);

}
