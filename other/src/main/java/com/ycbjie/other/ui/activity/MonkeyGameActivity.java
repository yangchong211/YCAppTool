package com.ycbjie.other.ui.activity;


import android.widget.Button;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;
import com.ycbjie.other.weight.LuckyMonkeyPanelView;

import java.util.Random;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/6/23
 *     desc  : 老虎机抽奖活动
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_MONKEY_ACTIVITY)
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
