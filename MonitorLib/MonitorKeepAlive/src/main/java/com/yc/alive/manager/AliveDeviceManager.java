package com.yc.alive.manager;

import androidx.annotation.RestrictTo;

import com.yc.alive.model.AliveDeviceModel;
import com.yc.alive.service.KAEmuiAccessibility;
import com.yc.alive.service.KAMiuiAccessibility;
import com.yc.alive.service.KAOppoAccessibility;
import com.yc.alive.service.KASamsungAccessibility;
import com.yc.alive.service.KASmartAccessibility;
import com.yc.alive.service.KAVivoAccessibility;
import com.yc.alive.util.AliveLogUtils;
import com.yc.alive.util.KARomUtils;
import com.yc.alive.util.KASystemPropertiesUtils;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_10;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_11;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_13;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_14;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_15;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9_MIX_2;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9_MI_5;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9_MI_6;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPO_V3_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPO_V3_2;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPO_V5_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPP_V3_0_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.SAMSUNG_24;
import static com.yc.alive.constant.AliveRomConst.SupportModel.SMART_V2_5;
import static com.yc.alive.constant.AliveRomConst.SupportModel.SMART_V4_2;
import static com.yc.alive.constant.AliveRomConst.SupportModel.SUNMI_711;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_2_5;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_3_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_3_1;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_4_0;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_DEFAULT;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_EMUI;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_MIUI;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_OPPO;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_SAMSUNG;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_SMART;
import static com.yc.alive.constant.AliveRomConst.SupportType.TYPE_VIVO;

/**
 * 设备信息管理
 */
@RestrictTo(LIBRARY)
public class AliveDeviceManager {

    private static final String TAG = "KADeviceManager";

    private AliveDeviceModel device;
    private static boolean sIsSupport = false;

    private AliveDeviceManager() {
        device = new AliveDeviceModel();
        init();
    }

    private static final class InnerHolder {
        private static final AliveDeviceManager INSTANCE = new AliveDeviceManager();
    }

    public static AliveDeviceManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    private void init() {
        device.manufacturer = KASystemPropertiesUtils.BUILD_MANUFACTURER;
        device.model = KASystemPropertiesUtils.BUILD_MODEL;
        device.sdkInt = KASystemPropertiesUtils.BUILD_SDK_INT;
        device.sdkStr = KASystemPropertiesUtils.BUILD_SDK_STR;

        device.romOppoVersionName = KASystemPropertiesUtils.ROM_OPPO_VERSION_NAME;

        device.romVivoVersionName = KASystemPropertiesUtils.ROM_VIVO_VERSION_NAME;
        device.romVivoVersionCode = KASystemPropertiesUtils.ROM_VIVO_VERSION_CODE;

        device.romEmuiVersionName = KASystemPropertiesUtils.ROM_EMUI_VERSION_NAME;
        device.romEmuiVersionCode = String.valueOf(KASystemPropertiesUtils.ROM_EMUI_VERSION_CODE);

        device.romMiuiVersionName = KASystemPropertiesUtils.ROM_MIUI_VERSION_NAME;
        device.romMiuiVersionCode = KASystemPropertiesUtils.ROM_MIUI_VERSION_CODE;

        device.romSmartisanVersionName = KASystemPropertiesUtils.ROM_SMARTISAN_VERSION_NAME;

        AliveLogUtils.d(TAG, device.print());

        initSupport();
    }

    public boolean isOppo() {
        return device.type == TYPE_OPPO;
    }

    public boolean isVivo() {
        return device.type == TYPE_VIVO;
    }

    public boolean isEmui() {
        return device.type == TYPE_EMUI;
    }

    public boolean isMiui() {
        return device.type == TYPE_MIUI;
    }

    public boolean isSamsung() {
        return device.type == TYPE_SAMSUNG;
    }

    public boolean isSmartisan() {
        return device.type == TYPE_SMART;
    }

    public boolean isSupport() {
        return sIsSupport;
    }

    public AliveDeviceModel getDevice() {
        return device;
    }

