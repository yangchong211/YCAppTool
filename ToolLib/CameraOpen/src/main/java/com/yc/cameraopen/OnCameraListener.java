package com.yc.cameraopen;

public interface OnCameraListener {
    void onCameraCallback(byte[] data, int width, int height);
}
