package com.yc.photo.internal.ui;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.yc.photo.internal.entity.Album;
import com.yc.photo.internal.entity.Item;
import com.yc.photo.internal.entity.SelectionSpec;
import com.yc.photo.internal.model.AlbumMediaCollection;
import com.yc.photo.internal.ui.adapter.PreviewPagerAdapter;
import com.yc.statusbar.bar.StateAppBar;

import java.util.ArrayList;
import java.util.List;


public class AlbumPreviewActivity extends BasePreviewActivity implements
        AlbumMediaCollection.AlbumMediaCallbacks {

    public static final String EXTRA_ALBUM = "extra_album";
    public static final String EXTRA_ITEM = "extra_item";
    private AlbumMediaCollection mCollection = new AlbumMediaCollection();

    private boolean mIsAlreadySetPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SelectionSpec.getInstance().hasInited || getIntent()==null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        StateAppBar.translucentStatusBar(this, true);
        mCollection.onCreate(this, this);
        if (getIntent()!=null){
            Album album = getIntent().getParcelableExtra(EXTRA_ALBUM);
            mCollection.load(album);
            Item item = getIntent().getParcelableExtra(EXTRA_ITEM);
            if (mSpec.countable) {
                mCheckView.setCheckedNum(mSelectedCollection.checkedNumOf(item));
            } else {
                mCheckView.setChecked(mSelectedCollection.isSelected(item));
            }
            updateSize(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCollection.onDestroy();
    }

    @Override
    public void onAlbumMediaLoad(Cursor cursor) {
        List<Item> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            items.add(Item.valueOf(cursor));
        }
        //cursor.close();

        if (items.isEmpty()) {
            return;
        }

        PreviewPagerAdapter adapter = (PreviewPagerAdapter) mPager.getAdapter();
        if (adapter!=null && getIntent()!=null){
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
            if (!mIsAlreadySetPosition) {
                //onAlbumMediaLoad is called many times..
                mIsAlreadySetPosition = true;
                Item selected = getIntent().getParcelableExtra(EXTRA_ITEM);
                int selectedIndex = items.indexOf(selected);
                mPager.setCurrentItem(selectedIndex, false);
                mPreviousPos = selectedIndex;
            }
        }

    }

    @Override
    public void onAlbumMediaReset() {

    }
}
