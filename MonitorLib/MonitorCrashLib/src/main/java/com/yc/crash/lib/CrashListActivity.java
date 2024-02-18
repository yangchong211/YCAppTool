package com.yc.crash.lib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yc.appfilelib.AppFileUtils;
import com.yc.crash.R;
import com.yc.eastadapterlib.OnItemClickListener;
import com.yc.eastadapterlib.OnItemLongClickListener;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toolutils.AppIntentUtils;
import com.yc.toolutils.AppLogUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 崩溃列表的展示页面
 *     revise:
 * </pre>
 */
public class CrashListActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLlBack;
    private TextView mTvDelete;
    private TextView tvAbout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecycleView;
    private List<File> fileList = new ArrayList<>();
    private CrashInfoAdapter crashInfoAdapter;
    private ProgressDialog progressDialog;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, CrashListActivity.class);
            target.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_list);
        StateAppBar.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.crash_tool_bar_color));
        initFindViewById();
        initViews();
        initRefreshView();
        initRecycleView();
        initListener();
    }

    private void initFindViewById() {
        mLlBack = findViewById(R.id.ll_back);
        mTvDelete = findViewById(R.id.tv_delete);
        tvAbout = findViewById(R.id.tv_about);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mRecycleView = findViewById(R.id.recycleView);
    }

    private void initViews() {
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRecycleView.setLayoutManager(linearLayoutManager);
    }

    private void initListener() {
        mLlBack.setOnClickListener(this);
        mTvDelete.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
    }

    private void initRefreshView() {
        //设置刷新球颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLACK, Color.YELLOW, Color.RED, Color.GREEN);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mSwipeRefreshLayout.isRefreshing()){
                    initCrashFileList();
                }
            }
        });
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                initCrashFileList();
            }
        });
    }

    private void initRecycleView() {
        crashInfoAdapter = new CrashInfoAdapter(this);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(crashInfoAdapter);
        crashInfoAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                if (fileList.size()>position && position>=0){
                    //弹出Dialog是否删除当前
                    AlertDialog.Builder builder = new AlertDialog.Builder(CrashListActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("是否删除当前日志?");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog = ProgressDialog.show(CrashListActivity.this, "提示", "正在删除...");
                            progressDialog.show();
                            DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
                                @Override
                                public void run() {
                                    File file = fileList.get(position);
                                    AppFileUtils.deleteFile(file.getPath());
                                    //重新获取
                                    getCrashList();
                                }
                            });
                        }
                    });
                    builder.show();
                }
                return false;
            }
        });
        crashInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (fileList.size()>position && position>=0){
                    Intent intent = new Intent(CrashListActivity.this, CrashDetailsActivity.class);
                    File file = fileList.get(position);
                    intent.putExtra(CrashDetailsActivity.INTENT_KEY_FILE_PATH, file.getAbsolutePath());
                    startActivity(intent);
                }
            }
        });
    }


    private void initCrashFileList() {
        //获取日志
        DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
            @Override
            public void run() {
                getCrashList();
            }
        });
    }

    private void getCrashList() {
        //重新获取
        fileList.clear();
        String filePath = CrashHelperUtils.getCrashLogPath(this);
        AppLogUtils.d("CrashListActivity : " , "file path : " + filePath);
        //路径  /storage/emulated/0/Android/data/com.yc.lifehelper/cache/crashLogs
        fileList = AppFileUtils.getFileList(filePath);
        //排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file01, File file02) {
                try {
                    //根据修改时间排序
                    long lastModified01 = file01.lastModified();
                    long lastModified02 = file02.lastModified();
                    if (lastModified01 > lastModified02) {
                        return -1;
                    } else {
                        return 1;
                    }
                } catch (Exception e) {
                    return 1;
                }
            }
        });

        //通知页面刷新
        DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.hide();
                }
                crashInfoAdapter.setData(fileList);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_delete) {
            deleteAll();
        } else if (i == R.id.ll_back) {
            finish();
        } else if (i == R.id.tv_about){
            AppIntentUtils.openLink(this, "https://github.com/yangchong211/YCAndroidTool");
        }
    }

    private void deleteAll() {
        //弹出Dialog是否删除全部
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否删除全部日志?");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog = ProgressDialog.show(
                        CrashListActivity.this, "提示", "正在删除...");
                progressDialog.show();
                DelegateTaskExecutor.getInstance().executeOnCpu(new Runnable() {
                    @Override
                    public void run() {
                        File fileCrash = new File(CrashHelperUtils.getCrashLogPath(CrashListActivity.this));
                        AppFileUtils.deleteAllFiles(fileCrash);
                        //重新获取
                        getCrashList();
                    }
                });
            }
        });
        builder.show();
    }

}
