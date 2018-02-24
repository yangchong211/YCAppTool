package com.ns.yc.lifehelper.model.entry;


import me.yokeyword.indexablerv.IndexableEntity;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/6/23
 * 描    述：标签，可以自定义字段
 * 修订历史：
 * ================================================
 */
public class TagEntity implements IndexableEntity {

    private String id;
    private String name;
    private String pinyin;
    private boolean selector;

    public TagEntity() {
    }

    public TagEntity(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexByField) {
        this.name = indexByField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        this.pinyin = pinyin;
    }
}
