package com.yc.interceptortime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author : yangchong
 * @email : yangchong211@163.com
 * @time : 2017/5/18
 * @desc : 拦截器管理者
 * revise :
 */
public class InterceptorManager implements IInterceptor {

    private static volatile InterceptorManager singleton = null;
    private final AtomicLong interceptorID = new AtomicLong();
    protected CommonCallback<MonitorBean> monitorCallback;
    private final List<CommonCallback<InterceptorBean>> interceptorCallbacks = new ArrayList<>();

    //统一的拦截器
    protected CommonCallback<InterceptorBean> interceptorCallback = interceptorBean -> {
        for (CommonCallback<InterceptorBean> interceptor : interceptorCallbacks) {
            interceptor.result(interceptorBean);
        }
    };

    public static InterceptorManager getInstance() {
        if (singleton == null) {
            synchronized (InterceptorManager.class) {
                if (singleton == null) {
                    singleton = new InterceptorManager();
                }
            }
        }
        return singleton;
    }

    private InterceptorManager() {

    }

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
        //开始触发回调
        interceptorCallback.result(interceptorBean);
        return interceptorBean;
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
                baseResult.setTimeCos(System.currentTimeMillis() - begin);
            }
            //记录响应属性
            responseInterceptor(baseResult, interceptorBean);
        }
        return baseResult;
    }

    public BaseResult testMethodSync() {
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
