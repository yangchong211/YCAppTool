package com.ycbjie.note.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ycbjie.note.R;
import com.ycbjie.note.model.bean.NotePadDetail;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：简易记事本list适配器
 * 修订历史：
 * ================================================
 */
public class NotePadListAdapter extends RecyclerArrayAdapter<NotePadDetail> {


    public NotePadListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }


    private class ViewHolder extends BaseViewHolder<NotePadDetail> {

        TextView tv_list_title ,tv_id, tv_list_summary , tv_list_time , tv_list_group ;

        ViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_note_pad_list);
            tv_list_title = $(R.id.tv_list_title);
            tv_id = $(R.id.tv_id);
            tv_list_summary = $(R.id.tv_list_summary);
            tv_list_time = $(R.id.tv_list_time);
            tv_list_group = $(R.id.tv_list_group);
        }

        @Override
        public void setData(NotePadDetail data) {
            super.setData(data);
            if(data!=null){
                //将数据保存在itemView的Tag中，以便点击时进行获取
                itemView.setTag(data);
                tv_list_title.setText(data.getTitle());
                tv_id.setText(String.valueOf(data.getId()));
                tv_list_summary.setText(data.getContent());
                tv_list_time.setText(data.getCreateTime());
                tv_list_group.setText(data.getGroupName());
            }
        }

    }
}
