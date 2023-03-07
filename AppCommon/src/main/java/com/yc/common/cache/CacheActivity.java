package com.yc.common.cache;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;

import com.yc.apploglib.AppLogHelper;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.store.BaseDataCache;
import com.yc.store.StoreToolHelper;
import com.yc.toastutils.ToastUtils;

public class CacheActivity extends BaseActivity {

    private RoundTextView tvView1;
    private RoundTextView tvView2;
    private RoundTextView tvView3;
    private RoundTextView tvView4;
    private RoundTextView tvView5;
    private RoundTextView tvView6;
    private RoundTextView tvView7;
    private RoundTextView tvView8;
    private RoundTextView tvView9;
    private RoundTextView tvView10;
    private RoundTextView tvView11;
    private RoundTextView tvView12;
    private ImageView ivImageView;
    private ImageView ivImageView2;

    private BaseDataCache dataCache;
    private BaseDataCache mmkvCache;
    private BaseDataCache memoryCache;
    private BaseDataCache lruMemoryCache;
    private BaseDataCache lruDiskCache;
    private BaseDataCache storeCache;
    private BaseDataCache fastSpCache;

    @Override
    public int getContentView() {
        return R.layout.activity_base_text_view;
    }

    @Override
    public void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);
        tvView6 = findViewById(R.id.tv_view_6);
        tvView7 = findViewById(R.id.tv_view_7);
        tvView8 = findViewById(R.id.tv_view_8);
        tvView9 = findViewById(R.id.tv_view_9);
        tvView10 = findViewById(R.id.tv_view_10);
        tvView11 = findViewById(R.id.tv_view_11);
        tvView12 = findViewById(R.id.tv_view_12);
        ivImageView = findViewById(R.id.iv_image_view);
        ivImageView2 = findViewById(R.id.iv_image_view2);
    }

    @Override
    public void initListener() {
        tvView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("sp存数据");
                sp1();
            }
        });
        tvView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("sp取数据");
                sp2();
            }
        });
        tvView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("mmkv存数据");
                mmvk1();
            }
        });
        tvView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("mmkv取数据");
                mmvk2();
            }
        });
        tvView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmvk3();
            }
        });
        tvView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("disk存数据");
                disk1();
            }
        });
        tvView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("disk取数据");
                disk2();
            }
        });
        tvView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("store存数据");
                store1();
            }
        });
        tvView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("store取数据");
                store2();
            }
        });
        tvView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("fastSp存数据");
                fastSp1();
            }
        });
        tvView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("fastSp取数据");
                fastSp2();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {
        initCache();
        tvView1.setText("1.1 使用sp存储数据");
        tvView2.setText("1.2 使用sp获取数据");
        tvView3.setText("2.1 使用mmkv存储数据");
        tvView4.setText("2.2 使用mmkv获取数据");
        tvView5.setText("2.3 清除mmkv所有数据");
        tvView6.setText("3.1 使用lruDisk存储数据");
        tvView7.setText("3.2 使用lruDisk获取数据");
        tvView8.setText("4.1 使用store存储数据");
        tvView9.setText("4.2 使用store获取数据");
        tvView10.setText("5.1 使用fastSp存储数据");
        tvView11.setText("5.2 使用fastSp获取数据");
    }

    private void initCache() {
        dataCache = StoreToolHelper.getInstance().getSpCache();
        mmkvCache = StoreToolHelper.getInstance().getMmkvDiskCache();
        memoryCache = StoreToolHelper.getInstance().getMemoryCache();
        lruMemoryCache = StoreToolHelper.getInstance().getLruMemoryCache();
        lruDiskCache = StoreToolHelper.getInstance().getLruDiskCache();
        storeCache = StoreToolHelper.getInstance().getStoreCache();
        fastSpCache = StoreToolHelper.getInstance().getFastSpCache();
    }

    private void sp1() {
        dataCache.saveBoolean("cacheKey1",true);
        dataCache.saveFloat("cacheKey2",2.0f);
        dataCache.saveInt("cacheKey3",3);
        dataCache.saveLong("cacheKey4",4);
        dataCache.saveString("cacheKey5","doubi5");
        dataCache.saveDouble("cacheKey6",5.20);
    }

    private void sp2() {
        boolean data1 = dataCache.readBoolean("cacheKey1", false);
        float data2 = dataCache.readFloat("cacheKey2", 0);
        int data3 = dataCache.readInt("cacheKey3", 0);
        long data4 = dataCache.readLong("cacheKey4", 0);
        String data5 = dataCache.readString("cacheKey5", "");
        double data6 = dataCache.readDouble("cacheKey6", 0.0);
        AppLogHelper.d("取数据 cacheKey1: ",data1);
        AppLogHelper.d("取数据 cacheKey2: ",data2);
        AppLogHelper.d("取数据 cacheKey3: ",data3);
        AppLogHelper.d("取数据 cacheKey4: ",data4);
        AppLogHelper.d("取数据 cacheKey5: ",data5);
        AppLogHelper.d("取数据 cacheKey6: ",data6);
    }

    private void mmvk1() {
        mmkvCache.saveBoolean("spkey1",true);
        mmkvCache.saveFloat("spkey2",2.0f);
        mmkvCache.saveInt("spkey3",3);
        mmkvCache.saveLong("spkey4",4);
        mmkvCache.saveString("spkey5","doubi5");
        mmkvCache.saveDouble("spkey6",5.20);
    }

    private void mmvk2() {
        boolean spkey1 = mmkvCache.readBoolean("spkey1", false);
        float spkey2 = mmkvCache.readFloat("spkey2", 0);
        int spkey3 = mmkvCache.readInt("spkey3", 0);
        long spkey4 = mmkvCache.readLong("spkey4", 0);
        String spkey5 = mmkvCache.readString("spkey5", "");
        double spkey6 = mmkvCache.readDouble("spkey6", 0.0);
        AppLogHelper.d("mmkvCache取数据 spkey1: ",spkey1);
        AppLogHelper.d("mmkvCache取数据 spkey2: ",spkey2);
        AppLogHelper.d("mmkvCache取数据 spkey3: ",spkey3);
        AppLogHelper.d("mmkvCache取数据 spkey4: ",spkey4);
        AppLogHelper.d("mmkvCache取数据 spkey5: ",spkey5);
        AppLogHelper.d("mmkvCache取数据 spkey6: ",spkey6);
    }

    private void mmvk3() {
        mmkvCache.clearData();
    }

    private void disk1() {
        lruDiskCache.saveBoolean("spkey1",true);
        lruDiskCache.saveFloat("spkey2",2.0f);
        lruDiskCache.saveInt("spkey3",3);
        lruDiskCache.saveLong("spkey4",4);
        lruDiskCache.saveString("spkey5","doubi5");
        lruDiskCache.saveDouble("spkey6",5.20);
    }

    private void disk2() {
        boolean spkey1 = lruDiskCache.readBoolean("spkey1", false);
        float spkey2 = lruDiskCache.readFloat("spkey2", 0);
        int spkey3 = lruDiskCache.readInt("spkey3", 0);
        long spkey4 = lruDiskCache.readLong("spkey4", 0);
        String spkey5 = lruDiskCache.readString("spkey5", "");
        double spkey6 = lruDiskCache.readDouble("spkey6", 0.0);
        AppLogHelper.d("disk取数据 spkey1: ",spkey1);
        AppLogHelper.d("disk取数据 spkey2: ",spkey2);
        AppLogHelper.d("disk取数据 spkey3: ",spkey3);
        AppLogHelper.d("disk取数据 spkey4: ",spkey4);
        AppLogHelper.d("disk取数据 spkey5: ",spkey5);
        AppLogHelper.d("disk取数据 spkey6: ",spkey6);
    }

    private void disk3() {
        lruDiskCache.clearData();
    }


    private void store1() {
        storeCache.saveBoolean("spkey1",true);
        storeCache.saveFloat("spkey2",2.0f);
        storeCache.saveInt("spkey3",3);
        storeCache.saveLong("spkey4",4);
        storeCache.saveString("spkey5","doubi5");
        storeCache.saveDouble("spkey6",5.20);
    }

    private void store2() {
        boolean spkey1 = storeCache.readBoolean("spkey1", false);
        float spkey2 = storeCache.readFloat("spkey2", 0);
        int spkey3 = storeCache.readInt("spkey3", 0);
        long spkey4 = storeCache.readLong("spkey4", 0);
        String spkey5 = storeCache.readString("spkey5", "");
        double spkey6 = storeCache.readDouble("spkey6", 0.0);
        AppLogHelper.d("disk取数据 spkey1: ",spkey1);
        AppLogHelper.d("disk取数据 spkey2: ",spkey2);
        AppLogHelper.d("disk取数据 spkey3: ",spkey3);
        AppLogHelper.d("disk取数据 spkey4: ",spkey4);
        AppLogHelper.d("disk取数据 spkey5: ",spkey5);
        AppLogHelper.d("disk取数据 spkey6: ",spkey6);
    }


    private void fastSp1() {
        fastSpCache.saveBoolean("spkey1",true);
        fastSpCache.saveFloat("spkey2",2.0f);
        fastSpCache.saveInt("spkey3",3);
        fastSpCache.saveLong("spkey4",4);
        fastSpCache.saveString("spkey5","doubi5");
        fastSpCache.saveDouble("spkey6",5.20);
    }

    private void fastSp2() {
        boolean spkey1 = fastSpCache.readBoolean("spkey1", false);
        float spkey2 = fastSpCache.readFloat("spkey2", 0);
        int spkey3 = fastSpCache.readInt("spkey3", 0);
        long spkey4 = fastSpCache.readLong("spkey4", 0);
        String spkey5 = fastSpCache.readString("spkey5", "");
        double spkey6 = fastSpCache.readDouble("spkey6", 0.0);
        AppLogHelper.d("fastSp取数据 spkey1: ",spkey1);
        AppLogHelper.d("fastSp取数据 spkey2: ",spkey2);
        AppLogHelper.d("fastSp取数据 spkey3: ",spkey3);
        AppLogHelper.d("fastSp取数据 spkey4: ",spkey4);
        AppLogHelper.d("fastSp取数据 spkey5: ",spkey5);
        AppLogHelper.d("fastSp取数据 spkey6: ",spkey6);
    }

}
