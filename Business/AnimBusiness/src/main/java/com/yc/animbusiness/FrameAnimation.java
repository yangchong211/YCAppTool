package com.yc.animbusiness;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
                } else {
                    rocketAnimation.stop();
                }
                isStart = !isStart;
            }
        });

    }
}
