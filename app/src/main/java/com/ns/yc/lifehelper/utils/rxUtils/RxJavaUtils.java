package com.ns.yc.lifehelper.utils.rxUtils;

import android.view.View;
import android.widget.Toast;

import com.ns.yc.lifehelper.ui.main.view.MainActivity;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by yc on 2018/6/13.
 */

public class RxJavaUtils {

    public static void RxClick(View view){
        Observable.create(new MyOnSubscribe(view))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<View>() {
                    @Override
                    public void call(View view) {
                        System.out.println("我在call方法中,我被点击了,有反应了");
                    }
                });
    }


    public static class MyOnSubscribe implements Observable.OnSubscribe<View> {
        MyOnSubscribe(View view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("我被狂点中...");
                    //发布通知
                    mSubscriber.onNext(view);
                }
            });
        }

        private Subscriber mSubscriber;
        @Override
        public void call(Subscriber<? super View> subscriber) {
            mSubscriber = subscriber;
        }
    }



    public interface RxJavaCallback {
        void onSuccess();
    }

    /**
     * 点击view，测试正确
     *
     * @param view
     * @param time     时间
     * @param callback 回调
     */
    /*public static void clickView(final View view, int time, final RxJavaCallback callback) {
        if (view == null) return;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 订阅没取消
                        if (!emitter.isDisposed()) {
                            emitter.onNext("");
                            //  emitter.onComplete();
                        }
                    }
                });

            }
        })
                .throttleFirst(time, TimeUnit.MILLISECONDS) // 控制 time 的时间内
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String aVoid) throws Exception {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    }
                });
    }*/


}
