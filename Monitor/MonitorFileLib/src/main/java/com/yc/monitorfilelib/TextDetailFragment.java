package com.yc.monitorfilelib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * description: 文本详情页面
 * @author  杨充
 * @since   2021/8/11
 */
public class TextDetailFragment extends Fragment {
    
    private LinearLayout mLlBackLayout;
    private TextView mTvTitle;
    private TextView mTvShare;
    private RecyclerView mRecyclerView;
    private TextContentAdapter mTextContentAdapter;
    private File mFile;
    private final List<String> mContentList = new ArrayList<>();
    private static final String TAG = "TextDetailFragment";
    private static final int CODE = 1000;
    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_file_content, 
                container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewById(view);
        initTitleView();
        initRecyclerView();
        initData();
    }

    private void initViewById(View view) {
        mLlBackLayout = view.findViewById(R.id.ll_back_layout);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvShare = view.findViewById(R.id.tv_share);
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initTitleView() {
        mLlBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile();
            }
        });
    }

    /**
     * Android平台中，能操作文件夹的只有两个地方：
     * sdcard
     * data/data/<package-name>/files
     */
    private void shareFile() {
        //分享
        if (mFile!=null){
            //先把文件转移到外部存储文件
            //请求权限
            //检查版本是否大于M
            if (ContextCompat.checkSelfPermission(mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE);
            } else {
                //先把文件转移到外部存储文件
                File srcFile = new File(mFile.getPath());
                String newFilePath = FileExplorerUtils.getFileSharePath() + "/fileShare.txt";
                File destFile = new File(newFilePath);
                //拷贝文件，将data/data源文件拷贝到新的目标文件路径下
                boolean copy = FileExplorerUtils.copyFile(srcFile, destFile);
                if (copy) {
                    //分享
                    FileExplorerUtils.shareFile(mActivity, destFile);
                } else {
                    Toast.makeText(getContext(), "文件保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "file当前为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mTextContentAdapter = new TextContentAdapter(mActivity,mContentList);
        mRecyclerView.setAdapter(mTextContentAdapter);
    }

    private void initData() {
        Bundle data = getArguments();
        if (data != null) {
            mFile = (File)data.getSerializable("file_key");
        }
        if (mFile != null) {
            mTvTitle.setText(mFile.getName());
            FileReadTask task = new FileReadTask(this);
            task.execute(new File[]{mFile});
        }
    }

    public void finish() {
        FileExplorerActivity activity = (FileExplorerActivity)getActivity();
        if (activity != null) {
            activity.doBack(this);
        }
    }

    private static class FileReadTask extends AsyncTask<File, String, Void> {

        private final WeakReference<TextDetailFragment> mReference;

        public FileReadTask(TextDetailFragment fragment) {
            mReference = new WeakReference(fragment);
        }

        protected Void doInBackground(File... files) {
            FileReader fileReader = null;
            BufferedReader br = null;
            try {
                //大概思路是，一次读取一行，然后
                fileReader = new FileReader(files[0]);
                br = new BufferedReader(fileReader);
                String textLine;
                while((textLine = br.readLine()) != null) {
                    //一次读取一行
                    publishProgress(new String[]{textLine});
                }
            } catch (IOException exception) {
                FileExplorerUtils.logError(TAG+exception.toString());
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
            return null;
        }

        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (mReference.get() != null) {
                mReference.get().mTextContentAdapter.append(values[0]);
            }
        }
    }
}
