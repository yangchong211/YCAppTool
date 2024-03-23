package com.yc.keyeventlib.board;

import static android.content.Context.INPUT_SERVICE;

import android.content.Context;
import android.hardware.input.InputManager;
import android.hardware.input.InputManager.InputDeviceListener;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.yc.keyeventlib.code.IKeyEvent;
import com.yc.toolutils.system.AppLogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 按键相关的实现类
 */
public abstract class DispatchEventImpl implements IDispatchEvent {

    public final static String TAG = DispatchEventImpl.class.getSimpleName();
    //实现的按键类
    private IKeyEvent keyEvent;
    //按键发现和接触回调
    private final List<ResultCallback<HashMap<Integer, InputDevice>>> attachCallbacks = new ArrayList<>();
    //按键事件回调
    private final List<ResultCallback<IKeyEvent>> keyEventCallbacks = new ArrayList<>();
    //所有外接键盘的容器
    private final HashMap<Integer, InputDevice> keyBoardMap = new HashMap<>();
    //输入设备相关
    private InputManager mIm;
    //上次按回车的时间，防抖动策略
    private long lastEnterTime;
    //防斗动时间间隔
    private long antiTime = 5;
    //监听回调
    private final InputDeviceListener inputDeviceListener = new InputDeviceListener() {
        @Override
        public void onInputDeviceAdded(int deviceId) {
            AppLogUtils.v(TAG, "onInputDeviceAdded device.deviceId()=" + deviceId);
            InputDevice device = InputDevice.getDevice(deviceId);
            if (!keyBoardMap.containsKey(deviceId)) {
                keyBoardMap.put(deviceId, device);
            }
            AppLogUtils.v(TAG, "device.getName()=" + device.getName() + " device.getId() ");
            onAttachDevices(keyBoardMap);
        }

        @Override
        public void onInputDeviceRemoved(int deviceId) {
            AppLogUtils.v(TAG, "onInputDeviceRemoved device.deviceId()=" + deviceId);
            keyBoardMap.remove(deviceId);
            onAttachDevices(keyBoardMap);
        }

        @Override
        public void onInputDeviceChanged(int deviceId) {
            AppLogUtils.v(TAG, "onInputDeviceChanged device.deviceId()=" + deviceId);
        }
    };

    @Override
    public void init(Context context) {
        mIm = (InputManager) context.getApplicationContext().getSystemService(INPUT_SERVICE);
        if (mIm != null) {
            mIm.registerInputDeviceListener(inputDeviceListener, null);
        }
        final int[] devices = InputDevice.getDeviceIds();
        for (int j : devices) {
            AppLogUtils.v(TAG, "isKeyboard-" + j);
            InputDevice device = InputDevice.getDevice(j);
            AppLogUtils.v(TAG, "isKeyboard-" + device.getName());
            if (!keyBoardMap.containsKey(device.getId())) {
                keyBoardMap.put(device.getId(), device);
                AppLogUtils.v(TAG, "initKeyboard-" + device.getId());
            }
        }
        onAttachDevices(keyBoardMap);
    }

    @Override
    public boolean dispatchKeyEvent(int keyCode, KeyEvent event) {
        int size = keyEventCallbacks.size();
        //从后面开始
        for (int i = 0; i < size; i++) {
            ResultCallback<IKeyEvent> keyEventCallback = keyEventCallbacks.get(i);
            //先做内部转换
            if (keyEvent == null) {
                keyEvent = getKeyEvent();
            }
            keyEvent.setKeyCode(keyCode);
            keyEvent.setKeyEvent(event);
            //部分场景下会消费按键事件
            if (keyEvent == null) {
                return true;
            }
            if (keyEventCallback.result(keyEvent)) {
                //只要有一个拦截下来了就不继续回传了
                return true;
            }
        }
        //回调
        return false;
    }

    @Override
    public IKeyEvent handleAttachKeyEvent(IKeyEvent iKeyEvent, boolean enableNumToNavigation) {
        if (iKeyEvent.isEnter()) {
            if (System.currentTimeMillis() - lastEnterTime < antiTime) {
                return null;
            }
            lastEnterTime = System.currentTimeMillis();
        }
        int keyCode = iKeyEvent.getKeyCode();
        //数字4
        if (iKeyEvent.isNum4() && enableNumToNavigation) {
            keyCode = KeyEvent.KEYCODE_DPAD_UP;
        }
        //数字6
        if (iKeyEvent.isNum6() && enableNumToNavigation) {
            keyCode = KeyEvent.KEYCODE_DPAD_DOWN;
        }
        //数字8
        if (iKeyEvent.isNum8() && enableNumToNavigation) {
            keyCode = KeyEvent.KEYCODE_DPAD_UP;
        }
        //数字2
        if (iKeyEvent.isNum2() && enableNumToNavigation) {
            keyCode = KeyEvent.KEYCODE_DPAD_DOWN;
        }
        //DELETE转返回
        if (iKeyEvent.isDelete()) {
            keyCode = KeyEvent.KEYCODE_ESCAPE;
        }
        //更新事件
        iKeyEvent.setKeyCode(keyCode);
        iKeyEvent.setKeyEvent(new KeyEvent(iKeyEvent.getKeyEvent().getAction(), keyCode));
        return iKeyEvent;
    }

    @Override
    public void addAttachCallback(ResultCallback<HashMap<Integer, InputDevice>> attachCallback) {
        if (!attachCallbacks.contains(attachCallback)) {
            attachCallbacks.add(attachCallback);
        }
    }

    @Override
    public void rmAttachCallback(ResultCallback<HashMap<Integer, InputDevice>> attachCallback) {
        attachCallbacks.remove(attachCallback);
    }

    @Override
    public void addDispatchCallback(ResultCallback<IKeyEvent> keyEventCallback) {
        if (!keyEventCallbacks.contains(keyEventCallback)) {
            keyEventCallbacks.add(keyEventCallback);
        }
    }

    @Override
    public void removeDispatchCallback(ResultCallback<IKeyEvent> keyEventCallback) {
        keyEventCallbacks.remove(keyEventCallback);
    }

    @Override
    public void deInit() {
        if (mIm != null) {
            mIm.unregisterInputDeviceListener(inputDeviceListener);
        }
    }

    public void setAntiTime(long antiTime) {
        this.antiTime = antiTime;
    }

    /**
     * 外接键盘处理
     *
     * @param deviceHashMap 外接USB键盘
     */
    private void onAttachDevices(HashMap<Integer, InputDevice> deviceHashMap) {
        for (ResultCallback<HashMap<Integer, InputDevice>> commonCallback : attachCallbacks) {
            commonCallback.result(deviceHashMap);
        }
    }

    /**
     * 转化为按键的处理能力
     *
     * @param keyCode keycode码
     * @param event   event事件
     * @return 按键能力
     */
    public IKeyEvent toKeyEvent(int keyCode, KeyEvent event) {
        IKeyEvent ikeyEvent = this.getKeyEvent();
        ikeyEvent.setKeyCode(keyCode);
        ikeyEvent.setKeyEvent(event);
        return ikeyEvent;
    }
}
