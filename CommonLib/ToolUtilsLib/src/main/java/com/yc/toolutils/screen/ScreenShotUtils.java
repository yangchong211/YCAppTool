package com.yc.toolutils.screen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//监听屏幕截图
public final class ScreenShotUtils {


    /**
     * 读取媒体数据库时需要读取的列
     */
    private static final String[] MEDIA_PROJECTIONS = {
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
    };
    /**
     * 读取媒体数据库时需要读取的列, 其中 WIDTH 和 HEIGHT 字段在 API 16 以后才有
     */
    private static final String[] MEDIA_PROJECTIONS_API_16 = {
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
        MediaStore.Images.ImageColumns.WIDTH,
        MediaStore.Images.ImageColumns.HEIGHT,
    };

    /**
     * 截屏依据中的路径判断关键字
     */
    private static final String[] KEYWORDS = {
        "screenshot", "screen_shot", "screen-shot", "screen shot",
        "screencapture", "screen_capture", "screen-capture", "screen capture",
        "screencap", "screen_cap", "screen-cap", "screen cap", "截屏"
    };

    private static List<ScreenShotUtils> sScreenShotManagerList = new ArrayList<>();

    private Point mScreenRealSize;

    /**
     * 已回调过的路径
     */
    private final List<String> mHasCallbackPaths = new ArrayList<>();

    private WeakReference<Context> mContext;

    private OnScreenShotListener mListener;

    private long mStartListenTime;

    /**
     * 内部存储器内容观察者
     */
    private MediaContentObserver mInternalObserver;

    /**
     * 外部存储器内容观察者
     */
    private MediaContentObserver mExternalObserver;

