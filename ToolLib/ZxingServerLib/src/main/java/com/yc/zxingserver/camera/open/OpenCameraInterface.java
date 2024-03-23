package com.yc.zxingserver.camera.open;

import android.hardware.Camera;

import com.yc.toolutils.system.AppLogUtils;

public final class OpenCameraInterface {

    /**
     * For {@link #open(int)}, means no preference for which camera to open.
     */
    public static final int NO_REQUESTED_CAMERA = -1;

    private OpenCameraInterface() {
    }

    /**
     * Opens the requested camera with {@link Camera#open(int)}, if one exists.
     *
     * @param cameraId camera ID of the camera to use. A negative value
     *                 or {@link #NO_REQUESTED_CAMERA} means "no preference", in which case a rear-facing
     *                 camera is returned if possible or else any camera
     * @return handle to {@link OpenCamera} that was opened
     */
    public static OpenCamera open(int cameraId) {

        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            AppLogUtils.w( "No cameras!");
            return null;
        }
        if (cameraId >= numCameras) {
            AppLogUtils.w( "Requested camera does not exist: " + cameraId);
            return null;
        }

        if (cameraId <= NO_REQUESTED_CAMERA) {
            cameraId = 0;
            while (cameraId < numCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, cameraInfo);
                if (CameraFacing.values()[cameraInfo.facing] == CameraFacing.BACK) {
                    break;
                }
                cameraId++;
            }
            if (cameraId == numCameras) {
                AppLogUtils.i("No camera facing " + CameraFacing.BACK + "; returning camera #0");
                cameraId = 0;
            }
        }
        AppLogUtils.i("Opening camera #" + cameraId);
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        Camera camera = Camera.open(cameraId);
        if (camera == null) {
            return null;
        }
        return new OpenCamera(cameraId,
                camera,
                CameraFacing.values()[cameraInfo.facing],
                cameraInfo.orientation);
    }

}