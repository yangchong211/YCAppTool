package com.yc.monitorfilelib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.toolutils.AppSpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 *     author : 杨充
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : sp详情页面
 *     revise :
 * </pre>
 */
public class SpDetailFragment extends Fragment {

    private LinearLayout mLlBackLayout;
    private TextView mTvTitle;
    private TextView mTvShare;
    private RecyclerView mRecyclerView;
    private SpContentAdapter mSpContentAdapter;
    private static final String TAG = "SpDetailFragment";
    private Activity mActivity;
    private String spTableName;

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

    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mSpContentAdapter = new SpContentAdapter(mActivity);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mSpContentAdapter);
    }

    private void initData() {
        List<SpDataBean> spBeans = getSpBeans();
        if (spBeans.isEmpty()) {
            finish();
        }
        mSpContentAdapter.setData(spBeans);
        mSpContentAdapter.notifyDataSetChanged();
    }

    private List<SpDataBean> getSpBeans() {
        ArrayList<SpDataBean> spBeans = new ArrayList<>();
        Bundle data = getArguments();
        if (data != null && mActivity != null) {
            File mFile = (File) getArguments().getSerializable("file_key");
            if (mFile == null) {
                return spBeans;
            }
            spTableName = mFile.getName().replace(FileExplorerUtils.XML, "");
            mTvTitle.setText(spTableName);
            Map<String, ?> all = AppSpUtils.getInstance(spTableName).getAll();
            if (all.isEmpty()) {
                return spBeans;
            }
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                spBeans.add(new SpDataBean(entry.getKey(), entry.getValue()));
            }
        }
        return spBeans;

    }

    public void spUpData(SpDataBean bean) {
        String key = bean.key;
        switch (bean.value.getClass().getSimpleName()) {
            case SpInputType.STRING:
                AppSpUtils.getInstance().put(key, bean.value.toString());
                break;
            case SpInputType.BOOLEAN:
                AppSpUtils.getInstance().put(key, (boolean) bean.value);
                break;
            case SpInputType.INTEGER:
                AppSpUtils.getInstance().put(key, (int) bean.value);
                break;
            case SpInputType.FLOAT:
                AppSpUtils.getInstance().put(key, (float) bean.value);
                break;
            case SpInputType.LONG:
                AppSpUtils.getInstance().put(key, (long) bean.value);
                break;
            default:
                break;
        }

    }

    public void finish() {
        FileExplorerActivity activity = (FileExplorerActivity) getActivity();
        if (activity != null) {
            activity.doBack(this);
        }
    }

}
