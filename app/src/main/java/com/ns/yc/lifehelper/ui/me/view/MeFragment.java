package com.ns.yc.lifehelper.ui.me.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.comment.config.AppConfig;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.inter.listener.NoDoubleClickListener;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.ui.me.contract.MeFragmentContract;
import com.ns.yc.lifehelper.ui.me.presenter.MeFragmentPresenter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeCollectActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeFeedBackActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeLoginActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MePersonActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeQoneActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeSettingActivity;
import com.ns.yc.lifehelper.ui.me.view.activity.MeTimerActivity;
import com.ns.yc.lifehelper.utils.AppToolUtils;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import butterknife.BindView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 我的页面
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public class MeFragment extends BaseFragment<MeFragmentPresenter> implements
        View.OnClickListener , MeFragmentContract.View{


    @BindView(R.id.rl_me_timer)
    RelativeLayout rlMeTimer;
    @BindView(R.id.rl_me_qone)
    RelativeLayout rlMeQone;
    @BindView(R.id.rl_me_project)
    RelativeLayout rlMeProject;
    @BindView(R.id.rl_me_collect)
    RelativeLayout rlMeCollect;
    @BindView(R.id.rl_me_question)
    RelativeLayout rlMeQuestion;
    @BindView(R.id.rl_me_setting)
    RelativeLayout rlMeSetting;
    @BindView(R.id.rl_me_feed_back)
    RelativeLayout rlMeFeedBack;
    @BindView(R.id.tv_me_phone_number)
    TextView tvMePhoneNumber;
    @BindView(R.id.rl_me_phone)
    LinearLayout rlMePhone;
    @BindView(R.id.iv_person_image)
    ImageView ivPersonImage;
    @BindView(R.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R.id.ll_person)
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
    public int getContentView() {
        return R.layout.fragment_me;
    }

    @Override
    public void initView() {
        setRipperView();
    }

    private void setRipperView() {
        AppToolUtils.setRipper(rlMeTimer);
        AppToolUtils.setRipper(rlMeQone);
        AppToolUtils.setRipper(rlMeProject);
        AppToolUtils.setRipper(rlMeCollect);
        AppToolUtils.setRipper(rlMeQuestion);
        AppToolUtils.setRipper(rlMeSetting);
        AppToolUtils.setRipper(rlMeFeedBack);
        AppToolUtils.setRipper(rlMePhone);
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
        rlMePhone.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                LogUtils.e("避免重复点击，测试1秒是否调用几次");
                toCallMe();
            }
        });
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
                startActivity(MeTimerActivity.class);
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

                break;
            case R.id.iv_person_image:
                if(AppConfig.INSTANCE.isLogin()){
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
        Intent callIntent = IntentUtils.getCallIntent(number);
        startActivity(callIntent);
    }

}
