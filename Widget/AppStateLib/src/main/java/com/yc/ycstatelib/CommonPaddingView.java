package com.yc.ycstatelib;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/6
 *     desc  : 自定义状态管理，通用填充view
 *     revise:
 * </pre>
 */
public class CommonPaddingView extends FrameLayout implements View.OnClickListener {

    private Context mContext;
    public static final int DEFAULT = 0;          // 当前显示的状态
    public static final int NET = 1;          // 显示无网络视图
    public static final int LOADING = 2;        // 显示loading视图
    public static final int HIDE = 3;            // 隐藏整个视图
    public static final int EMPTY = 4;        // 空视图
    public static final int NEW_STYLE = 5;    // 文字描述+按钮
    private int currentState = DEFAULT;
    private ViewStub loadingSub;
    private ViewStub networkSub;
    private ViewStub emptySub;
    private ViewStub newStyleSub;
    private View loadingView;
    private View networkView;
    private View emptyView;
    private View newStyleView;

    private ImageView ivEmptyBg;
    private TextView ivEmptyDesc;
    private TextView ivNewStyleBtn;
    private TextView ivNewStyleDesc;
    private PaddingViewListener paddingViewListener;
    private View viewFooterFill;
    private ViewGroup mViewContainer;
    private boolean mNight = false;
    private ImageView loading_iv;
    private Animation animation;

    public CommonPaddingView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CommonPaddingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonPaddingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        setBackgroundColor(Color.WHITE);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mRootView = inflater.inflate(R.layout.custom_padding_view, this, true);
        mViewContainer = mRootView.findViewById(R.id.ll_custom_padding_view);
        viewFooterFill = mRootView.findViewById(R.id.view_footer_fill);
        loadingSub = (ViewStub) mRootView.findViewById(R.id.loading);
        networkSub = (ViewStub) mRootView.findViewById(R.id.network_view);
        emptySub = (ViewStub) mRootView.findViewById(R.id.empty);
        newStyleSub = (ViewStub) mRootView.findViewById(R.id.new_style);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setNightMode(boolean isNight) {
        mNight = isNight;
        refreshNightTheme();
    }

    private void refreshNightTheme() {
        if (mViewContainer != null && mContext != null) {
            mViewContainer.setBackgroundColor(mNight ? mContext.getResources().getColor(R.color.color_555555) :
                    mContext.getResources().getColor(R.color.color_FFFFFF));
        }
    }

