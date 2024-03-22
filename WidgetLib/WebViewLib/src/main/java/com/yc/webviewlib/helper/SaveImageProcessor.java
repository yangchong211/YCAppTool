/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.webviewlib.helper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.tencent.smtt.sdk.WebView;
import com.yc.compress.image.ImageSaveUtils;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppEncodeUtils;
import com.yc.webviewlib.utils.OkHttpUtils;
import com.yc.webviewlib.utils.WebFileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 长按保存图片功能，因为请求图片涉及网络请求，后期这部分需要优化，暂时未用到
 * </pre>
 */
public final class SaveImageProcessor {

    /**
     * 上下文
     */
    private Context mContext;

    public boolean showActionMenu(final WebView webView) {
        final Context context = webView.getContext();
        if (context == null) {
            return false;
        }
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(new CharSequence[] {"保存图片"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (hasExtStoragePermission(mContext)){
                    saveImage(mContext, webView);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private void saveImage(final Context context, final WebView webView) {
        File rootImagePath = WebFileUtils.getImageDir(context);
        if (rootImagePath == null) {
            ToastUtils.showRoundRectToast("保存图片失败");
            return;
        }
        WebView.HitTestResult hitTestResult = webView.getHitTestResult();
        if (hitTestResult == null) {
            ToastUtils.showRoundRectToast("保存图片失败");
            return;
        }
        String imageUrl = hitTestResult.getExtra();
        if (imageUrl == null) {
            ToastUtils.showRoundRectToast("保存图片失败");
            return;
        } else if (imageUrl.startsWith("data:")) {
            String imageData = imageUrl.replaceFirst("data:image\\/\\w+;base64,", "");
            byte[] imageDataBytes = AppEncodeUtils.base64Decode(imageData);
            String fileName = WebFileUtils.getImageName("");
            File imageFile = new File(rootImagePath, fileName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                fileOutputStream.write(imageDataBytes);
                fileOutputStream.close();
                String imagePath = imageFile.getAbsolutePath();
                insertMedia(context, imagePath);
                ToastUtils.showRoundRectToast("保存图片成功"+imagePath);
            } catch (IOException e) {
                ToastUtils.showRoundRectToast("保存图片成功");
                e.printStackTrace();
            }
        } else {
            Uri imageUri = Uri.parse(imageUrl);
            String fileName = imageUri.getLastPathSegment();
            if (TextUtils.isEmpty(fileName)) {
                fileName = WebFileUtils.getStringMd5(imageUrl);
            }
            final File imageFile = new File(rootImagePath, fileName);
            OkHttpUtils.downloadFile(context, imageUrl, imageFile, new OkHttpUtils.FileCallback() {
                @Override
                public void success() {
                    final String imagePath = imageFile.getAbsolutePath();
                    insertMedia(context, imagePath);
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showRoundRectToast("保存图片成功"+imagePath);
                        }
                    });
                }

                @Override
                public void fail(int i, Exception e) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showRoundRectToast("请稍后再试，请链接网络");
                        }
                    });
                }
            });
        }
    }

    private boolean hasExtStoragePermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 刷新图片
     * @param context                               上下文
     * @param imagePath                             图片路径
     */
    private void insertMedia(Context context, String imagePath) {
        if (imagePath==null || imagePath.length()==0 || context==null){
            return;
        }
        ImageSaveUtils.insertImage(context, imagePath);
    }

}

