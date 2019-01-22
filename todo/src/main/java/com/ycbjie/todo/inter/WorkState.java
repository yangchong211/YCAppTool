package com.ycbjie.todo.inter;


import android.content.Intent;

import com.ycbjie.library.db.cache.CacheTaskDetailEntity;


public interface WorkState {

    void initView(Intent intent);

    void onComplete(CacheTaskDetailEntity entity);

}
