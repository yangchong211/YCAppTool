package com.ns.yc.lifehelper.ui.me.view.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.model.TResult;
import com.ns.yc.lifehelper.R;
import com.ycbjie.library.inter.listener.MePersonBaseListener;
import com.ycbjie.library.utils.image.ImageUtils;


public class MePersonBaseView extends FrameLayout implements View.OnClickListener {

    private ImageView iv_app_person_photo;
    private TextView tv_app_person_pic;
    private TextView tv_person_name;
    private TextView tv_app_person_name;
    private RelativeLayout rl_person_coll;
    private RelativeLayout rl_person_comm;
    private RelativeLayout rl_person_company;
    private RelativeLayout rl_person_duty;
    private RelativeLayout rl_person_email;
    private RelativeLayout rl_person_show;
    private RelativeLayout rl_person_area;
    private TextView tv_person_area;
    private RelativeLayout rl_person_name;
    private TextView tv_person_email;
    private TextView tv_person_show;
    private TextView tv_person_comment;
    private TextView tv_person_company;
    private TextView tv_person_duty;
    private View view;

    public MePersonBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private MePersonBaseListener listener;
    public void setListener(MePersonBaseListener listener) {
        this.listener = listener;
    }

    private void init() {
        initFindViewById();
        initListener();
    }

    private void initFindViewById() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.view_people_certification_info, this);
            iv_app_person_photo = (ImageView) view.findViewById(R.id.iv_app_person_photo);
            tv_app_person_pic = (TextView) view.findViewById(R.id.tv_app_person_pic);
            tv_person_name = (TextView) view.findViewById(R.id.tv_person_name);
            tv_app_person_name = (TextView) view.findViewById(R.id.tv_app_person_name);
            tv_person_comment = (TextView) view.findViewById(R.id.tv_person_comment);
            tv_person_company = (TextView) view.findViewById(R.id.tv_person_company);
            tv_person_duty = (TextView) view.findViewById(R.id.tv_person_duty);
            tv_person_email = (TextView) view.findViewById(R.id.tv_person_email);
            tv_person_show = (TextView) view.findViewById(R.id.tv_person_show);
            rl_person_name = (RelativeLayout) view.findViewById(R.id.rl_person_name);
            rl_person_coll = (RelativeLayout) view.findViewById(R.id.rl_person_coll);
            rl_person_comm = (RelativeLayout) view.findViewById(R.id.rl_person_comm);
            rl_person_company = (RelativeLayout) view.findViewById(R.id.rl_person_company);
            rl_person_duty = (RelativeLayout) view.findViewById(R.id.rl_person_duty);
            rl_person_email = (RelativeLayout) view.findViewById(R.id.rl_person_email);
            rl_person_show = (RelativeLayout) view.findViewById(R.id.rl_person_show);
            rl_person_area = (RelativeLayout) view.findViewById(R.id.rl_person_area);
            tv_person_area = (TextView) view.findViewById(R.id.tv_person_area);
        }
    }

    private void initListener() {
        if(view!=null){
            tv_app_person_pic.setOnClickListener(this);
            rl_person_name.setOnClickListener(this);
            rl_person_coll.setOnClickListener(this);
            rl_person_comm.setOnClickListener(this);
            rl_person_company.setOnClickListener(this);
            rl_person_duty.setOnClickListener(this);
            rl_person_email.setOnClickListener(this);
            rl_person_show.setOnClickListener(this);
            rl_person_area.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_app_person_pic:
                listener.editPersonLogo(view);
                break;
            case R.id.rl_person_name:
                listener.editPersonInfo(view,"姓名",tv_app_person_name.getText().toString());
                break;
            case R.id.rl_person_coll:
                listener.editPersonColl(view);
                break;
            case R.id.rl_person_comm:
                listener.editPersonCom(view);
                break;
            case R.id.rl_person_company:
                listener.editPersonInfo(view,"公司",tv_app_person_name.getText().toString());
                break;
            case R.id.rl_person_duty:
                listener.editPersonInfo(view,"职位",tv_app_person_name.getText().toString());
                break;
            case R.id.rl_person_email:
                listener.editPersonInfo(view,"邮箱",tv_person_email.getText().toString());
                break;
            case R.id.rl_person_show:
                listener.editPersonInfo(view,"简介",tv_person_show.getText().toString());
                break;
            case R.id.rl_person_area:
                listener.editPersonCity(view);
                break;
            default:
                break;
        }
    }

    /**设置logo图片*/
    public void setPersonLogo(String url , Activity activity){
        if(url!=null && url.length()>0){
            ImageUtils.loadImgByPicasso(activity,url,iv_app_person_photo);
        }
    }

    public void setPersonLogo(Bitmap bm , Activity activity){
        if(bm!=null){
            iv_app_person_photo.setImageBitmap(bm);
        }
    }

    public void setPersonLogo(TResult result) {
        String compressPath = result.getImage().getCompressPath();
        if(compressPath!=null && compressPath.length()>0){
            iv_app_person_photo.setImageURI(Uri.parse(compressPath));
        }
    }

    /**设置姓名*/
    public void setPersonName(String text){
        tv_app_person_name.setText(text);
    }

    /**设置评论*/
    public void setPersonCom(String text){
        tv_person_comment.setText(text);
    }

    /**设置公司*/
    public void setPersonCompany(String text){
        tv_person_company.setText(text);
    }

    /**设置职位*/
    public void setPersonDuty(String text){
        tv_person_duty.setText(text);
    }

    /**设置邮箱*/
    public void setPersonEmail(String text){
        tv_person_email.setText(text);
    }

    /**设置简介*/
    public void setPersonShow(String text){
        tv_person_show.setText(text);
    }

    /**设置地区*/
    public void setPersonArea(String text){
        tv_person_area.setText(text);
    }

    public String getPersonName(){
        return tv_app_person_name.getText().toString().trim();
    }

}
