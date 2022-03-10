package com.pedaily.yc.ycdialoglib.dialog.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.pedaily.yc.ycdialoglib.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : DialogAdapter
 *     revise:
 * </pre>
 */
public class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    private static final int VERTICAL = OrientationHelper.VERTICAL;
    private List<CustomItem> mItems = Collections.emptyList();
    private OnItemClickListener itemClickListener;

    private int orientation;
    private int layout;
    private Context context;

    private int padding;
    private int topPadding;
    private int leftPadding;
    private int topIcon;
    private int leftIcon;

    DialogAdapter(Context context, List<CustomItem> mItems, int layout, int orientation) {
        setList(mItems);
        this.context = context;
        this.layout = layout;
        this.orientation = orientation;

        padding = context.getResources().getDimensionPixelSize(R.dimen.app_normal_margin);
        topPadding = context.getResources().getDimensionPixelSize(R.dimen.app_tiny_margin);
        leftPadding = context.getResources().getDimensionPixelSize(R.dimen.app_normal_margin);
        topIcon = context.getResources().getDimensionPixelSize(R.dimen.bottom_dialog_top_icon);
        leftIcon = context.getResources().getDimensionPixelSize(R.dimen.bottom_dialog_left_icon);
    }

    private void setList(List<CustomItem> items) {
        mItems = items == null ? new ArrayList<CustomItem>() : items;
    }

    void setItemClick(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        notifyDataSetChanged();
    }

    public void setLayout(int layout) {
        this.layout = layout;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layout == CustomDialog.GRID) {
            return new TopHolder(new LinearLayout(parent.getContext()));
        } else if (orientation == HORIZONTAL) {
            return new TopHolder(new LinearLayout(parent.getContext()));
        } else {
            return new LeftHolder(new LinearLayout(parent.getContext()));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CustomItem item = mItems.get(position);

        TopHolder topHolder;
        LeftHolder leftHolder;

        if (layout == CustomDialog.GRID) {
            topHolder = (TopHolder) holder;

            topHolder.item.setText(item.getTitle());
            topHolder.item.setCompoundDrawablesWithIntrinsicBounds(null, topHolder.icon(item.getIcon()), null, null);
            topHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.click(item);
                    }
                }
            });
        } else if (orientation == HORIZONTAL) {
            topHolder = (TopHolder) holder;

            topHolder.item.setText(item.getTitle());
            topHolder.item.setCompoundDrawablesWithIntrinsicBounds(null, topHolder.icon(item.getIcon()), null, null);
            topHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.click(item);
                    }
                }
            });
        } else {
            leftHolder = (LeftHolder) holder;

            leftHolder.item.setText(item.getTitle());
            leftHolder.item.setCompoundDrawablesWithIntrinsicBounds(leftHolder.icon(item.getIcon()), null, null, null);
            leftHolder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.click(item);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private int getScreenWidth(Context c) {
        return c.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 顶部的
     */
    private class TopHolder extends RecyclerView.ViewHolder {

        private TextView item;

        TopHolder(View view) {
            super(view);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = getScreenWidth(context) / 5;

            item = new TextView(view.getContext());
            item.setLayoutParams(params);
            item.setMaxLines(1);
            item.setEllipsize(TextUtils.TruncateAt.END);
            item.setGravity(Gravity.CENTER);
            item.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray_dark));
            item.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.app_normal_margin));
            item.setCompoundDrawablePadding(topPadding);
            item.setPadding(0, padding, 0, padding);

            TypedValue typedValue = new TypedValue();
            view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
            item.setBackgroundResource(typedValue.resourceId);

            ((LinearLayout) view).addView(item);
        }

        private Drawable icon(Drawable drawable) {
            if (drawable != null) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                @SuppressWarnings("SuspiciousNameCombination")
                Drawable resizeIcon = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, topIcon, topIcon, true));
                Drawable.ConstantState state = resizeIcon.getConstantState();
                resizeIcon = DrawableCompat.wrap(state == null ? resizeIcon : state.newDrawable().mutate());
                return resizeIcon;
            }
            return null;
        }
    }

    /**
     * 左边的
     */
    private class LeftHolder extends RecyclerView.ViewHolder {

        private TextView item;

        LeftHolder(View view) {
            super(view);

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            view.setLayoutParams(params);
            item = new TextView(view.getContext());
            item.setLayoutParams(params);
            item.setMaxLines(1);
            item.setEllipsize(TextUtils.TruncateAt.END);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray_black));
            item.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.app_normal_margin));
            item.setCompoundDrawablePadding(leftPadding);
            item.setPadding(padding, padding, padding, padding);

            TypedValue typedValue = new TypedValue();
            view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    typedValue, true);
            item.setBackgroundResource(typedValue.resourceId);

            ((LinearLayout) view).addView(item);
        }

        private Drawable icon(Drawable drawable) {
            if (drawable != null) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                @SuppressWarnings("SuspiciousNameCombination")
                Drawable resizeIcon = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, leftIcon, leftIcon, true));
                Drawable.ConstantState state = resizeIcon.getConstantState();
                resizeIcon = DrawableCompat.wrap(state == null ? resizeIcon : state.newDrawable().mutate());
                return resizeIcon;
            }
            return null;
        }
    }

}
