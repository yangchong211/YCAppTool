package com.yc.animbusiness;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yc.animation.AnimatorCompat;
import com.yc.animation.animator.Animator;
import com.yc.animation.listener.AbsAnimatorListener;
import com.yc.animation.listener.AnimatorUpdateListener;
import com.yc.animation.utils.LoggerUtil;
import com.yc.library.base.mvp.BaseActivity;

public class AnimMainActivity extends BaseActivity implements View.OnClickListener {

    private Animator animator;
    private ConstraintLayout root;
    private TextView text;
    private Button start;
    private Button stop;
    private Button change;
    private Button reverse;
    private ImageView imageSmall;
    private ImageView imageLarge;
    private Switch switch1;
    private TextView topView2;
    private TextView topView1;
    private TextView vector;
    private TextView customView;
    private Switch switch2;
    private int type = 0;


    @Override
    public int getContentView() {
        return R.layout.activity_anim_lib;
    }

    @Override
    public void initView() {
        initFindById();
    }

    private void initFindById() {
        root = findViewById(R.id.root);
        text = findViewById(R.id.text);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        change = findViewById(R.id.change);
        reverse = findViewById(R.id.reverse);
        imageSmall = findViewById(R.id.imageView_small);
        imageLarge = findViewById(R.id.imageView_large);
        switch1 = findViewById(R.id.switch1);
        topView2 = findViewById(R.id.topView2);
        topView1 = findViewById(R.id.topView1);
        vector = findViewById(R.id.vector);
        customView = findViewById(R.id.custom_view);
        switch2 = findViewById(R.id.switch2);


        this.topView1.setVisibility(View.GONE);
        this.topView2.setVisibility(View.GONE);
    }

