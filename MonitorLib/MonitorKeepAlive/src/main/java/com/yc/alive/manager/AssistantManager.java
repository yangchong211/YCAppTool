package com.yc.alive.manager;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.RestrictTo;

import com.yc.alive.OnSettingListener;
import com.yc.alive.constant.AliveErrorConst.CODE;
import com.yc.alive.constant.AliveSettingType.TYPE;
import com.yc.alive.model.AliveIntentModel;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.service.AccessibilityService;
import com.yc.alive.service.AliveOnePxService;
import com.yc.alive.util.AliveAppUtils;
import com.yc.alive.util.AliveExecutorUtils;
import com.yc.alive.util.AliveLogUtils;
import com.yc.alive.util.AlivePermissionUtils;

import java.util.LinkedList;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveErrorConst.ERROR_CODE_FIND_NODE;
import static com.yc.alive.constant.AliveErrorConst.ERROR_CODE_NO_PERMISSION;
import static com.yc.alive.constant.AliveErrorConst.ERROR_CODE_OPEN_FAIL;
import static com.yc.alive.constant.AliveErrorConst.getMessage;
import static com.yc.alive.constant.AliveSettingType.TYPE_ACCESSIBILITY_SERVICE;
import static com.yc.alive.constant.AliveSettingType.TYPE_BATTERY;
import static com.yc.alive.constant.AliveSettingType.TYPE_FLOAT_WINDOW;
import static com.yc.alive.constant.AliveSettingType.TYPE_NOTIFICATION;
import static com.yc.alive.constant.AliveSettingType.TYPE_SELF_START;
import static com.yc.alive.constant.AliveSettingType.TYPE_WIFI_NEVER_SLEEP;
import static com.yc.alive.util.AlivePermissionUtils.isFloatWindowEnabled;

/**
 * 自动助手管理类
 */
@RestrictTo(LIBRARY)
public class AssistantManager {

    private static final String TAG = "KAAssistantManager";

    private boolean mIsInit;
    private Context mContext;
    private String mAppName;
    private OnSettingListener mListener;
    private int mFloatWindowLayoutId;
    private LinkedList<AliveOptionModel> mOptionList = new LinkedList<>();

    private AssistantManager() {
    }

    private static final class InnerHolder {
        private static final AssistantManager INSTANCE = new AssistantManager();
    }

    public static AssistantManager getInstance() {
        return InnerHolder.INSTANCE;
    }

    public Context getContext() {
        return mContext;
    }

    public String getAppName() {
        return mAppName;
    }

    public int getFloatWindowLayoutId() {
        return mFloatWindowLayoutId;
    }

    /**
     * 初始化信息
     */
    public void init(Context context) {
        if (mIsInit) {
            return;
        }
        if (context == null) {
            return;
        }
        mContext = context.getApplicationContext();
        mAppName = AliveAppUtils.getAppName(mContext);
        mIsInit = true;
    }

    /**
     * 是否支持
     */
    public boolean isSupport() {
        if (notInit()) {
            return false;
        }

        return AliveDeviceManager.getInstance().isSupport();
    }

    /**
     * 设置悬浮窗布局
     */
    public void setFloatWindowLayoutId(@LayoutRes int layoutId) {
        this.mFloatWindowLayoutId = layoutId;
    }

    /**
     * 获取设备型号
     */
    public String getDeviceInfo() {
        if (notInit()) {
            return null;
        }

        return AliveDeviceManager.getInstance().getDevice().toString();
    }

    /**
     * 启动 1 像素 服务
     */
    public void startOnePxService() {
        if (notInit()) {
            return;
        }
        AliveOnePxService.launch(mContext);
    }

    /**
     * 辅助功能是否可用
     */
    public boolean isAccessibilityEnabled() {
        if (notInit()) {
            return false;
        }
        return AlivePermissionUtils.isAccessibilityServiceEnabled(mContext, AccessibilityService.class);
    }

    /**
     * wifi 从不休眠
     */
    public boolean isWifiNeverSleepEnabled() {
        if (notInit()) {
            return false;
        }
        return AlivePermissionUtils.isWifiNeverSleepEnabled(mContext);
    }

