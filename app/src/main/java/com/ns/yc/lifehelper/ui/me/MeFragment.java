package com.ns.yc.lifehelper.ui.me;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.main.MainActivity;
import com.ns.yc.lifehelper.ui.me.activity.MeLoginActivity;
import com.ns.yc.lifehelper.ui.me.activity.MeQoneActivity;
import com.ns.yc.lifehelper.ui.me.view.MeCollectActivity;
import com.ns.yc.lifehelper.ui.me.view.MeFeedBackActivity;
import com.ns.yc.lifehelper.ui.me.view.MePersonActivity;
import com.ns.yc.lifehelper.ui.me.view.MeSettingActivity;
import com.ns.yc.lifehelper.ui.other.timer.TimerActivity;
import com.pedaily.yc.ycdialoglib.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：我的页面
 * 修订历史：
 * ================================================
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.rl_me_timer)
    RelativeLayout rlMeTimer;
    @Bind(R.id.rl_me_qone)
    RelativeLayout rlMeQone;
    @Bind(R.id.rl_me_project)
    RelativeLayout rlMeProject;
    @Bind(R.id.rl_me_collect)
    RelativeLayout rlMeCollect;
    @Bind(R.id.rl_me_question)
    RelativeLayout rlMeQuestion;
    @Bind(R.id.rl_me_setting)
    RelativeLayout rlMeSetting;
    @Bind(R.id.rl_me_feed_back)
    RelativeLayout rlMeFeedBack;
    @Bind(R.id.tv_me_phone_number)
    TextView tvMePhoneNumber;
    @Bind(R.id.rl_me_phone)
    LinearLayout rlMePhone;
    @Bind(R.id.iv_person_image)
    ImageView ivPersonImage;
    @Bind(R.id.tv_person_name)
    TextView tvPersonName;
    @Bind(R.id.ll_person)
    LinearLayout llPerson;
    private MainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        rlMeTimer.setOnClickListener(this);
        rlMeQone.setOnClickListener(this);
        rlMeProject.setOnClickListener(this);
        rlMeCollect.setOnClickListener(this);
        rlMeQuestion.setOnClickListener(this);
        rlMeSetting.setOnClickListener(this);
        rlMeFeedBack.setOnClickListener(this);
        rlMePhone.setOnClickListener(this);
        ivPersonImage.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_me_timer:
                startActivity(TimerActivity.class);
                break;
            case R.id.rl_me_qone:
                startActivity(MeQoneActivity.class);
                break;
            case R.id.rl_me_project:
                ToastUtil.showStart(activity, 0);
                break;
            case R.id.rl_me_collect:
                startActivity(MeCollectActivity.class);
                break;
            case R.id.rl_me_setting:
                startActivity(MeSettingActivity.class);
                break;
            case R.id.rl_me_feed_back:
                startActivity(MeFeedBackActivity.class);
                break;
            case R.id.rl_me_phone:
                ToastUtils.showShortSafe("打电话");
                break;
            case R.id.iv_person_image:
                if(Constant.isLogin){
                    startActivity(MePersonActivity.class);
                }else {
                    startActivity(MeLoginActivity.class);
                }
                break;
        }
    }

}
