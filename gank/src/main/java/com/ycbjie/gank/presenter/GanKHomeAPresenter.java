package com.ycbjie.gank.presenter;

import android.support.annotation.NonNull;

import com.ycbjie.gank.api.GanKModel;
import com.ycbjie.gank.bean.bean.CategoryResult;
import com.ycbjie.gank.contract.GanKHomeAContract;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.http.ExceptionUtils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营
 *     revise:
 * </pre>
 */
public class GanKHomeAPresenter implements GanKHomeAContract.Presenter {

    private GanKHomeAContract.View mHomeView;
    @NonNull
    private CompositeDisposable disposable;

    public GanKHomeAPresenter(GanKHomeAContract.View homeView) {
        this.mHomeView = homeView;
        disposable = new CompositeDisposable();
    }

    /**
     * 开始绑定数据
     */
    @Override
    public void subscribe() {
        //获取图片
        getBanner(false);
        //开始缓存图片
        cacheRandomImg();
    }

    /**
     * 解除绑定数据
     */
    @Override
    public void unSubscribe() {
        disposable.clear();
    }

    /**
     * 点击按钮获取随机图片
     */
    @Override
    public void getRandomBanner() {
        getBanner(true);
    }


    /**
     * 保存图片地址
     */
    @Override
    public void saveCacheImgUrl(String url) {
        AppConfig.INSTANCE.setBannerUrl(url);
    }


    private void getBanner(boolean isRandom) {
        mHomeView.startBannerLoadingAnim();
        mHomeView.disEnableFabButton();
        Observable<CategoryResult> observable;
        if (isRandom) {
            observable = GanKModel.getInstance().getRandomBeauties(1);
        } else {
            observable =  GanKModel.getInstance().getCategoryDate("福利", 1, 1);
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onError(Throwable e) {
                        mHomeView.errorImage();
                        mHomeView.enableFabButton();
                        mHomeView.stopBannerLoadingAnim();
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(CategoryResult result) {
                        if (result != null && result.results != null && result.results.size() > 0
                                && result.results.get(0).url != null) {
                            String url = result.results.get(0).url;
                            mHomeView.setBanner(url);
                        } else {
                            mHomeView.errorImage();
                        }
                    }
                });

    }


    private void cacheRandomImg() {
        // 不显示欢迎妹子，也就不需要预加载了
        if (!AppConfig.INSTANCE.isShowGirlImg()) {
            return;
        }
        // 概率出现欢迎妹子
        if (AppConfig.INSTANCE.isProbabilityShowImg()) {
            if (Math.random() < 0.75) {
                AppConfig.INSTANCE.setBannerUrl("");
                return;
            }
        }

        GanKModel.getInstance().getRandomBeauties(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CategoryResult>() {
                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(CategoryResult result) {
                        if (result != null && result.results != null && result.results.size() > 0
                                && result.results.get(0).url != null) {
                            mHomeView.cacheImg(result.results.get(0).url);
                        }
                    }
                });

    }

}
