package com.yc.cameraopen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraManager.AvailabilityCallback;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TextureListener implements TextureView.SurfaceTextureListener {

    private static final int IMAGE_FORMAT = ImageFormat.YUV_420_888;
    public static final Size IMAGE_SIZE = new Size(600, 800);
    private final Context mContext;
    private final CameraManager mCameraManager;
    private final String mCameraId;
    private CameraCaptureSession cameraCaptureSession;
    private ImageReader mPreviewImageReader;
    private final OnCameraListener mListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextureListener(Context context, String cameraId, OnCameraListener listener) {
        this.mContext = context;
        this.mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        this.mCameraId = cameraId;
        this.mListener = listener;
        try {
            this.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() throws Exception {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            throw new Exception("请检查摄像头权限是否开启");
        }
        String[] cameraIdList = this.mCameraManager.getCameraIdList();
        CameraOpenUtils.d("TextureListener init cameraIdList : " + Arrays.toString(cameraIdList));
        boolean found = Arrays.asList(this.mCameraManager.getCameraIdList()).contains(this.mCameraId);
        if (!found) {
            throw new Exception("读取系统 cameraId = " + this.mCameraId + " 失败，请检查硬件连接情况");
        }
        CameraOpenUtils.d("TextureListener init success");
    }

    /**
     * SurfaceTexture准备就绪
     * 已经在 init 的时候进行了权限检查
     *
     * @param surfaceTexture surface
     * @param width          WIDTH
     * @param height         HEIGHT
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        CameraOpenUtils.d("TextureListener onSurfaceTextureAvailable");
        surfaceTexture.setDefaultBufferSize(IMAGE_SIZE.getWidth(), IMAGE_SIZE.getHeight());
        onImageReader();
        openCamera(surfaceTexture);
    }

    /**
     * SurfaceTexture缓冲大小变化
     *
     * @param surface surface
     * @param width   WIDTH
     * @param height  HEIGHT
     */
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    /**
     * SurfaceTexture即将被销毁
     *
     * @param surface surface
     */
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        CameraOpenUtils.d("TextureListener onSurfaceTextureDestroyed");
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
        return true;
    }

    /**
     * SurfaceTexture通过updateImage更新
     *
     * @param surface surface
     */
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void onImageReader() {
        //简单来说就是图像读取器。ImageReader可以直接获取屏幕渲染数据，得到屏幕数据是image格式。
        mPreviewImageReader = ImageReader.newInstance(IMAGE_SIZE.getWidth(),
                IMAGE_SIZE.getHeight(), ImageFormat.YUV_420_888, 2);
        ImageHandler.getInstance().startThread();
        //用于通知新图像可用的回调接口
        //注册一个侦听器，以便在 ImageReader 中提供新图像时调用该侦听器。
        mPreviewImageReader.setOnImageAvailableListener(reader -> {
            Image image = reader.acquireLatestImage();
            if (image == null) {
                //如果 ImageReader 没有以等于生产速率的速率获取和释放图像，则图像源最终将在尝试渲染到 Surface 时停止或丢弃图像。
                CameraOpenUtils.d("TextureListener onImageReader image null");
                return;
            }
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();

            byte[] data = CameraOpenUtils.nv21ImageToByte(image);

            // 拿到byte[]数据，触发回调
            mListener.onCameraCallback(data, imageWidth, imageHeight);

            // 释放图像资源
            image.close();
        }, ImageHandler.getInstance().getHandler());
    }

    /**
     * 打开相机操作
     *
     * @param surfaceTexture surfaceTexture
     */
    @SuppressLint("MissingPermission")
    private void openCamera(SurfaceTexture surfaceTexture) {
        CameraOpenUtils.d("TextureListener openCamera");
        try {
            //调用CameraManager的openCamera函数
            // 第一个参数是cameraId
            // 第二个参数是一个Callback对象，用于给应用返回open camera的结果
            // 第三个参数是一个Handler对象，上面的callback回调函数将会执行在这个Handler对象所在的线程中
            this.mCameraManager.openCamera(mCameraId, new CameraDevice.StateCallback() {
                /**
                 * 当相机成功打开时回调该方法，接下来可以执行创建预览的操作
                 * @param camera the camera device that has become opened
                 */
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    CameraOpenUtils.d("TextureListener openCamera onOpened " + mCameraId);
                    onOpenedCamera(camera, surfaceTexture);
                }

                /**
                 * 当相机断开连接时回调该方法，应该在此执行释放相机的操作
                 * @param camera the device that has been disconnected
                 */
                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    CameraOpenUtils.d("TextureListener openCamera onDisconnected");
                    ImageHandler.getInstance().stopThread();
                    //关闭图像读取器
                    if (mPreviewImageReader != null) {
                        mPreviewImageReader.close();
                        mPreviewImageReader = null;
                    }
                }

                /**
                 * 当相机打开失败时，应该在此执行释放相机的操作
                 * CameraDevice.StateCallback.ERROR_CAMERA_IN_USE	当前相机设备已经在一个更高优先级的地方打开了
                 * CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE	已打开相机数量到上限了，无法再打开新的相机了
                 * CameraDevice.StateCallback.ERROR_CAMERA_DISABLED	由于相关设备策略该相机设备无法打开，详细可见 DevicePolicyManager 的 setCameraDisabled(ComponentName, boolean) 方法
                 * CameraDevice.StateCallback.ERROR_CAMERA_DEVICE	相机设备发生了一个致命错误
                 * CameraDevice.StateCallback.ERROR_CAMERA_SERVICE	相机服务发生了一个致命错误
                 * @param camera The device reporting the error
                 * @param error The error code.
                 *
                 */
                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    CameraOpenUtils.d("TextureListener openCamera onError" + error);
                    ImageHandler.getInstance().stopThread();
                    if (mPreviewImageReader != null) {
                        mPreviewImageReader.close();
                        mPreviewImageReader = null;
                    }
                }

                /**
                 * 当相机关闭时回调该方法，这个方法可以不用实现
                 * @param camera the camera device that has become closed
                 */
                @Override
                public void onClosed(@NonNull CameraDevice camera) {
                    super.onClosed(camera);
                    CameraOpenUtils.d("TextureListener openCamera onClosed");
                }
            }, null);

            //相机设备变为可用或不可打开的回调。
            this.mCameraManager.registerAvailabilityCallback(new AvailabilityCallback() {
                @Override
                public void onCameraAvailable(@NonNull String cameraId) {
                    super.onCameraAvailable(cameraId);
                    CameraOpenUtils.d("TextureListener onCameraAvailable " + cameraId);
                }

                @Override
                public void onCameraUnavailable(@NonNull String cameraId) {
                    super.onCameraUnavailable(cameraId);
                    CameraOpenUtils.d("TextureListener onCameraUnavailable " + cameraId);
                }
            }, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.mCameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
                    /**
                     * 相机的手电筒模式无法通过 setTorchMode 设置。
                     * 如果之前通过调用 setTorchMode 打开了火炬模式，则在调用 onTorchModeUnavailable 之前将它关闭。
                     * setTorchMode 将失败，直到火炬模式再次进入禁用或启用状态。此方法的默认实现不执行任何操作。
                     * @param cameraId 手电筒模式不可用的相机的唯一标识符。
                     */
                    @Override
                    public void onTorchModeUnavailable(@NonNull String cameraId) {
                        super.onTorchModeUnavailable(cameraId);
                    }

                    /**
                     * 相机的手电筒模式已启用或禁用，并且可以通过 setTorchMode 进行更改。
                     * @param cameraId 手电筒模式已更改的相机的唯一标识符。
                     * @param enabled 相机手电筒模式已更改为的状态。
                     *                当手电筒模式已打开并且可以关闭时为 true。 当手电筒模式已关闭且可以打开时为 false。
                     */
                    @Override
                    public void onTorchModeChanged(@NonNull String cameraId, boolean enabled) {
                        super.onTorchModeChanged(cameraId, enabled);
                    }
                }, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            CameraOpenUtils.d("TextureListener openCamera CameraAccessException " + e.getMessage());
        }
    }


    private void onOpenedCamera(CameraDevice cameraDevice, SurfaceTexture surfaceTexture) {
        try {
            //获取一个Surface可用于Images为此 生产的ImageReader
            Surface imageReaderSurface = mPreviewImageReader.getSurface();
            //使用指定模板创建一个 CaptureRequest.Builder 用于新的捕获请求构建。
            //TEMPLATE_PREVIEW	用于创建一个相机预览请求。相机会优先保证高帧率而不是高画质	所有相机设备
            //TEMPLATE_STILL_CAPTURE	用于创建一个拍照请求。相机会优先保证高画质而不是高帧率	所有相机设备
            //TEMPLATE_RECORD	用于创建一个录像请求。相机会使用标准帧率，并设置录像级别的画质	所有相机设备
            //TEMPLATE_VIDEO_SNAPSHOT	用于创建一个录像时拍照的请求。相机会尽可能的保证照片质量的同时不破坏正在录制的视频质量	硬件支持级别高于 LEGACY 的相机设备
            //TEMPLATE_ZERO_SHUTTER_LAG	用于创建一个零延迟拍照的请求。相机会尽可能的保证照片质量的同时不损失预览图像的帧率，3A（自动曝光、自动聚焦、自动白平衡）都为 auto 模式	支持 PRIVATE_REPROCESSING 和 YUV_REPROCESSING 的相机设备
            //TEMPLATE_MANUAL	用于创建一个手动控制相机参数的请求。相机所有自动控制将被禁用，后期处理参数为预览质量，手动控制参数被设置为合适的默认值，需要用户自己根据需求来调整各参数	支持 MANUAL_SENSOR 的相机设备
            CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(new Surface(surfaceTexture));
            //设置预览输出的 Surface
            captureRequestBuilder.addTarget(imageReaderSurface);
            CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    onConfiguredCamera(captureRequestBuilder, session);
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            };
            List<Surface> surfaces = Arrays.asList(new Surface(surfaceTexture), imageReaderSurface);
            //使用一个指定的 Surface 输出列表创建一个相机捕捉会话。
            //outputs ： 输出的 Surface 集合，每个 CaptureRequest 的输出 Surface 都应该是 outputs 的一个子元素。
            //callback ： 创建会话的回调。成功时将调用 CameraCaptureSession.StateCallback 的 onConfigured(CameraCaptureSession session) 方法。
            //handler ： 指定回调执行的线程，传 null 时默认使用当前线程的 Looper。
            cameraDevice.createCaptureSession(surfaces, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void onConfiguredCamera(CaptureRequest.Builder captureRequestBuilder, CameraCaptureSession session) {
        CameraOpenUtils.d("TextureListener onOpenedCamera onConfigured " + session);
        cameraCaptureSession = session;
        try {
            //设置连续自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            //captureRequestBuilder.set(CaptureRequest.SENSOR_FRAME_DURATION, TimeUnit.MICROSECONDS.toNanos(33));
            Handler handler = ImageHandler.getInstance().getHandler();
            CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    //这个会打印很频繁
                    //CameraOpenUtils.d("TextureListener onOpenedCamera onCaptureCompleted " +session);
                }
            };
            // 生成一个预览的请求
            CaptureRequest captureRequest = captureRequestBuilder.build();
            // 开始预览，即设置反复请求
            cameraCaptureSession.setRepeatingRequest(captureRequest, captureCallback, handler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}