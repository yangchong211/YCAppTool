package com.ycbjie.ycwebview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.webviewlib.widget.FileReaderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class FileDisplayActivity extends AppCompatActivity {

    private FileReaderView mDocumentReaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        copy();
        init();
    }

    String filePath;
    public void init() {
        mDocumentReaderView = findViewById(R.id.documentReaderView);
        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹出保存图片的对话框
                new AlertDialog.Builder(FileDisplayActivity.this)
                        .setItems(new String[]{"加载word文档", "加载txt文件", "加载表格文档", "加载ppt文件", "加载pdf文件"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                filePath = getFilePath(0);
                                                int length = filePath.length();
                                                mDocumentReaderView.show(filePath);
                                                break;
                                            case 1:
                                                filePath = getFilePath(1);
                                                mDocumentReaderView.show(filePath);
                                                break;
                                            case 2:
                                                filePath = getFilePath(2);
                                                mDocumentReaderView.show(filePath);
                                                break;
                                            case 3:
                                                filePath = getFilePath(3);
                                                mDocumentReaderView.show(filePath);
                                                break;
                                            case 4:
                                                filePath = getFilePath(4);
                                                mDocumentReaderView.show(filePath);
                                                break;
                                            default:
                                                filePath = getFilePath(0);
                                                mDocumentReaderView.show(filePath);
                                                break;
                                        }
                                    }
                                })
                        .show();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDocumentReaderView != null) {
            mDocumentReaderView.stop();
        }
    }


    private String getFilePath(int position) {
        String path = null;
        switch (position) {
            case 0:
                path = getFilesDir().getAbsolutePath() + File.separator + "test.docx";
                break;

            case 1:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.txt";
                break;

            case 2:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.xlsx";
                break;

            case 3:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.pptx";
                break;

            case 4:
                path = getFilesDir().getAbsolutePath() + File.separator +"test.pdf";
                break;

            default:
                break;
        }
        return path;
    }


    private void copy() {
        // 开始复制
        String path = "file" + File.separator;
        copyAssetsFileToAppFiles(path + "test.docx", "test.docx");
        copyAssetsFileToAppFiles(path + "test.pdf", "test.pdf");
        copyAssetsFileToAppFiles(path + "test.pptx", "test.pptx");
        copyAssetsFileToAppFiles(path + "test.txt", "test.txt");
        copyAssetsFileToAppFiles(path + "test.xlsx", "test.xlsx");
    }


    /**
     * 从assets目录中复制某文件内容
     *
     * @param assetFileName assets目录下的文件
     * @param newFileName   复制到/data/data/package_name/files/目录下文件名
     */
    private void copyAssetsFileToAppFiles(String assetFileName, String newFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = this.getAssets().open(assetFileName);
            fos = this.openFileOutput(newFileName, Context.MODE_PRIVATE);
            int byteCount = 0;
            byte[] buffer = new byte[1024];
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
