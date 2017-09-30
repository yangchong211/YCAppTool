package com.ns.yc.lifehelper.ui.other.toDo.dialog;

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

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.toDo.adapter.ChooseDialogAdapter;
import com.ns.yc.lifehelper.ui.other.toDo.view.ToDoAddActivity;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：图片选择对话框
 * 修订历史：
 * ================================================
 */
public class ChoosePaperColorDialog extends DialogFragment {

    private ChooseDialogAdapter mChooseDialogAdapter;
    RecyclerView mRvDlChooseColor;

    public static ChoosePaperColorDialog newInstance(int currCheckItemUri) {
        ChoosePaperColorDialog dialog = new ChoosePaperColorDialog();
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
        View view = inflater.inflate(R.layout.dl_choose_paper_color, container, false);
        mRvDlChooseColor = (RecyclerView) view.findViewById(R.id.rv_dl_choose_color);
        mRvDlChooseColor.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mChooseDialogAdapter = new ChooseDialogAdapter(getActivity());
        mChooseDialogAdapter.setCheckItem(getArguments().getInt("chooseCheck"));
        mChooseDialogAdapter.setOnItemClickListener(new ChooseDialogAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ((ToDoAddActivity) getActivity()).loadBgImgWithIndex(position);
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
