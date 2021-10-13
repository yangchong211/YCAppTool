package com.yc.businessinterface;



import java.util.HashMap;
import java.util.Map;



public class BusinessTransfer {

    private static final String UPDATE_MANAGER = "updateManager";
    private static final String USER_MANAGER = "userManager";
    private Map<String, String> implMap;
    private IUpdateManager updateManager;
    private IUserManager userManager;

    private BusinessTransfer() {
        implMap = new HashMap<>();
    }

    public static BusinessTransfer $() {
        return ServiceTransferLoader.INSTANCE;
    }

    private static class ServiceTransferLoader {
        private static final BusinessTransfer INSTANCE = new BusinessTransfer();
    }

    private void setImpl(String key, String className) {
        implMap.put(key, className);
    }

    private <T> T instaneImpl(T t, String key) {
        if (t == null) {
            String className = implMap.get(key);
            try {
                return (T) Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

    /**
     * 获取版本更新的接口对象
     * @return                          版本更新
     */
    public IUpdateManager getUpdate() {
        return updateManager = instaneImpl(updateManager, UPDATE_MANAGER);
    }

    /**
     * 获取用户相关服务
     * @return                          返回接口对象
     */
    public IUserManager getUserManager() {
        return userManager = instaneImpl(userManager,USER_MANAGER);
    }

    //在app中初始化，相当于将键和对应的类的className储存到HashMap集合中
    //这个是将IUpdateManager接口的实现类UpdateManagerImpl注入
    public void injectBusinessImpl() {
        BusinessTransfer businessTransfer = BusinessTransfer.$();
        businessTransfer.setImpl(UPDATE_MANAGER, "com.ycbjie.library.impl.UpdateManagerImpl");
        businessTransfer.setImpl(USER_MANAGER, "com.ycbjie.library.impl.UserManagerImpl");
    }

}


