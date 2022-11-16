package com.yc.animbusiness;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2022/5/17
 *     desc   : 帧动画
 *     revise :
 * </pre>
 */
public class FrameAnimation extends AppCompatActivity {

    private TextView tvAnim1;
    private TextView tvAnim2;
    private ImageView ivFrameAnim1;
    private ImageView ivFrameAnim2;
    private boolean isStart = false;
    private boolean isStart2 = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_anim);

        tvAnim1 = findViewById(R.id.tv_anim_1);
        tvAnim2 = findViewById(R.id.tv_anim_2);
        ivFrameAnim1 = findViewById(R.id.iv_frame_anim1);
        ivFrameAnim2 = findViewById(R.id.iv_frame_anim2);

        AnimationDrawable rocketAnimation = (AnimationDrawable) ivFrameAnim1.getBackground();
        tvAnim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart){
                    rocketAnimation.start();
                    ToastUtils.showRoundRectToast("开始帧动画");
                } else {
                    rocketAnimation.stop();
                    ToastUtils.showRoundRectToast("停止帧动画");
                }
                isStart = !isStart;
            }
        });

        tvAnim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationDrawable animationDrawable = new AnimationDrawable();
                for (int i = 1; i < 3; i++) {
                    int id = getResources().getIdentifier("icon_audio_" + i, "drawable", getPackageName());
                    Drawable drawable = FrameAnimation.this.getResources().getDrawable(id);
                    if (null != drawable) {
                        animationDrawable.addFrame(drawable, 300);
                    }
                }
                ivFrameAnim2.setImageDrawable(animationDrawable);
                if (!isStart2){
                    animationDrawable.start();
                    ToastUtils.showRoundRectToast("开始帧动画");
                } else {
                    animationDrawable.stop();
                    ToastUtils.showRoundRectToast("停止帧动画");
                }
                isStart2 = !isStart2;
            }
        });

    }
}
