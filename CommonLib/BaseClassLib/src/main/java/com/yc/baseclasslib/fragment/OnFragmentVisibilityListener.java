package com.yc.baseclasslib.fragment;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2019/5/11
 *     desc   : fragment是否可见监听listener
 *     revise :
 * </pre>
 */
public interface OnFragmentVisibilityListener {

    /**
     * 是否可见
     *
     * @param visibility true表示可见，否则不可见
     */
    void onFragmentVisibilityChanged(boolean visibility);

}
