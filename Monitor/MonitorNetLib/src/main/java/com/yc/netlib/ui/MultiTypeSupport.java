package com.yc.netlib.ui;

import java.util.List;

/**
 * <pre>
 *     @author  yangchong
 *     email  : yangchong211@163.com
 *     time   : 2020/07/15
 *     desc   : 接口
 *     revise:
 * </pre>
 */
public interface MultiTypeSupport<T> {

    int getLayoutId(List<T> data, int position);

}
