package com.yc.appleet.linked;

import com.yc.library.base.mvp.BaseActivity;

import java.util.LinkedHashMap;

public class LinkedActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return 0;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void testLinkedHashMap(){
        LinkedHashMap<String, Integer> lmap = new LinkedHashMap<String, Integer>();
        lmap.put("语文", 1);
        lmap.put("数学", 2);
        lmap.put("英语", 3);
        lmap.put("历史", 4);
        lmap.put("政治", 5);
        lmap.put("地理", 6);
        lmap.put("生物", 7);
        lmap.put("化学", 8);
        Integer integer = lmap.get("政治");
        for(LinkedHashMap.Entry<String, Integer> entry : lmap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        Integer integer1 = lmap.get("生物");
    }
}
