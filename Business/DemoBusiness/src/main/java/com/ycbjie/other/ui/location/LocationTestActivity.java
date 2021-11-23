package com.ycbjie.other.ui.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.yc.location.bean.DefaultLocation;
import com.yc.location.listener.LocationListener;
import com.yc.location.manager.DefaultLocationManager;
import com.yc.location.config.LocationUpdateOption;
import com.yc.location.bean.ErrorInfo;
import com.yc.location.log.LogHelper;
import com.yc.location.utils.LocationUtils;
import com.ycbjie.other.R;

import java.io.File;
import java.text.SimpleDateFormat;


public class LocationTestActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    protected DefaultLocationManager locManager = null;
    protected SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

    Handler handler = null;
    Toast toast = null;

    MapView mMapView;
    AMap mMap;
    boolean watchmode = true;
    boolean toastOnce = true;
    boolean zoomOnce = true;

    long timeGetLoc = 0L;
    double loc_lon;
    double loc_lat;
    double loc_acc;
    double loc_speed;

    boolean mLocErr = false;

    private String provider = null;
    private int mErrNo;
    private String mErrMessage;
    private LocationListener locListener0 = new LocationListener() {
        @Override
        public void onLocationChanged(DefaultLocation didiLocation) {

            LogHelper.write("demo, ##InnerTestActivity locListener 0, get location:" + didiLocation.toString());
            //Log.i("lcc", "lcc, on locListener0#onLocationChanged");
            mLocErr = false;
            timeGetLoc = System.currentTimeMillis();

            loc_lon = didiLocation.getLongitude();
            loc_lat = didiLocation.getLatitude();
            loc_acc = didiLocation.getAccuracy();
            loc_speed = didiLocation.getSpeed();
            provider = didiLocation.getProvider();

            LatLng latLng = new LatLng(didiLocation.getLatitude(), didiLocation.getLongitude());
            updateMarker(latLng, didiLocation.getBearing());
            if (watchmode) {
                moveCamera(latLng, zoomOnce);
            } else {
                if (toastOnce) {
                    toast("观察模式 OFF", Toast.LENGTH_SHORT);
                    toastOnce = false;
                }
            }

            zoomOnce = false;
        }

        @Override
        public void onLocationError(int errNo, ErrorInfo errInfo) {
            LogHelper.i("demo", "onLocationError, errNo is:" + errNo);
            timeGetLoc = System.currentTimeMillis();
            mLocErr = true;
            mErrNo = errNo;
            mErrMessage = errInfo.getErrMessage();
        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {
            Log.i("InnerTestActivity", "##status "+name+",status "+status+",desc "+desc);
        }
    };

    private LocationListener locListener1 = new LocationListener() {
        @Override
        public void onLocationChanged(DefaultLocation didiLocation) {
            //Log.i("InnerTestActivity", "##InnerTestActivity locListener 3 ");
            //Log.i("lcc", "lcc, on locListener1#onLocationChanged");
            TextView tv = (TextView)findViewById(R.id.location_info1);
            String content = "";

            
            content += "FixTime: " + simpleDateFormat.format(System.currentTimeMillis()) + "\n";
            content += "Lon: " + String.format("%.6f", didiLocation.getLongitude()) + "\n";
            content += "Lat: " + String.format("%.6f", didiLocation.getLatitude()) + "\n";
            content += "Accuracy(m): " + didiLocation.getAccuracy() + " speed(m/s): " + didiLocation.getSpeed() + "\n";

            DefaultLocation last = locManager.getLastKnownLocation();
            if(last != null) {
                content += "Last Location: " + String.format("%.6f, %.6f", last.getLongitude(), last.getLatitude()) ;
            }

            tv.setText(content);
        }

        @Override
        public void onLocationError(int errNo, ErrorInfo errInfo) {
            TextView tv = (TextView)findViewById(R.id.location_info1);
            String content = "ERROR: " + errNo + ":" + errInfo.getErrMessage();
            tv.setText(content);

        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {

        }
    };

    private LocationListener onceLocListener = new LocationListener() {
        @Override
        public void onLocationChanged(DefaultLocation didiLocation) {
            //Log.i("InnerTestActivity", "##InnerTestActivity locListener 3 ");
            //Log.i("lcc", "lcc, on onceLocListener#onLocationChanged");

            TextView tv = (TextView)findViewById(R.id.once_location_info1);
            String content = "";


//            content += "FixTime: " + simpleDateFormat.format(System.currentTimeMillis()) + "\n";
            content += "Lon: " + String.format("%.6f", didiLocation.getLongitude()) + " ";
            content += "Lat: " + String.format("%.6f", didiLocation.getLatitude());
//            content += "Accuracy(m): " + didiLocation.getAccuracy() + " speed(m/s): " + didiLocation.getSpeed() + "\n";

            tv.setText(content);
        }

        @Override
        public void onLocationError(int errNo, ErrorInfo errInfo) {
            TextView tv = (TextView)findViewById(R.id.once_location_info1);
            String content = "ERROR: " + errInfo.getErrNo() + ":" + errInfo.getErrMessage();
            tv.setText(content);

        }

        @Override
        public void onStatusUpdate(String name, int status, String desc) {

        }
    };

    private LocationUpdateOption mUpdateOption;
    private BitmapDescriptor mMarkerBitmap;
    private Marker mMarker;
    private MarkerOptions mMarkerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_test);
        initView();

        handler = new Handler();
        handler.post(this);

        checkPermission();

        mMapView = (MapView) findViewById(R.id.map1);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();

        registerMapDrag();

        zoomOnce = true;
    }

    private void initView() {
        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.stop_btn).setOnClickListener(this);
        findViewById(R.id.start_btn1).setOnClickListener(this);
        findViewById(R.id.stop_btn1).setOnClickListener(this);
        findViewById(R.id.once).setOnClickListener(this);
        findViewById(R.id.get_status).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMapView.onResume();

        locManager = DefaultLocationManager.getInstance(this.getApplicationContext());

        String SDKV = locManager.getVersion();
        ((TextView)findViewById(R.id.buildText)).setText(SDKV);


        //locManager.useTencentSDK(true);
        //locManager.setPhonenum("15901080503");
        //locManager.startLocService(locListener, 1000);
    }

    @Override
    protected void onPause() {

        mMapView.onPause();

        if (toast != null) {
            toast.cancel();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(handler != null) handler.removeCallbacks(this);
        handler = null;
        mMapView.onDestroy();
        zoomOnce = true;
        locManager.removeLocationUpdates(locListener0);
        locManager.removeLocationUpdates(locListener1);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.once) {

            checkPermission();
            if (locManager != null) {
                locManager.requestLocationUpdateOnce(onceLocListener, "didi_loc_test");
            }
        } else if (id == R.id.start_btn || id == R.id.start_btn1) {//Log.i("locsdkdemo","-InnerTestActivity- press start btn");

            checkPermission();

            if (locManager != null) {
                locManager = DefaultLocationManager.getInstance(this.getApplicationContext());
                int b = LocationUtils.isGooglePlayServicesAvailable(this);

                //locManager.startLocService(locListener0, 1000);
                locManager.enableMockLocation(true);
                String FILE_PATH_P1 = "/Android/data/";
                String FILE_PATH_P2 = "/locsdk/log/";
                String packageName = this.getPackageName();
                String filePath = Environment.getExternalStorageDirectory().toString() + FILE_PATH_P1 + packageName + FILE_PATH_P2;
                File path = new File(filePath);
                locManager.setLogPath(path);
//                    locManager.setCoordinateType(DIDILocation.COORDINATE_TYPE_WGS84);
                mUpdateOption = locManager.getDefaultLocationUpdateOption();
                mUpdateOption.setModuleKey("didi_loc_test");
                EditText editText = null;
                if (v.getId() == R.id.start_btn) {

                    editText = (EditText) findViewById(R.id.edit_freq);
                } else {
                    editText = (EditText) findViewById(R.id.edit_freq1);

                }
                Editable text = editText.getText();
                if (!TextUtils.isEmpty(text)) {
                    try {
                        int fres = Integer.parseInt(text.toString());
                        switch (fres) {
                            case 1:
                                mUpdateOption.setInterval(LocationUpdateOption.IntervalMode.HIGH_FREQUENCY);
                                break;
                            case 3:
                                mUpdateOption.setInterval(LocationUpdateOption.IntervalMode.NORMAL);
                                break;
                            case 9:
                                mUpdateOption.setInterval(LocationUpdateOption.IntervalMode.LOW_FREQUENCY);
                                break;
                            case 36:
                                mUpdateOption.setInterval(LocationUpdateOption.IntervalMode.BATTERY_SAVE);
                                break;
                            default:
                                mUpdateOption.setInterval(LocationUpdateOption.IntervalMode.NORMAL);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mUpdateOption.setInterval(LocationUpdateOption.IntervalMode.NORMAL);
                    }
                }
                if (v.getId() == R.id.start_btn) {

                    locManager.requestLocationUpdates(locListener0, mUpdateOption);

                } else {
                    locManager.requestLocationUpdates(locListener1, mUpdateOption);

                }

                Log.i(LocationTestActivity.class.getSimpleName(), "SDK VER " + locManager.getVersion());


            }
        } else if (id == R.id.stop_btn || id == R.id.stop_btn1) {//Log.i("locsdkdemo","-InnerTestActivity- press stop btn");
            int viewId = v.getId();
            if (viewId == R.id.stop_btn) {

            } else {
                ((TextView) findViewById(R.id.location_info1)).setText("bamaiInfo");
            }
            final LocationListener listener = (v.getId() == R.id.stop_btn ? locListener0 : locListener1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //if(locManager != null) locManager.stopLocService();
                    if (locManager != null)
                        locManager.removeLocationUpdates(listener);
                }
            }).start();
            mMap.clear();
            mMarker = null;

            timeGetLoc = 0L;
        } else if (id == R.id.get_status) {
            int cellState = locManager.getCellStatus();
            int wifiState = locManager.getWifiStatus();
            int gpsState = locManager.getGpsStatus();
            String content = "cell: " + cellState + " wifi:" + wifiState + " gpsState:" + gpsState;
            ((TextView) findViewById(R.id.status)).setText(content);
            //            case R.id.setfreq_btn:
//                if (locManager != null) {
//                    int gpsstate = locManager.getGpsStatus();
//                    int wifistate = locManager.getWifiStatus();
//                    int cellState = locManager.getCellStatus();
//
//                    EditText editText = (EditText)findViewById(R.id.edit_freq);
//                    Editable text = editText.getText();
//                    if (!TextUtils.isEmpty(text)) {
//                        try {
//                            int fres = Integer.parseInt(text.toString());
////                            mUpdateOptionn.
////                            locManager.setInterval(fres * 1000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
        }

    }

    @Override
    public void run() { // loop who update ui

        TextView tv = (TextView)findViewById(R.id.hello_tv);
        String content = "";

        content += "Now: " + simpleDateFormat.format(System.currentTimeMillis()) + "\n";
        if(timeGetLoc <= 0L) {
            content += "未定位";
        } else if (!mLocErr) {
            content += "FixTime: " + simpleDateFormat.format(timeGetLoc) + "\n";
            content += "Lon: " + String.format("%.6f", loc_lon) + "\n";
            content += "Lat: " + String.format("%.6f", loc_lat) + "\n";
            content += "Accuracy(m): " + loc_acc + " speed(m/s): " + loc_speed + "\n";
            content += "Provider: " + provider + "\n";
        } else {
            content += "ERROR: " + mErrNo + ":" + mErrMessage + "\n";
        }

        DefaultLocation didiLocation = locManager.getLastKnownLocation();
        if(didiLocation != null) {
            content += "Last Location: " + String.format("%.6f, %.6f", didiLocation.getLongitude(), didiLocation.getLatitude()) ;
        }


        tv.setText(content);

        if(handler != null) handler.postDelayed(this, 500);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            }, 0);//自定义的code
        }
    }

    private void toast(String text, int length) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), text, length);
        } else {
            toast.setText(text);
            toast.setDuration(length);
        }

        toast.show();
    }

    private void updateMarker(LatLng location, float dir) {
        if (null == mMarker) {
            mMarkerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location_image);
            mMarkerOptions = new MarkerOptions()
                    .icon(mMarkerBitmap)
                    .position(location);
            mMarker = mMap.addMarker(mMarkerOptions);
        } else {
            mMarker.setPosition(location);
        }

        mMarker.setRotateAngle(360 - (float)dir);
    }

    private void moveCamera(LatLng location, boolean needZoom) {
        CameraPosition position = mMap.getCameraPosition();
        if (position != null) {

            float zoom = position.zoom;

            if (needZoom) {
                zoom = mMap.getMaxZoomLevel() - 1;
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));
        }
    }

    private void registerMapDrag() {
        mMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    watchmode = false;
                }
            }
        });
    }
}
