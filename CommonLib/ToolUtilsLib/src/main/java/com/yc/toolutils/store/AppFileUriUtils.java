package com.yc.toolutils.store;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;


import com.yc.toolutils.stream.FileIoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : uri和file相互转化工具类
 *     revise:
 * </pre>
 */
public final class AppFileUriUtils {

    private AppFileUriUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 将目标路径解析城uri
     * @param context       上下文
     * @param resPath       路径
     * @return
     */
    public static Uri res2Uri(Context context, String resPath) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resPath);
    }

    /**
     * 将file转化为uri
     *
     * @param file          file文件
     * @return uri          uri
     */
    public static Uri file2Uri(Context context, final File file) {
        if (!isFileExists(context, file)) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //Android7.0
            //第二个参数表示要用哪个ContentProvider，这个唯一值在AndroidManifest.xml里定义了
            //若是没有定义MyFileProvider，可直接使用FileProvider替代
            String authority = context.getPackageName() + ".fileUriProvider";
            return ExplorerProvider.getUriForFile(context, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 将uri转化为file的路径
     * @param context           上下文
     * @param uri               uri
     * @return
     */
    public static String uri2String(Context context, final Uri uri){
        File file = uri2File(context, uri);
        if (file == null){
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * 将uri转化为file
     * @param context           上下文
     * @param uri               uri
     * @return
     */
    public static File uri2File(Context context, final Uri uri) {
        if (uri == null) {
            return null;
        }
        File file = uri2FileReal(context, uri);
        if (file != null) {
            return file;
        }
        return copyUri2Cache(context, uri);
    }

    /**
     * Uri to file.
     *
     * @param uri The uri.
     * @return file
     */
    private static File uri2FileReal(Context context, final Uri uri) {
        Log.d("UriUtils", uri.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && uri.getPath() != null) {
            return getExternals(context,uri);
        }
        //以 file:// 开头的使用第三方应用打开
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            //如果uri的scheme是file
            if (uri.getPath() != null) {
                return new File(uri.getPath());
            }
            Log.d("UriUtils", uri.toString() + " parse failed. -> 0");
            return null;
        }
        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        //Before 4.4 , API 19 content:// 开头, 比如 content://media/external/images/media/123
        if (!isKitKat && ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            if (DocumentUtils.isGoogleMedia(uri)){
                return new File(uri.getLastPathSegment());
            }
            //如果uri的scheme是content
            return getFileFromUri(context, uri, "2");
        }
        // After 4.4 , API 19
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4以后，专为Android4.4设计的从Uri获取文件绝对路径
            if (DocumentsContract.isDocumentUri(context, uri)) {
                return getDocumentUri(context,uri);
            }
        }
        Log.d("UriUtils", uri.toString() + " parse failed. -> 3");
        return getFileFromUri(context,uri,"5");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static File getDocumentUri(Context context, Uri uri) {
        if (DocumentUtils.isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            if ("primary".equalsIgnoreCase(type)) {
                return new File(Environment.getExternalStorageDirectory() + "/" + split[1]);
            } else {
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_0");
                return getStorage(context,uri,split);
            }
        } else if (DocumentUtils.isDownloadsDocument(uri)) {
            String id = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                id = DocumentsContract.getDocumentId(uri);
            }
            if (TextUtils.isEmpty(id)) {
                Log.d("UriUtils", uri.toString() + " parse failed(id is null). -> 1_1");
                return null;
            }
            if (id != null) {
                if (id.startsWith("raw:")) {
                    return new File(id.substring(4));
                } else if (id.startsWith("msf:")) {
                    id = id.split(":")[1];
                }
            }
            long availableId = 0;
            try {
                availableId = Long.parseLong(id);
            } catch (Exception e) {
                return null;
            }
            String[] contentUriPrefixesToTry = new String[]{
                    "content://downloads/public_downloads",
                    "content://downloads/all_downloads",
                    "content://downloads/my_downloads"
            };
            for (String contentUriPrefix : contentUriPrefixesToTry) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId);
                try {
                    File file = getFileFromUri(context, contentUri, "1_1");
                    if (file != null) {
                        return file;
                    }
                } catch (Exception ignore) {
                }
            }
            Log.d("UriUtils", uri.toString() + " parse failed. -> 1_1");
            return null;
        } else if (DocumentUtils.isMediaDocument(uri)) {
            String docId = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                docId = DocumentsContract.getDocumentId(uri);
            }
            String[] split = new String[0];
            if (docId != null) {
                split = docId.split(":");
            }
            final String type = split[0];
            Uri contentUri;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            } else {
                Log.d("UriUtils", uri.toString() + " parse failed. -> 1_2");
                return null;
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{split[1]};
            return getFileFromUri(context, contentUri, selection, selectionArgs, "1_2");
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            //MediaStore (and general)
            if (DocumentUtils.isGoogleMedia(uri)){
                return new File(uri.getLastPathSegment());
            }
            return getFileFromUri(context, uri, "1_3");
        } else {
            Log.d("UriUtils", uri.toString() + " parse failed. -> 1_4");
            return getFileFromUri(context,uri,"1_4");
        }
    }

    private static File getStorage(Context context, Uri uri,String[] split) {
        final String type = split[0];
        // Below logic is how External Storage provider build URI for documents
        // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getUuid = storageVolumeClazz.getMethod("getUuid");
            Method getState = storageVolumeClazz.getMethod("getState");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Method isEmulated = storageVolumeClazz.getMethod("isEmulated");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                //String uuid = (String) getUuid.invoke(storageVolumeElement);

                final boolean mounted = Environment.MEDIA_MOUNTED.equals(getState.invoke(storageVolumeElement))
                        || Environment.MEDIA_MOUNTED_READ_ONLY.equals(getState.invoke(storageVolumeElement));

                //if the media is not mounted, we need not get the volume details
                if (!mounted) {
                    continue;
                }
                //Primary storage is already handled.
                if ((Boolean) isPrimary.invoke(storageVolumeElement)
                        && (Boolean) isEmulated.invoke(storageVolumeElement)) {
                    continue;
                }
                String uuid = (String) getUuid.invoke(storageVolumeElement);

                if (uuid != null && uuid.equals(type)) {
                    return new File(getPath.invoke(storageVolumeElement) + "/" + split[1]);
                }
            }
        } catch (Exception ex) {
            Log.d("UriUtils", uri.toString() + " parse failed. " + ex.toString() + " -> 1_0");
        }
        return null;
    }

    private static File getExternals(Context context, Uri uri) {
        String path = uri.getPath();
        String[] externals = new String[]{"/external/", "/external_path/"};
        File file;
        for (String external : externals) {
            if (path.startsWith(external)) {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                file = new File(absolutePath + path.replace(external, "/"));
                if (file.exists()) {
                    Log.d("UriUtils", uri.toString() + " -> " + external);
                    return file;
                }
            }
        }
        file = null;
        if (path.startsWith("/files_path/")) {
            file = new File(context.getFilesDir().getAbsolutePath()
                    + path.replace("/files_path/", "/"));
        } else if (path.startsWith("/cache_path/")) {
            file = new File(context.getCacheDir().getAbsolutePath()
                    + path.replace("/cache_path/", "/"));
        } else if (path.startsWith("/external_files_path/")) {
            file = new File(context.getExternalFilesDir(null).getAbsolutePath()
                    + path.replace("/external_files_path/", "/"));
        } else if (path.startsWith("/external_cache_path/")) {
            file = new File(context.getExternalCacheDir().getAbsolutePath()
                    + path.replace("/external_cache_path/", "/"));
        }
        if (file != null && file.exists()) {
            Log.d("UriUtils", uri.toString() + " -> " + path);
            return file;
        }
        return null;
    }

    private static File getFileFromUri(Context context, final Uri uri, final String code) {
        return getFileFromUri(context, uri, null, null, code);
    }

    private static File getFileFromUri(Context context, final Uri uri, final String selection,
                                       final String[] selectionArgs, final String code) {
        final String column = MediaStore.Files.FileColumns.DATA;
        final String[] projection = new String[]{column};
        ContentResolver contentResolver = context.getContentResolver();
        //利用 ContentResolver 寻找
        final Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);
        if (cursor == null) {
            Log.d("UriUtils", uri.toString() + " parse failed(cursor is null). -> " + code);
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndex(column);
                if (columnIndex > -1) {
                    return new File(cursor.getString(columnIndex));
                } else {
                    Log.d("UriUtils", uri.toString() + " parse failed(columnIndex: " + columnIndex + " is wrong). -> " + code);
                    return null;
                }
            } else {
                Log.d("UriUtils", uri.toString() + " parse failed(moveToFirst return false). -> " + code);
                return null;
            }
        } catch (Exception e) {
            Log.d("UriUtils", uri.toString() + " parse failed. -> " + code);
            return null;
        } finally {
            cursor.close();
        }
    }

    private static File copyUri2Cache(Context context, Uri uri) {
        Log.d("UriUtils", "copyUri2Cache() called");
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            File file = new File(context.getCacheDir(), "" + System.currentTimeMillis());
            File srcFile = new File(file.getAbsolutePath());
            FileIoUtils.writeFileFromIS1(is,srcFile);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isFileExists(Context context, final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        String filePath = file.getAbsolutePath();
        File newFile = TextUtils.isEmpty(filePath) ? null : new File(filePath);
        if (newFile == null) {
            return false;
        }
        if (newFile.exists()) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                Uri uri = Uri.parse(filePath);
                ContentResolver cr = context.getContentResolver();
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (afd == null) {
                    return false;
                }
                try {
                    afd.close();
                } catch (IOException ignore) {
                }
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }

}
