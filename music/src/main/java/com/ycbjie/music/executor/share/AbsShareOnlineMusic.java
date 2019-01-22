package com.ycbjie.music.executor.share;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.ycbjie.music.api.OnLineMusicModel;
import com.ycbjie.music.executor.inter.IExecutor;
import com.ycbjie.music.model.bean.DownloadInfo;
import com.ycbjie.music.utils.share.ShareComment;
import com.ycbjie.music.utils.share.ShareDetailBean;
import com.ycbjie.music.utils.share.ShareDialog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     author: yangchong
 *     blog  :
 *     time  : 2018/01/24
 *     desc  : 分享在线歌曲
 *     revise:
 * </pre>
 */

public abstract class AbsShareOnlineMusic implements IExecutor<Void> {

    private Context mContext;
    private String mTitle;
    private String mSongId;
    private String mImage;

    protected AbsShareOnlineMusic(Context context, String title, String songId, String image) {
        mContext = context;
        mTitle = title;
        mSongId = songId;
        mImage = image;
    }

    @Override
    public void execute() {
        onPrepare();
        share();
    }

    private void share() {
        // 获取歌曲播放链接
        OnLineMusicModel model = OnLineMusicModel.getInstance();
        model.getMusicDownloadInfo(OnLineMusicModel.METHOD_DOWNLOAD_MUSIC,mSongId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadInfo>() {
                    @Override
                    public void accept(DownloadInfo downloadInfo) throws Exception {
                        if (downloadInfo == null || downloadInfo.getBitrate() == null) {
                            onExecuteFail(null);
                            return;
                        }
                        String file_link = downloadInfo.getBitrate().getFile_link();
                        onExecuteSuccess(null);

                        ShareDetailBean shareDetailBean = new ShareDetailBean();
                        shareDetailBean.setShareType(ShareComment.ShareType.SHARE_GOODS);
                        shareDetailBean.setContent("歌曲分享");
                        shareDetailBean.setTitle(mTitle);
                        shareDetailBean.setImage(mImage);
                        shareDetailBean.setUrl(file_link);
                        ShareDialog shareDialog = new ShareDialog(mContext,shareDetailBean);
                        shareDialog.show(((FragmentActivity)mContext).getSupportFragmentManager());


                        /*Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.share_music,
                                mContext.getString(R.string.app_name), mTitle,
                                file_link));
                        mContext.startActivity(Intent.createChooser(intent,
                                mContext.getString(R.string.share)));*/
                    }
                });
    }
}
