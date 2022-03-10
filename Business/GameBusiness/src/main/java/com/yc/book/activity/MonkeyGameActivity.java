package com.yc.book.activity;


import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yc.book.weight.monkey.LuckyMonkeyPanelView;

import com.yc.library.base.mvp.BaseActivity;
import com.yc.statusbar.bar.StateAppBar;
import com.ycbjie.book.R;

import java.util.Random;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/6/23
 *     desc  : 老虎机抽奖活动
 *     revise:
 * </pre>
 */
public class MonkeyGameActivity extends BaseActivity {


    private LuckyMonkeyPanelView lucky_panel;
    private Button btn_action;

    @Override
    public int getContentView() {
        return R.layout.activity_monkey_game;
    }

    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this,true);
        lucky_panel = findViewById(R.id.lucky_panel);
        btn_action = findViewById(R.id.btn_action);
    }

    @Override
    public void initListener() {
        btn_action.setOnClickListener(v -> {
            if (!lucky_panel.isGameRunning()) {
                lucky_panel.startGame();
                btn_action.setText("暂停");
            } else {
                int stayIndex = new Random().nextInt(8);
                lucky_panel.tryToStop(stayIndex);
                btn_action.setText("开始");
            }
        });
    }

    @Override
    public void initData() {

    }
}
