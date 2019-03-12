package com.ycbjie.todo.presenter;

import android.content.Context;

import com.ycbjie.todo.contract.WorkNewContract;

import io.realm.Realm;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/21
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class WorkNewPresenter implements WorkNewContract.Presenter {

    private WorkNewContract.View mView;
    private CompositeSubscription mSubscriptions;
    private Context mContext;
    private Realm realm;

    //注意：这里不能传Activity上下文，否则报错：Cannot return null from a non-@Nullable @Provides method
    /*@Inject
    WorkNewPresenter(Context context) {
        mContext = context;
    }*/

    public WorkNewPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void bindView(WorkNewContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
        mContext = null;
    }


}
