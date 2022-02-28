package cn.ycbjie.ycthreadpoollib.builder;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/05/17
 *     desc  : 枚举
 *     revise:
 * </pre>
 */
public enum ThreadPoolType {
    CACHED,
    FIXED,
    SCHEDULED,
    SINGLE,
    CUSTOM;

    private ThreadPoolType() {
    }
}
