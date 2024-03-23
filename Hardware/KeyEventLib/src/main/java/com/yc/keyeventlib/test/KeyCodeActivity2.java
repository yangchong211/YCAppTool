package com.yc.keyeventlib.test;


import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.keyeventlib.R;
import com.yc.keyeventlib.code.KeyCodeHelper;
import com.yc.keyeventlib.code.KeyEventImpl;
import com.yc.toolutils.system.AppLogUtils;

public class KeyCodeActivity2 extends AppCompatActivity {

    private TextView tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_event);
        AppLogUtils.d("KeyCodeActivity : yc onCreate" );
        tvContent = findViewById(R.id.content);
        KeyCodeHelper.getInstance().setKeycodeImpl(new KeyEventImpl());
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        AppLogUtils.d("KeyCodeActivity : yc onKeyUp " + keyCode);
        String msg = KeyCodeHelper.getInstance().getKeyMsg(keyCode);
        AppLogUtils.d("KeyCodeActivity : yc onKeyUp msg " + msg);
        tvContent.setText(msg);
        return super.onKeyUp(keyCode,event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AppLogUtils.d("KeyCodeActivity : yc onKeyDown" );
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

}
