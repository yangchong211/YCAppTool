package com.ns.yc.lifehelper.ui.data.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.data.contract.OpenFileContract;
import com.ns.yc.lifehelper.ui.data.model.LoadFileModel;
import com.ns.yc.lifehelper.ui.data.presenter.OpenFilePresenter;
import com.ns.yc.lifehelper.ui.weight.tencent.SuperFileView;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.pedaily.yc.ycdialoglib.bottomMenu.CustomBottomDialog;
import com.pedaily.yc.ycdialoglib.bottomMenu.CustomItem;
import com.pedaily.yc.ycdialoglib.bottomMenu.OnItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14
 * 描    述：打开word，pdf，ppt，等文档文件
 * 修订历史：
 * ================================================
 */
public class OpenFileActivity extends BaseActivity implements OpenFileContract.View, View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.file_view)
    SuperFileView fileView;
    private String TAG = "OpenFileActivity";

    private OpenFileContract.Presenter presenter = new OpenFilePresenter(this);
    private List<String> fileData;
    private String filePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_open_file;
    }

    @Override
    public void initView() {
        initToolBar();
        initSuperFileView();
    }

    private void initToolBar() {
        toolbarTitle.setText("腾讯X5控件");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        fileData = presenter.getFileData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.open_file_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                startOpenBottomDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initSuperFileView() {
        fileView.setOnGetFilePathListener(new SuperFileView.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView mSuperFileView) {
                getFilePathAndShowFile(mSuperFileView);
            }
        });
        setFilePath(filePath);
        fileView.show();
    }

    private void getFilePathAndShowFile(SuperFileView mSuperFileView) {
        if (getFilePath().contains("http")) {           //网络地址要先下载
            downLoadFromNet(getFilePath(),mSuperFileView);
        } else {
            fileView.displayFile(new File(getFilePath()));
        }
    }

    private void downLoadFromNet(final String url, final SuperFileView mSuperFileView) {
        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                cacheFile.delete();
                return;
            }
        }


        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "下载文件-->onResponse");
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len ;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    if(responseBody==null){
                        return;
                    }
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        Log.d(TAG, "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    Log.d(TAG, "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    Log.d(TAG, "文件下载成功,准备展示文件。");
                    //2.ACache记录文件的有效期
                    mSuperFileView.displayFile(fileN);
                } catch (Exception e) {
                    Log.d(TAG, "文件下载异常 = " + e.toString());
                } finally {
                    try {
                        if (is != null){
                            is.close();
                        }
                    } catch (IOException e) {
                        Log.d(TAG, "文件下载异常 = " + e.toString());
                    }
                    try {
                        if (fos != null){
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.d(TAG, "文件下载异常 = " + e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    Log.d(TAG, "删除下载失败文件");
                    file.delete();
                }
            }
        });
    }


    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }


    private void startOpenBottomDialog() {
        new CustomBottomDialog(this)
                .title("选择分类")
                .setCancel(true,"取消选择")
                .orientation(CustomBottomDialog.VERTICAL)
                .inflateMenu(R.menu.open_file_menu, new OnItemClickListener() {
                    @Override
                    public void click(CustomItem item) {
                        int id = item.getId();
                        switch (id){
                            case R.id.item_word:
                                filePath = "";
                                filePath = fileData.get(0);
                                initSuperFileView();
                                break;
                            case R.id.item_pdf:

                                break;
                            case R.id.item_xls:

                                break;
                            case R.id.item_ppt:

                                break;
                            case R.id.item_txt:

                                break;
                        }
                    }
                })
                .show();
    }


    @Override
    public Activity getActivity() {
        return this;
    }


    /***
     * 获取缓存目录
     */
    private File getCacheDir(String url) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");
    }

    /***
     * 绝对路径获取缓存文件
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/" + getFileName(url));
        Log.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     */
    private String getFileName(String url) {
        String fileName = AppUtil.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     */
    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            Log.d(TAG, "paramString---->null");
            return str;
        }
        Log.d(TAG,"paramString:"+paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d(TAG,"i <= -1");
            return str;
        }
        str = paramString.substring(i + 1);
        Log.d(TAG,"paramString.substring(i + 1)------>"+str);
        return str;
    }



}
