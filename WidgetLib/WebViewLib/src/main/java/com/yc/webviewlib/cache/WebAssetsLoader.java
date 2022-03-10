package com.yc.webviewlib.cache;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2020/5/17
 *     desc  : 资源处理loader
 *     revise:
 * </pre>
 */
public class WebAssetsLoader {

    private static volatile WebAssetsLoader assetsLoader;
    private Context mContext;
    /**
     * 资源内存缓存集合
     * 多线程条件是数据安全的，没有用到锁，但是用到读写分离。【底层双数组实现，但有一点不具有时效性】
     * CopyOnWriteArrayList适合使用在读操作远远大于写操作的场景里，比如缓存。
     */
    private CopyOnWriteArraySet<String> mAssetResSet;
    /**
     * 文件名称
     */
    private String mDir = "";
    /**
     * 是否清理
     */
    private boolean mCleared = false;
    /**
     * 是否是后缀方法
     */
    private boolean mIsSuffixMod = false;

    public static WebAssetsLoader getInstance() {
        if (assetsLoader==null){
            synchronized (WebAssetsLoader.class){
                if (assetsLoader==null){
                    assetsLoader = new WebAssetsLoader();
                }
            }
        }
        return assetsLoader;
    }

    public WebAssetsLoader isAssetsSuffixMod(boolean suffixMod){
        mIsSuffixMod = suffixMod;
        return this;
    }

    public WebAssetsLoader init(Context context){
        mContext = context;
        mAssetResSet = new CopyOnWriteArraySet<>();
        mCleared = false;
        return this;
    }

    /**
     * 获取url的path路径
     * @param url                           url链接
     * @return
     */
    private String getUrlPath(String url){
        String uPath="";
        try {
            URL u = new URL(url);
            uPath = u.getPath();
            if (uPath.startsWith("/")){
                if (uPath.length()==1){
                    return uPath;
                }
                uPath = uPath.substring(1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return uPath;
    }

    /**
     * 通过路径找到对应的资源，然后将file文件转化成数据流InputStream， 并且返回回来
     * @param url                           url地址
     * @return
     */
    public InputStream getResByUrl(String url){
        //获取url的path路径
        String uPath = getUrlPath(url);
        if (TextUtils.isEmpty(uPath)){
            return null;
        }
        if (!mIsSuffixMod){
            if (TextUtils.isEmpty(mDir)){
                return getAssetFileStream(uPath);
            }else{
                return getAssetFileStream(mDir + File.separator + uPath);
            }
        }
        if (mAssetResSet!=null){
            //直接遍历集合
            for (String p: mAssetResSet) {
                if (uPath.endsWith(p)){
                    if (TextUtils.isEmpty(mDir)){
                        return getAssetFileStream(p);
                    }else{
                        return getAssetFileStream(mDir + File.separator + p);
                    }
                }
            }
        }
        return null;
    }

    public WebAssetsLoader setDir(final String dir){
        mDir = dir;
        return this;
    }

    /**
     * 初始化数据
     * @return
     */
    public WebAssetsLoader initData(){
        if (!mIsSuffixMod){
            return this;
        }
        if (mAssetResSet.size()==0){
           new Thread(new Runnable() {
               @Override
               public void run() {
                   initResourceNoneRecursion(mDir);
               }
           }).start();
        }
        return this;
    }

    public void clear(){
        mCleared = true;
        if (mAssetResSet!=null&&mAssetResSet.size()>0){
            mAssetResSet.clear();
        }
    }

    /**
     * 将asset的file添加到集合中
     * @param file
     */
    private void addAssetsFile(String file){
        String flag = mDir + File.separator;
        if (!TextUtils.isEmpty(mDir)){
            int pos = file.indexOf(flag);
            if (pos>=0){
                file = file.substring(pos+flag.length());
            }
        }
        mAssetResSet.add(file);
    }

    /**
     * 初始化资源，拿到缓存路径，然后遍历中的所有文件。将文件的路径保存到集合中
     * @param dir       文件
     */
    private void initResourceNoneRecursion(String dir){
        try {
            LinkedList<String> list = new LinkedList<>();
            String[] resData = mContext.getAssets().list(dir);
            if (resData != null) {
                for (String res : resData) {
                    String sub = dir + File.separator + res;
                    String[] tmp = mContext.getAssets().list(sub);
                    if (tmp != null) {
                        if (tmp.length == 0) {
                            addAssetsFile(sub);
                        } else {
                            list.add(sub);
                        }
                    }
                }
            }
            while (!list.isEmpty()){
                if (mCleared){
                    break;
                }
                String last = list.removeFirst();
                String[] tmp = mContext.getAssets().list(last);
                if (tmp != null) {
                    if (tmp.length == 0) {
                        addAssetsFile(last);
                    } else {
                        for(String sub : tmp){
                            String[] tmp1 = mContext.getAssets().list(last+File.separator+sub);
                            if (tmp1 != null) {
                                if (tmp1.length == 0){
                                    addAssetsFile(last+File.separator+sub);
                                }else{
                                    list.add(last+File.separator+sub);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地文件中获取数据，将file文件转化成数据流InputStream
     * @param path
     * @return
     */
    public InputStream getAssetFileStream(String path) {
        try {
            //读取assets路径下的文件
            return mContext.getAssets().open(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
