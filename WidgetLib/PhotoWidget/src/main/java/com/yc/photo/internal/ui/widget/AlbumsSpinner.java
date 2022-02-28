package com.yc.photo.internal.ui.widget;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yc.photo.R;
import com.yc.photo.internal.entity.Album;
import com.yc.photo.internal.utils.Platform;


public class AlbumsSpinner {

    private static final int MAX_SHOWN_COUNT = 6;
    private CursorAdapter mAdapter;
    private TextView mSelected;
    private ListPopupWindow mListPopupWindow;
    private Drawable drawableIcon;
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;

    public AlbumsSpinner(@NonNull Context context) {
        mListPopupWindow = new ListPopupWindow(context, null, R.attr.listPopupWindowStyle);
        mListPopupWindow.setModal(true);
        float density = context.getResources().getDisplayMetrics().density;
        //mListPopupWindow.setContentWidth((int) (216 * density));
        int screenWidth = getScreenWidth(context);
        mListPopupWindow.setContentWidth(screenWidth);
        //mListPopupWindow.setHorizontalOffset((int) (16 * density));
        //该处偏移到title布局下面
        mListPopupWindow.setVerticalOffset((int) (0 * density));
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumsSpinner.this.onItemSelected(parent.getContext(), position);
                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(parent, view, position, id);
                }
            }
        });
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public void setSelection(Context context, int position) {
        mListPopupWindow.setSelection(position);
        onItemSelected(context, position);
    }

    private void onItemSelected(Context context, int position) {
        mListPopupWindow.dismiss();
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        Album album = Album.valueOf(cursor);
        String displayName = album.getDisplayName(context);
        if (displayName==null || displayName.length()==0){
            displayName = "相册";
        }
        if (mSelected.getVisibility() == View.VISIBLE) {
            mSelected.setText(displayName);
        } else {
            if (Platform.hasICS()) {
                mSelected.setAlpha(0.0f);
                mSelected.setVisibility(View.VISIBLE);
                mSelected.setText(displayName);
                mSelected.animate().alpha(1.0f).setDuration(context.getResources().getInteger(
                        android.R.integer.config_longAnimTime)).start();
            } else {
                mSelected.setVisibility(View.VISIBLE);
                mSelected.setText(displayName);
            }
        }
        if (drawableIcon==null){
            drawableIcon = mSelected.getContext().getResources().getDrawable(R.drawable.icon_title_bottom);
        }
        drawableIcon.setBounds(0, 0, drawableIcon.getMinimumWidth(), drawableIcon.getMinimumHeight());
        mSelected.setCompoundDrawables(null, null, drawableIcon, null);
        mSelected.setCompoundDrawablePadding(10);
    }

    public void setAdapter(CursorAdapter adapter) {
        mListPopupWindow.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setSelectedTextView(TextView textView) {
        mSelected = textView;
//        // tint dropdown arrow icon
//        Drawable[] drawables = mSelected.getCompoundDrawables();
//        Drawable right = drawables[2];
//        TypedArray ta = mSelected.getContext().getTheme().obtainStyledAttributes(
//                new int[]{R.attr.album_element_color});
//        int color = ta.getColor(0, 0);
//        ta.recycle();
//        right.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        mSelected.setVisibility(View.GONE);
        mSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemHeight = v.getResources().getDimensionPixelSize(R.dimen.album_item_height);
                mListPopupWindow.setHeight(mAdapter.getCount() > MAX_SHOWN_COUNT ?
                                itemHeight * MAX_SHOWN_COUNT : itemHeight * mAdapter.getCount());
                mListPopupWindow.show();
            }
        });
        mSelected.setOnTouchListener(mListPopupWindow.createDragToOpenListener(mSelected));
    }

    public void setPopupAnchorView(View view) {
        mListPopupWindow.setAnchorView(view);
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return -1;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

}
