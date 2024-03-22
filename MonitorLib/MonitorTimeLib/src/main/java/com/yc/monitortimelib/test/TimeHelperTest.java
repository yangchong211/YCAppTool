package com.yc.monitortimelib.test;

import com.yc.monitortimelib.interceptor.CommonCallback;
import com.yc.monitortimelib.interceptor.InterceptorBean;
import com.yc.monitortimelib.interceptor.InterceptorManager;
import com.yc.monitortimelib.interceptor.MonitorBean;
import com.yc.toolutils.AppLogUtils;

public class TimeHelperTest {

    public void test1(){
        InterceptorManager.getInstance().addInterceptor(new CommonCallback<InterceptorBean>() {
            @Override
            public void result(InterceptorBean interceptorBean) {
                AppLogUtils.d("addInterceptor: " + interceptorBean.toString());
            }
        });
        InterceptorManager.getInstance().setMonitorCallback(new CommonCallback<MonitorBean>() {
            @Override
            public void result(MonitorBean monitorBean) {
                AppLogUtils.d("addInterceptor monitorBean : " + monitorBean.toString());
            }
        });
    }

}
