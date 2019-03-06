package com.ycbjie.music.utils.share;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedaily.yc.ycdialoglib.dialogFragment.BaseDialogFragment;
import com.ycbjie.library.utils.handler.HandlerHolder;
import com.ycbjie.music.R;



/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211/YCBlogs
 *     time  : 2017/01/30
 *     desc  : 分享弹窗，主要是为了练习策略者模式
 *     revise:
 * </pre>
 */
public class ShareDialog extends BaseDialogFragment implements View.OnClickListener {

    private Context mContext;
    private ShareTypeBean shareTypeBean;
    private TextView mTvImg;
    private TextView mTvWxFriend;
    private TextView mTvWxMoment;
    private TextView mTvOther;
    private ImageView mIvCancel;

    private HandlerHolder handler = new HandlerHolder(msg -> {
        switch (msg.what){
            case 1:
                TextView textView1 = (TextView) msg.obj;
                showBottomInAnimation(textView1);
                break;
            case 2:
                TextView textView2 = (TextView) msg.obj;
                showBottomOutAnimation(textView2);
                break;
            default:
                break;
        }
    });

    public ShareDialog(){
        
    }
    
    @SuppressLint("ValidFragment")
    public ShareDialog(Context activity , ShareTypeBean shareTypeBean) {
        this.mContext = activity;
        this.shareTypeBean = shareTypeBean;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置DialogFragment全屏显示
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                window.setLayout(width, height);
            }
        }
    }



    @Override
    public int getLayoutRes() {
        return R.layout.dialog_bottom_share_view;
    }

    @Override
    public void bindView(View v) {
        setLocal(Local.BOTTOM);
        mTvImg = v.findViewById(R.id.tv_img);
        mTvWxFriend = v.findViewById(R.id.tv_wx_friend);
        mTvWxMoment = v.findViewById(R.id.tv_wx_moment);
        mTvOther = v.findViewById(R.id.tv_other);
        FrameLayout mFlCancel = v.findViewById(R.id.fl_cancel);
        mIvCancel = v.findViewById(R.id.iv_cancel);


        mFlCancel.setOnClickListener(this);
        //mTvImg.setOnClickListener(this);
        //mTvWxFriend.setOnClickListener(this);
        //mTvWxMoment.setOnClickListener(this);
        //mTvOther.setOnClickListener(this);
        showDialogAnim();
        touchDownAnim(mTvImg,mTvWxMoment,mTvWxFriend,mTvOther,ShareComment.ShareViewType.SHARE_CREATE_POSTER);
        touchDownAnim(mTvWxFriend,mTvImg,mTvWxMoment,mTvOther, ShareComment.ShareViewType.SHARE_FRIEND);
        touchDownAnim(mTvWxMoment,mTvWxFriend,mTvImg,mTvOther, ShareComment.ShareViewType.SHARE_FRIEND_CIRCLE);
        touchDownAnim(mTvOther,mTvWxFriend,mTvWxMoment,mTvImg, ShareComment.ShareViewType.SHARE_SAVE_PIC);
    }

    @Override
    protected boolean isCancel() {
        return false;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fl_cancel) {
            dismissDialogAnim();

        } else if (i == R.id.tv_img) {
            touchDownAnim(mTvImg, mTvWxMoment, mTvWxFriend, mTvOther, ShareComment.ShareViewType.SHARE_CREATE_POSTER);

        } else if (i == R.id.tv_wx_friend) {
            touchDownAnim(mTvWxFriend, mTvImg, mTvWxMoment, mTvOther, ShareComment.ShareViewType.SHARE_FRIEND);

        } else if (i == R.id.tv_wx_moment) {
            touchDownAnim(mTvWxMoment, mTvWxFriend, mTvImg, mTvOther, ShareComment.ShareViewType.SHARE_FRIEND_CIRCLE);

        } else if (i == R.id.tv_other) {
            touchDownAnim(mTvOther, mTvWxFriend, mTvWxMoment, mTvImg, ShareComment.ShareViewType.SHARE_SAVE_PIC);

        }
    }


    /**
     * 弹出对话框，有动画
     */
    private void showDialogAnim() {
        mTvImg.setVisibility(View.GONE);
        mTvWxFriend.setVisibility(View.GONE);
        mTvWxMoment.setVisibility(View.GONE);
        mTvOther.setVisibility(View.GONE);
        // 菜单按钮动画
        mIvCancel.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_rotate_right));
        // 小队分享 要展示三个
        // 选项动画
        delayedBottomInRunnable(0, mTvImg);
        delayedBottomInRunnable(100, mTvWxFriend);
        delayedBottomInRunnable(200, mTvWxMoment);
        delayedBottomInRunnable(300, mTvOther);
    }

    private void dismissDialogAnim() {
        mIvCancel.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_rotate_left));
        showBottomOutAnimation(mTvOther);
        delayedBottomOutRunnable(50, mTvWxMoment);
        delayedBottomOutRunnable(100, mTvWxFriend);
        delayedBottomOutRunnable(150, mTvImg);
        if(isAdded()){
            dismiss();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void touchDownAnim(final TextView view1, final TextView view2, final TextView view3, final TextView view4, final String type) {
        view1.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    // 按下 放大
                    Animation animationDown  = AnimationUtils.loadAnimation(mContext, R.anim.share_touch_down);
                    view1.startAnimation(animationDown);
                    break;
                case MotionEvent.ACTION_UP:
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.share_touch_up_select_scale);
                    view1.startAnimation(animation);
                    // 其余的view 缩小消失
                    view2.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_touch_up_noselect_scale));
                    view3.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_touch_up_noselect_scale));
                    view4.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_touch_up_noselect_scale));
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Share share = new Share();
                            String share_type = shareTypeBean.getShareType();
                            switch (share_type) {
                                case ShareComment.ShareType.SHARE_GOODS:    // 商品分享
                                    share.setSharePolicy(new ShareGoodsPolicy());
                                    break;
                                case ShareComment.ShareType.SHARE_MATERIAL: // 素材分享
                                    share.setSharePolicy(new ShareMaterialPolicy());
                                    break;
                                case ShareComment.ShareType.SHARE_TEAM:     //  小队分享
                                    share.setSharePolicy(new ShareTrimPolicy());
                                    break;
                                case ShareComment.ShareType.SHARE_SPECIAL:  //专题分享
                                    share.setSharePolicy(new ShareSpecialPolicy());
                                    break;
                                default:
                                    break;
                            }
                            share.share(type, shareTypeBean, getContext());
                            dismiss();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                default:
                    break;
            }
            return false;
        });
    }


    /**
     * 延时向上的任务
     * @param time                          time
     * @param textView                      textView
     */
    private void delayedBottomInRunnable(int time, final TextView textView) {
        Message message = new Message();
        message.what = 1;
        message.obj = textView;
        handler.sendMessageDelayed(message,time);
    }

    /**
     * 延时向下的任务
     *
     * @param time                          time
     * @param textView                      textView
     */
    private void delayedBottomOutRunnable(int time, final TextView textView) {
        Message message = new Message();
        message.what = 2;
        message.obj = textView;
        handler.sendMessageDelayed(message,time);
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showBottomOutAnimation(textView);
            }
        }, time);*/
    }


    /**
     * show 弹出动画
     * @param textView                      textView
     */
    private void showBottomInAnimation(TextView textView) {
        textView.setVisibility(View.VISIBLE);
        textView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_bottom_in));
    }

    /**
     * show 收回动画
     *
     * @param textView                      textView
     */
    private void showBottomOutAnimation(TextView textView) {
        textView.setVisibility(View.INVISIBLE);
        textView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.share_bottom_out));
    }


}
