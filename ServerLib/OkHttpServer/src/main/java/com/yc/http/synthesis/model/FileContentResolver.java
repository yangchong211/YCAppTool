package com.yc.http.synthesis.model;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.http.synthesis.EasyUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2021/04/18
 *    desc   : 文件内容解析器
 */
public class FileContentResolver extends File {

    private final ContentResolver mContentResolver;
    private final Uri mContentUri;

    private MediaType mContentType;
    private String mFileName;

    public FileContentResolver(Context context, Uri uri) {
        this(context.getContentResolver(), uri);
    }

    public FileContentResolver(ContentResolver resolver, Uri uri) {
        this(resolver, uri, null);
    }

    public FileContentResolver(Context context, Uri uri, String fileName) {
        this(context.getContentResolver(), uri, fileName);
    }

    public FileContentResolver(ContentResolver resolver, Uri uri, String fileName) {
        super(new File(uri.toString()).getPath());
        mContentResolver = resolver;
        // 请注意这个 uri 是通过 ContentResolver.insert 方法生成的，并且没有经过修改的，否则会导致文件流读取失败
        // 经过测试，ContentResolver.insert 生成的 uri 类型为 Uri.HierarchicalUri 这个内部类的
        mContentUri = uri;
        if (!TextUtils.isEmpty(fileName)) {
            mFileName = fileName;
            mContentType = ContentType.guessMimeType(fileName);
        } else {
            mFileName = getName();
            mContentType = ContentType.STREAM;
        }
    }

    /**
     * 设置真实的文件名（用于 {@link okhttp3.MultipartBody.Builder#addFormDataPart(String, String, RequestBody)} 方法中的 fileName 属性）
     */
    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    /**
     * 获取真实的文件名
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * 设置内容类型（用于 {@link RequestBody#contentType()} 方法）
     */
    public void setContentType(MediaType type) {
        mContentType = type;
    }

    /**
     * 获取内容类型
     */
    public MediaType getContentType() {
        return mContentType;
    }

    /**
     * 获取内容的 uri
     */
    public Uri getContentUri() {
        return mContentUri;
    }

    /**
     * 打开文件输入流
     */
    public InputStream openInputStream() throws FileNotFoundException {
        return mContentResolver.openInputStream(mContentUri);
    }

    /**
     * 打开文件输出流
     */
    public OutputStream openOutputStream() throws FileNotFoundException {
        return mContentResolver.openOutputStream(mContentUri);
    }

    @Override
    public boolean delete() {
        return mContentResolver.delete(mContentUri, null, null) > 0;
    }

    @Override
    public boolean renameTo(@NonNull File dest) {
        return false;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public long length() {
        InputStream inputStream = null;
        try {
            inputStream = openInputStream();
            if (inputStream != null) {
                return inputStream.available();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EasyUtils.closeStream(inputStream);
        }
        return 0;
    }

    @Override
    public boolean exists() {
        try {
            // 通过输入流来验证文件是否存在
            InputStream inputStream = openInputStream();
            if (inputStream != null) {
                // 关闭输入流
                EasyUtils.closeStream(inputStream);
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isFile() {
        return exists();
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public boolean mkdir() {
        return true;
    }

    @Override
    public boolean mkdirs() {
        return true;
    }

    @Nullable
    @Override
    public File getParentFile() {
        return null;
    }

    @Override
    public boolean setLastModified(long time) {
        return false;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Nullable
    @Override
    public String[] list() {
        return null;
    }

    @Nullable
    @Override
    public String[] list(@Nullable FilenameFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public File[] listFiles() {
        return null;
    }

    @Nullable
    @Override
    public File[] listFiles(@Nullable FileFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public File[] listFiles(@Nullable FilenameFilter filter) {
        return null;
    }
}