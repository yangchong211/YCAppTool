package com.ns.yc.lifehelper.utils.localFile;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.ns.yc.lifehelper.utils.localFile.bean.AppInfo;
import com.ns.yc.lifehelper.utils.localFile.bean.AudioItem;
import com.ns.yc.lifehelper.utils.localFile.bean.FileBean;
import com.ns.yc.lifehelper.utils.localFile.bean.ImgFolderBean;
import com.ns.yc.lifehelper.utils.localFile.bean.VideoItem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理者, 可以获取本机的各种文件
 */
public class FileManager {

    private static FileManager mInstance;
    private static Object mLock = new Object();

    public static FileManager getInstance(){
         if (mInstance == null){
             synchronized (mLock){
                 if (mInstance == null){
                     mInstance = new FileManager();
                 }
             }
         }
        return mInstance;
    }


    /**
     * 获取本机音乐列表
     * @return
     */
    public ArrayList<AudioItem> getMusics(Context context) {
        ArrayList<AudioItem> musics = new ArrayList<>();
        Cursor c = null;
        ContentResolver mContentResolver = context.getContentResolver();
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;          //Url,即查询路径
            String[] projection = null;                                     //查询时希望获得的列，如果填null，则返回所有列
            String selection = null;                                        //查询时的条件，select语句中where用到，可填null
            String[] selectionArgs = null;                                  //查询条件属性值
            String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;   //查询到的数据的默认排序，null则不进行排序

            //从数据库中获取数据
            c = mContentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
            if(c!=null){
                while (c.moveToNext()) {
                    String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));            // 路径
                    if (!new File(path).exists()) {
                        continue;
                    }
                    String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));    // 歌曲名
                    String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));          // 专辑
                    String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));        // 作者
                    long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));                // 大小
                    int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));          // 时长
                    //int time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));                   // 歌曲的id
                    // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                    AudioItem music = new AudioItem(name, path, album, artist, size, duration);
                    musics.add(music);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return musics;
    }



    /**
     * 获取本机视频列表
     * @return
     */
    private static List<VideoItem> getVideos(Context context) {
        List<VideoItem> videos = new ArrayList<>();
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor c = null;
        try {
            // String[] mediaColumns = { "_id", "_data", "_display_name",
            // "_size", "date_modified", "duration", "resolution" };
            c = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));                // 路径
                if (!new File(path).exists()) {
                    continue;
                }
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));                         // 视频的id
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));        // 视频名称
                String resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));    //分辨率
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));                    // 大小
                long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));            // 时长
                long date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));           //修改时间
                VideoItem video = new VideoItem(id, path, name, resolution, size, date, duration);
                videos.add(video);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return videos;
    }



    /**
     * 获取视频缩略图
     * @param id
     * @return
     */
    private static Bitmap getVideoThumbnail(Context context , int id) {
        ContentResolver mContentResolver = context.getContentResolver();
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options);
        return bitmap;
    }

    /**
     * 通过文件类型得到相应文件的集合
     **/
    private static List<FileBean> getFilesByType(Context context , int fileType) {
        List<FileBean> files = new ArrayList<>();
        ContentResolver mContentResolver = context.getContentResolver();
        // 扫描files文件库
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data", "_size"}, null, null, null);
            int dataindex = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            int sizeindex = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE);

            while (c.moveToNext()) {
                String path = c.getString(dataindex);
                if (FileToolUtils.getFileType(path) == fileType) {
                    if (!FileToolUtils.isExists(path)) {
                        continue;
                    }
                    long size = c.getLong(sizeindex);
                    FileBean fileBean = new FileBean(path, FileToolUtils.getFileIconByPath(path));
                    files.add(fileBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return files;
    }



    /**
     * 得到图片文件夹集合
     */
    private static List<ImgFolderBean> getImageFolders(Context context) {
        List<ImgFolderBean> folders = new ArrayList<ImgFolderBean>();
        ContentResolver mContentResolver = context.getContentResolver();
        // 扫描图片
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
            List<String> mDirs = new ArrayList<String>();//用于保存已经添加过的文件夹目录
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
                File parentFile = new File(path).getParentFile();
                if (parentFile == null)
                    continue;

                String dir = parentFile.getAbsolutePath();
                //如果已经添加过
                if (mDirs.contains(dir)) {
                    continue;
                }

                mDirs.add(dir);//添加到保存目录的集合中
                ImgFolderBean folderBean = new ImgFolderBean();
                folderBean.setDir(dir);
                folderBean.setFistImgPath(path);
                if (parentFile.list() == null)
                    continue;
                int count = parentFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
                            return true;
                        }
                        return false;
                    }
                }).length;
                folderBean.setCount(count);
                folders.add(folderBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return folders;
    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    private static List<String> getImgListByDir(String dir) {
        ArrayList<String> imgPaths = new ArrayList<>();
        File directory = new File(dir);
        if (directory == null || !directory.exists()) {
            return imgPaths;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (FileToolUtils.isPicFile(path)) {
                imgPaths.add(path);
            }
        }
        return imgPaths;
    }

    /**
     * 获取已安装apk的列表
     */
    private static List<AppInfo> getAppInfos(Context mContext) {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        //获取到包的管理者
        PackageManager packageManager = mContext.getPackageManager();
        //获得所有的安装包
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        //遍历每个安装包，获取对应的信息
        for (PackageInfo packageInfo : installedPackages) {
            AppInfo appInfo = new AppInfo();
            appInfo.setApplicationInfo(packageInfo.applicationInfo);
            appInfo.setVersionCode(packageInfo.versionCode);
            //得到icon
            Drawable drawable = packageInfo.applicationInfo.loadIcon(packageManager);
            appInfo.setIcon(drawable);
            //得到程序的名字
            String apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            appInfo.setApkName(apkName);
            //得到程序的包名
            String packageName = packageInfo.packageName;
            appInfo.setApkPackageName(packageName);
            //得到程序的资源文件夹
            String sourceDir = packageInfo.applicationInfo.sourceDir;
            File file = new File(sourceDir);
            //得到apk的大小
            long size = file.length();
            appInfo.setApkSize(size);

            System.out.println("---------------------------");
            System.out.println("程序的名字:" + apkName);
            System.out.println("程序的包名:" + packageName);
            System.out.println("程序的大小:" + size);


            //获取到安装应用程序的标记
            int flags = packageInfo.applicationInfo.flags;

            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //表示系统app
                appInfo.setIsUserApp(false);
            } else {
                //表示用户app
                appInfo.setIsUserApp(true);
            }

            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                //表示在sd卡
                appInfo.setIsRom(false);
            } else {
                //表示内存
                appInfo.setIsRom(true);
            }
            appInfos.add(appInfo);
        }
        return appInfos;
    }

}
