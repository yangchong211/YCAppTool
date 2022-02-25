package com.yc.monitorfilelib;

public final class SpDataBean {

    public String key;
    public Object value;
    public Class clazz;

    private SpDataBean() {

    }

    public SpDataBean(String key, Object value) {
        this.key = key;
        this.value = value;
        clazz = value.getClass();
    }

    public Object toDefaultClass(String string) {
        setDefaultClass(string);
        return value;
    }

    private void setDefaultClass(String string) {
        switch (clazz.getSimpleName()) {
            case SpInputType.FLOAT:
                value = Float.valueOf(string);
                break;
            case SpInputType.DOUBLE:
                value = Double.valueOf(string);
                break;
            case SpInputType.INTEGER:
                value = Integer.valueOf(string);
                break;
            case SpInputType.STRING:
                value = String.valueOf(string);
                break;
            case SpInputType.LONG:
                value = Long.valueOf(string);
                break;
            case SpInputType.BOOLEAN:
                value = Boolean.valueOf(string);
                break;
        }
    }

}