    /**
     * 运行在 UI 线程的 Handler, 用于运行监听器回调
     */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());


    private ScreenShotUtils(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("The context must not be null.");
        }
        mContext = new WeakReference<>(context);

        // 获取屏幕真实的分辨率
        if (mScreenRealSize == null) {
            mScreenRealSize = getRealScreenSize();
            if (mScreenRealSize != null) {
                d("Screen Real Size: " + mScreenRealSize.x + " * " + mScreenRealSize.y);
            } else {
                d("Get screen real size failed.");
            }
        }
    }

    /**
     * 监控截屏
     */
    public static void register(Application application) {
        assertInMainThread();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ScreenShotUtils screenShotManager = new ScreenShotUtils(activity);
                sScreenShotManagerList.add(screenShotManager);
                screenShotManager.setListener(new OnScreenShotListener() {
                    @Override
                    public void onShot(String imagePath, WeakReference<Context> contextWeakReference) {
                        if (null != contextWeakReference && null != contextWeakReference.get()) {
                            Toast.makeText(activity,"截图成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                screenShotManager.startListen();
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                for (Iterator<ScreenShotUtils> iterator = sScreenShotManagerList.iterator(); iterator.hasNext(); ) {
                    ScreenShotUtils screenShotManager = iterator.next();
                    if (null == screenShotManager.mContext
                            || null == screenShotManager.mContext.get()
                            || null != screenShotManager.mContext
                            && null != screenShotManager.mContext.get()
                            && screenShotManager.mContext.get() == activity) {
                        screenShotManager.stopListen();
                        screenShotManager.setListener(null);
                        iterator.remove();
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    /**
     * 启动监听
     */
    public void startListen() {
        assertInMainThread();

        mHasCallbackPaths.clear();

        // 记录开始监听的时间戳
        mStartListenTime = System.currentTimeMillis();

        // 创建内容观察者
        mInternalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, mUiHandler);
        mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mUiHandler);

        // 注册内容观察者
        if (null != mContext && null != mContext.get()) {
            mContext.get().getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
            );
            mContext.get().getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
            );
        }
    }

    /**
     * 停止监听
     */
    public void stopListen() {
        assertInMainThread();

        // 注销内容观察者
        if (mInternalObserver != null) {
            try {
                if (null != mContext && null != mContext.get()) {
                    mContext.get().getContentResolver().unregisterContentObserver(mInternalObserver);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mInternalObserver = null;
        }
        if (mExternalObserver != null) {
            try {
                if (null != mContext && null != mContext.get()) {
                    mContext.get().getContentResolver().unregisterContentObserver(mExternalObserver);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mExternalObserver = null;
        }

        // 清空数据
        mStartListenTime = 0;
        mHasCallbackPaths.clear();
    }

    public Context getContext() {
        if (null != mContext && null != mContext.get()) {
            return mContext.get();
        } else {
            return null;
        }
    }

    /**
     * 处理媒体数据库的内容改变
     */
    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            if (null != mContext && null != mContext.get()) {
                cursor = mContext.get().getContentResolver().query(
                    contentUri,
                    Build.VERSION.SDK_INT < 16 ? MEDIA_PROJECTIONS : MEDIA_PROJECTIONS_API_16,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
                );
            }

            if (cursor == null) {
                d("Deviant logic.");
                return;
            }
            if (!cursor.moveToFirst()) {
                d("Cursor no data.");
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
            int widthIndex = -1;
            int heightIndex = -1;
            if (Build.VERSION.SDK_INT >= 16) {
                widthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
                heightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);
            }

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);
            int width = 0;
            int height = 0;
            if (widthIndex >= 0 && heightIndex >= 0) {
                width = cursor.getInt(widthIndex);
                height = cursor.getInt(heightIndex);
            } else {
                // API 16 之前, 宽高要手动获取
                Point size = getImageSize(data);
                width = size.x;
                height = size.y;
            }

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken, width, height);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private Point getImageSize(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        return new Point(options.outWidth, options.outHeight);
    }

    /**
     * 处理获取到的一行数据
     */
    private void handleMediaRowData(String data, long dateTaken, int width, int height) {
        if (checkScreenShot(data, dateTaken, width, height)) {
            d("ScreenShot: path = " + data + "; size = " + width + " * " + height
                + "; date = " + dateTaken);
            if (mListener != null && !checkCallback(data)) {
                mListener.onShot(data, mContext);
            }
        } else {
            // 如果在观察区间媒体数据库有数据改变，又不符合截屏规则，则输出到 log 待分析
            d("Media content changed, but not screenshot: path = " + data
                + "; size = " + width + " * " + height + "; date = " + dateTaken);
        }
    }

    /**
     * 判断指定的数据行是否符合截屏条件
     */
    private boolean checkScreenShot(String data, long dateTaken, int width, int height) {

        /**
         * 判断依据一: 时间判断
         */
        // 如果加入数据库的时间在开始监听之前, 或者与当前时间相差大于10秒, 则认为当前没有截屏
        if (dateTaken < mStartListenTime || (System.currentTimeMillis() - dateTaken) > 10 * 1000) {
            return false;
        }

        /**
         * 判断依据二: 尺寸判断
         */
        if (mScreenRealSize != null) {
            // 如果图片尺寸超出屏幕, 则认为当前没有截屏
            if (!((width <= mScreenRealSize.x && height <= mScreenRealSize.y) ||
                (height <= mScreenRealSize.x && width <= mScreenRealSize.y))) {
                return false;
            }
        }

        /**
         * 判断依据三: 路径判断
         */
        if (TextUtils.isEmpty(data)) {
            return false;
        }
        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否已回调过, 某些手机ROM截屏一次会发出多次内容改变的通知; <br/> 删除一个图片也会发通知, 同时防止删除图片时误将上一张符合截屏规则的图片当做是当前截屏.
     */
    private boolean checkCallback(String imagePath) {
        if (mHasCallbackPaths.contains(imagePath)) {
            return true;
        }
        // 大概缓存15~20条记录便可
        if (mHasCallbackPaths.size() >= 20) {
            for (int i = 0; i < 5; i++) {
                mHasCallbackPaths.remove(0);
            }
        }
        mHasCallbackPaths.add(imagePath);
        return false;
    }

    /**
     * 获取屏幕分辨率
     */
    private Point getRealScreenSize() {
        Point screenSize = null;
        try {
            screenSize = new Point();
            if (null == mContext || null == mContext.get()) {
                return null;
            }
            WindowManager windowManager = (WindowManager) mContext.get().getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                defaultDisplay.getRealSize(screenSize);
            } else {
                try {
                    Method getRawW = Display.class.getMethod("getRawWidth");
                    Method getRawH = Display.class.getMethod("getRawHeight");
                    screenSize.set(
                        (Integer) getRawW.invoke(defaultDisplay),
                        (Integer) getRawH.invoke(defaultDisplay)
                    );
                } catch (Exception e) {
                    screenSize.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenSize;
    }

    /**
     * 设置截屏监听器
     */
    public void setListener(OnScreenShotListener listener) {
        mListener = listener;
    }

    public interface OnScreenShotListener {

        void onShot(String imagePath, WeakReference<Context> context);
    }

    private static void assertInMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            String methodMsg = null;
            if (elements != null && elements.length >= 4) {
                methodMsg = elements[3].toString();
            }
            throw new IllegalStateException("Call the method must be in main thread: " + methodMsg);
        }
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        private Uri mContentUri;

        MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            /**
             * 判断依据：应用在前台
             */
            if (null != mContext && null != mContext.get()) {
                boolean runningInForeground = isRunningInForeground(mContext.get().getApplicationContext());
                if (runningInForeground) {
                    handleMediaContentChange(mContentUri);
                }
            }
        }
    }
    
    private void d(String log){
        Log.d("ScreenShotUtils",log);
    }

    public static boolean isRunningInForeground(Context context) {
        try {
            Class clazz = ActivityManager.RunningAppProcessInfo.class;
            Field processStateField = clazz.getDeclaredField("processState");
            ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            //其中RunningAppProcessInfo类则封装了正在运行着的进程信息，当然也包含了正在运行的app的包名
            //可以activitymanager.getRunningAppProcesses()获取当前运行的app列表
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            if (null == processInfos || processInfos.isEmpty()) {
                return false;
            }
            String packageName = context.getPackageName();
            //对比自身的包名，来判断本身app是否处于前台运行。
            Iterator<ActivityManager.RunningAppProcessInfo> var5 = processInfos.iterator();
            while(var5.hasNext()) {
                ActivityManager.RunningAppProcessInfo rapi = var5.next();
                if (rapi.importance == 100 && rapi.importanceReasonCode == 0) {
                    try {
                        int processState = processStateField.getInt(rapi);
                        if (processState == 2 && rapi.pkgList != null && rapi.pkgList.length > 0) {
                            return rapi.pkgList[0].equals(packageName);
                        }
                    } catch (Exception var8) {
                    }
                }
            }
        } catch (Exception var9) {
        }
        return false;
    }
}

