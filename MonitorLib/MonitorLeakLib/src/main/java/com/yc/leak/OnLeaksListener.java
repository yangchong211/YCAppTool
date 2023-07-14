package com.yc.leak;

import java.util.HashMap;

public interface OnLeaksListener {

    void onLeaks(long cosTime, HashMap<String, Integer> hashMap);

}
