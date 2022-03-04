package com.yc.alive.constant;


import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;


/**
 * Rom 相关常量
 */
@RestrictTo(LIBRARY)
public class AliveRomConst {

    /**
     * 支持的类型，如 华为，小米
     */
    public static class SupportType {

        public static final int TYPE_DEFAULT = -1;
        public static final int TYPE_OPPO = 1;
        public static final int TYPE_VIVO = 2;
        public static final int TYPE_EMUI = 3;
        public static final int TYPE_MIUI = 4;
        public static final int TYPE_SAMSUNG = 5;
        public static final int TYPE_MEIZU = 6;
        public static final int TYPE_SMART = 7;
        public static final int TYPE_SUNMI = 8;

        @IntDef({ TYPE_DEFAULT, TYPE_OPPO, TYPE_VIVO, TYPE_EMUI, TYPE_MIUI, TYPE_SAMSUNG, TYPE_MEIZU, TYPE_SMART, TYPE_SUNMI })
        @Retention(RetentionPolicy.SOURCE)
        public @interface TYPE {
        }
    }

    /**
     * 支持的具体型号，如 小米-V9
     */
    public static class SupportModel {

        // oppo A57 6.0.1
        public static final String OPPO_V3_0 = "V3.0";
        public static final String OPPP_V3_0_0 = "V3.0.0";
        public static final String OPPO_V3_2 = "V3.2";
        public static final String OPPO_V5_0 = "V5.0";

        // vivo Xplay5A 5.1.1
        public static final String VIVO_2_5 = "2.5";
        public static final String VIVO_3_0 = "3.0";
        public static final String VIVO_3_1 = "3.1";
        public static final String VIVO_4_0 = "4.0";

        public static final String EMUI_10 = "10";
        // HW-P9 VIE-AL10 7.0
        public static final String EMUI_11 = "11";
        public static final String EMUI_13 = "13";
        public static final String EMUI_14 = "14";
        public static final String EMUI_15 = "15";

        // MI-NOTE 6.0
        public static final String MIUI_V9 = "V9";
        public static final String MIUI_V9_MIX_2 = "MIX 2";
        public static final String MIUI_V9_MI_6 = "MI 6";
        public static final String MIUI_V9_MI_5 = "MI 5";

        // 锤子 2.5.0-2016010519-user-sfo_lte
        public static final String SMART_V2_5 = "2.5.0";
        // 锤子 4.2.2-2018051615-user-ocr
        public static final String SMART_V4_2 = "4.2";

        public static final String SAMSUNG_24 = "24";

        public static final String SUNMI_711 = "7.1.1";

        @StringDef({
            OPPO_V3_0, OPPP_V3_0_0, OPPO_V3_2, OPPO_V5_0,
            VIVO_2_5, VIVO_3_0, VIVO_3_1, VIVO_4_0,
            EMUI_10, EMUI_11, EMUI_13, EMUI_14, EMUI_15,
            MIUI_V9, MIUI_V9_MIX_2, MIUI_V9_MI_6, MIUI_V9_MI_5,
            SMART_V2_5, SMART_V4_2,
            SAMSUNG_24,
            SUNMI_711})
        @Retention(RetentionPolicy.SOURCE)
        public @interface MODEL {
        }
    }
}
