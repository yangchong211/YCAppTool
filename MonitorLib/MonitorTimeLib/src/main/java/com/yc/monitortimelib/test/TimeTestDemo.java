package com.yc.monitortimelib.test;

import com.yc.monitortimelib.interceptor.BaseInterceptor;
import com.yc.monitortimelib.interceptor.BaseParam;
import com.yc.monitortimelib.interceptor.BaseResult;
import com.yc.monitortimelib.interceptor.CommonCallback;
import com.yc.monitortimelib.interceptor.InterceptorBean;
import com.yc.monitortimelib.interceptor.InterceptorManager;
import com.yc.monitortimelib.interceptor.InvokeListener;
import com.yc.monitortimelib.interceptor.MonitorBean;
import com.yc.monitortimelib.interceptor.ResultCallback;
import com.yc.monitortimelib.monitor.MonitorListener;
import com.yc.monitortimelib.monitor.MonitorTimeHelper;
import com.yc.toolutils.AppLogUtils;

public class TimeTestDemo {

    public void test1(){
        InterceptorManager.getInstance().addInterceptor(new CommonCallback<InterceptorBean>() {
            @Override
            public void result(InterceptorBean interceptorBean) {
                //统计函数耗时
                AppLogUtils.d("addInterceptor: " + interceptorBean.toString());
            }
        });
        InterceptorManager.getInstance().setMonitorCallback(new CommonCallback<MonitorBean>() {
            @Override
            public void result(MonitorBean monitorBean) {
                //监听函数耗时卡顿时间
                AppLogUtils.d("addInterceptor monitorBean : " + monitorBean.toString());
            }
        });



        MonitorTimeHelper.init(true, null);
        MonitorTimeHelper.setOnMonitorListener(new MonitorListener() {
            @Override
            public void onMonitorResult(String processName, String result) {
                AppLogUtils.d("TimeMonitor processName: " + processName + " , result: " + result);
            }
        });
    }


    /**
     * 使用案例1: 这种是使用单利对象调用
     *
     * @return BaseResult
     */
    public BaseResult testMethodSync() {
        InterceptorManager instance = InterceptorManager.getInstance();
        BaseParam baseParam = new BaseParam("doubi");
        baseParam.setTimeout(5000);
        return (BaseResult) instance.handleInterceptor(baseParam, new ResultCallback() {
            @Override
            public BaseResult getResult() {
                BaseResult baseRt = new BaseResult();
                try {
                    //这个地方做业务逻辑处理
                    Thread.sleep(6000);
                    //插入成功
                    baseRt.setSuccess(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    baseRt.setSuccess(false);
                    baseRt.setException(e);
                    baseRt.setErrMsg(e.toString());
                }
                return baseRt;
            }
        });
    }

    /**
     * 使用案例2: 这种是继承使用
     *
     * @return BaseResult
     */
    public static class Test1 extends BaseInterceptor {

        public BaseResult testMethodSync2() {
            BaseParam baseParam = new BaseParam("doubi");
            baseParam.setTimeout(5000);
            return (BaseResult) handleInterceptor(baseParam, new ResultCallback() {
                @Override
                public BaseResult getResult() {
                    BaseResult baseRt = new BaseResult();
                    try {
                        //这个地方做业务逻辑处理
                        Thread.sleep(6000);
                        //插入成功
                        baseRt.setSuccess(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        baseRt.setSuccess(false);
                        baseRt.setException(e);
                        baseRt.setErrMsg(e.toString());
                    }
                    return baseRt;
                }
            });
        }
    }


    private void test2() {
        InterceptorManager instance = InterceptorManager.getInstance();
        instance.methodSync("test2", new InvokeListener() {
            @Override
            public void invoke() {
                //执行函数
            }
        });
    }

    private void test3() {
        MonitorTimeHelper.start("testTime1");
        try {
            Thread.sleep(1231);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        MonitorTimeHelper.end("testTime1");

        for (int i = 0 ; i<10 ; i++ ) {
            int count = i;
            try {
                MonitorTimeHelper.start("testTimeA" + count);
                Thread.sleep(1231);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                MonitorTimeHelper.end("testTimeA" + count);
            }
        }
    }
}
