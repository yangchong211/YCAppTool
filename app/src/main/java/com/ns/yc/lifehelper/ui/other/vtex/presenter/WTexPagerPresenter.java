package com.ns.yc.lifehelper.ui.other.vtex.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.api.http.vtex.VTexApi;
import com.ns.yc.lifehelper.ui.other.vtex.contract.WTexPagerContract;
import com.ns.yc.lifehelper.ui.other.vtex.model.bean.TopicListBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class WTexPagerPresenter implements WTexPagerContract.Presenter {

    private WTexPagerContract.View mView;
    private CompositeSubscription mSubscriptions;


    public WTexPagerPresenter(WTexPagerContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }


    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void getData(String mType) {
        Subscription subscribe = Observable.just(VTexApi.TAB_HOST + mType)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Document>() {
                    @Override
                    public Document call(String s) {
                        try {
                            return Jsoup.connect(s).timeout(10000).get();
                        } catch (IOException e) {
                            LogUtils.e(e.toString());
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .filter(new Func1<Document, Boolean>() {
                    @Override
                    public Boolean call(Document document) {
                        return document != null;
                    }
                })
                .map(new Func1<Document, List<TopicListBean>>() {
                    @Override
                    public List<TopicListBean> call(Document doc) {
                        List<TopicListBean> mList = new ArrayList<>();
                        Elements itemElements = doc.select("div.cell.item");    //item根节点
                        int count = itemElements.size();
                        for (int i = 0; i < count; i++) {
                            Elements titleElements = itemElements.get(i).select("div.cell.item table tr td span.item_title > a");   //标题
                            Elements imgElements = itemElements.get(i).select("div.cell.item table tr td img.avatar");              //头像
                            Elements nodeElements = itemElements.get(i).select("div.cell.item table tr span.small.fade a.node");    //节点
                            Elements commentElements = itemElements.get(i).select("div.cell.item table tr a.count_livid");          //评论数
                            Elements nameElements = itemElements.get(i).select("div.cell.item table tr span.small.fade strong a");  //作者 & 最后回复
                            Elements timeElements = itemElements.get(i).select("div.cell.item table tr span.small.fade");           //更新时间

                            TopicListBean bean = new TopicListBean();

                            if (titleElements.size() > 0) {
                                bean.setTitle(titleElements.get(0).text());
                                bean.setTopicId(parseId(titleElements.get(0).attr("href")));
                            }
                            if (imgElements.size() > 0) {
                                bean.setImgUrl(parseImg(imgElements.get(0).attr("src")));
                            }
                            if (nodeElements.size() > 0) {
                                bean.setNode(nodeElements.get(0).text());
                            }
                            if (nameElements.size() > 0) {
                                bean.setName(nameElements.get(0).text());
                            }
                            //存在没有 最后回复者、评论数、更新时间的情况
                            if (nameElements.size() > 1) {
                                bean.setLastUser(nameElements.get(1).text());
                            }
                            if (commentElements.size() > 0) {
                                bean.setCommentNum(Integer.valueOf(commentElements.get(0).text()));
                            }
                            if (timeElements.size() > 1) {
                                bean.setUpdateTime(parseTime(timeElements.get(1).text()));
                            }
                            mList.add(bean);
                        }
                        return mList;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TopicListBean>>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.e("WTexPagerPresenter----------" + "结束");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e("WTexPagerPresenter----------" + e.getMessage());
                    }

                    @Override
                    public void onNext(List<TopicListBean> topicListBeen) {
                        LogUtils.e("WTexPagerPresenter----------" + "成功");
                        mView.showContent(topicListBeen);
                    }
                });
        mSubscriptions.add(subscribe);
    }

    public String parseId(String str) {
        int idEnd = str.indexOf("#");
        return str.substring(3, idEnd);
    }

    public String parseTime(String str) {
        int timeEnd = str.indexOf("  •");
        if (timeEnd == -1) {
            return str;
        }
        return str.substring(0, timeEnd);
    }

    public static String parseImg(String str) {
        return "http:" + str;
    }

}
