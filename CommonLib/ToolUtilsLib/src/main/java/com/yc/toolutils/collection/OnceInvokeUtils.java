package com.yc.toolutils.collection;

import com.yc.toolutils.system.AppLogUtils;

import java.util.HashMap;


/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCAppTool
 *     email  : yangchong211@163.com
 *     time  : 2018/09/23
 *     desc  : 一次执行工具类
 *     revise:
 * </pre>
 */
public final class OnceInvokeUtils {

    private final HashMap<String, Boolean> hashMap = new HashMap<>();

    public void invoke(String key , CallbackListener callback){
        // Unit函数相当于java中的void
        // 首先 Unit 本身是一个用 object 表示的单例，所以可以理解为Kotlin有一个类，这个类只有一个单例对象，叫 Unit 。
        // 在Kotlin中，一切方法/函数都是表达式，表达式是总是有值的，所以每一个方法都必有一个返回值。
        // 如果没有用 return 明确的指定，那么一般来说就会用自动帮我们加上 Unit
        if (hashMap.get(key)==null || !hashMap.get(key).booleanValue()) {
            AppLogUtils.d("invoke:${key} -> go head");
            callback.invoke();
            hashMap.put(key,true);
        } else {
            AppLogUtils.d("invoke:${key} -> has been called ,just ignore");
        }
    }

    public void removeAll() {
        AppLogUtils.d("reset");
        hashMap.clear();
    }

    public void reset(String key) {
        AppLogUtils.d("reset: " + key);
        hashMap.put(key,false);
    }

    /**
     * 接口
     */
    public interface CallbackListener {
        /**
         * 执行体
         */
        void invoke();
    }

}
