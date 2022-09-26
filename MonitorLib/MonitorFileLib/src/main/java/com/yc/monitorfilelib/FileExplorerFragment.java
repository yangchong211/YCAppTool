package com.yc.monitorfilelib;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.appfilelib.AppFileUtils;
import com.yc.eastadapterlib.OnItemClickListener;
import com.yc.eastadapterlib.OnItemLongClickListener;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppWindowUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;


/**
 * <pre>
 *     author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 文件管理页面fragment
 *     revise :
 * </pre>
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
        //定义fragment的返回键逻辑
        requireActivity().getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            //返回上一层
            onBackPressed();
        }
    };

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
                    boolean copyToClipBoard = AppWindowUtils.copyToClipBoard(getContext(), path);
                    if (copyToClipBoard) {
                        ToastUtils.showRoundRectToast("拷贝成功");
                    }
                } else {
                    ToastUtils.showRoundRectToast("当前为空");
                }
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFileInfoAdapter = new FileListAdapter(getContext());
        mFileInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (mFileList.size() > i && i >= 0) {
                    File fileInfo = mFileList.get(i);
                    if (fileInfo.exists() && fileInfo.isFile()) {
                        //如果是文件，则直接打开文件
                        if (FileExplorerUtils.isImage(fileInfo)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("file_key", fileInfo);
                            showContent(ImageDetailFragment.class, bundle);
                        } else if (FileExplorerUtils.isSp(fileInfo)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("file_key", fileInfo);
                            showContent(SpDetailFragment.class, bundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("file_key", fileInfo);
                            showContent(TextDetailFragment.class, bundle);
                        }
                    } else {
                        //否则是文件夹，则继续打开对应的list列表
                        mCurDir = fileInfo;
                        mTvTitle.setText(mCurDir.getName());
                        //拿到当前目录的列表数据
                        setAdapterData(AppFileUtils.getFileList(mCurDir));
                    }
                }
            }
        });
        mFileInfoAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
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
        if (mFileList.size() > position && position >= 0) {
            //弹出Dialog是否删除当前
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("是否删除当前文件?");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    PoolThread executor = PoolThread.ThreadBuilder.createSingle().build();
                    executor.setName("delete_file");
                    //executor.setDelay(2, TimeUnit.SECONDS);
                    // 启动异步任务
                    executor.async(new Callable<Boolean>(){
                        @Override
                        public Boolean call() throws Exception {
                            File file = mFileList.get(position);
                            return AppFileUtils.deleteDirectory(file);
                        }
                    }, new AsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean isDel) {
                            Log.e("PoolThreadAsyncCallback","成功");
                            if (isDel) {
                                ToastUtils.showRoundRectToast("删除成功");
                                mFileInfoAdapter.remove(position);
                                //mFileList.remove(position);
                                //mFileInfoAdapter.notifyItemRemoved(position);
                            } else {
                                ToastUtils.showRoundRectToast("删除失败");
                            }
                        }

                        @Override
                        public void onFailed(Throwable t) {
                            Log.e("PoolThreadAsyncCallback","失败");
                        }

                        @Override
                        public void onStart(String threadName) {
                            Log.e("PoolThreadAsyncCallback","开始");
                        }
                    });
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
        mFileInfoAdapter.clearAll();
        mFileInfoAdapter.setData(mFileList);
    }

    public void showContent(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        FileExplorerActivity activity = (FileExplorerActivity) getActivity();
        if (activity != null) {
            activity.showContent(fragmentClass, bundle);
        }
    }

    protected boolean onBackPressed() {
        if (mCurDir == null) {
            finish();
            return true;
        } else if (isRootFile(getContext(), mCurDir)) {
            mTvTitle.setText("沙盒游览");
            setAdapterData(initRootFileInfo(getContext()));
            mCurDir = null;
            return false;
        } else {
            mCurDir = mCurDir.getParentFile();
            if (mCurDir != null) {
                mTvTitle.setText(mCurDir.getName());
                List<File> fileInfos = AppFileUtils.getFileList(mCurDir);
                setAdapterData(fileInfos);
            }
            return false;
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
     * 初始化默认文件。注意：加External和不加(默认)的比较
     * 相同点:1.都可以做app缓存目录。2.app卸载后，两个目录下的数据都会被清空。
     * 不同点:1.目录的路径不同。前者的目录存在外部SD卡上的。后者的目录存在app的内部存储上。
     *       2.前者的路径在手机里可以直接看到。后者的路径需要root以后，用Root Explorer 文件管理器才能看到。
     *
     * @param context 上下文
     * @return 列表
     */
    private List<File> initDefaultRootFileInfos(Context context) {
        List<File> fileInfos = new ArrayList<>();
        //第一个是文件父路径
        File parentFile = context.getFilesDir().getParentFile();
        if (parentFile != null) {
            fileInfos.add(parentFile);
            FileExplorerUtils.logInfo(TAG + parentFile.getPath());
        }
        //路径：/data/user/0/com.yc.lifehelper

        //第二个是缓存文件路径
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            fileInfos.add(externalCacheDir);
            FileExplorerUtils.logInfo(TAG + externalCacheDir.getPath());
        }
        //路径：/storage/emulated/0/Android/data/com.yc.lifehelper/cache

        //第三个是外部file路径
        File externalFilesDir = context.getExternalFilesDir((String) null);
        if (externalFilesDir != null) {
            fileInfos.add(externalFilesDir);
            FileExplorerUtils.logInfo(TAG + externalFilesDir.getPath());
        }
        //路径：/storage/emulated/0/Android/data/com.yc.lifehelper/files
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
