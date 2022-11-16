package com.yc.animbusiness.reveal;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yc.animbusiness.R;

public class RevealAnimationActivity extends AppCompatActivity {

    boolean flag;
    FloatingActionButton fab;
    private View mPuppet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_animation);
        initToolbar();
        mPuppet = findViewById(R.id.view_puppet);
        flag = mPuppet.getVisibility() == View.VISIBLE;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                launchRevealAnimation();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void launchRevealAnimation() {
        Animation animation = mPuppet.getAnimation();
        if (animation != null) {
            animation.cancel();
        }

        int[] vLocation = new int[2];
        fab.getLocationInWindow(vLocation);
        int centerX = vLocation[0] + fab.getWidth() / 2;
        int centerY = vLocation[1] + fab.getHeight() / 2;

        View view = findViewById(R.id.toolbar_layout);
        int hypotenuse = (int) Math.hypot(view.getWidth(), view.getHeight());

        if (flag) {
            final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mPuppet, centerX, centerY, hypotenuse, 0);
            circularReveal.setDuration(2000);
            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mPuppet.setVisibility(View.GONE);
                    circularReveal.removeListener(this);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            circularReveal.start();
            flag = false;
        } else {
            final Animator circularReveal = ViewAnimationUtils.createCircularReveal(mPuppet, centerX, centerY, 0, hypotenuse);
            circularReveal.setDuration(2000);
            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    circularReveal.removeListener(this);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mPuppet.setVisibility(View.VISIBLE);
            circularReveal.start();
            flag = true;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        }else if (i == R.id.action_settings){
            startActivity(new Intent(RevealAnimationActivity.this, RevealEffectActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reveal_animation, menu);
        return true;
    }
}
