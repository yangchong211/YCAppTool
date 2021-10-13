package com.yc.mocklocationlib.gpsmock.ui;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.yc.mocklocationlib.R;
import com.yc.mocklocationlib.gpsmock.bean.SettingItem;

public class SettingItemAdapter extends AbsRecyclerAdapter<AbsViewBinder<SettingItem>, SettingItem> {
    private static final String TAG = "SettingItemAdapter";
    protected SettingItemAdapter.OnSettingItemClickListener mOnSettingItemClickListener;
    protected SettingItemAdapter.OnSettingItemSwitchListener mOnSettingItemSwitchListener;

    public SettingItemAdapter(Context context) {
        super(context);
    }

    protected AbsViewBinder<SettingItem> createViewHolder(View view, int viewType) {
        return new SettingItemAdapter.SettingItemViewHolder(view);
    }

    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_setting, parent, false);
    }

    public void setOnSettingItemClickListener(SettingItemAdapter.OnSettingItemClickListener onSettingItemClickListener) {
        this.mOnSettingItemClickListener = onSettingItemClickListener;
    }

    public void setOnSettingItemSwitchListener(SettingItemAdapter.OnSettingItemSwitchListener onSettingItemSwitchListener) {
        this.mOnSettingItemSwitchListener = onSettingItemSwitchListener;
    }

    public interface OnSettingItemSwitchListener {
        void onSettingItemSwitch(View var1, SettingItem var2, boolean var3);
    }

    public interface OnSettingItemClickListener {
        void onSettingItemClick(View var1, SettingItem var2);
    }

    public class SettingItemViewHolder extends AbsViewBinder<SettingItem> {
        private static final String TAG = "SettingItemViewHolder";
        protected TextView mDesc;
        protected TextView mSubDesc;
        protected CheckBox mMenuSwitch;
        protected ImageView mIcon;
        protected TextView mRightDesc;
        protected RadioGroup mSettingRadioGroup;

        public SettingItemViewHolder(View view) {
            super(view);
        }

        protected void getViews() {
            this.mMenuSwitch = (CheckBox)this.getView(R.id.menu_switch);
            this.mDesc = (TextView)this.getView(R.id.desc);
            this.mSubDesc = (TextView)this.getView(R.id.sub_desc);
            this.mIcon = (ImageView)this.getView(R.id.right_icon);
            this.mRightDesc = (TextView)this.getView(R.id.right_desc);
            this.mSettingRadioGroup = (RadioGroup)this.getView(R.id.item_setting_radio_group);
        }

        public void bind(final SettingItem settingItem) {
            if (settingItem.desc != 0) {
                this.mDesc.setText(settingItem.desc);
            } else {
                this.mDesc.setText(settingItem.descStr);
            }

            if (TextUtils.isEmpty(settingItem.subDescStr)) {
                this.mSubDesc.setVisibility(View.GONE);
            } else {
                this.mSubDesc.setText(settingItem.subDescStr);
                this.mSubDesc.setVisibility(View.VISIBLE);
            }

            if (settingItem.canCheck) {
                this.mMenuSwitch.setVisibility(View.VISIBLE);
                this.mMenuSwitch.setChecked(settingItem.isChecked);
                this.mMenuSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settingItem.isChecked = isChecked;
                        SettingItemAdapter.this.mOnSettingItemSwitchListener.onSettingItemSwitch(SettingItemViewHolder.this.mMenuSwitch, settingItem, isChecked);
                    }
                });
            }

            if (settingItem.icon != 0) {
                this.mIcon.setVisibility(View.VISIBLE);
                this.mIcon.setImageResource(settingItem.icon);
            }

            if (!TextUtils.isEmpty(settingItem.rightDesc)) {
                this.mRightDesc.setVisibility(View.VISIBLE);
                this.mRightDesc.setText(settingItem.rightDesc);
            }

        }

        protected void onViewClick(View view, SettingItem data) {
            super.onViewClick(view, data);
            if (SettingItemAdapter.this.mOnSettingItemClickListener != null) {
                SettingItemAdapter.this.mOnSettingItemClickListener.onSettingItemClick(view, data);
            }

        }
    }
}

