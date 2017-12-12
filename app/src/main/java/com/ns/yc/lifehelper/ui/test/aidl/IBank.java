package com.ns.yc.lifehelper.ui.test.aidl;


public interface IBank {

    /**
     * 开户
     * @param name          开户名
     * @param password      密码
     * @return              开户信息
     */
    String OpenAccount(String name,String password);

    /**
     * 存钱
     * @param money         金额
     * @param account       账户
     * @return              存钱信息
     */
    String saveMoney(int money,String account);

    /**
     * 取钱
     * @param money         金额
     * @param account       账户
     * @param password      密码
     * @return              取钱信息
     */
    String tackMoney(int money,String account,String password);

    /**
     * 销户
     * @param account       账户
     * @param password      密码
     * @return              销户信息
     */
    String closeAccount(String account,String password);


}
