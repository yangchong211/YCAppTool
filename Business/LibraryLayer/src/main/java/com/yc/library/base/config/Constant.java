package com.yc.library.base.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constant {

    public static final String GITHUB = "https://github.com/yangchong211/YCBlogs";
    public static final String LIFE_HELPER = "https://github.com/yangchong211/LifeHelper";
    public static final String JIAN_SHU = "https://www.jianshu.com/u/b7b2c6ed9284";
    public static final String JUE_JIN = "https://juejin.im/user/5939433efe88c2006afa0c6e";
    public static final String ZHI_HU = "https://www.zhihu.com/people/yczbj/activities";

    /**
     * 配合CoordinatorLayout使用
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATES{
        int EXPANDED = 3;
        int COLLAPSED = 2;
        int INTERMEDIATE = 1;
    }

    /**-------------------------------------集合-------------------------------------------------**/
    public static final String URL = "url";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";


}
