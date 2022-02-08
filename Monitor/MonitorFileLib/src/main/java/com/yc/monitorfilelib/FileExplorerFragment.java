package com.yc.monitorfilelib;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * description: 文件管理页面fragment
 * @author  杨充
 * @since   2021/8/11
 */
public class FileExplorerFragment extends Fragment {

    private LinearLayout mLlBackLayout;
    private TextView mTvTitle;
    private TextView mTvLook;
    private RecyclerView mRecyclerView;
    private File mCurDir;
    private FileListAdapter mFileInfoAdapter;
    private final List<File> mFileList = new ArrayList<>();
    private static final String TAG = "FileExplorerFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_file_list, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewById(view);
        initTitleView();
        initRecyclerView();
    }

    private void initViewById(View view) {
        mLlBackLayout = view.findViewById(R.id.ll_back_layout);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvLook = view.findViewById(R.id.tv_look);
        mRecyclerView = view.findViewById(R.id.recyclerView);
    }

    private void initTitleView() {
        mLlBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一层
                onBackPressed();
            }
        });
        mTvLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurDir != null) {
                    String path = mCurDir.getPath();
                    FileExplorerUtils.copyToClipBoard(getContext(), path);
                } else {
                    Toast.makeText(getContext(), "当前为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFileInfoAdapter = new FileListAdapter(getContext(), mFileList);
        mFileInfoAdapter.setOnViewClickListener(new FileListAdapter.OnViewClickListener() {
            public void onViewClick(View v, File fileInfo) {
                if (fileInfo.isFile()) {
                    //如果是文件夹，则继续打开
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("file_key", fileInfo);
                    showContent(TextDetailFragment.class, bundle);
                } else {
                    mCurDir = fileInfo;
                    mTvTitle.setText(mCurDir.getName());
                    setAdapterData(getFileInfos(mCurDir));
                }
            }
        });
        mFileInfoAdapter.setOnViewLongClickListener(new FileListAdapter.OnViewLongClickListener() {
            @Override
            public boolean onViewLongClick(View v, File fileInfo,int position) {
                //长按弹窗，让测试可以选择是否删除文件
                delFile(position);
                return false;
            }
        });
        List<File> files = initRootFileInfo(getContext());
        setAdapterData(files);
        mRecyclerView.setAdapter(mFileInfoAdapter);
    }

    private void delFile(int position) {
        if (mFileList.size()>position && position>=0){
            //弹出Dialog是否删除当前
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("是否删除当前文件?");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //删除文件
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = mFileList.get(position);
                            boolean isDel = FileExplorerUtils.deleteDirectory(file);
                            if (isDel){
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                        mFileList.remove(position);
                                        mFileInfoAdapter.notifyItemRemoved(position);
                                    }
                                });
                            } else {
                                mRecyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });
            builder.show();
        }
    }

    private void setAdapterData(List<File> fileInfos) {
        if (fileInfos.isEmpty()) {
            mFileList.clear();
        } else {
            if (mFileList.size() > 0) {
                mFileList.clear();
            }
            mFileList.addAll(fileInfos);
        }
        mFileInfoAdapter.notifyDataSetChanged();
    }

    public void showContent(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        FileExplorerActivity activity = (FileExplorerActivity) getActivity();
        if (activity != null) {
            activity.showContent(fragmentClass, bundle);
        }
    }

    protected void onBackPressed() {
        if (mCurDir == null) {
            finish();
        } else if (isRootFile(getContext(), mCurDir)) {
            mTvTitle.setText("沙盒游览");
            setAdapterData(initRootFileInfo(getContext()));
            mCurDir = null;
        } else {
            mCurDir = mCurDir.getParentFile();
            if (mCurDir != null) {
                mTvTitle.setText(mCurDir.getName());
                List<File> fileInfos = getFileInfos(mCurDir);
                setAdapterData(fileInfos);
            }
        }
    }

    /**
     * 返回
     */
    public void finish() {
        FileExplorerActivity activity = (FileExplorerActivity) getActivity();
        if (activity != null) {
            activity.doBack(this);
        }
    }

    /**
     * 获取某个file
     * @param dir                       file文件
     * @return
     */
    private List<File> getFileInfos(File dir) {
        List<File> fileInfos = new ArrayList<>();
        if (dir.listFiles() != null) {
            File[] files = dir.listFiles();
            if (files != null) {
                int length = files.length;
                for (int i = 0; i < length; ++i) {
                    File file = files[i];
                    fileInfos.add(file);
                }
            }
        }
        return fileInfos;
    }

    /**
     * 初始化root file文件
     *
     * @param context 上下文
     * @return
     */
    private List<File> initRootFileInfo(Context context) {
        List<File> rootFiles = getRootFiles();
        if (rootFiles == null || rootFiles.isEmpty()) {
            return initDefaultRootFileInfos(context);
        } else {
            List<File> files = new ArrayList<>();
            Iterator<File> iterator = rootFiles.iterator();
            while (iterator.hasNext()) {
                File file = (File) iterator.next();
                files.add(file);
            }
            return files;
        }
    }

    /**
     * 初始化默认文件。注意：加External和不加的比较
     * 相同点:1.都可以做app缓存目录。2.app卸载后，两个目录下的数据都会被清空。
     * 不同点:1.目录的路径不同。前者的目录存在外部SD卡上的。后者的目录存在app的内部存储上。
     *       2.前者的路径在手机里可以直接看到。后者的路径需要root以后，用Root Explorer 文件管理器才能看到。
     * @param context 上下文
     * @return
     */
    private List<File> initDefaultRootFileInfos(Context context) {
        List<File> fileInfos = new ArrayList<>();
        //第一个是文件父路径
        File parentFile = context.getFilesDir().getParentFile();
        if (parentFile!=null){
            fileInfos.add(parentFile);
            FileExplorerUtils.logInfo(TAG+parentFile.getPath());
        }

        //第二个是缓存文件路径
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir!=null){
            fileInfos.add(externalCacheDir);
            FileExplorerUtils.logInfo(TAG+externalCacheDir.getPath());
        }

        //第三个是外部file路径
        File externalFilesDir = context.getExternalFilesDir((String) null);
        if (externalFilesDir!=null){
            fileInfos.add(externalFilesDir);
            FileExplorerUtils.logInfo(TAG+externalFilesDir.getPath());
        }
        return fileInfos;
    }

    /**
     * 判断是否是root file文件
     *
     * @param context 上下文
     * @param file    file文件
     * @return
     */
    private boolean isRootFile(Context context, File file) {
        if (file == null) {
            return false;
        } else {
            List<File> rootFiles = getRootFiles();
            if (rootFiles != null) {
                Iterator<File> iterator = rootFiles.iterator();
                if (iterator.hasNext()) {
                    File rootFile = iterator.next();
                    return file.equals(rootFile);
                }
            }
            return file.equals(context.getExternalCacheDir()) ||
                    file.equals(context.getExternalFilesDir((String) null)) ||
                    file.equals(context.getFilesDir().getParentFile());
        }
    }

    /**
     * 获取root文件
     *
     * @return
     */
    private List<File> getRootFiles() {
        if (getArguments() != null) {
            File dir = (File) getArguments().getSerializable("dir_key");
            if (dir != null && dir.exists()) {
                File[] files = dir.listFiles();
                if (files == null) {
                    return null;
                }
                return Arrays.asList(files);
            }
        }
        return null;
    }
}
