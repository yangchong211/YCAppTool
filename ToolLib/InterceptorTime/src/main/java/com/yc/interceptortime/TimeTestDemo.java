package com.yc.interceptortime;

public class TimeTestDemo {


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


}