    /**
     * 通知功能是否可用
     */
    public boolean isNotificationEnabled() {
        if (notInit()) {
            return false;
        }
        return AlivePermissionUtils.isNotificationEnabled(mContext);
    }

    public AliveIntentModel getIntentModel(@TYPE int type) {
        isSupport();
        return AliveOptionManager.getInstance().getOption(type).intent;
    }

    /**
     * 自动设置
     *
     * @param listener 监听
     * @param types 类型
     */
    public void autoSetting(OnSettingListener listener, @TYPE int...types) {
        if (notInit()) {
            return;
        }

        mListener = listener;

        if (types == null || types.length == 0) {
            AliveLogUtils.d(TAG, "type == null");
            return;
        }

        if (!isSupport()) {
            AliveLogUtils.d(TAG, "isSupport == false");
            return;
        }

        mOptionList.clear();
        AliveOptionModel optionAccessibility = AliveOptionManager.getInstance().getOption(TYPE_ACCESSIBILITY_SERVICE);
        if (optionAccessibility == null) {
            AliveLogUtils.d(TAG, "optionAccessibility == null");
            return;
        }
        mOptionList.add(optionAccessibility);
        mOptionList.add(AliveOptionManager.getInstance().getOption(TYPE_FLOAT_WINDOW));
        for (int type : types) {
            if (type == TYPE_ACCESSIBILITY_SERVICE || type == TYPE_FLOAT_WINDOW) {
                continue;
            }
            AliveOptionModel option = AliveOptionManager.getInstance().getOption(type);
            mOptionList.add(option);
        }

        toSettingOptions();
    }

