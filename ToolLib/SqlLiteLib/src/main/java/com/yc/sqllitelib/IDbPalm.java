package com.yc.sqllitelib;


import java.util.HashMap;

public interface IDbPalm<T> {

    /**
     * 添加单个掌纹数据
     *
     * @param featureId id
     * @param feature   数据
     */
    void add(String featureId, T feature);

    /**
     * 添加所有掌纹数据
     *
     * @param features 数据
     */
    void addAll(HashMap<String, T> features);

    /**
     * 查找某个指定掌纹数据
     *
     * @param featureId id
     * @return 掌纹数据
     */
    T get(String featureId);

    /**
     * 删除某些掌纹数据
     *
     * @param featureId id
     */
    void delete(String featureId);

    /**
     * 删除某些掌纹数据
     *
     * @param featureIds id
     */
    void delete(String[] featureIds);

    /**
     * 获取sql所有数据
     *
     * @return 集合
     */
    HashMap<String, T> getAllData();

    /**
     * 清楚所有数据
     */
    void clear();

}
