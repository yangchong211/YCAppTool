package com.yc.blurview.blur;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

import androidx.collection.LruCache;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/22
 * 描    述：模糊视图
 * 修订历史：
 * ================================================
 */
public class BlurBehind {

    private static final String CACHE_BLURRED_IMAGE = "CACHE_BLURRED_IMAGE";
    private static final int BLUR_RADIUS = 12;
    private static final int DEFAULT_ALPHA = 100;

    private static final LruCache<String, Bitmap> mImageCache = new LruCache<>(1);
    private CacheBlurExecuteTask CacheBlurExecuteTask;
    private int mAlpha = DEFAULT_ALPHA;
    private int mFilterColor = -1;

    private enum State {
        READY,
        EXECUTING
    }

    private State mState = State.READY;
    private static BlurBehind mInstance;

    public static BlurBehind getInstance() {
        if (mInstance == null) {
            mInstance = new BlurBehind();
        }
        return mInstance;
    }

    public void execute(Activity activity, OnBlurListener onBlurListener) {
        if (mState.equals(State.READY)) {
            mState = State.EXECUTING;
            CacheBlurExecuteTask = new CacheBlurExecuteTask(activity, onBlurListener);
            CacheBlurExecuteTask.execute();
        }
    }

    public BlurBehind withAlpha(int alpha) {
        this.mAlpha = alpha;
        return this;
    }

    public BlurBehind withFilterColor(int filterColor) {
        this.mFilterColor = filterColor;
        return this;
    }

    public void setBackground(Activity activity) {
        if (mImageCache.size() != 0) {
            BitmapDrawable bd = new BitmapDrawable(activity.getResources(), mImageCache.get(CACHE_BLURRED_IMAGE));
            bd.setAlpha(mAlpha);
            if (mFilterColor != -1) {
                bd.setColorFilter(mFilterColor, PorterDuff.Mode.DST_ATOP);
            }
            activity.getWindow().setBackgroundDrawable(bd);
            mImageCache.remove(CACHE_BLURRED_IMAGE);
            CacheBlurExecuteTask = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class CacheBlurExecuteTask extends AsyncTask<Void, Void, Void> {
        
        private Activity activity;
        private OnBlurListener onBlurListener;
        private View decorView;
        private Bitmap image;

        CacheBlurExecuteTask(Activity activity, OnBlurListener onBlurListener) {
            this.activity = activity;
            this.onBlurListener = onBlurListener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            decorView = activity.getWindow().getDecorView();
            decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            decorView.setDrawingCacheEnabled(true);
            decorView.buildDrawingCache();
            image = decorView.getDrawingCache();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap blurredBitmap = CustomBlur.apply(activity, image, BLUR_RADIUS);
            mImageCache.put(CACHE_BLURRED_IMAGE, blurredBitmap);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            decorView.destroyDrawingCache();
            decorView.setDrawingCacheEnabled(false);
            activity = null;
            onBlurListener.onBlurComplete();
            mState = State.READY;
        }
    }

    public interface OnBlurListener {
        void onBlurComplete();
    }
}
