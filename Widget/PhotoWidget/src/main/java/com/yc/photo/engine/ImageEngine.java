package com.yc.photo.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;


public interface ImageEngine {

    /**
     * Load thumbnail of a static image resource.
     *
     * @param context     Context
     * @param resize      Desired size of the origin image
     * @param placeholder Placeholder drawable when image is not loaded yet
     * @param imageView   ImageView widget
     * @param uri         Uri of the loaded image
     */
    void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri);

    /**
     * Load thumbnail of a gif image resource. You don't have to load an animated gif when it's only
     * a thumbnail tile.
     *
     * @param context     Context
     * @param resize      Desired size of the origin image
     * @param placeholder Placeholder drawable when image is not loaded yet
     * @param imageView   ImageView widget
     * @param uri         Uri of the loaded image
     */
    void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri);

    /**
     * Load a static image resource.
     *
     * @param context   Context
     * @param resizeX   Desired x-size of the origin image
     * @param resizeY   Desired y-size of the origin image
     * @param imageView ImageView widget
     * @param uri       Uri of the loaded image
     */
    void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri);

    /**
     * Load a gif image resource.
     *
     * @param context   Context
     * @param resizeX   Desired x-size of the origin image
     * @param resizeY   Desired y-size of the origin image
     * @param imageView ImageView widget
     * @param uri       Uri of the loaded image
     */
    void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri);

    /**
     * Whether this implementation supports animated gif.
     * Just knowledge of it, convenient for users.
     *
     * @return true support animated gif, false do not support animated gif.
     */
    boolean supportAnimatedGif();
}
