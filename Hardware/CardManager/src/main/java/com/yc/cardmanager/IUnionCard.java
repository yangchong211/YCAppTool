package com.yc.cardmanager;

public interface IUnionCard {

    /**
     * 获取卡类型
     * @return      类型
     */
    CardType getCardType();


    /**
     * 寻找卡号
     *
     * @return 获取卡号
     */
    String search();

    /**
     * 该方法相当于 先getCardType + 后search
     * 获取类型并校验卡号
     * @return      返回卡号
     */
    String verify();
}
