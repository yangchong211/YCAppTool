package com.ycbjie.note.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ycbjie.note.R;
import com.ycbjie.note.utils.NoteDB;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/13
 *     desc  : markDown记事本
 *     revise:
 * </pre>
 */
public class MdNoteAdapter extends BaseAdapter {

    private Context mContext;

    protected class ViewHolder {
        TextView mNoteDate;
        TextView mNoteTitle;
    }

    public MdNoteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return NoteDB.getInstance().size();
    }

    @Override
    public Object getItem(int position) {
        return NoteDB.getInstance().get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((NoteDB.Note) getItem(position)).key;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.layout_note_item, null);
                ViewHolder holder = new ViewHolder();
                holder.mNoteDate = convertView.findViewById(R.id.NoteDateText);
                holder.mNoteTitle = convertView.findViewById(R.id.NoteTitleText);
                convertView.setTag(holder);
            }
        }

        NoteDB.Note note = (NoteDB.Note) getItem(position);
        if (note != null && convertView!=null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mNoteDate.setText(getDateStr(note.date));
            holder.mNoteTitle.setText(note.title);
        }

        return convertView;
    }

    public static String getDateStr(long milliseconds) {
        return new SimpleDateFormat("yyyy年MM月dd日 EEEE HH点mm分", Locale.CHINA).format(milliseconds);
    }
}
