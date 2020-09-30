package com.ycbjie.note.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ns.yc.yccustomtextlib.edit.inter.ImageLoader;
import com.ns.yc.yccustomtextlib.edit.inter.OnHyperTextListener;
import com.ns.yc.yccustomtextlib.edit.manager.HyperManager;
import com.ns.yc.yccustomtextlib.edit.model.HyperEditData;
import com.ns.yc.yccustomtextlib.edit.view.HyperImageView;
import com.ns.yc.yccustomtextlib.edit.view.HyperTextView;
import com.yc.imageserver.transformations.TransformationScale;
import com.ycbjie.note.R;
import com.ycbjie.note.utils.ModelStorage;

import java.util.List;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/01/09
 *     desc  : 预览页面
 *     revise:
 * </pre>
 */
public class PreviewArticleActivity extends AppCompatActivity {

    private HyperTextView htvContent;
    private Disposable mDisposable;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (mDisposable != null && !mDisposable.isDisposed()){
                mDisposable.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_article);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        htvContent = findViewById(R.id.htv_content);
        TextView tv_title = findViewById(R.id.tv_title);
        ImageView iv_image = findViewById(R.id.iv_image);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_time = findViewById(R.id.tv_time);
        initHyper();
        List<HyperEditData> hyperEditData = ModelStorage.getInstance().getHyperEditData();
        showDataSync(hyperEditData);
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(R.drawable.shape_load_bg)
                .placeholder(R.drawable.img_load_fail)
                .error(R.drawable.img_load_fail)
                .into(iv_image);
        htvContent.setOnHyperTextListener(new OnHyperTextListener() {
            @Override
            public void onImageClick(View view, String imagePath) {

            }
        });
    }


    /**
     * 异步方式显示数据
     */
    private void showDataSync(final List<HyperEditData> content){
        if (content==null || content.size()==0){
            return;
        }
        htvContent.clearAllLayout();
        Observable.create(new ObservableOnSubscribe<HyperEditData>() {
            @Override
            public void subscribe(ObservableEmitter<HyperEditData> emitter) {
                try {
                    for (int i=0; i<content.size(); i++){
                        //这个有点像是thread线程中的join方法，主要是保证顺序执行。
                        //比如，开始3个线程，如果执行结果按照顺序，则需要用到join方法，具体原理可以看看emitter.onNext源码
                        emitter.onNext(content.get(i));
                    }
                    emitter.onComplete();
                } catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        })
                //.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<HyperEditData>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(HyperEditData bean) {
                        try {
                            int type = bean.getType();
                            switch (type){
                                //文字类型
                                case 1:
                                    htvContent.addTextViewAtIndex(htvContent.getLastIndex(), bean.getInputStr());
                                    break;
                                //图片类型
                                case 2:
                                    htvContent.addImageViewAtIndex(htvContent.getLastIndex(), bean.getImagePath());
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }



    private void initHyper(){
        HyperManager.getInstance().setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(final String imagePath, final HyperImageView imageView, final int imageHeight) {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(imagePath)
                        .placeholder(R.drawable.img_load_fail)
                        .error(R.drawable.img_load_fail)
                        .into(new TransformationScale(imageView));
            }
        });
    }


}
