package com.ycbjie.note.ui.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.ns.yc.yccustomtextlib.hyper.HyperTextEditor;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.model.cache.CacheNotePad;
import com.ycbjie.library.inter.listener.OnDatePickListener;
import com.ycbjie.library.inter.listener.OnTimePickListener;
import com.ycbjie.library.loader.GlideImageLoader;
import com.ycbjie.library.utils.AppUtils;
import com.ycbjie.library.utils.image.BitmapUtils;
import com.ycbjie.library.utils.time.TimerUtils;
import com.ycbjie.note.R;
import com.ycbjie.note.model.bean.NotePadDetail;
import com.ycbjie.note.receiver.ReminderReceiver;
import com.ycbjie.note.ui.fragment.DatePickerFragment;
import com.ycbjie.note.ui.fragment.TimePickerFragment;
import com.ycbjie.note.utils.SDCardUtils;
import com.ycbjie.note.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：简易记事本
 * 修订历史：
 * ================================================
 */
public class NotePadNewActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    Toolbar toolbar;
    TextView tvNoteDetailDate;
    TextView tvNoteType;
    EditText etNewTitle;
    HyperTextEditor etNewContent;
    CheckBox cbAlarm;
    TextView tvNoteTime;
    TextView tvNoteDate;

    private int maxImgCount = 5;                        //允许选择图片最大数
    private int flag;
    private Realm realm;
    private RealmResults<CacheNotePad> cacheNotePads;
    private NotePadDetail notePadDetail;
    private int id;
    private boolean isPicked = false;
    private AlarmManager alarmMgr;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(realm!=null){
            realm.close();
        }*/
    }


    @Override
    protected void onStop() {
        super.onStop();
        //如果APP处于后台，或者手机锁屏，则保存数据
        if (AppUtils.isAppOnBackground(getApplicationContext())
                || AppUtils.isLockScreen(getApplicationContext())) {
            saveNoteData(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backAndExit();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_not_pad_new;
    }


    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);


        tvNoteDetailDate = (TextView) findViewById(R.id.tv_note_detail_date);
        tvNoteType = (TextView) findViewById(R.id.tv_note_type);
        tvNoteDate = (TextView) findViewById(R.id.tv_note_date);
        tvNoteTime = (TextView) findViewById(R.id.tv_note_time);
        cbAlarm = (CheckBox) findViewById(R.id.cb_alarm);
        etNewTitle = (EditText) findViewById(R.id.et_new_title);
        etNewContent = (HyperTextEditor) findViewById(R.id.et_new_content);


        initIntentData();
        initToolBar();
        initImagePicker();
        initAlarmManager();
    }



    private void initToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            flag = intent.getIntExtra("flag", 0);
            id = intent.getIntExtra("id", 1);
            if (flag == 0) {
                //新建
                toolbarTitle.setText("新建笔记");
                tvNoteDetailDate.setText("当下");
                tvNoteType.setText("默认笔记");
            } else {
                //编辑
                toolbarTitle.setText("编辑笔记");
                Bundle bundle = intent.getBundleExtra("data");
                notePadDetail = (NotePadDetail) bundle.getSerializable("notePad");
                showData(notePadDetail);
            }
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvNoteType.setOnClickListener(this);
        tvNoteDate.setOnClickListener(this);
        tvNoteTime.setOnClickListener(this);
        cbAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //first time, so pick date and time.
                    if (!isPicked) {
                        pickDate();
                    }
                    //has date and time ,just show it.
                    showDateTimeViews();
                } else {
                    hideDateTimeViews();
                }
            }
        });
    }


    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_pad_menu_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_insert_image) {
            //此时让富文本控件获取到焦点
            etNewContent.setFocusable(true);
            etNewContent.setFocusableInTouchMode(true);
            etNewContent.requestFocus();
            insertPhoto();
        } else if (i == R.id.action_new_save) {
            saveNoteData(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            backAndExit();
        } else if (i == R.id.tv_note_type) {
            ToastUtils.showRoundRectToast("笔记类型后期添加");
        } else if (i == R.id.tv_note_date) {
            pickDate();
        } else if (i == R.id.tv_note_time) {
            pickTime();
        }
    }


    private void showDateTimeViews() {
        tvNoteDate.setVisibility(View.VISIBLE);
        tvNoteTime.setVisibility(View.VISIBLE);
    }

    private void hideDateTimeViews() {
        tvNoteDate.setVisibility(View.INVISIBLE);
        tvNoteTime.setVisibility(View.INVISIBLE);
        // If the alarm has been set, cancel it.
        cancelReminder();
    }

    private PendingIntent alarmIntent;
    private long mItemID = -1L;
    private void cancelReminder() {
        if (alarmIntent == null && mItemID != -1L) {
            //not add note
            Intent mIntent = new Intent(NotePadNewActivity.this,
                    ReminderReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(NotePadNewActivity.this,
                    (int) mItemID, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        if (alarmMgr!=null){
            alarmMgr.cancel(alarmIntent);
        }
    }


    /**
     * 初始化图片选择器
     */
    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == 100) {
                //异步方式插入图片
                insertImagesSync(data);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == 101) {

            }
        }
    }

    /**
     * 展示数据
     */
    private void showData(final NotePadDetail notePadDetail) {
        if (notePadDetail.getCreateTime() != null) {
            tvNoteDetailDate.setText(notePadDetail.getCreateTime());
        }
        tvNoteType.setText(String.valueOf(notePadDetail.getType()));
        etNewTitle.setText(notePadDetail.getTitle());
        Editable eText = etNewTitle.getText();
        Selection.setSelection(eText, eText.length());
        etNewContent.post(new Runnable() {
            @Override
            public void run() {
                //showEditData(myContent);
                etNewContent.clearAllLayout();
                showDataSync(notePadDetail.getContent());
            }
        });
    }

    /**
     * 异步方式显示数据
     */
    private void showDataSync(final String html) {
        Subscription subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showRoundRectToast("解析错误：图片不存在或已损坏");
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains(SDCardUtils.getPictureDir())) {
                            etNewContent.addImageViewAtIndex(etNewContent.getLastIndex(), text);
                        } else {
                            etNewContent.addEditTextAtIndex(etNewContent.getLastIndex(), text);
                        }
                    }
                });
    }


    /**
     * 显示数据
     */
    protected void showEditData(Subscriber<? super String> subscriber, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img")) {
                    String imagePath = StringUtils.getImgSrc(text);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        ToastUtils.showRoundRectToast( "图片" + i + "已丢失，请重新插入！");
                    }
                } else {
                    subscriber.onNext(text);
                }
            }
            subscriber.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            subscriber.onError(e);
        }
    }


    /**
     * 插入图片，打开图片选择器
     */
    private void insertPhoto() {
        ImagePicker.getInstance().setSelectLimit(maxImgCount);
        Intent intent = new Intent(NotePadNewActivity.this, ImageGridActivity.class);
        startActivityForResult(intent, 100);
    }

    /**
     * 保存笔记数据
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     */
    private void saveNoteData(boolean isBackground) {
        String title = etNewTitle.getText().toString().trim();
        String type = tvNoteType.getText().toString();
        String time = tvNoteDetailDate.getText().toString();
        String content = getHyperContent();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.showRoundRectToast("标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showRoundRectToast("内容不能为空");
            return;
        }
        switch (flag) {
            case 0:                 //新建笔记保存
                insertNewDataRealm(isBackground, title, type, time, content);
                break;
            case 1:                 //修改笔记保存
                editOldDataRealm(isBackground, title, type, time, content);
                break;
        }
    }

    /**
     * 获取超文本内容
     */
    private String getHyperContent() {
        List<HyperTextEditor.EditData> editList = etNewContent.buildEditData();
        StringBuffer content = new StringBuffer();
        for (HyperTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
                //Log.d("HyperTextEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
                //Log.d("HyperTextEditor", "commit imgePath=" + itemData.imagePath);
            }
        }
        return content.toString();
    }


    /**
     * 异步插入图片
     * 用这个一直报错：Center crop requires calling resize with positive width and height
     */
    private ArrayList<String> photos = new ArrayList<>();

    private void insertImagesSync(final Intent data) {
         Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                etNewContent.measure(0, 0);
                int width = ScreenUtils.getScreenWidth();
                int height = ScreenUtils.getScreenHeight();
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                photos.clear();
                if (images != null) {
                    for (int a = 0; a < images.size(); a++) {
                        String path = images.get(a).path;
                        photos.add(path);
                    }
                }
                //ArrayList<String> photos = data.getStringArrayListExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                //下面这个是不行的
                //ArrayList<String> photos = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                //ArrayList<String> photos = data.getStringArrayListExtra(ImagePicker.EXTRA_FROM_ITEMS);

                //可以同时插入多张图片
                for (String imagePath : photos) {
                    Log.e("NotePadNewActivity", "###path=" + imagePath);
                    Bitmap bitmap = BitmapUtils.getSmallBitmap(imagePath, width, height);     //压缩图片
                    //bitmap = BitmapFactory.decodeFile(imagePath);
                    imagePath = SDCardUtils.saveToSdCard(bitmap);
                    Log.e("NotePadNewActivity", "###imagePath=" + imagePath);
                    subscriber.onNext(imagePath);
                }
                subscriber.onCompleted();
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())                       //生产事件在io
                .observeOn(AndroidSchedulers.mainThread())          //消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        etNewContent.addEditTextAtIndex(etNewContent.getLastIndex(), " ");
                        ToastUtils.showRoundRectToast("图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("NotePadNewActivity", e.getMessage() + "===" + e.getLocalizedMessage());
                        ToastUtils.showRoundRectToast("图片插入失败" + e.getMessage());
                    }

                    @Override
                    public void onNext(String imagePath) {
                        etNewContent.insertImage(imagePath, etNewContent.getMeasuredWidth());
                    }
                });
    }


    /**
     * 插入一条笔记
     */
    private void insertNewDataRealm(boolean isBackground, String title, String type, String time, String content) {
        Realm realm = Realm.getDefaultInstance();
        if (realm != null && realm.where(CacheNotePad.class).findAll() != null) {
            cacheNotePads = realm.where(CacheNotePad.class).findAll();
        } else {
            return;
        }
        realm.beginTransaction();
        CacheNotePad notePad = realm.createObject(CacheNotePad.class);
        notePad.setId(id);
        notePad.setTitle(title);
        notePad.setContent(content);
        notePad.setGroupId(1);
        notePad.setGroupName(type);
        notePad.setType(2);
        notePad.setBgColor("#FFFFFF");
        notePad.setIsEncrypt(0);
        notePad.setHasAlarm(cbAlarm.isChecked());
        notePad.setCreateTime(TimeUtils.getFriendlyTimeSpanByNow(time));
        realm.insert(notePad);
        realm.commitTransaction();
        if(cbAlarm.isChecked()){
            addReminder(title,content,id);
        }

        flag = 1;//插入以后只能是编辑
        if (!isBackground) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    private void addReminder(String title, String content, int id) {
        String time = String.valueOf(tvNoteDate.getText()) + " " + String.valueOf(tvNoteTime.getText());
        Date date = TimeUtils.string2Date(time, new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        //long strTime = TimeUtils.date2Millis(date);
        long strTime = date.getTime();
        Intent mIntent = new Intent(NotePadNewActivity.this, ReminderReceiver.class);
        mIntent.putExtra("title", title);
        mIntent.putExtra("content", content);
        alarmIntent = PendingIntent.getBroadcast(NotePadNewActivity.this, id, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, strTime, alarmIntent);
    }


    /**
     * 编辑一条笔记
     */
    private void editOldDataRealm(boolean isBackground, final String title, final String type, final String time, final String content) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CacheNotePad> notePads =
                realm.where(CacheNotePad.class)
                        .equalTo("id", this.id)
                        .findAll();
        int size = notePads.size();
        Log.e("notePads", size + "");
        //notePads.deleteAllFromRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CacheNotePad pad = realm.createObject(CacheNotePad.class);
                pad.setTitle(title);
                pad.setGroupName(type);
                pad.setCreateTime(time);
                pad.setContent(content);
            }
        });
        if (!isBackground) {
            finish();
        }
    }


    /**
     * 退出的操作
     */
    private void backAndExit() {
        String title = etNewTitle.getText().toString().trim();
        String content = getHyperContent();
        if (flag == 0) {                //新建笔记
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                saveNoteData(false);
            } else {

            }
        } else if (flag == 1) {          //编辑笔记
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                saveNoteData(false);
            } else {

            }
        }
        finish();
    }



    private void initAlarmManager() {
        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //setPicked(true);
        //setAlarmChecked(true);
        //tvNoteDate.setText(TimeUtils.date2String(new Date(),new SimpleDateFormat("yyyy-MM-dd")));
        //tvNoteTime.setText(TimeUtils.date2String(new Date(),new SimpleDateFormat("HH:mm")));
    }


    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    public void setAlarmChecked(boolean isChecked) {
        cbAlarm.setChecked(isChecked);
    }


    private void pickDate() {
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.setOnDatePickListener(new OnDatePickListener() {
            @Override
            public void onDatePick(int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = new Date(calendar.getTimeInMillis());
                String text = TimeUtils.date2String(date, new SimpleDateFormat("yyyy-MM-dd"));
                tvNoteDate.setText(text);
                confirmTimeValidity(year, monthOfYear, dayOfMonth);
                PickTimeIfCreateAlarm();
            }

            @Override
            public void onDatePickCancel() {
                if (!isPicked) {
                    cbAlarm.setChecked(false);
                }
            }
        });
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    private void confirmTimeValidity(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        if (!TimerUtils.isSameDay(calendar, Calendar.getInstance())) {
            return;
        }
        String timeStr = tvNoteTime.getText().toString().trim();
        if (TextUtils.isEmpty(timeStr)) {
            return;
        }
        Date timeDate = TimeUtils.string2Date(timeStr,new SimpleDateFormat("HH:mm"));
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(timeDate);
        rightNow.set(Calendar.YEAR, year);
        rightNow.set(Calendar.MONTH, monthOfYear);
        rightNow.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (rightNow.getTimeInMillis() < System.currentTimeMillis() + 60000) {
            ToastUtils.showRoundRectToast("The time has passed");
            pickDate();
        }
    }


    private void PickTimeIfCreateAlarm() {
        if (!isPicked) {
            pickTime();
        }
    }


    private void pickTime() {
        TimePickerFragment timePickDialog = new TimePickerFragment();
        timePickDialog.setOnTimePickListener(new OnTimePickListener() {
            @Override
            public void onTimePick(int hourOfDay, int minute) {
                String dateStr = String.valueOf(tvNoteDate.getText());
                Date date = TimeUtils.string2Date(dateStr,new SimpleDateFormat("yyyy-MM-dd"));
                Calendar tvCalendar = Calendar.getInstance();
                tvCalendar.setTime(date);

                boolean isToday = TimerUtils.isSameDay(tvCalendar, Calendar.getInstance());
                //if the date is today,decide time has not passed
                Calendar rightNow = Calendar.getInstance();
                rightNow.set(Calendar.HOUR_OF_DAY, hourOfDay);
                rightNow.set(Calendar.MINUTE, minute);
                if (isToday) {
                    confirmTimePast(rightNow);
                }
                String time = TimeUtils.date2String(rightNow.getTime(),new SimpleDateFormat("HH:mm"));
                tvNoteTime.setText(time);
                if (!isPicked) {
                    isPicked = true;
                }
            }

            private void confirmTimePast(Calendar rightNow) {
                if (rightNow.getTimeInMillis() < System.currentTimeMillis() + 60000) {
                    //Alarm time over now less than one minute,choose time again
                    ToastUtils.showRoundRectToast("The time has passed");
                    pickTime();
                }
            }

            @Override
            public void onTimePickCancel() {
                if (!isPicked) {
                    cbAlarm.setChecked(false);
                }
            }
        });
        timePickDialog.show(getFragmentManager(), "TimePickerDialog");
    }



}
