package com.yc.keyeventlib;


import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.keyeventlib.board.AndroidKeyBoard;
import com.yc.keyeventlib.board.KeyEventManager;
import com.yc.keyeventlib.board.ResultCallback;
import com.yc.keyeventlib.code.IKeyEvent;
import com.yc.toolutils.AppLogUtils;

public class KeyCodeActivity extends AppCompatActivity {

    private boolean isInit = false;
    private TextView tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_event);
        AppLogUtils.d("KeyCodeActivity : yc onCreate" );
        tvContent = findViewById(R.id.content);
        if (!isInit){
            KeyEventManager.getInstance().init(this);
            KeyEventManager.getInstance().setKeyboard(new AndroidKeyBoard());
            isInit = true;
        }
        KeyEventManager.getInstance().getKeyboard().addDispatchCallback(keyBoardCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyEventManager.getInstance().getKeyboard().removeDispatchCallback(keyBoardCallback);
        KeyEventManager.getInstance().getKeyboard().deInit();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AppLogUtils.d("KeyCodeActivity : yc onKeyUp " + keyCode);
        return super.onKeyUp(keyCode,event);
    }

    /**
     * 所有的View全部实现了该接口并重写了该方法，该方法用来捕捉手机键盘被按下的事件。
     * @param keyCode The value in event.getKeyCode().
     * @param event Description of the key event.
     *
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AppLogUtils.d("KeyCodeActivity : yc onKeyDown" );
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!isInit){
            KeyEventManager.getInstance().init(this);
            KeyEventManager.getInstance().setKeyboard(new AndroidKeyBoard());
            isInit = true;
        }
        AppLogUtils.d("KeyCodeActivity : yc dispatchKeyEvent" );
        KeyEventManager.getInstance().getKeyboard().dispatchKeyEvent(event.getKeyCode(),event);
        return super.dispatchKeyEvent(event);
    }


    private final ResultCallback<IKeyEvent> keyBoardCallback = new ResultCallback<IKeyEvent>() {
        @Override
        public boolean result(IKeyEvent keyEvent) {
            //键盘设备回调
            int keycode = keyEvent.getKeyEvent().getKeyCode();
            String msg = KeyEventManager.getInstance().getKeyMsg();
            AppLogUtils.d("KeyCodeActivity : yc result keycode " + keycode);
            AppLogUtils.d("KeyCodeActivity : yc onKeyUp msg " + msg);
            tvContent.setText("逗比"+msg);
            return false;
        }
    };
}
