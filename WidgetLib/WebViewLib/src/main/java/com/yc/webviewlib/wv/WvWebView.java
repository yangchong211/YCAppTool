package com.yc.webviewlib.wv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.Keep;
import androidx.appcompat.app.AlertDialog;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebView;
import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.base.X5WebViewClient;
import com.yc.webviewlib.view.BaseWebView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义WebView类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 *             该demo可以作为学习案例，实现js交互的思路和BridgeWebView不一样，对比学习
 * </pre>
 */
public class WvWebView extends BaseWebView {

    private static final String BRIDGE_NAME = "WVJBInterface";
    private static final int HANDLE_MESSAGE = 4;
    private MyHandler mainThreadHandler = null;
    private JsCloseListener javascriptCloseWindowListener=null;
    private ArrayList<WvMessage> startupMessageQueue = null;
    private Map<String, ResponseCallback> responseCallbacks = null;
    private Map<String, WvJsHandler> messageHandlers = null;
    private long uniqueId = 0;
    private boolean alertBoxBlock=true;
    
    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        private WeakReference<Context> mContextReference;

        MyHandler(Context context) {
            super(Looper.getMainLooper());
            mContextReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            final Context context = mContextReference.get();
            if (context != null) {
                switch (msg.what) {
                    case HANDLE_MESSAGE:
                        WvWebView.this.handleMessage((String) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public WvWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WvWebView(Context context) {
        super(context);
        init();
    }

    public void disableJavascriptAlertBoxSafetyTimeout(boolean disable){
        alertBoxBlock = !disable;
    }

    public void callHandler(String handlerName) {
        callHandler(handlerName, null, null);
    }

    public  void callHandler(String handlerName, Object data) {
        callHandler(handlerName, data, null);
    }

    public  <T> void callHandler(String handlerName, Object data, ResponseCallback<T> responseCallback) {
        sendData(data, responseCallback, handlerName);
    }

    /**
     * Test whether the handler exist in javascript
     * @param handlerName
     * @param callback
     */
    public void hasJavascriptMethod(String handlerName, final MethodExistCallback callback){
        callHandler("_hasJavascriptMethod", handlerName, new ResponseCallback() {
            @Override
            public void onResult(Object data) {
                callback.onResult((boolean)data);
            }
        });
    }

    /**
     * set a listener for javascript closing the current activity.
     */
    public void setJavascriptCloseWindowListener(JsCloseListener listener){
        javascriptCloseWindowListener=listener;
    }

    /**
     * js调native
     * @param handlerName                               名称
     * @param handler                                   消息
     * @param <T>                                       T
     * @param <R>                                       R
     */
    public <T,R> void registerHandler(String handlerName, WvJsHandler<T,R> handler) {
        if (handlerName == null || handlerName.length() == 0 || handler == null) {
            return;
        }
        messageHandlers.put(handlerName, handler);
    }

    /**
     * send the onResult message to javascript
     * 发送消息给js
     * @param data                                      data
     * @param responseCallback                          callback
     * @param handlerName                               handlerName
     */
    private void sendData(Object data, ResponseCallback responseCallback, String handlerName) {
        if (data == null && (handlerName == null || handlerName.length() == 0)) {
            return;
        }
        WvMessage message = new WvMessage();
        if (data != null) {
            message.data = data;
        }
        if (responseCallback != null) {
            String callbackId = "java_cb_" + (++uniqueId);
            responseCallbacks.put(callbackId, responseCallback);
            message.callbackId = callbackId;
        }
        if (handlerName != null) {
            message.handlerName = handlerName;
        }
        queueMessage(message);
    }

    private synchronized void queueMessage(WvMessage message) {
        if (startupMessageQueue != null) {
            startupMessageQueue.add(message);
        } else {
            dispatchMessage(message);
        }
    }

    private void dispatchMessage(WvMessage message) {
        String messageJson = JsonHelper.messageToJsonObject(message).toString();
        String format = String.format("WebViewJavascriptBridge._handleMessageFromJava(%s)", messageJson);
        WvWebView.super.evaluateJavascript(format);
    }

    // handle the onResult message from javascript
    private void handleMessage(String info) {
        try {
            JSONObject jo = new JSONObject(info);
            WvMessage message = JsonHelper.JsonObjectToMessage(jo);
            if (message.responseId != null) {
                ResponseCallback responseCallback = responseCallbacks.remove(message.responseId);
                if (responseCallback != null) {
                    responseCallback.onResult(message.responseData);
                }
            } else {
                ResponseCallback responseCallback = null;
                if (message.callbackId != null) {
                    final String callbackId = message.callbackId;
                    responseCallback = new ResponseCallback() {
                        @Override
                        public void onResult(Object data) {
                            WvMessage msg = new WvMessage();
                            msg.responseId = callbackId;
                            msg.responseData = data;
                            dispatchMessage(msg);
                        }
                    };
                }

                WvJsHandler handler;
                handler = messageHandlers.get(message.handlerName);
                if (handler != null) {
                    handler.handler(message.data, responseCallback);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Keep
    void init() {
        mainThreadHandler = new MyHandler(getContext());
        this.responseCallbacks = new HashMap<>();
        this.messageHandlers = new HashMap<>();
        this.startupMessageQueue = new ArrayList<>();
        super.setWebChromeClient(mWebChromeClient);
        super.setWebViewClient(mWebViewClient);
        registerHandler("_hasNativeMethod", new WvJsHandler() {
            @Override
            public void handler(Object data, ResponseCallback callback) {
                callback.onResult(messageHandlers.get(data) != null);
            }
        });
        registerHandler("_closePage", new WvJsHandler() {
            @Override
            public void handler(Object data, ResponseCallback callback) {
                if(javascriptCloseWindowListener==null ||javascriptCloseWindowListener.onClose()){
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });
        registerHandler("_disableJavascriptAlertBoxSafetyTimeout", new WvJsHandler() {
            @Override
            public void handler(Object data, ResponseCallback callback) {
                disableJavascriptAlertBoxSafetyTimeout((boolean)data);
            }
        });
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.JELLY_BEAN){
            super.addJavascriptInterface(new Object() {
                @Keep
                @JavascriptInterface
                public void notice(String info) {
                    Message msg = mainThreadHandler.obtainMessage(HANDLE_MESSAGE, info);
                    mainThreadHandler.sendMessage(msg);
                }
            }, BRIDGE_NAME);
        }

    }

    protected X5WebViewClient generateBridgeWebViewClient() {
        return mWebViewClient;
    }

    protected X5WebChromeClient generateBridgeWebChromeClient() {
        return mWebChromeClient;
    }

    private X5WebChromeClient mWebChromeClient = new X5WebChromeClient(this,(Activity) getContext()) {

        private boolean isShowContent = false;
        private int max = 85;

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress>max && !isShowContent) {
                try {
                    InputStream is = view.getContext().getAssets().open("WvWebViewJavascriptBridge.js");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    String js = new String(buffer);
                    WvWebView.super.evaluateJavascript(js);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (WvWebView.this) {
                    if (startupMessageQueue != null) {
                        for (int i = 0; i < startupMessageQueue.size(); i++) {
                            dispatchMessage(startupMessageQueue.get(i));
                        }
                        startupMessageQueue = null;
                    }
                }
                isShowContent = true;
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, final String message, final JsResult result) {
            if(!alertBoxBlock){
                result.confirm();
            }
            Dialog alertDialog = new AlertDialog.Builder(getContext()).
                    setMessage(message).
                    setCancelable(false).
                    setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if(alertBoxBlock) {
                                result.confirm();
                            }
                        }
                    })
                    .create();
            alertDialog.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            if(!alertBoxBlock){
                result.confirm();
            }
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(alertBoxBlock) {
                        if (which == Dialog.BUTTON_POSITIVE) {
                            result.confirm();
                        } else {
                            result.cancel();
                        }
                    }
                }
            };
            new AlertDialog.Builder(getContext())
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, listener).show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, final String message,
                                  String defaultValue, final JsPromptResult result) {
            if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.JELLY_BEAN){
                String prefix="_wvjbxx";
                if(message.equals(prefix)){
                    Message msg = mainThreadHandler.obtainMessage(HANDLE_MESSAGE, defaultValue);
                    mainThreadHandler.sendMessage(msg);
                }
                return true;
            }
            if(!alertBoxBlock){
                result.confirm();
            }
            final EditText editText = new EditText(getContext());
            editText.setText(defaultValue);
            if (defaultValue != null) {
                editText.setSelection(defaultValue.length());
            }
            float dpi = getContext().getResources().getDisplayMetrics().density;
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(alertBoxBlock) {
                        if (which == Dialog.BUTTON_POSITIVE) {
                            result.confirm(editText.getText().toString());
                        } else {
                            result.cancel();
                        }
                    }
                }
            };
            new AlertDialog.Builder(getContext())
                    .setTitle(message)
                    .setView(editText)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, listener)
                    .show();
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int t = (int) (dpi * 16);
            layoutParams.setMargins(t, 0, t, 0);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            editText.setLayoutParams(layoutParams);
            int padding = (int) (15 * dpi);
            editText.setPadding(padding - (int) (5 * dpi), padding, padding, padding);
            return true;
        }
    };

    private X5WebViewClient mWebViewClient = new X5WebViewClient(this,getContext()) {

    };

}
