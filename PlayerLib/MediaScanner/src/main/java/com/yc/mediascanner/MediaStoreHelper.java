package com.yc.mediascanner;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.yc.toolutils.context.AppToolUtils;


public final class MediaStoreHelper {

    private MediaStoreHelper() {
        throw new AssertionError();
    }

    /**
     * 扫描本地的音频文件。
     *
     * @param resolver ContentResolver 对象，不能为 null
     * @param decoder  {@link Decoder} 对象，不能为 null
     * @param <T>      媒体文件对应的实体类型
     * @return {@link Scanner} 对象，调用该对象的 {@code scan()} 方法即可开始扫描本地媒体文件
     */
    public static <T> Scanner<T> scanAudio(@NonNull ContentResolver resolver, @NonNull Decoder<T> decoder) {
        AppToolUtils.checkNotNull(resolver);
        AppToolUtils.checkNotNull(decoder);
        return new AudioScanner<>(resolver, decoder);
    }

    /**
     * 扫描本地的视频文件。
     *
     * @param resolver ContentResolver 对象，不能为 null
     * @param decoder  {@link Decoder} 对象，不能为 null
     * @param <T>      媒体文件对应的实体类型
     * @return {@link Scanner} 对象，调用该对象的 {@code scan()} 方法即可开始扫描本地媒体文件
     */
    public static <T> Scanner<T> scanVideo(@NonNull ContentResolver resolver, @NonNull Decoder<T> decoder) {
        AppToolUtils.checkNotNull(resolver);
        AppToolUtils.checkNotNull(decoder);

        return new VideoScanner<>(resolver, decoder);
    }

    /**
     * 扫描本地的图片文件。
     *
     * @param resolver ContentResolver 对象，不能为 null
     * @param decoder  {@link Decoder} 对象，不能为 null
     * @param <T>      媒体文件对应的实体类型
     * @return {@link Scanner} 对象，调用该对象的 {@code scan()} 方法即可开始扫描本地媒体文件
     */
    public static <T> Scanner<T> scanImages(@NonNull ContentResolver resolver, @NonNull Decoder<T> decoder) {
        AppToolUtils.checkNotNull(resolver);
        AppToolUtils.checkNotNull(decoder);

        return new ImagesScanner<>(resolver, decoder);
    }



    /**
     * 解码器，用于将 Cursor 中扫描到的媒体文件转换成对应的实体对象。
     *
     * @param <T> 媒体文件对应的实体类型
     */
    public static abstract class Decoder<T> {
        /**
         * 将 Cursor 中当前 index 处的行数据转换成对应的实体对象。
         *
         * @param cursor Cursor 对象。Cursor 的 index 已自动设置好，无需手动设置。
         * @return 行数据对应的实体对象
         */
        public abstract T decode(Cursor cursor);

        public static int getDateAdded(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
        }

        public static int getDateModified(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
        }

        public static String getDisplayName(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
        }

        public static String getMimeType(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE));
        }

        public static int getSize(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
        }

        public static int getDuration(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION));
        }

        public static String getTitle(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE));
        }

        public static int getId(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
        }

        public static String getAudioArtist(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        }

        public static int getAudioArtistId(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID));
        }

        public static String getAudioAlbum(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));
        }

        public static int getAudioAlbumId(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));
        }

        public static boolean audioIsAlarm(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_ALARM)) != 0;
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        public static boolean audioIsAudioBook(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_AUDIOBOOK)) != 0;
        }

        public static boolean audioIsMusic(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_MUSIC)) != 0;
        }

        public static boolean audioIsNotification(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_NOTIFICATION)) != 0;
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public static boolean audioIsPending(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_PENDING)) != 0;
        }

        public static boolean audioIsPodcast(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_PODCAST)) != 0;
        }

        public static boolean audioIsRingtone(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.IS_RINGTONE)) != 0;
        }

        public static int getAudioTrack(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TRACK));
        }

        public static int getAudioYear(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR));
        }

        public static Uri getAudioUri(Cursor cursor) {
            return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getId(cursor));
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public static int getVideoWidth(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.WIDTH));
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public static int getVideoHeight(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.HEIGHT));
        }

        public static String getVideoCategory(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.CATEGORY));
        }

        public static String getVideoColorRange(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DESCRIPTION));
        }

        public static boolean videoIsPrivate(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.IS_PRIVATE)) != 0;
        }

        public static String getVideoLanguage(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.LANGUAGE));
        }

        @Deprecated
        public static float getVideoLatitude(Cursor cursor) {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.LATITUDE));
        }

        @Deprecated
        public static float getVideoLongitude(Cursor cursor) {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.LONGITUDE));
        }

        @Deprecated
        public static int getVideoMiniThumbMagic(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC));
        }

        public static String getVideoTags(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.TAGS));
        }

        public static Uri getVideoUri(Cursor cursor) {
            return ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, getId(cursor));
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public static int getImageWidth(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.WIDTH));
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public static int getImageHeight(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.HEIGHT));
        }

        public static String getImageDescription(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION));
        }

        public static boolean imageIsPrivate(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.IS_PRIVATE)) != 0;
        }

        @Deprecated
        public static float getImageLatitude(Cursor cursor) {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LATITUDE));
        }

        @Deprecated
        public static float getImageLongitude(Cursor cursor) {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LONGITUDE));
        }

        @Deprecated
        public static int getImageMiniThumbMagic(Cursor cursor) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC));
        }

        @Deprecated
        public static String getImagePicasaId(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.PICASA_ID));
        }

        public static Uri getImageUri(Cursor cursor) {
            return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getId(cursor));
        }
    }


}