    private void initSupport() {
        if (KARomUtils.isOppo(device)) {
            device.type = TYPE_OPPO;
            initOppo();
        } else if (KARomUtils.isVivo(device)) {
            device.type = TYPE_VIVO;
            initVivo();
        } else if (KARomUtils.isEmui(device)) {
            device.type = TYPE_EMUI;
            initEmui();
        } else if (KARomUtils.isMiui(device)) {
            device.type = TYPE_MIUI;
            initMiui();
        } else if (KARomUtils.isSmart(device)) {
            device.type = TYPE_SMART;
            initSmart();
        } else if (KARomUtils.isSamsung(device)) {
            device.type = TYPE_SAMSUNG;
            initSamsung();
        } else if (KARomUtils.isSunMi(device)) {
            device.type = TYPE_SAMSUNG;
            initSunMi();
        } else {
            device.type = TYPE_DEFAULT;
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createDefault());
            sIsSupport = false;
        }
    }

    private void initOppo() {
        device.romVersionName = device.romOppoVersionName;
        if (OPPO_V3_0.equals(device.romOppoVersionName)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createOppoV3_0());
            AliveOptionManager.getInstance().setAccessibility(new KAOppoAccessibility());
            sIsSupport = true;
        } else if (OPPP_V3_0_0.equals(device.romOppoVersionName)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createOppoV3_0_0());
            AliveOptionManager.getInstance().setAccessibility(new KAOppoAccessibility());
            sIsSupport = true;
        } else if (OPPO_V3_2.equals(device.romOppoVersionName)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createOppoV3_2());
            AliveOptionManager.getInstance().setAccessibility(new KAOppoAccessibility());
            sIsSupport = true;
        } else if (OPPO_V5_0.equals(device.romOppoVersionName)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createOppoV5_0());
            AliveOptionManager.getInstance().setAccessibility(new KAOppoAccessibility());
            sIsSupport = true;
        } else {
            sIsSupport = false;
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createOppoDefault());
        }
    }

    private void initVivo() {
        device.romVersionName = device.romVivoVersionName;
        device.romVersionCode = device.romVivoVersionCode;
        if (VIVO_2_5.equals(device.romVivoVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createVivo2_5());
            AliveOptionManager.getInstance().setAccessibility(new KAVivoAccessibility());
            sIsSupport = true;
        } else if (VIVO_3_0.equals(device.romVivoVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createVivoV3_0());
            AliveOptionManager.getInstance().setAccessibility(new KAVivoAccessibility());
            sIsSupport = true;
        } else if (VIVO_3_1.equals(device.romVivoVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createVivo3_1());
            AliveOptionManager.getInstance().setAccessibility(new KAVivoAccessibility());
            sIsSupport = true;
        } else if (VIVO_4_0.equals(device.romVivoVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createVivo4_0());
            AliveOptionManager.getInstance().setAccessibility(new KAVivoAccessibility());
            sIsSupport = true;
        } else {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createOppoDefault());
            sIsSupport = false;
        }
    }

    private void initEmui() {
        device.romVersionName = device.romEmuiVersionName;
        device.romVersionCode = device.romEmuiVersionCode;
        if (EMUI_10.equals(device.romEmuiVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createEMUIV10());
            AliveOptionManager.getInstance().setAccessibility(new KAEmuiAccessibility());
            sIsSupport = true;
        } else if (EMUI_11.equals(device.romEmuiVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createEMUIV11());
            AliveOptionManager.getInstance().setAccessibility(new KAEmuiAccessibility());
            sIsSupport = true;
        } else if (EMUI_13.equals(device.romEmuiVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createEMUIV13());
            AliveOptionManager.getInstance().setAccessibility(new KAEmuiAccessibility());
            sIsSupport = true;
        } else if (EMUI_14.equals(device.romEmuiVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createEMUIV14());
            AliveOptionManager.getInstance().setAccessibility(new KAEmuiAccessibility());
            sIsSupport = true;
        } else if (EMUI_15.equals(device.romEmuiVersionCode)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createEMUIV15());
            AliveOptionManager.getInstance().setAccessibility(new KAEmuiAccessibility());
            sIsSupport = true;
        } else {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createEMUIDefault());
            sIsSupport = false;
        }
    }

    private void initMiui() {
        device.romVersionName = device.romMiuiVersionName;
        device.romVersionCode = String.valueOf(device.romMiuiVersionCode);
        if (MIUI_V9.equals(device.romMiuiVersionName)) {
            AliveOptionManager.getInstance().setAccessibility(new KAMiuiAccessibility());
            if (MIUI_V9_MIX_2.equals(device.model)) {
                device.romVersionName = MIUI_V9_MIX_2;
                AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createMIUIV9_MIX_2());
            } else if (MIUI_V9_MI_6.equals(device.model)) {
                device.romVersionName = MIUI_V9_MI_6;
                AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createMIUIV9_MI_6());
            } else if (MIUI_V9_MI_5.equals(device.model)) {
                device.romVersionName = MIUI_V9_MI_5;
                AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createMIUIV9_MI_5());
            } else {
                AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createMIUIV9());
            }
            sIsSupport = true;
        } else {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createMIUIDefault());
            sIsSupport = false;
        }
    }

    private void initSmart() {
        device.romVersionName = device.romSmartisanVersionName;
        if (device.romSmartisanVersionName.startsWith(SMART_V2_5)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createSmartV2_5());
            AliveOptionManager.getInstance().setAccessibility(new KASmartAccessibility());
            sIsSupport = true;
        } else if (device.romSmartisanVersionName.startsWith(SMART_V4_2)) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createSmartV4_2());
            AliveOptionManager.getInstance().setAccessibility(new KASmartAccessibility());
            sIsSupport = true;
        } else {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createSmartDefault());
            sIsSupport = false;
        }
    }

    private void initSamsung() {
        if (SAMSUNG_24.equals(String.valueOf(device.sdkInt))) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createSamsungV24());
            AliveOptionManager.getInstance().setAccessibility(new KASamsungAccessibility());
            sIsSupport = true;
        } else {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createSamsungDefault());
            sIsSupport = false;
        }
    }

    private void initSunMi() {
        sIsSupport = false;
        if (SUNMI_711.equals(String.valueOf(device.sdkStr))) {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createSunMi711());
        } else {
            AliveOptionManager.getInstance().initOptions(AliveOptionFactory.createDefault());
        }
    }
}
