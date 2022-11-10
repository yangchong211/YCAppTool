package com.yc.apptool.transition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.fragment.app.FragmentActivity;

import com.yc.apptool.R;
import com.yc.transition.TransitionCallback;
import com.yc.transition.TransitionController;
import com.yc.transition.TransitionParam;
import com.yc.transition.TransitionUtils;


public class VideoPlayActivity extends FragmentActivity {

    private static final String ANIM_PARAM = "ANIM_PARAM";

    /**
     * 封面图
     */
    private ImageView mIvCover;

    /**
     * 外部控件位置参数
     */
    private TransitionParam targetAnimBean;

    private TransitionController transitionController;

    public static void intentStart(Context context, TransitionParam animBean) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(ANIM_PARAM, animBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setupView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (transitionController != null) {
            transitionController.transitionRelease();
        }
    }

    private void setupView() {
        mIvCover = (ImageView) findViewById(R.id.video_cover);
        targetAnimBean = getIntent().getParcelableExtra(ANIM_PARAM);
        transitionController = new TransitionController.Builder()
                .with(findViewById(R.id.main_root_layer))
                .setInterpolator(PathInterpolatorCompat.create(0.32F, 0.94F, 0.6F, 1.0F))
                .duration(320)
                .build();
        transitionController.transitionEnter(targetAnimBean, new TransitionCallback() {
            @Override
            public void onTransitionStop() {

            }
        });
        mIvCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (targetAnimBean != null) {
            transitionController.transitionExit(new TransitionCallback() {
                @Override
                public void onTransitionStop() {
                    finish();
                }
            });
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        TransitionUtils.finishTransition(this);
    }
}
