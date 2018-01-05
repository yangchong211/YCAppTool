package com.ns.yc.lifehelper.ui.me.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.ui.me.contract.MeFragmentContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeFragmentPresenter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeCollectActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeLoginActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MePersonActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeQoneActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeSettingActivity;
import com.ns.yc.lifehelper.ui.other.timer.TimerActivity;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/21
 * 描    述：我的页面
 * 修订历史：
 *          v1.5 17年9月8日修改
 * ================================================
 */
public class MeFragment extends BaseFragment implements View.OnClickListener , MeFragmentContract.View{


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
    private MeFragmentContract.Presenter presenter = new MeFragmentPresenter(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(activity!=null){
            activity = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
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
        presenter.getRedHotMessageData();
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
                toCallMe();
                break;
            case R.id.iv_person_image:
                if(Constant.isLogin){
                    startActivity(MePersonActivity.class);
                }else {
                    startActivity(MeLoginActivity.class);
                }
                break;
            default:
                break;
        }
    }


    private void toCallMe() {
        String number = tvMePhoneNumber.getText().toString().trim();
        Intent myIntentDial = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        startActivity(myIntentDial);
    }

    /*@Subscribe
    public void onEventMainThread(LoginSuccessEvent event) {
        if (null != event) {
            String msg = event.getMsg();
            tvPersonName.setText(String.format("网友%s", msg));
            ivPersonImage.setImageResource(R.drawable.image_default);
        }
    }*/

}
