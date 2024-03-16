package com.yc.toolutils.share;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 本地分享工具库
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCToolLib
 * </pre>
 */
public final class LocalShareHelper {

    private static final String TAG = "LocalShareHelper";

    /**
     * 上下文
     */
    private final Activity activity;
    /**
     * 分享类型
     */
    private @ShareContentType final String contentType;
    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享Uri
     */
    private final Uri shareFileUri;
    /**
     * 分享内容
     */
    private final String contentText;
    /**
     * 分享到特定应用的包名
     */
    private final String componentPackageName;
    /**
     * 分享到特定应用的类名
     */
    private final String componentClassName;
    /**
     * 分享完成回传码
     */
    private final int requestCode;
    /**
     * 是否使用系统分享
     */
    private final boolean forcedUseSystemChooser;

    private LocalShareHelper(@NonNull Builder builder) {
        this.activity = builder.activity;
        this.contentType = builder.contentType;
        this.title = builder.title;
        this.shareFileUri = builder.shareFileUri;
        this.contentText = builder.textContent;
        this.componentPackageName = builder.componentPackageName;
        this.componentClassName = builder.componentClassName;
        this.requestCode = builder.requestCode;
        this.forcedUseSystemChooser = builder.forcedUseSystemChooser;
    }

    /**
     * 调用系统分享
     */
    public void shareBySystem () {
        if (checkShareParam()) {
            Intent shareIntent = createShareIntent();
            if (shareIntent == null) {
                Log.e(TAG, "shareBySystem cancel.");
                return;
            }
            if (title == null) {
                title = "";
            }
            if (forcedUseSystemChooser) {
                shareIntent = Intent.createChooser(shareIntent, title);
            }
            if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
                try {
                    if (requestCode != -1) {
                        activity.startActivityForResult(shareIntent, requestCode);
                    } else {
                        activity.startActivity(shareIntent);
                    }
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }
    }

    /**
     * 创建分享intent
     * @return  intent
     */
    private Intent createShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addCategory("android.intent.category.DEFAULT");
        if (!TextUtils.isEmpty(this.componentPackageName) && !TextUtils.isEmpty(componentClassName)){
            ComponentName comp = new ComponentName(componentPackageName, componentClassName);
            shareIntent.setComponent(comp);
        }
        switch (contentType) {
            case ShareContentType.TEXT :
                shareIntent.putExtra(Intent.EXTRA_TEXT, contentText);
                shareIntent.setType("text/plain");
                break;
            case ShareContentType.IMAGE :
            case ShareContentType.AUDIO :
            case ShareContentType.VIDEO :
            case ShareContentType.FILE:
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addCategory("android.intent.category.DEFAULT");
                shareIntent.setType(contentType);
                shareIntent.putExtra(Intent.EXTRA_STREAM, shareFileUri);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                Log.d(TAG, "Share uri: " + shareFileUri.toString());

                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, shareFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                break;
            default:
                Log.e(TAG, contentType + " is not support share type.");
                shareIntent = null;
                break;
        }

        return shareIntent;
    }

    /**
     * 检测属性
     * @return
     */
    private boolean checkShareParam() {
        if (this.activity == null) {
            Log.e(TAG, "activity is null.");
            return false;
        }
        if (TextUtils.isEmpty(this.contentType)) {
            Log.e(TAG, "Share content type is empty.");
            return false;
        }
        if (ShareContentType.TEXT.equals(contentType)) {
            if (TextUtils.isEmpty(contentText)) {
                Log.e(TAG, "Share text context is empty.");
                return false;
            }
        } else {
            if (this.shareFileUri == null) {
                Log.e(TAG, "Share file path is null.");
                return false;
            }
        }
        return true;
    }

    public static class Builder {
        private final Activity activity;
        private @ShareContentType String contentType = ShareContentType.FILE;
        private String title;
        private String componentPackageName;
        private String componentClassName;
        private Uri shareFileUri;
        private String textContent;
        private int requestCode = -1;
        private boolean forcedUseSystemChooser = true;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setContentType(@ShareContentType String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public Builder setShareFileUri(Uri shareFileUri) {
            this.shareFileUri = shareFileUri;
            return this;
        }

        public Builder setTextContent(String textContent) {
            this.textContent = textContent;
            return this;
        }

        public Builder setShareToComponent(String componentPackageName, String componentClassName) {
            this.componentPackageName = componentPackageName;
            this.componentClassName = componentClassName;
            return this;
        }

        public Builder setOnActivityResult (int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder forcedUseSystemChooser (boolean enable) {
            this.forcedUseSystemChooser = enable;
            return this;
        }

        public LocalShareHelper build() {
            return new LocalShareHelper(this);
        }

    }
}
