package com.ns.yc.lifehelper.ui.other.mobilePlayer.fragment;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantKeys;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.activity.MobileVideoPlayActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.adapter.MobileVideoListAdapter;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.bean.VideoItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：视频list页面
 * 修订历史：
 * ================================================
 */
public class MobileVideoFragment extends BaseFragment {

    @Bind(R.id.listView)
    ListView listView;
    private MobilePlayerActivity activity;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MobilePlayerActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_mobile_player;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // parent就是ListView对象
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("启动次数","点击-----");
                // 读取视频列表
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                ArrayList<VideoItem> items = getVideoItems(cursor);

                // 把当前点击的位置和视频列表传到视频播放器界面
                // 注意，intent传递集合数据，那么实体类必须序列化，也就是 implements Serializable
                Intent intent = new Intent(activity, MobileVideoPlayActivity.class);
                intent.putExtra(ConstantKeys.CURRENT_POSITION, position);
                intent.putExtra(ConstantKeys.MOBILE_VIDEO_ITEMS, items);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        // getContext().getContentResolver().query() 这个方法会运行在主线程
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            /** 当查询完成的时候会调用这个方法，这个方法会运行在UI线程，参数cursor就是查询出来的结果  */
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                listView.setAdapter(new MobileVideoListAdapter(activity, cursor));
            }
        };

        int token = 0;                                           // 相当于Message.what
        Object cookie = null;                                    // 相当于Message.obj
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;   // 指定要查询的内容为视频
        String[] projection = {MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATA};   // 指定要查询的列
        String selection = null;                                // 指定查询条件  name=? and age>?
        String[] selectionArgs = null;                          // 指定查询条件中的参数
        String orderBy = MediaStore.Video.Media.TITLE + " ASC"; // 对标题进行升序
        queryHandler.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);// 这个查询方法会运行在子线程
    }


    /**
     * 把Cursor中所有的记录读取出来，保存到ArrayList中
     * @param cursor
     * @return
     */
    private ArrayList<VideoItem> getVideoItems(Cursor cursor) {
        ArrayList<VideoItem> items = new ArrayList<>();
        cursor.moveToFirst();           // 移动到第一条
        do {
            VideoItem item = VideoItem.fromCursor(cursor);
            items.add(item);
        } while (cursor.moveToNext());
        return items;
    }


}
