package com.yc.common.file;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.yc.appfilelib.BufferIoUtils;
import com.yc.appfilelib.FileIoUtils;
import com.yc.appmediastore.AppFileUriUtils;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.file.AppFileUtils;

import java.io.File;

public class FileActivity extends BaseActivity implements View.OnClickListener {

    private RoundTextView tvFile1;
    private RoundTextView tvFile2;
    private RoundTextView tvFile3;
    private RoundTextView tvFile4;
    private RoundTextView tvFile5;
    private RoundTextView tvFile6;
    private RoundTextView tvFile7;
    private RoundTextView tvFile8;
    private RoundTextView tvFile9;
    private RoundTextView tvFile10;
    private TextView tvContent;

    @Override
    public int getContentView() {
        return R.layout.activity_file_test;
    }

    @Override
    public void initView() {
        tvFile1 = findViewById(R.id.tv_file_1);
        tvFile2 = findViewById(R.id.tv_file_2);
        tvFile3 = findViewById(R.id.tv_file_3);
        tvFile4 = findViewById(R.id.tv_file_4);
        tvFile5 = findViewById(R.id.tv_file_5);
        tvFile6 = findViewById(R.id.tv_file_6);
        tvFile7 = findViewById(R.id.tv_file_7);
        tvFile8 = findViewById(R.id.tv_file_8);
        tvFile9 = findViewById(R.id.tv_file_9);
        tvFile10 = findViewById(R.id.tv_file_10);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    public void initListener() {
        tvFile1.setOnClickListener(this);
        tvFile2.setOnClickListener(this);
        tvFile3.setOnClickListener(this);
        tvFile4.setOnClickListener(this);
        tvFile5.setOnClickListener(this);
        tvFile6.setOnClickListener(this);
        tvFile7.setOnClickListener(this);
        tvFile8.setOnClickListener(this);
        tvFile9.setOnClickListener(this);
        tvFile10.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tvFile1) {
            String txt = AppFileUtils.getExternalCachePath(this, "txt");
            String content = "2你好这个是写入的文件。明确一个前提：各个业务组件之间不会是相互隔离而是必然存在一些交互的；\n" +
                    "\n" +
                    "业务复用：在Module A需要引用Module B提供的某个功能，比如需要版本更新业务逻辑，而我们一般都是使用强引用的Class显式的调用；\n" +
                    "业务复用：在Module A需要调用Module B提供的某个方法，例如别的Module调用用户模块退出登录的方法；\n" +
                    "业务获取参数：登陆环境下，在Module A，C，D，E多个业务组件需要拿到Module B登陆注册组件中用户信息id，name，info等参数访问接口数据；\n" +
                    "\n" +
                    "\n" +
                    "这几种调用形式大家很容易明白，正常开发中大家也是毫不犹豫的调用。但是在组件化开发的时候却有很大的问题：\n" +
                    "\n" +
                    "由于业务组件之间没有相互依赖，组件Module B的Activity Class在自己的Module中，那Module A必然引用不到，这样无法调用类的功能方法；由此：必然需要一种支持组件化需求的交互方式，提供平行级别的组件间调用函数通信交互的功能。\n" +
                    "\n" +
                    "作者：杨充\n" +
                    "链接：https://juejin.cn/post/6938008708295163934\n" +
                    "来源：稀土掘金\n" +
                    "逗比著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。";
            String fileName = txt + File.separator + "yc1.txt";
            AppLogUtils.d("FileActivity : 写文件 路径" , fileName);
            FileIoUtils.writeString2File1(content,fileName);
            ToastUtils.showRoundRectToast("写入完成");
        } else if (v == tvFile2) {
            String txt = AppFileUtils.getExternalCachePath(this, "txt");
            String fileName = txt + File.separator + "yc1.txt";
            AppLogUtils.d("FileActivity : 读文件 路径" , fileName);
            String file2String = BufferIoUtils.readFile2String2(fileName);
            tvContent.setText(file2String);
            AppLogUtils.d("FileActivity : 读文件" , file2String);
        } else if (v == tvFile3) {
            String txt = AppFileUtils.getExternalCachePath(this, "txt");
            String fileName = txt + File.separator + "yc1.txt";
            String newFileName = txt + File.separator + "ycdoubi哈哈哈.txt";
            BufferIoUtils.copyFile(fileName,newFileName);
            ToastUtils.showRoundRectToast("复制完成");
        } else if (v == tvFile4) {
            //打开txt文档
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1024);
        } else if (v == tvFile10){
            FileExplorerActivity.startActivity(FileActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1024) {
            Uri uri = data.getData();
            File file = AppFileUriUtils.uri2File(this, uri);
            AppLogUtils.d("FileActivity : 回调" , file.getPath() + "  " +file.getName());
            String file2String = FileIoUtils.readFile2String(file.getPath());
            tvContent.setText(file2String);
            ToastUtils.showRoundRectToast("写入完成");
        }
    }
}
