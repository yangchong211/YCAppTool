package com.ycbjie.music.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ns.yc.ycutilslib.loadingDialog.LoadDialog;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.music.R;
import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.executor.download.AbsDownloadOnlineMusic;
import com.ycbjie.music.executor.online.AbsPlayOnlineMusic;
import com.ycbjie.music.executor.share.AbsShareOnlineMusic;
import com.ycbjie.music.inter.OnMoreClickListener;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.bean.OnLineSongListInfo;
import com.ycbjie.music.model.bean.OnlineMusicList;
import com.ycbjie.music.ui.adapter.LineMusicAdapter;
import com.ycbjie.music.ui.fragment.PlayMusicFragment;
import com.ycbjie.music.utils.FileMusicUtils;
import com.ycbjie.music.utils.ImageUtils;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.io.File;
import java.util.List;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class OnlineMusicActivity extends BaseActivity implements View.OnClickListener {


    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private YCRefreshView recyclerView;

    private OnLineSongListInfo mListInfo;
    private LineMusicAdapter adapter;
    private int mOffset = 1;
    private static final String MUSIC_LIST_SIZE = "10";
    private boolean isPlayFragmentShow = false;
    private PlayMusicFragment mPlayFragment;
    private String type = "1";

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        super.onBackPressed();
    }


    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_bar;
    }


    @Override
    public void initView() {
        if (!BaseAppHelper.get().checkServiceAlive()) {
            return;
        }
        initFindById();
        initIntentData();
        initToolBar();
        initRecyclerView();
    }

    private void initFindById() {

        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);
        recyclerView = findViewById(R.id.recyclerView);

    }


    private void initToolBar() {
        if(mListInfo!=null){
            toolbarTitle.setText(mListInfo.getTitle());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initIntentData() {
        if (getIntent()!=null){
            mListInfo = (OnLineSongListInfo) getIntent()
                    .getSerializableExtra("music_list_type");
            type = mListInfo.getType();
        }
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }


    @Override
    public void initData() {
        recyclerView.showProgress();
        getData(mOffset);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();

        } else {
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LineMusicAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOffset = 0;
                initData();
            }
        });
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        mOffset++;
                        getData(mOffset);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    ToastUtils.showRoundRectToast("没有网络");
                }
            }

            @Override
            public void onMoreClick() {

            }
        });
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showRoundRectToast("没有网络");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showRoundRectToast("没有网络");
                }
            }
        });
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
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>-1){
                    OnlineMusicList.SongListBean onlineMusic = adapter.getAllData().get(position);
                    playMusic(onlineMusic);
                }
            }
        });
        adapter.setOnMoreClickListener(new OnMoreClickListener() {
            @Override
            public void onMoreClick(int position) {
                //这个地方需要+1
                showMoreDialog(position + 1);
            }
        });
    }


    private void addHeader(final OnlineMusicList onlineMusicList) {
        adapter.removeAllHeader();
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return getLayoutInflater().inflate(R.layout.header_online_music, parent, false);
            }

            @Override
            public void onBindView(View view) {
                final ImageView iv_header_bg = (ImageView) view.findViewById(R.id.iv_header_bg);
                final ImageView iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                TextView tv_update_date = (TextView) view.findViewById(R.id.tv_update_date);
                TextView tv_comment = (TextView) view.findViewById(R.id.tv_comment);

                tv_title.setText(onlineMusicList.getBillboard().getName());
                tv_update_date.setText(getString(R.string.recent_update, onlineMusicList.getBillboard().getUpdate_date()));
                tv_comment.setText(onlineMusicList.getBillboard().getComment());
                GlideApp.with(OnlineMusicActivity.this)
                        .asBitmap()
                        .load(onlineMusicList.getBillboard().getPic_s640())
                        .placeholder(R.drawable.default_cover)
                        .error(R.drawable.default_cover)
                        .override(200, 200)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                iv_cover.setImageBitmap(resource);
                                iv_header_bg.setImageBitmap(ImageUtils.blur(resource));
                            }
                        });
            }
        });
    }


    private void getData(final int offset) {
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getSongListInfo(OnLineMusicModel.METHOD_GET_MUSIC_LIST,
                type, MUSIC_LIST_SIZE, String.valueOf(offset))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OnlineMusicList>() {
                    @Override
                    public void accept(OnlineMusicList onlineMusicList) throws Exception {
                        if (onlineMusicList == null || onlineMusicList.getSong_list() == null || onlineMusicList.getSong_list().size() == 0) {
                            return;
                        }
                        addHeader(onlineMusicList);
                        adapter.addAll(onlineMusicList.getSong_list());
                        recyclerView.showRecycler();
                        setOnLineMusic(onlineMusicList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof RuntimeException) {
                            // 歌曲全部加载完成
                            recyclerView.showError();
                            return;
                        }
                        if (offset == 0) {
                            recyclerView.showError();
                        } else {
                            ToastUtils.showRoundRectToast(Utils.getApp().getResources().getString(R.string.load_fail));
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    private void setOnLineMusic(OnlineMusicList onlineMusicList) {
        List<OnlineMusicList.SongListBean> song_list = onlineMusicList.getSong_list();
        for (int a=0 ; a<song_list.size() ; a++){
            AudioBean audioBean = new AudioBean();
            audioBean.setId(song_list.get(a).getSong_id());
            audioBean.setTitle(song_list.get(a).getTitle());
            audioBean.setArtist(song_list.get(a).getArtist_name());
            audioBean.setAlbum(song_list.get(a).getAlbum_800_800());
            audioBean.setDuration(song_list.get(a).getFile_duration()*1000);
        }
    }


    private void showMoreDialog(int position) {
        final OnlineMusicList.SongListBean onlineMusic = adapter.getAllData().get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(adapter.getAllData().get(position).getTitle());
        String path = FileMusicUtils.getMusicDir() + FileMusicUtils.getMp3FileName(
                onlineMusic.getArtist_name(), onlineMusic.getTitle());
        File file = new File(path);
        int itemsId = file.exists() ? R.array.online_music_dialog_without_download : R.array.online_music_dialog;
        dialog.setItems(itemsId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    // 分享
                    case 0:
                        share(onlineMusic);
                        break;
                    // 查看歌手信息
                    case 1:
                        lookArtistInfo(onlineMusic);
                        break;
                    // 下载
                    case 2:
                        download(onlineMusic);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.show();
    }

    /**
     * 展示页面
     */
    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = PlayMusicFragment.newInstance("OnLine");
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }


    /**
     * 隐藏页面
     */
    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    /**
     * 播放音乐
     * @param onlineMusic                       onlineMusic
     */
    private void playMusic(OnlineMusicList.SongListBean onlineMusic) {
        new AbsPlayOnlineMusic(this, onlineMusic) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onExecuteSuccess(AudioBean music) {
                BaseAppHelper.get().getMusicService().play(music);
                showPlayingFragment();
                ToastUtils.showRoundRectToast("正在播放" + music.getTitle());
            }

            @Override
            public void onExecuteFail(Exception e) {
                ToastUtils.showRoundRectToast(Utils.getApp().getResources().getString(R.string.unable_to_play));
            }
        }.execute();
    }


    /**
     * 分享音乐
     *
     * @param onlineMusic 实体类
     */
    private void share(OnlineMusicList.SongListBean onlineMusic) {
        new AbsShareOnlineMusic(this, onlineMusic.getTitle(), onlineMusic.getSong_id()
                ,onlineMusic.getAlbum_1000_1000()) {
            @Override
            public void onPrepare() {
                LoadDialog.show(OnlineMusicActivity.this, "下载中……");
            }

            @Override
            public void onExecuteSuccess(Void aVoid) {
                LoadDialog.dismiss(OnlineMusicActivity.this);
            }

            @Override
            public void onExecuteFail(Exception e) {
                LoadDialog.dismiss(OnlineMusicActivity.this);
            }
        }.execute();
    }


    /**
     * 查看歌手信息
     *
     * @param onlineMusic 实体类
     */
    private void lookArtistInfo(OnlineMusicList.SongListBean onlineMusic) {
        Intent intent = new Intent(this, ArtistInfoActivity.class);
        intent.putExtra("artist_id", onlineMusic.getTing_uid());
        startActivity(intent);
    }


    /**
     * 下载音乐
     *
     * @param onlineMusic 实体类
     */
    private void download(final OnlineMusicList.SongListBean onlineMusic) {
        new AbsDownloadOnlineMusic(this, onlineMusic) {
            @Override
            public void onPrepare() {
                LoadDialog.show(OnlineMusicActivity.this, "下载中……");
            }

            @Override
            public void onExecuteSuccess(Void aVoid) {
                LoadDialog.dismiss(OnlineMusicActivity.this);
                ToastUtils.showRoundRectToast("下载成功" + onlineMusic.getTitle());
            }

            @Override
            public void onExecuteFail(Exception e) {
                LoadDialog.dismiss(OnlineMusicActivity.this);
                ToastUtils.showRoundRectToast("下载失败");
            }
        }.execute();
    }


}
