package com.yc.shadow;

import android.graphics.Path;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/7/20
 *     desc  : 工具类
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCWidgetLib
 * </pre>
 */
public final class ShapeUtils {

    static Path roundedRect(float left, float top, float right, float bottom,
                            float rx, float ry, boolean tl, boolean tr, boolean br, boolean bl) {
        Path path = new Path();
        if (rx < 0) {
            rx = 0f;
        }
        if (ry < 0) {
            ry = 0f;
        }
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) {
            rx = width / 2;
        }
        if (ry > height / 2) {
            ry = height / 2;
        }
        float widthMinusCorners = width - 2 * rx;
        float heightMinusCorners = height - 2 * ry;

        path.moveTo(right, top + ry);
        if (tr) {
            path.rQuadTo(0f, -ry, -rx, -ry);//top-right corner
        } else {
            path.rLineTo(0f, -ry);
            path.rLineTo(-rx, 0f);
        }
        path.rLineTo(-widthMinusCorners, 0f);
        if (tl) {
            path.rQuadTo(-rx, 0f, -rx, ry); //top-left corner
        } else {
            path.rLineTo(-rx, 0f);
            path.rLineTo(0f, ry);
        }
        path.rLineTo(0f, heightMinusCorners);

        if (bl) {
            path.rQuadTo(0f, ry, rx, ry);//bottom-left corner
        } else {
            path.rLineTo(0f, ry);
            path.rLineTo(rx, 0f);
        }

        path.rLineTo(widthMinusCorners, 0f);
        if (br) {
            path.rQuadTo(rx, 0f, rx, -ry); //bottom-right corner
        } else {
            path.rLineTo(rx, 0f);
            path.rLineTo(0f, -ry);
        }

        path.rLineTo(0f, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }

    static Path roundedRect(float left, float top, float right, float bottom,
                            float tl, float tr, float br, float bl) {
        Path path = new Path();
        if (tl < 0) {
            tl = 0f;
        }
        if (tr < 0) {
            tr = 0f;
        }
        if (br < 0) {
            br = 0f;
        }
        if (bl < 0) {
            bl = 0f;
        }
        float width = right - left;
        float height = bottom - top;
        float min = Math.min(width, height);
        if (tl > min / 2) {
            tl = min / 2;
        }
        if (tr > min / 2) {
            tr = min / 2;
        }
        if (br > min / 2) {
            br = min / 2;
        }
        if (bl > min / 2) {
            bl = min / 2;
        }
        if (tl == tr && tr == br && br == bl && tl == min / 2) {
            float radius = min / 2F;
            path.addCircle(left + radius, top + radius, radius, Path.Direction.CW);
            return path;
        }

        path.moveTo(right, top + tr);
        if (tr > 0) {
            path.rQuadTo(0f, -tr, -tr, -tr);//top-right corner
        } else {
            path.rLineTo(0f, -tr);
            path.rLineTo(-tr, 0f);
        }
        path.rLineTo(-(width - tr - tl), 0f);
        if (tl > 0) {
            path.rQuadTo(-tl, 0f, -tl, tl); //top-left corner
        } else {
            path.rLineTo(-tl, 0f);
            path.rLineTo(0f, tl);
        }
        path.rLineTo(0f, height - tl - bl);

        if (bl > 0) {
            path.rQuadTo(0f, bl, bl, bl);//bottom-left corner
        } else {
            path.rLineTo(0f, bl);
            path.rLineTo(bl, 0f);
        }

        path.rLineTo(width - bl - br, 0f);
        if (br > 0) {
            path.rQuadTo(br, 0f, br, -br); //bottom-right corner
        } else {
            path.rLineTo(br, 0f);
            path.rLineTo(0f, -br);
        }

        path.rLineTo(0f, -(height - br - tr));

        path.close();//Given close, last lineto can be removed.

        return path;
    }

}
