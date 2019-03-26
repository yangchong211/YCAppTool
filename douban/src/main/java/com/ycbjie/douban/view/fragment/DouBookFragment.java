package com.ycbjie.douban.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBanModel;
import com.ycbjie.douban.bean.DouBookBean;
import com.ycbjie.douban.contract.DouBookContract;
import com.ycbjie.douban.presenter.DouBookPresenter;
import com.ycbjie.douban.view.activity.DouBookActivity;
import com.ycbjie.douban.view.activity.DouBookDetailActivity;
import com.ycbjie.douban.view.adapter.DouBookAdapter;
import com.ycbjie.library.base.mvp.BaseLazyFragment;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.weight.FullyGridLayoutManager;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣读书页面
 *     revise:
 * </pre>
 */
public class DouBookFragment extends BaseLazyFragment implements DouBookContract.View{

    YCRefreshView recyclerView;
    private DouBookActivity activity;
    private String mType;
    private DouBookAdapter adapter;

    private DouBookContract.Presenter presenter = new DouBookPresenter(this);

    public static DouBookFragment newInstance(String param1) {
        DouBookFragment fragment = new DouBookFragment();
        Bundle args = new Bundle();
        args.putString("DouBookFragment", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (DouBookActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mType = arguments.getString("DouBookFragment");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(position -> {
            if (adapter.getAllData().size()>position && position>=0){
                List<String> author = adapter.getAllData().get(position).getAuthor();
                Intent intent = new Intent(activity, DouBookDetailActivity.class);
                intent.putExtra("title",adapter.getAllData().get(position).getTitle());
                if (author!=null && author.size()>0){
                    intent.putExtra("author",author.get(0));
                }
                intent.putExtra("image",adapter.getAllData().get(position).getImage());
                intent.putExtra("average",adapter.getAllData().get(position).getRating().getAverage());
                intent.putExtra("numRaters",String.valueOf(adapter.getAllData().get(position).getRating().getNumRaters()));
                intent.putExtra("pubdate",adapter.getAllData().get(position).getPubdate());
                intent.putExtra("publisher",adapter.getAllData().get(position).getPublisher());
                intent.putExtra("id",adapter.getAllData().get(position).getId());
                intent.putExtra("alt",adapter.getAllData().get(position).getAlt());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onLazyLoad() {
        recyclerView.showProgress();
        getTopMovieData(mType , 0 , 30);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new FullyGridLayoutManager(activity, 3));
        adapter = new DouBookAdapter(activity);
        recyclerView.setAdapter(adapter);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getTopMovieData(mType, adapter.getAllData().size(), adapter.getAllData().size() + 21);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMoreClick() {

            }
        });

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        //刷新
        recyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                getTopMovieData(mType , 0 , 30);
            } else {
                recyclerView.setRefreshing(false);
                Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getTopMovieData(String mType , final int start, int count) {
        DouBanModel model = DouBanModel.getInstance();
        model.getBook(mType,start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouBookBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DouBookBean bookBean) {
                        if(adapter==null){
                            adapter = new DouBookAdapter(activity);
                        }

                        if(start==0){
                            if(bookBean!=null && bookBean.getBooks()!=null && bookBean.getBooks().size()>0){
                                adapter.clear();
                                adapter.addAll(bookBean.getBooks());
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.showEmpty();
                                recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                            }
                        } else {
                            if(bookBean!=null && bookBean.getBooks()!=null && bookBean.getBooks().size()>0){
                                adapter.addAll(bookBean.getBooks());
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.stopMore();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                        recyclerView.setErrorView(R.layout.view_custom_empty_data);
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
