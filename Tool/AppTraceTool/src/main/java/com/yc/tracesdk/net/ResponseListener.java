package com.yc.tracesdk.net;

public interface ResponseListener {
    public void onReceiveResponse(String response);

    public void onReceiveError(int code);
}
