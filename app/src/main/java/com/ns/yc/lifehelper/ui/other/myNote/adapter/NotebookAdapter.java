package com.ns.yc.lifehelper.ui.other.myNote.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.myNote.bean.NoteDetail;
import com.ns.yc.lifehelper.ui.other.myNote.view.NoteAddFragment;
import com.ns.yc.lifehelper.weight.noteView.HTQDragGridView;

import java.util.Collections;
import java.util.List;


public class NotebookAdapter extends BaseAdapter implements HTQDragGridView.DragGridBaseAdapter {

    private List<NoteDetail> datas;
    private final Activity aty;
    private int currentHidePosition = -1;
    private boolean dataChange = false;
    private final int width;
    private final int height;

    public NotebookAdapter(Activity aty, List<NoteDetail> data) {
        super();
        this.datas = data;
        this.aty = aty;
        width = ScreenUtils.getScreenWidth() / 2;
        height = SizeUtils.dp2px(40);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<NoteDetail> getDatas() {
        return datas;
    }

    /**
     * 数据是否发生了改变
     * 
     * @return
     */
    public boolean getDataChange() {
        return dataChange;
    }

    static class ViewHolder {
        TextView date;
        ImageView state;
        ImageView thumbtack;
        View titleBar;
        TextView content;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        NoteDetail data = datas.get(position);

        ViewHolder holder = null;
        if (v == null) {
            holder = new ViewHolder();
            v = View.inflate(aty, R.layout.item_note_book, null);
            holder.titleBar = v.findViewById(R.id.item_note_titlebar);
            holder.date = (TextView) v.findViewById(R.id.item_note_tv_date);
            holder.state = (ImageView) v.findViewById(R.id.item_note_img_state);
            holder.thumbtack = (ImageView) v.findViewById(R.id.item_note_img_thumbtack);
            holder.content = (TextView) v.findViewById(R.id.item_note_content);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        LayoutParams params = (LayoutParams) holder.content.getLayoutParams();
        params.width = width;
        params.height = (params.width - height);
        holder.content.setLayoutParams(params);

        holder.titleBar.setBackgroundColor(NoteAddFragment.sTitleBackGrounds[1]);
        holder.date.setText(data.getTime());
        if (data.getId() > 0) {
            holder.state.setVisibility(View.GONE);
        } else {
            holder.state.setVisibility(View.VISIBLE);
        }
        holder.thumbtack.setImageResource(NoteAddFragment.sThumbtackImg[1]);
        holder.content.setText(Html.fromHtml(data.getContent()));
        holder.content.setBackgroundColor(NoteAddFragment.sBackGrounds[1]);
        if (position == currentHidePosition) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
        return v;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        dataChange = true;
        if (oldPosition >= datas.size() || oldPosition < 0) {
            return;
        }
        NoteDetail temp = datas.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(datas, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(datas, i, i - 1);
            }
        }
        datas.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.currentHidePosition = hidePosition;
        notifyDataSetChanged();
    }
}
