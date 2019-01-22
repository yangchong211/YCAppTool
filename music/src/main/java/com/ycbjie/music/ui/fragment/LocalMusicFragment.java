package com.ycbjie.music.ui.fragment;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ycbjie.library.base.mvp.BackLazyFragment;
import com.ycbjie.music.R;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.inter.OnMoreClickListener;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.ui.activity.MusicActivity;
import com.ycbjie.music.ui.activity.MusicInfoActivity;
import com.ycbjie.music.ui.adapter.LocalMusicAdapter;
import com.ycbjie.music.utils.share.ShareComment;
import com.ycbjie.music.utils.share.ShareDetailBean;
import com.ycbjie.music.utils.share.ShareDialog;
import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import java.util.List;
import java.util.Objects;


/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2017/6/6
 *     desc  : 本地音乐list也米娜
 *     revise:
 * </pre>
 */
public class LocalMusicFragment extends BackLazyFragment implements View.OnClickListener {

    private YCRefreshView recyclerView;

    private MusicActivity activity;
    private LocalMusicAdapter adapter;
    private List<AudioBean> music;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MusicActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    @Override
    public void initListener() {
        adapter.setOnMoreClickListener(new OnMoreClickListener() {
            @Override
            public void onMoreClick(int position) {
                if (music.size() >= position) {
                    final AudioBean localMusic = music.get(position);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle(localMusic.getTitle());
                    dialog.setItems(R.array.local_music_dialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                // 分享
                                case 0:
                                    shareMusic(localMusic);
                                    break;
                                // 设为铃声
                                case 1:
                                    requestSetRingtone(localMusic);
                                    break;
                                // 查看歌曲信息
                                case 2:
                                    MusicInfoActivity.start(getContext(), localMusic);
                                    break;
                                // 删除
                                case 3:

                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
                if(musicList!=null && musicList.size()>0 && musicList.size()>position && position>=0){
                    BaseAppHelper.get().getMusicService().play(position);
                    adapter.updatePlayingPosition(BaseAppHelper.get().getMusicService());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    @Override
    public void initData() {

    }

    @Override
    public void onLazyLoad() {
        recyclerView.showProgress();
        getMusicData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new LocalMusicAdapter(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkUtils.isConnected()){
                    onLazyLoad();
                }else {
                    com.pedaily.yc.ycdialoglib.toast.ToastUtils.showRoundRectToast("没有网络");
                }
            }
        });
    }


    private void getMusicData() {
        //第一种方法，直接扫描，推荐不要使用，如果本地音乐少可以使用。如果几百首，那么会导致线程阻塞和卡顿
        //music = FileScanManager.getInstance().scanMusic(activity);
        //第二种方法，在服务中扫描，推荐使用
        music = BaseAppHelper.get().getMusicList();
        if (music.size() > 0) {
            adapter.clear();
            adapter.addAll(music);
            adapter.notifyDataSetChanged();
            adapter.updatePlayingPosition(BaseAppHelper.get().getMusicService());
            recyclerView.showRecycler();
        } else {
            recyclerView.showEmpty();
        }
    }


    /**
     * 分享
     */
    private void shareMusic(AudioBean localMusic) {
        String title = localMusic.getTitle();
        String artist = localMusic.getArtist();
        ShareDetailBean shareDetailBean = new ShareDetailBean();
        shareDetailBean.setShareType(ShareComment.ShareType.SHARE_GOODS);
        shareDetailBean.setContent(artist);
        shareDetailBean.setTitle(title);
        shareDetailBean.setImage("");
        ShareDialog shareDialog = new ShareDialog(activity,shareDetailBean);
        shareDialog.show(activity.getSupportFragmentManager());
    }


    /**
     * 设置为铃声
     */
    private void requestSetRingtone(AudioBean localMusic) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(getContext())) {
            ToastUtils.showShort(R.string.no_permission_setting);
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            startActivityForResult(intent, 0);
        } else {
            setRingtone(localMusic);
        }
    }

    /**
     * 设置铃声
     */
    private void setRingtone(AudioBean localMusic) {
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(localMusic.getPath());
        // 查询音乐文件在媒体库是否存在
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri, null,
                    MediaStore.MediaColumns.DATA + "=?", new String[]{localMusic.getPath()}, null);
        }
        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            String _id = cursor.getString(0);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_PODCAST, false);

            getContext().getContentResolver().update(uri, values, MediaStore.MediaColumns.DATA + "=?",
                    new String[]{localMusic.getPath()});
            Uri newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
            RingtoneManager.setActualDefaultRingtoneUri(getContext(), RingtoneManager.TYPE_RINGTONE, newUri);
            ToastUtils.showShort("设置铃声成功");
        }
        cursor.close();
    }

    /**
     * 点击MainActivity中的控制器，更新musicFragment中的mLocalMusicFragment
     */
    public void onItemPlay() {
        if (BaseAppHelper.get().getMusicService().getPlayingMusic().getType() == AudioBean.Type.LOCAL) {
            recyclerView.scrollToPosition(BaseAppHelper.get().getMusicService().getPlayingPosition());
        }
        adapter.updatePlayingPosition(BaseAppHelper.get().getMusicService());
        adapter.notifyDataSetChanged();
    }

    public void onRefresh() {
        RecyclerView mRecyclerView = recyclerView.getRecyclerView();
        LinearLayoutManager manager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        int firstVisibleItemPosition = 0;
        if (manager != null) {
            firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        }
        if (firstVisibleItemPosition == 0) {
            onLazyLoad();
            return;
        }
        mRecyclerView.scrollToPosition(5);
        mRecyclerView.smoothScrollToPosition(0);
    }


}
