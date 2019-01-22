package com.ycbjie.todo.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ycbjie.todo.R;
import com.ycbjie.todo.ui.activity.WorkNewActivity;
import com.ycbjie.todo.ui.adapter.WorkChooseAdapter;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：图片选择对话框
 * 修订历史：
 * ================================================
 */
public class WorkChooseColorDialog extends DialogFragment {

    private WorkChooseAdapter mChooseDialogAdapter;
    private RecyclerView mRvDlChooseColor;

    public static WorkChooseColorDialog newInstance(int currCheckItemUri) {
        WorkChooseColorDialog dialog = new WorkChooseColorDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("chooseCheck", currCheckItemUri);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("选择背景图片");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_recycler_view, container, false);
        mRvDlChooseColor = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRvDlChooseColor.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mChooseDialogAdapter = new WorkChooseAdapter(getActivity());
        mChooseDialogAdapter.setCheckItem(getArguments().getInt("chooseCheck"));
        mChooseDialogAdapter.setOnItemClickListener(new WorkChooseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ((WorkNewActivity) getActivity()).loadBgImgWithIndex(position);
                mChooseDialogAdapter.setCheck(position);
                dismiss();
            }
        });
        mRvDlChooseColor.setAdapter(mChooseDialogAdapter);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mChooseDialogAdapter = null;
        mRvDlChooseColor = null;
    }

}
