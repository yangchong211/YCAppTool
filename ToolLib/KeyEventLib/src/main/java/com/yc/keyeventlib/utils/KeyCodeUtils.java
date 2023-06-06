package com.yc.keyeventlib.utils;

import android.view.KeyEvent;
import android.view.View;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;



public final class KeyCodeUtils {

    public static String getInput(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_NUMPAD_0:
                return "0";
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_NUMPAD_1:
                return "1";
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_NUMPAD_2:
                return "2";
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_NUMPAD_3:
                return "3";
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_NUMPAD_4:
                return "4";
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_NUMPAD_5:
                return "5";
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_NUMPAD_6:
                return "6";
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_NUMPAD_7:
                return "7";
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_NUMPAD_8:
                return "8";
            case KeyEvent.KEYCODE_9:
            case KeyEvent.KEYCODE_NUMPAD_9:
                return "9";
            case KeyEvent.KEYCODE_PERIOD:
            case KeyEvent.KEYCODE_NUMPAD_DOT:
                return ".";
            case KeyEvent.KEYCODE_DEL:
                return "cancel";
        }
        return "";
    }

}
