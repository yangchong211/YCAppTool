package com.ns.yc.lifehelper.ui.other.toDo.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.cache.CacheToDoDetail;
import com.ns.yc.lifehelper.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.toDo.view.ToDoTimerActivity;
import com.ns.yc.lifehelper.ui.other.toDo.view.adapter.ToDoAdapter;
import com.ns.yc.lifehelper.ui.other.toDo.bean.ToDoDetail;
import com.pedaily.yc.ycdialoglib.bottomMenu.CustomBottomDialog;
import com.pedaily.yc.ycdialoglib.bottomMenu.CustomItem;
import com.pedaily.yc.ycdialoglib.bottomMenu.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14.
 * 描    述：时光日志页面
 * 修订历史：
 * ================================================
 */
public class ToDoTimerFragment extends BaseFragment {

    private static final String TYPE = "";
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private String mType;
    private ToDoTimerActivity activity;
    private List<ToDoDetail.ToDo> list = new ArrayList<>();
    private Realm realm;
    private RealmResults<CacheToDoDetail> cacheToDoDetails;
    private ToDoAdapter adapter;

    public static ToDoTimerFragment newInstance(String param1) {
        ToDoTimerFragment fragment = new ToDoTimerFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ToDoTimerActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if(realm!=null){
            realm.close();
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mType = arguments.getString(TYPE);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_to_do_main;
    }

    @Override
    public void initView() {
        initRealm();
        initRecycleView();
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        addDataNotify();
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ToDoAdapter(list,activity);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        adapter.setOnItemLongClickListener(new ToDoAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                startOpenBottomDialog(position);
            }
        });
    }


    public void addDataNotify(){
        initRealm();
        if(realm==null){
            return;
        }
        if(realm.where(CacheToDoDetail.class).findAll()!=null && realm.where(CacheToDoDetail.class).findAll().size()>0){
            cacheToDoDetails = realm.where(CacheToDoDetail.class)
                    .equalTo("dayOfWeek",mType)
                    .findAll();
        }else {
            return;
        }
        list.clear();
        for(int a=0 ; a<cacheToDoDetails.size() ; a++){
            ToDoDetail.ToDo toDoDetail = new ToDoDetail.ToDo();
            toDoDetail.setContent(cacheToDoDetails.get(a).getContent());
            toDoDetail.setDayOfWeek(cacheToDoDetails.get(a).getDayOfWeek());
            toDoDetail.setTime(cacheToDoDetails.get(a).getTime());
            toDoDetail.setIcon(cacheToDoDetails.get(a).getIcon());
            toDoDetail.setTitle(cacheToDoDetails.get(a).getTitle());
            toDoDetail.setPriority(cacheToDoDetails.get(a).getPriority());
            list.add(toDoDetail);
        }
        if(adapter!=null){
            //adapter.notifyItemInserted(list.size()-1);
            adapter.notifyDataSetChanged();
        }
    }


    private void startOpenBottomDialog(int position) {
        new CustomBottomDialog(activity)
                .title("选择分类")
                .setCancel(true,"取消选择")
                .orientation(CustomBottomDialog.VERTICAL)
                .inflateMenu(R.menu.to_do_bottom_sheet, new OnItemClickListener() {
                    @Override
                    public void click(CustomItem item) {
                        //String title = item.getTitle();
                        int id = item.getId();
                        switch (id){
                            case R.id.to_do_flag:

                                break;
                            case R.id.to_do_put_off:

                                break;
                            case R.id.to_do_edit:

                                break;
                            case R.id.to_do_delete:

                                break;
                        }
                    }
                })
                .show();
    }


}
