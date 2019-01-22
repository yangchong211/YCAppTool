package com.ns.yc.lifehelper.ui.me.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.ns.yc.lifehelper.R;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.inter.listener.MePersonBaseListener;
import com.ns.yc.lifehelper.ui.me.view.fragment.MePersonFragment;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.ycutilslib.viewPager.AutoHeightViewPager;
import com.pedaily.yc.ycdialoglib.dialog.CustomSelectDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的个人中心页面
 * 修订历史：
 * ================================================
 */
public class MePersonActivity extends BaseActivity implements View.OnClickListener
        , TakePhoto.TakeResultListener, InvokeListener {

    TextView tvTitleLeft;
    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    MePersonBaseView basePersonView;
    TabLayout tabLayout;
    AutoHeightViewPager vpContent;


    private Uri imageUri;
    private CropOptions cropOptions;
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    private String[] titles = {"创业者","工作者"};
    private String[] type = {"first","second"};
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type =
                PermissionManager.onRequestPermissionsResult(
                        requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_me_person;
    }



    @Override
    public void initView() {
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        basePersonView = findViewById(R.id.base_person_view);
        tabLayout = findViewById(R.id.tab_layout);
        vpContent = findViewById(R.id.vp_content);


        toolbarTitle.setText("个人信息中心");
        initFragmentList();
        initViewPagerAndTab();
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        basePersonView.setListener(new MePersonBaseListener() {
            @Override
            public void editPersonInfo(View view, String title, String editText) {

            }

            @Override
            public void editPersonLogo(View view) {
                showDialogAnim();
            }

            @Override
            public void editPersonCity(View view) {

            }

            @Override
            public void editPersonColl(View view) {

            }

            @Override
            public void editPersonCom(View view) {

            }
        });
    }



    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initFragmentList() {
        mTitleList.clear();
        mFragments.clear();
        for(int a=0 ; a<titles.length ; a++){
            mTitleList.add(titles[a]);
            mFragments.add(MePersonFragment.newInstance(type[a]));
        }
    }


    private void initViewPagerAndTab() {
        /*
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        // vpContent.setOffscreenPageLimit(2);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(vpContent);
    }


    private void showDialogAnim() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        DialogUtils.showDialog(this,new CustomSelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:                 //拍照
                        //Toast.makeText(AppPersonActivity.this,"拍照",Toast.LENGTH_SHORT).show();
                        startGetCamPhoto();
                        break;
                    case 1:                 //相册
                        startGetPhotoFromFile();
                        break;
                }
            }
        }, names);
    }



    /**
     * 拍照获取图片
     */
    private void startGetCamPhoto() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        configCompress(takePhoto);
        takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
    }


    /**
     * 从本地获取图片
     */
    private void startGetPhotoFromFile() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        configCompress(takePhoto);
        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
    }


    /**
     * 配置图片参数
     */
    private void configCompress(TakePhoto takePhoto) {
        //配置图片压缩
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(102400)                                                 //压缩到的最大大小
                .setMaxPixel(SizeUtils.dp2px(80.0f))        //长或宽不超过的最大像素
                .enablePixelCompress(true)                                          //是否启用像素压缩
                .enableQualityCompress(true)                                        //是否启用质量压缩
                .enableReserveRaw(true)                                             //是否保留原文件
                .create();
        takePhoto.onEnableCompress(config, false);

        //鲁班压缩
        /*LubanOptions option=new LubanOptions.Builder()
                .setMaxHeight(SizeUtils.dp2px(AppPersonActivity.this,80.0f))
                .setMaxWidth(SizeUtils.dp2px(AppPersonActivity.this,80.0f))
                .setMaxSize(102400)
                .create();
        config=CompressConfig.ofLuban(option);
        config.enableReserveRaw(false);*/


        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setCorrectImage(true);              //是否裁切图片
        builder.setWithOwnGallery(true);            //设置自带裁切工具
        takePhoto.setTakePhotoOptions(builder.create());

        //cropOptions 裁剪配置类
        cropOptions = new CropOptions.Builder()
                .setAspectX(SizeUtils.dp2px(75.0f))
                .setAspectY(SizeUtils.dp2px(100.0f))
                .setWithOwnCrop(true)
                .create();
        /**
         * 从相机获取图片并裁剪
         * @param outPutUri 图片裁剪之后保存的路径
         * @param options   裁剪配置
         */
        //takePhoto.onPickFromCaptureWithCrop(imageUri,cropOptions);
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this)
                    .bind(new TakePhotoImpl(MePersonActivity.this, this));
        }
        return takePhoto;
    }


    /**
     * 权限回调
     *
     * @param invokeParam
     * @return
     */
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager
                .checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.i("takePhoto", "takeSuccess：" + result.getImage().getCompressPath());
        TImage image = result.getImage();
        File file = new File(image.getCompressPath());
        //目前先暂且直接设定
        basePersonView.setPersonLogo(result);
        //待图片获取成功后上传到服务器
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

}