    @Override
    public void initListener() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        vector.setOnClickListener(this);
        change.setOnClickListener(this);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchViews(isChecked);
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AnimatorCompat.doRealAnimation();
                } else {
                    AnimatorCompat.doFakeAnimation();
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == start){
            playAnimation();
        } else if (v == stop){
            stopAnimation();
        } else if (v == vector){

        } else if (v == change){
            if (type>8){
                type = 0;
            } else {
                type ++;
            }
            playAnimation();
        }
    }


    private void stopAnimation() {
        try {
            this.animator.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void switchViews(boolean isChecked) {
        this.animator = null;
        if (isChecked) {
            imageSmall.setVisibility(View.GONE);
            imageLarge.setVisibility(View.GONE);
            text.setVisibility(View.GONE);

            topView1.setVisibility(View.VISIBLE);
            topView2.setVisibility(View.GONE);
        } else {
            imageSmall.setVisibility(View.VISIBLE);
            imageLarge.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);

            topView1.setVisibility(View.GONE);
            topView2.setVisibility(View.GONE);
        }
    }

    public void playAnimation() {
        if (switch1.isChecked()) {
            playDemoAnimation();
        } else {
            playSampleAnimation();
        }
    }

    private void playDemoAnimation() {
        if (this.animator != null) {
            this.animator.start();
            return;
        }
        this.animator = AnimatorCompat
                .play(topView1).translateOutToTop()
                .with(topView2).translateInFromTop()
                .build()
                .start();
    }

    private void playSampleAnimation() {
        switch (type){
            case 0:
                // ValueAnimator的使用
                showValueAnimatorSample();
                break;
            case 1:
                // 单个view的自定义动画
                showSingleViewWithMultiPropertySample();
                break;
            case 2:
                // 多view的自定义动画组合
                showMultiViewWithMultiPropertySample();
                break;
            case 3:
                // 单个view的常用动画效果
                showSingleViewWithCommonAnimationSample();
                break;
            case 4:
                // 多view的常用动画效果组合
                showMultiViewWithCommonAnimationSample();
                break;
            case 5:
                // 缓动函数效果展示
                showEasingFunctionSample();
                break;
            case 6:
                // 自定义属性
                showCustomPropertySample();
                break;
            case 7:
                // Animation Listener
                showAnimationListenerSample();
                break;
            case 8:
                // 自定义动画播放序列
                showCustomPlaySequenceSample();
                break;
            default:
                break;
        }
    }

    /** 单个 view 的自定义动画 */
    private void showSingleViewWithMultiPropertySample() {
        AnimatorCompat
//                .play(imageSmall)
//                .rotate(0f, 180f)
//                .play(imageLarge)
//                .translateX(0f, 200f)
//                .translateY(0f, 200f)
                .play(text)
                .alpha(1f, 0.2f)
                .scaleX(0f, 1f)
//                .x(text.getX(), 0f)
//                .y(text.getY(), 0f)
                .duration(1000)
                .decelerate()
                .build()
                .start();
    }

    /** 自定义动画的多 view 组合 */
    private void showMultiViewWithMultiPropertySample() {
        AnimatorCompat
                .play(imageLarge).rotate(0f, 360).alpha(0f, 1f)
                .before(text).alpha(0f, 1f).scaleX(0f, 1f).startDelay(2000)
                .after(imageSmall).rotate(360f, 0f).alpha(0f, 1f)
                .build()
                .start();
    }

    /** 单个 view 的常用动画效果 */
    private void showSingleViewWithCommonAnimationSample() {
        AnimatorCompat
                .play(imageSmall).fadeIn()
//                .play(imageLarge).fadeOut()
//                .play(imageSmall).translateInFromTop()
//                .play(imageSmall).translateOutToTop()
//                .play(text).translateInFromBottom()
//                .play(text).translateOutToBottom()
//                .play(text).scaleIn()
//                .play(text).scaleOut()
//                .play(imageSmall).rotateCW()
//                .play(imageLarge).rotateCCW()
                .build()
                .start();
    }

    /** 常用动画效果的多 view 组合 */
    private void showMultiViewWithCommonAnimationSample() {
        AnimatorCompat
                .play(imageSmall).translateInFromTop()
                .with(imageLarge).translateInFromTop()
                .before(text).scaleIn()
                .build()
                .start();
    }

    /**
     * 缓动函数的效果展示：内部是用自定义Evaluator来实现
     * 缓动函数的其他效果参见：https://github.com/daimajia/AnimationEasingFunctions
     */
    private void showEasingFunctionSample() {
        AnimatorCompat
                .play(imageSmall).translateInFromTop().bounceEaseOut()
                .build()
                .start();
    }

    /** 自定义属性 */
    private void showCustomPropertySample() {
        AnimatorCompat
                .play(customView).property("Scale", 0f, 0.6f)
                .build()
                .start();
    }

    /** AnimationListener的使用 */
    private void showAnimationListenerSample() {
        AnimatorCompat
                .play(imageSmall).rotate(0f, 360).alpha(0f, 1f)
                .withListener(new AbsAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator, View view) {
                        super.onAnimationStart(animator, view);
                        LoggerUtil.d("动画事件", "onAnimationStart " + animator + " view: " + view);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator, View view) {
                        super.onAnimationEnd(animator, view);
                        LoggerUtil.d("动画事件", "onAnimationEnd " + animator + " view: " + view);
                    }
                })
                .before(text).alpha(0f, 1f).scaleX(0f, 1f).startDelay(2000)
                .withListener(new AbsAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator, View view) {
                        super.onAnimationStart(animator, view);
                        LoggerUtil.d("动画事件", "onAnimationStart " + animator + " view: " + view);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator, View view) {
                        super.onAnimationEnd(animator, view);
                        LoggerUtil.d("动画事件", "onAnimationEnd " + animator + " view: " + view);
                    }
                })
                .buildWithListener(new AbsAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator, View view) {
                        super.onAnimationStart(animator, view);
                        LoggerUtil.d("动画事件", "onAnimationStart " + animator + " view: " + view);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator, View view) {
                        super.onAnimationEnd(animator, view);
                        LoggerUtil.d("动画事件", "onAnimationEnd " + animator + " view: " + view);
                    }
                })
                .start();
    }

    /** 自定义动画播放序列 */
    private void showCustomPlaySequenceSample() {
        Animator animator1 = AnimatorCompat
                .play(imageSmall).rotate(0f, 360).alpha(0f, 1f)
                .before(text).alpha(0f, 1f).scaleX(0f, 1f).build();
        Animator animator2 = AnimatorCompat
                .play(imageLarge).rotateCCW()
                .before(customView).property("Scale", 0f, 0.6f).build();
        AnimatorCompat.playTogether(animator1, animator2).start();
//        SofaAnimatorCompat.playSequentially(animator1, animator2).start();
    }

    /** ValueAnimator的使用 */
    private void showValueAnimatorSample() {
        Animator animator1 = AnimatorCompat.valueAnimation().overshoot().duration(1000)
                .withListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(Animator animator, Object animatedValue) {
                        LoggerUtil.d("动画事件", "onAnimationUpdate animator: " + animator + " animatedValue: " + animatedValue);
                        imageSmall.setScaleX((Float) animatedValue);
                        imageSmall.setScaleY((Float) animatedValue);
                    }
                }).build();
        /*animator1.start();*/
        Animator animator2 = AnimatorCompat.valueAnimation().duration(1000)
                .withListener(new AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(Animator animator, Object animatedValue) {
                        LoggerUtil.d("动画事件", "onAnimationUpdate animator: " + animator + " animatedValue: " + animatedValue);
                        imageLarge.setAlpha((Float) animatedValue);
                    }
                }).build();
//        SofaAnimatorCompat.playTogether(animator1, animator2).start();
        AnimatorCompat
                .playSequentially(animator1, animator2)
                .start();
    }


    private void showUseCase(View mForegroundLayout, View mForegroundInfoLayout, int mForegroundViewWidth) {
        // Use library API
        AnimatorCompat
                .play(mForegroundLayout).translateX(mForegroundViewWidth).fadeOut().duration(300).decelerate()
                .before(mForegroundInfoLayout).scaleX(0.2f, 1f).scaleY(0.2f, 1f).alpha(0.2f, 1f).duration(300).overshoot().startDelay(100)
                .build()
                .start();
    }

}