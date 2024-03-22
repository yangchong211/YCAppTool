package com.yc.cameraopen;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraLayout extends FrameLayout {

    private TextureView tvRgbCamera;
    private TextureView tvIrCamera;
    private Button btnIrCamera;
    private Button btnSaveImage;
    private Context context;
    private boolean saveImages = false;
    private static final String IMAGE_CACHE_DIR = "yt_palm_image";

    public CameraLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CameraLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.display_camera_ui, this, true);
        initFindViewById(view);
        initListener();
    }

    private void initFindViewById(View view) {
        tvRgbCamera = view.findViewById(R.id.tv_rgb_camera);
        tvIrCamera = view.findViewById(R.id.tv_ir_camera);
        btnIrCamera = view.findViewById(R.id.btn_ir_camera);
        btnSaveImage = view.findViewById(R.id.btn_save_image);
    }

    private void initListener() {
        btnIrCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换显示
                int visibility = tvRgbCamera.getVisibility();
                //才是摄像头
                tvRgbCamera.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        btnSaveImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImages = true;
            }
        });
    }

    public void openCamera(OnCameraListener cameraListener) {
        // TODO: 请检查摄像头模组红外和彩色摄像头对应的ID是否适配
        final int cameraRGBId = 0;
        final int cameraIRId = 1;
        // 摄像头控制
//        TextureListener mCamera2RGB = new TextureListener(context, tvRgbCamera,
//                String.valueOf(cameraRGBId), (data, width, height) -> {
//            if (saveImages) {
//                String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
//                        Locale.getDefault()).format(new Date());
//                String pathRGB = Environment.getExternalStorageDirectory().getPath()
//                        + "/" + IMAGE_CACHE_DIR + "/" + time + "_RGB" + ".bin";
//                CameraOpenUtils.writeBinToFile(pathRGB, data);
//            }
//            //数据前处理，将摄像头出来的数据转换成SDK层面需要的格式
//            if (cameraListener != null) {
//                cameraListener.onCameraCallback(data, width, height);
//            }
//        });
//        tvRgbCamera.setSurfaceTextureListener(mCamera2RGB);
        TextureListener mCamera2IR = new TextureListener(context,
                String.valueOf(cameraIRId), (data, width, height) -> {
            if (saveImages) {
                String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                        Locale.getDefault()).format(new Date());
                String pathIr = Environment.getExternalStorageDirectory().getPath()
                        + "/" + IMAGE_CACHE_DIR + "/" + time + "_IR" + ".bin";
                CameraOpenUtils.writeBinToFile(pathIr, data);
            }
            if (cameraListener != null) {
                cameraListener.onCameraCallback(data, width, height);
            }
        });
        tvIrCamera.setSurfaceTextureListener(mCamera2IR);
    }

}