    public void setViewState(int status) {
        currentState = status;
        resetState();
        switch (status) {
            case NET:
                initNetworkView();
                if (null != networkView) {
                    setVisibility(View.VISIBLE);
                    networkView.setVisibility(View.VISIBLE);
                }
                break;
            case LOADING:
                initLoadingView();
                if (null != loadingView) {
                    setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.VISIBLE);
                    startLoading();
                }
                break;
            case HIDE:
                // 已经调用了resetState方法，这里无须执行其他的逻辑了
                setVisibility(View.GONE);
                break;
            case EMPTY:
                if (!StateToolUtils.isConnected(mContext)) {
                    setViewState(NET);
                    return;
                }
                initEmptyView();
                if (null != emptyView) {
                    setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                break;
            case NEW_STYLE:
                if (!StateToolUtils.isConnected(mContext)) {
                    setViewState(NET);
                    return;
                }

                initNewStyleView();

                if (null != newStyleView) {
                    setVisibility(View.VISIBLE);
                    newStyleView.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
    }

    private void resetState() {
        if (null != loadingView) {
            loadingView.setVisibility(View.GONE);
        }
        if (null != networkView) {
            networkView.setVisibility(View.GONE);
        }
        if (null != emptyView) {
            emptyView.setVisibility(View.GONE);
        }
        if (null != newStyleView) {
            newStyleView.setVisibility(View.GONE);
        }
        stopLoading();
    }

    public void hide() {
        setVisibility(GONE);
        stopLoading();
    }

    @Override
    public void onClick(View v) {
        if (v == ivNewStyleBtn) {
            if (null != paddingViewListener) {
                paddingViewListener.onNewStyleBtnClicked(v);
            }
        } else if (v == networkView) {
            if (null != paddingViewListener) {
                paddingViewListener.onDisableNetViewClicked(v);
            }
        }
    }

    public interface PaddingViewListener {

        void onDisableNetViewClicked(View view);

        void onNewStyleBtnClicked(View view);
    }


    private void startLoading() {
        stopLoading();
        //创建旋转动画
        animation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setRepeatCount(100);
        //设置无限
        animation.setRepeatMode(Animation.INFINITE);
        //设置为true，动画转化结束后被应用
        animation.setFillAfter(true);
        //#匀速圆周运动
        animation.setInterpolator(new LinearInterpolator());
        //开始动画
        if (loading_iv!=null){
            loading_iv.startAnimation(animation);
        }
    }


    private void stopLoading() {
        //注意需要销毁动画
        if (animation!=null){
            animation.cancel();
            loading_iv.clearAnimation();
        }
    }


    private void initLoadingView() {
        if (null == loadingView && null != loadingSub) {
            loadingView = loadingSub.inflate();
            loading_iv = loadingView.findViewById(R.id.loading_iv);
            loading_iv.setBackgroundResource(R.drawable.loading_bg);
        }
        refreshNightTheme();
    }

    private void initNetworkView() {
        if (null == networkView && null != networkSub) {
            networkView = networkSub.inflate();
            networkView.setOnClickListener(this);
        }
    }

    private void initEmptyView() {
        if (null == emptyView && null != emptySub) {
            emptyView = emptySub.inflate();
        }
    }

    private void initNewStyleView() {
        if (null == newStyleView && null != newStyleSub) {
            newStyleView = newStyleSub.inflate();
        }
    }

    public CommonPaddingView setEmptyTitle(String title) {
        initEmptyView();
        if (null == ivEmptyDesc && null != emptyView) {
            ivEmptyDesc = (TextView) emptyView.findViewById(R.id.tv_empty_name);
        }
        if (null != ivEmptyDesc) {
            ivEmptyDesc.setText(title);
        }
        return this;
    }

    public CommonPaddingView setEmptyTitle(int id) {
        initEmptyView();
        if (null == ivEmptyDesc && null != emptyView) {
            ivEmptyDesc = (TextView) emptyView.findViewById(R.id.tv_empty_name);
        }
        if (null != ivEmptyDesc) {
            ivEmptyDesc.setText(id);
        }
        return this;
    }

    public CommonPaddingView setEmptyTitleMaginTop(int topMagin) {
        initEmptyView();
        if (null == ivEmptyDesc && null != emptyView) {
            ivEmptyDesc = (TextView) emptyView.findViewById(R.id.tv_empty_name);
        }
        if (null != ivEmptyDesc) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivEmptyDesc.getLayoutParams();
            //设置上部间距
            layoutParams.topMargin = topMagin;
            ivEmptyDesc.setLayoutParams(layoutParams);
        }
        return this;
    }

    public CommonPaddingView setEmptyBackResource(int resourceId) {
        initEmptyView();
        if (null == ivEmptyBg && null != emptyView) {
            ivEmptyBg = (ImageView) emptyView.findViewById(R.id.iv_empty_bg);
        }
        if (null != ivEmptyBg) {
            ivEmptyBg.setImageResource(resourceId);
        }
        return this;
    }

    public CommonPaddingView setNewStyleDesc(String desc) {
        initNewStyleView();
        if (null == ivNewStyleDesc && null != newStyleView) {
            ivNewStyleDesc = (TextView) newStyleView.findViewById(R.id.tv_desc);
        }
        if (null != ivNewStyleDesc) {
            ivNewStyleDesc.setText(desc);
        }
        return this;
    }

    public CommonPaddingView setNewStyleBtnName(String name) {
        initNewStyleView();
        if (null == ivNewStyleBtn && null != newStyleView) {
            ivNewStyleBtn = (TextView) newStyleView.findViewById(R.id.tv_btn);
        }
        if (null != ivNewStyleBtn) {
            ivNewStyleBtn.setText(name);
            ivNewStyleBtn.setOnClickListener(this);
        }
        return this;
    }

    public CommonPaddingView setPaddingViewListener(PaddingViewListener paddingViewListener) {
        this.paddingViewListener = paddingViewListener;
        return this;
    }

    public void setViewFooterFillHeightDp(float height) {
        setViewFooterFillHeightPx(StateToolUtils.dp2px(mContext,height));
    }

    public void setViewFooterFillHeightPx(float height) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
                this.viewFooterFill.getLayoutParams();
        linearParams.height = (int) height;
        this.viewFooterFill.setLayoutParams(linearParams);
    }


    public static class Builder{

    }

}
