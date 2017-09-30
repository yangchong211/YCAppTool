package com.ns.yc.lifehelper.ui.other.weather.bean;


public class WeatherSuggestion {

    private String index;
    private String key;
    private String value;

    public WeatherSuggestion(String indexName, String key, String value) {
        this.index = indexName;
        this.key = key;
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
