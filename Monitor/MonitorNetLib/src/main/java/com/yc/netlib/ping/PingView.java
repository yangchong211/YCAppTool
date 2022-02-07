package com.yc.netlib.ping;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.widget.TextView;


public class PingView extends ScrollView implements NetDiagnoListener {

    private NetDiagnoService _netDiagnoService;
    private NetDiagnoListener listener;
    private TextView textView;
    private String showInfo = "";
    private String userId = "--";
    private String deviceId = "--";
    private String versionName = "0.0.0";

    public PingView(Context context) {
        super(context);
        init(context);
    }

    public PingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        addView(textView);
    }

    public void pingHost(String host) {
        showInfo = "";
        setText(showInfo);
        _netDiagnoService = new NetDiagnoService(getContext(), getContext().getPackageName()
                , versionName, userId, deviceId, host, this);
        _netDiagnoService.execute();
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void cancelPing() {
        if (_netDiagnoService!=null){
            _netDiagnoService.cancel(true);
            _netDiagnoService = null;
        }
    }

    public String getPingLog() {
        return showInfo;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (_netDiagnoService != null) {
            _netDiagnoService.stopNetDialogsis();
        }
    }

    /**
     * 诊断结束，输出全部日志记录
     * @param log                       log日志输出
     */
    @Override
    public void OnNetDiagnoFinished(String log) {
        if (listener != null) {
            listener.OnNetDiagnoFinished(log);
        }
        setText(log);
        fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void setLDNetDiagnoListener(NetDiagnoListener listener) {
        this.listener = listener;
    }

    private void setText(String log) {
        textView.setText(log);
    }

    /**
     * 监控网络诊断过程中的日志输出
     * @param log                       log日志输出
     */
    @Override
    public void OnNetDiagnoUpdated(String log) {
        if (listener != null) {
            listener.OnNetDiagnoUpdated(log);
        }
        showInfo += log;
        setText(showInfo);
        fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void OnNetStates(boolean isDomainParseOk, boolean isSocketConnected) {

    }
}