    private void toSettingOptions() {
        final AliveOptionModel option = mOptionList.pollFirst();
        if (option == null) {
            // 全部都执行完成
            // 关闭悬浮窗
            FloatWindowManager.getInstance().close();
            mListener.onAllCompleted();
            return;
        }

        switch (option.type) {
            case TYPE_ACCESSIBILITY_SERVICE: {
                // 1. 获取辅助功能权限
                boolean serviceEnabled =
                    AlivePermissionUtils.isAccessibilityServiceEnabled(mContext, AccessibilityService.class);
                if (!serviceEnabled) {
                    // 没有辅助功能权限，去开启
                    AliveOptionManager.getInstance().setCurrentOption(option);
                    boolean opened = AliveAppUtils.open(mContext, option);
                    if (!opened) {
                        processError(option.type, ERROR_CODE_OPEN_FAIL);
                    }
                } else {
                    // 有权限
                    processSuccess(TYPE_ACCESSIBILITY_SERVICE);
                    toSettingOptions();
                }
                break;
            }
            case TYPE_FLOAT_WINDOW: {
                // 2. 获取悬浮窗权限
                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {

                    private boolean floatWindowEnabled;

                    @Override
                    public void runWorker() {
                        floatWindowEnabled = isFloatWindowEnabled(mContext);
                        if (!floatWindowEnabled) {
                            AliveAppUtils.sleep(1000);
                            floatWindowEnabled = isFloatWindowEnabled(mContext);
                        }
                    }

                    @Override
                    public void runUI() {
                        if (!floatWindowEnabled) {
                            // 没有悬浮窗权限，去开启
                            AliveOptionManager.getInstance().setCurrentOption(option);
                            boolean opened = AliveAppUtils.open(mContext, option);
                            if (!opened) {
                                processError(option.type, ERROR_CODE_OPEN_FAIL);
                            }
                        } else {
                            // 有权限
                            // 显示悬浮窗
                            processSuccess(TYPE_FLOAT_WINDOW);
                            FloatWindowManager.getInstance().show(mContext);
                            toSettingOptions();
                        }
                    }
                });
                break;
            }
            case TYPE_NOTIFICATION:
            case TYPE_WIFI_NEVER_SLEEP:
            case TYPE_SELF_START:
            case TYPE_BATTERY: {
                AliveOptionManager.getInstance().setCurrentOption(option);
                if (option.isNotSupport) {
                    AliveLogUtils.d(TAG, "not support");
                    // 不支持，直接处理完成
                    notifyBack();
                    return;
                }
                boolean opened = AliveAppUtils.open(mContext, option);
                if (!opened) {
                    processError(option.type, ERROR_CODE_OPEN_FAIL);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 开启辅助功能后，每次返回到 APP 的页面都会调用这个方法 用于自动触发下一次的设置。
     */
    public void notifyBack() {
        AliveOptionModel option = AliveOptionManager.getInstance().getCurrentOption().clone();
        AliveOptionManager.getInstance().clearCurrentOption();
        if (option == null) {
            return;
        }

        int type = option.type;
        boolean failed = option.isFailed();
        if (failed) {
            // 自动查找节点失败
            processError(type, ERROR_CODE_FIND_NODE);
            // 设置下一项
            toSettingOptions();
            return;
        }

        switch (type) {
            case TYPE_ACCESSIBILITY_SERVICE: {
                // 设置辅助开关后, 重新判断权限
                boolean serviceEnabled =
                    AlivePermissionUtils.isAccessibilityServiceEnabled(mContext, AccessibilityService.class);
                AliveLogUtils.d(TAG, "notifyBack serviceEnabled " + serviceEnabled);
                if (serviceEnabled) {
                    // 获取权限成功，可以继续设置
                    processSuccess(type);
                    toSettingOptions();
                } else {
                    // 获取权限失败，报错
                    processError(type, ERROR_CODE_NO_PERMISSION);
                }
                break;
            }
            case TYPE_FLOAT_WINDOW: {
                // 设置悬浮窗权限后, 重新判断权限
                boolean hasPermission = isFloatWindowEnabled(mContext);
                if (hasPermission) {
                    // 获取权限成功
                    processSuccess(type);
                    // 显示悬浮窗
                    FloatWindowManager.getInstance().show(mContext);
                    // 继续设置
                    toSettingOptions();
                } else {
                    // 获取权限失败，报错
                    processError(type, ERROR_CODE_NO_PERMISSION);
                }
                break;
            }
            case TYPE_NOTIFICATION: {
                boolean notificationEnabled = AlivePermissionUtils.isNotificationEnabled(mContext);
                if (notificationEnabled) {
                    // 获取权限成功, 继续设置
                    processSuccess(type);
                    toSettingOptions();
                } else {
                    // 获取权限失败，报错
                    processError(type, ERROR_CODE_NO_PERMISSION);
                }
                break;
            }
            case TYPE_WIFI_NEVER_SLEEP: {
                boolean wifiNeverSleep = AlivePermissionUtils.isWifiNeverSleepEnabled(mContext);
                if (wifiNeverSleep) {
                    // 获取权限成功, 继续设置
                    processSuccess(type);
                    toSettingOptions();
                } else {
                    processError(type, ERROR_CODE_NO_PERMISSION);
                }
                break;
            }
            case TYPE_SELF_START:
                processSuccess(type);
                toSettingOptions();
                break;
            case TYPE_BATTERY:
                processSuccess(type);
                toSettingOptions();
                break;
            default:
                break;
        }
    }

    private void processSuccess(@TYPE int type) {
        if (mListener != null) {
            mListener.onSuccess(type);
        }
    }

    private void processError(@TYPE int type, @CODE int code) {
        AliveOptionManager.getInstance().clearCurrentOption();

        if (mListener != null) {
            mListener.onFailure(type, getMessage(code));
        }

        if (code == ERROR_CODE_OPEN_FAIL) {
            // 打开失败
            if (type == TYPE_ACCESSIBILITY_SERVICE) {
                // 如果是辅助功能，操作终止
                mOptionList.clear();
            } else {
                // 如果是其他，进行下一项操作
                toSettingOptions();
            }
        } else if (code == ERROR_CODE_NO_PERMISSION) {
            // 没有权限
            if (type == TYPE_ACCESSIBILITY_SERVICE) {
                // 如果是辅助功能，操作终止
                mOptionList.clear();
            } else {
                // 如果是其他，进行下一项操作
                toSettingOptions();
            }
        }
    }

    private boolean notInit() {
        if (!mIsInit) {
            AliveLogUtils.d(TAG, "not init");
            return true;
        }
        return false;
    }
}
