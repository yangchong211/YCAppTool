package com.yc.shadow.layout;

import android.os.Build;

import java.util.Objects;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/7/20
 *     desc  : 自定义阴影缓存key
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCWidgetLib
 * </pre>
 */
public final class ShadowKey {


    private final String name;
    private final int width;
    private final int height;

    public ShadowKey(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShadowKey key = (ShadowKey) o;
        if (width != key.width) {
            return false;
        }
        if (height != key.height) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(name, key.name);
        }
        return name != null ? name.equals(key.name) : key.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }
    
}
