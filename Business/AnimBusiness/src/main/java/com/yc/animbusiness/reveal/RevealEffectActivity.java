package com.yc.animbusiness.reveal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yc.animbusiness.R;


public class RevealEffectActivity extends AppCompatActivity implements View.OnClickListener {

    boolean flag = false;
    FloatingActionButton fab;
    Toolbar toolbar;
    private View mPuppet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_effect);
        initToolbar();

        mPuppet = findViewById(R.id.view_puppet);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        doRevealAnimation();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void doRevealAnimation() {
        int[] vLocation = new int[2];
        fab.getLocationInWindow(vLocation);
        int centerX = vLocation[0] + fab.getMeasuredWidth() / 2;
        int centerY = vLocation[1] + fab.getMeasuredHeight() / 2;

        int height = mPuppet.getHeight();
        int width = mPuppet.getWidth();
        int maxRradius = (int) Math.hypot(height, width);
        Log.e("hei", maxRradius + "");

        if (flag) {
            Animator animator = ViewAnimationUtils.createCircularReveal(mPuppet, centerX, centerY, maxRradius, 0);
            animator.setDuration(1000);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mPuppet.setVisibility(View.GONE);
                }
            });
            animator.start();
            flag = false;
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(mPuppet, centerX, centerY, 0, maxRradius);
            animator.setDuration(1000);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mPuppet.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
            flag = true;
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
