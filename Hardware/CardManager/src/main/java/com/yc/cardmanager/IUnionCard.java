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
     * 校验卡号并读取数据
     * @param type              类型
     * @param cardNo            卡号
     * @return                  卡数据
     */
    String verify(CardType type ,String cardNo);
}
