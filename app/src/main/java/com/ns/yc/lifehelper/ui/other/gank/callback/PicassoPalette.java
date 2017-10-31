package com.ns.yc.lifehelper.ui.other.gank.callback;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.util.Pair;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;


public class PicassoPalette implements Target, Callback {

    public interface CallBack{
        void onPaletteLoaded(Palette palette);
    }

    private LruCache<String,Palette> cache = new LruCache<>(40);

    private static final String TAG = "PicassoPalette";

    public static class Profile {
        public static final int VIBRANT = 0;
        public static final int VIBRANT_DARK = 1;
        public static final int VIBRANT_LIGHT = 2;
        public static final int MUTED = 3;
        public static final int MUTED_DARK = 4;
        public static final int MUTED_LIGHT = 5;

        @IntDef({VIBRANT, VIBRANT_DARK, VIBRANT_LIGHT, MUTED, MUTED_DARK, MUTED_LIGHT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface PaletteProfile {
        }
    }

    public static class Swatch {
        public static final int RGB = 0;
        public static final int TITLE_TEXT_COLOR = 1;
        public static final int BODY_TEXT_COLOR = 2;

        @IntDef({RGB, TITLE_TEXT_COLOR, BODY_TEXT_COLOR})
        @Retention(RetentionPolicy.SOURCE)
        public @interface PaletteSwatch {
        }
    }

    private PicassoPalette() {
    }

    private ImageView imageView;
    private String url;
    private Callback callback;

    private LinkedList<PaletteTarget> targets = new LinkedList<>();
    private ArrayList<CallBack> callbacks = new ArrayList<>();

    public static PicassoPalette with(String url, ImageView imageView) {
        PicassoPalette picassoPalette = new PicassoPalette();
        picassoPalette.url = url;
        picassoPalette.imageView = imageView;
        return picassoPalette;
    }

    public PicassoPalette use(@Profile.PaletteProfile int paletteProfile) {
        this.targets.add(new PaletteTarget(paletteProfile));
        return this;
    }

    public PicassoPalette intoBackground(View view) {
        return this.intoBackground(view, Swatch.RGB);
    }

    public PicassoPalette intoBackground(View view, @Swatch.PaletteSwatch int paletteSwatch) {
        if(this.targets.isEmpty())
            throw new UnsupportedOperationException("You must specify a palette with use(Profile.PaletteProfile)");

        this.targets.getLast().targetsBackground.add(new Pair<>(view, paletteSwatch));
        return this;
    }

    public PicassoPalette intoTextColor(TextView textView) {
        return this.intoBackground(textView, Swatch.TITLE_TEXT_COLOR);
    }

    public PicassoPalette intoTextColor(TextView textView, @Swatch.PaletteSwatch int paletteSwatch) {
        if(this.targets.isEmpty())
            throw new UnsupportedOperationException("You must specify a palette with use(Profile.PaletteProfile)");

        this.targets.getLast().targetsText.add(new Pair<>(textView, paletteSwatch));
        return this;
    }

    public PicassoPalette setPicassoCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public PicassoPalette intoCallBack(PicassoPalette.CallBack callBack){
        if(callBack != null)
            callbacks.add(callBack);
        return this;
    }


    //region apply
    private void apply(Palette palette) {
        for(PicassoPalette.CallBack c : callbacks){
            c.onPaletteLoaded(palette);
        }

        for(PaletteTarget target : targets) {
            Palette.Swatch swatch = null;
            switch (target.paletteProfile) {
                case Profile.VIBRANT:
                    swatch = palette.getVibrantSwatch();
                    break;
                case Profile.VIBRANT_DARK:
                    swatch = palette.getDarkVibrantSwatch();
                    break;
                case Profile.VIBRANT_LIGHT:
                    swatch = palette.getLightVibrantSwatch();
                    break;
                case Profile.MUTED:
                    swatch = palette.getMutedSwatch();
                    break;
                case Profile.MUTED_DARK:
                    swatch = palette.getDarkMutedSwatch();
                    break;
                case Profile.MUTED_LIGHT:
                    swatch = palette.getLightMutedSwatch();
                    break;
            }

            if (swatch != null) {
                for (Pair<View, Integer> t : target.targetsBackground) {
                    int color = getColor(swatch, t.second);
                    t.first.setBackgroundColor(color);
                }

                for (Pair<TextView, Integer> t : target.targetsText) {
                    int color = getColor(swatch, t.second);
                    t.first.setTextColor(color);
                }

                target.clear();
                this.callbacks = null;
            }
        }
    }

    private static int getColor(Palette.Swatch swatch, @Swatch.PaletteSwatch int paletteSwatch) {
        if (swatch != null) {
            switch (paletteSwatch) {
                case Swatch.RGB:
                    return swatch.getRgb();
                case Swatch.TITLE_TEXT_COLOR:
                    return swatch.getTitleTextColor();
                case Swatch.BODY_TEXT_COLOR:
                    return swatch.getBodyTextColor();
            }
        }else{
            Log.e(TAG,"error while generating Palette, null palette returned");
        }
        return 0;
    }

    private void start(Bitmap bitmap) {
        if(cache.get(url) != null){
            PicassoPalette.this.apply(cache.get(url));
        }
        else {
            new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    cache.put(url, palette);
                    PicassoPalette.this.apply(palette);
                }
            });
        }
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        start(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    @Override
    public void onSuccess() {
        if(this.callback != null)
            this.callback.onSuccess();
        Bitmap bitmap = ((BitmapDrawable) this.imageView.getDrawable()).getBitmap();
        onBitmapLoaded(bitmap, null);
    }

    @Override
    public void onError() {
        if(this.callback != null)
            this.callback.onError();
    }

}
