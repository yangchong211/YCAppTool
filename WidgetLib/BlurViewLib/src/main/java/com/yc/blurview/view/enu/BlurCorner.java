package com.yc.blurview.view.enu;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        BlurCorner.TOP_LEFT, BlurCorner.TOP_RIGHT,
        BlurCorner.BOTTOM_LEFT, BlurCorner.BOTTOM_RIGHT
})
public @interface BlurCorner {
    int TOP_LEFT = 0;
    int TOP_RIGHT = 1;
    int BOTTOM_RIGHT = 2;
    int BOTTOM_LEFT = 3;
}
