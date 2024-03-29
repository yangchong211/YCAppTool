package com.yc.monitortimelib.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BaseInterceptor implements IInterceptor {

    private final AtomicLong interceptorID = new AtomicLong();
    private CommonCallback<MonitorBean> monitorCallback;
    private final List<CommonCallback<InterceptorBean>> interceptorCallbacks = new ArrayList<>();

    /**
     * 统一的拦截器
     */
    private final CommonCallback<InterceptorBean> interceptorCallback = interceptorBean -> {
        for (CommonCallback<InterceptorBean> interceptor : interceptorCallbacks) {
            interceptor.result(interceptorBean);
        }
    };

    /**
     * 添加拦截器
     *
     * @param interceptorCallback 拦截器
     */
    @Override
    public void addInterceptor(CommonCallback<InterceptorBean> interceptorCallback) {
        if (!interceptorCallbacks.contains(interceptorCallback)) {
            interceptorCallbacks.add(interceptorCallback);
        }
    }


    /**
     * 删除拦截器
     *
     * @param interceptorCallback 拦截器
     */
    @Override
    public void removeInterceptor(CommonCallback<InterceptorBean> interceptorCallback) {
        interceptorCallbacks.remove(interceptorCallback);
    }

    /**
     * 设置卡顿监听
     *
     * @param monitorCallback 监听事件
     */
    public void setMonitorCallback(CommonCallback<MonitorBean> monitorCallback) {
        this.monitorCallback = monitorCallback;
    }


    /**
     * 获取拦截器实体类
     *
     * @param methodName 方法
     * @param basePm     参数集合
     * @return 实体类
     */
    private InterceptorBean getInterceptorBean(BaseParam basePm, String methodName) {
        InterceptorBean interceptorBean = new InterceptorBean();
        //开始时间
        interceptorBean.setBeginTime(System.currentTimeMillis());
        //id自增
        interceptorBean.setId(interceptorID.incrementAndGet());
        //方法名
        interceptorBean.setMethodName(methodName);
        //方法参数
        interceptorBean.setParam(basePm);
        return interceptorBean;
    }


    /**
     * 统一处理拦截器的逻辑
     *
     * @param basePm         请求
     * @param resultCallback 回调
     * @return 响应体
     */
    public BaseResult handleInterceptor(BaseParam basePm, ResultCallback resultCallback) {
        long begin = System.currentTimeMillis();
        BaseResult baseResult = null;
        //记录请求属性
        InterceptorBean interceptorBean = getInterceptorBean(basePm, basePm.getMethodName());
        try {
            //是否是独占的请求，不会复用通道
            baseResult = resultCallback.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            interceptorBean.setException(e);
            throw e;
        } finally {
            if (baseResult != null) {
                //记录函数耗时
                baseResult.setTimeCos(System.currentTimeMillis() - begin);
            }
            //记录响应属性
            responseInterceptor(baseResult, interceptorBean);
        }
        return baseResult;
    }

    /**
     * 统一处理拦截器的逻辑
     *
     * @param methodName     方法名称
     * @param invokeListener 回调
     * @return 响应体
     */
    public BaseResult methodSync(String methodName, InvokeListener invokeListener) {
        if (invokeListener == null) {
            return new BaseResult();
        }
        InterceptorManager instance = InterceptorManager.getInstance();
        BaseParam baseParam = new BaseParam(methodName);
        baseParam.setTimeout(1000);
        return instance.handleInterceptor(baseParam, () -> {
            BaseResult baseRt = new BaseResult();
            try {
                //这个地方做业务逻辑处理
                invokeListener.invoke();
                //插入成功
                baseRt.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                baseRt.setSuccess(false);
                baseRt.setException(e);
                baseRt.setErrMsg(e.toString());
            }
            return baseRt;
        });
    }

    /**
     * 响应拦截器实体类
     *
     * @param result          响应
     * @param interceptorBean 实体类
     */
    private void responseInterceptor(BaseResult result, InterceptorBean interceptorBean) {
        //结束时间
        interceptorBean.setEndTime(System.currentTimeMillis());
        //请求结果
        interceptorBean.setResult(result);
        //表示方法结束
        interceptorBean.setBefore(false);
        //开始触发拦截器回调
        interceptorCallback.result(interceptorBean);
        //监听方法卡顿操作
        long timeout = interceptorBean.getParam().getTimeout();
        if (interceptorBean.getEndTime() - interceptorBean.getBeginTime() > timeout) {
            if (monitorCallback != null) {
                MonitorBean monitorBean = new MonitorBean();
                monitorBean.setCos(interceptorBean.getEndTime() - interceptorBean.getBeginTime());
                monitorBean.setMethodName(interceptorBean.getMethodName());
                monitorBean.setParam(interceptorBean.getParam());
                monitorBean.setResult(interceptorBean.getResult());
                monitorCallback.result(monitorBean);
            }
        }
    }

}
