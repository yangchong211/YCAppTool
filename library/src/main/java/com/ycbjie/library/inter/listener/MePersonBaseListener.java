package com.ycbjie.library.inter.listener;

import android.view.View;

/**
 * Created by PC on 2017/5/3.
 * ScrollView滑动监听事件接口
 */

public interface MePersonBaseListener {

    /*** 编辑个人信息*/
    void editPersonInfo(View view, String title, String editText);
    /*** 获取个人头像*/
    void editPersonLogo(View view);
    /*** 编辑个人城市定位*/
    void editPersonCity(View view);
    /*** 编辑个人收藏*/
    void editPersonColl(View view);
    /*** 编辑个人评论*/
    void editPersonCom(View view);

}
