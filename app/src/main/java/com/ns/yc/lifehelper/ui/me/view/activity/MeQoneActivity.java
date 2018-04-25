package com.ns.yc.lifehelper.ui.me.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.model.bean.ImagePhotoBean;
import com.ns.yc.lifehelper.ui.me.view.adapter.GlideImageLoader;
import com.ns.yc.lifehelper.ui.me.view.adapter.Loader;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.ycphotolib.PhotoShowView;
import com.ns.yc.ycphotolib.bean.PhotoShowBean;
import com.ns.yc.ycphotolib.listener.PhotoShowListener;
import com.pedaily.yc.ycdialoglib.selectDialog.CustomSelectDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/27
 * 描    述：发表动态页面
 * 修订历史：
 * ================================================
 */
public class MeQoneActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.psv_view)
    PhotoShowView psvView;

    /**
     * 当前选择的所有图片
     */
    private List<ImagePhotoBean> selImageList;
    /**
     * 允许选择图片最大数
     */
    private int maxImgCount = 8;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private CustomSelectDialog selectDialog;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(selectDialog!=null && selectDialog.isShowing()){
            selectDialog.dismiss();
        }
    }


    @Override
    public int getContentView() {
        return R.layout.activity_me_qone;
    }

    @Override
    public void initView() {
        initToolBar();
        initPhotoView();
        initImagePicker();
    }

    private void initToolBar() {
        toolbarTitle.setText("发表动态");
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("发送");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_title_right:

                break;
            default:
                break;
        }
    }


    private void initPhotoView() {
        selImageList = new ArrayList<>();
        psvView.setNewData(selImageList);
        psvView.setImageLoaderInterface(new Loader());
        //展示有动画和无动画
        psvView.setShowAnim(true);
        psvView.setPickerListener(new PhotoShowListener() {
            @Override
            public void addOnClickListener(int remainNum) {
                Toast.makeText(MeQoneActivity.this,"添加",Toast.LENGTH_SHORT).show();
                showDialogAnim();
            }

            @Override
            public void picOnClickListener(List<PhotoShowBean> list, int position, int remainNum) {
                Toast.makeText(MeQoneActivity.this,"点击图片",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void delOnClickListener(int position, int remainNum) {
                Toast.makeText(MeQoneActivity.this,"删除",Toast.LENGTH_SHORT).show();
            }
        });
        psvView.show();
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }



    private void showDialogAnim() {
        List<String> names = new ArrayList<>();
        names.add("拍照");
        names.add("相册");
        selectDialog = DialogUtils.showDialog(this, new CustomSelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //拍照
                    case 0:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent1 = new Intent(MeQoneActivity.this, ImageGridActivity.class);
                        // 是否是直接打开相机
                        intent1.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true);
                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                        break;
                    //相册
                    case 1:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent2 = new Intent(MeQoneActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent2, REQUEST_CODE_SELECT);
                        break;
                    default:
                        break;
                }
            }
        }, names);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    for(int a=0 ; a<images.size() ; a++){
                        ImagePhotoBean imageBean = new ImagePhotoBean();
                        imageBean.setUrl(images.get(a).path);
                        selImageList.add(imageBean);
                    }
                    psvView.addData(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    for(int a=0 ; a<images.size() ; a++){
                        ImagePhotoBean imageBean = new ImagePhotoBean();
                        imageBean.setUrl(images.get(a).path);
                        selImageList.add(imageBean);
                    }
                    psvView.addData(selImageList);
                }
            }
        }
    }

}
