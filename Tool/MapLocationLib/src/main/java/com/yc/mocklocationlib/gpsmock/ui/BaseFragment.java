package com.yc.mocklocationlib.gpsmock.ui;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;


import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class BaseFragment extends Fragment {

    private View mRootView;
    private int mContainer;

    public BaseFragment() {
    }

    @LayoutRes
    protected int onRequestLayout() {
        return 0;
    }

    public final <T extends View> T findViewById(@IdRes int id) {
        return this.mRootView.findViewById(id);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int id = this.onRequestLayout();
        if (id > 0) {
            this.mRootView = inflater.inflate(id, container, false);
        }

        if (this.mRootView == null) {
            this.mRootView = this.onCreateView(savedInstanceState);
        }
        return this.mRootView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.tryGetContainerId();

        try {
            if (view.getContext() instanceof Activity) {
                ((Activity)view.getContext()).getWindow().getDecorView().requestLayout();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void tryGetContainerId() {
        if (this.mRootView != null) {
            View parent = (View)this.mRootView.getParent();
            if (parent != null) {
                this.mContainer = parent.getId();
            }
        }

    }

    protected View onCreateView(Bundle savedInstanceState) {
        return this.mRootView;
    }

    public int getContainer() {
        if (this.mContainer == 0) {
            this.tryGetContainerId();
        }

        return this.mContainer;
    }

    public void finish() {
        Objects.requireNonNull(getActivity()).finish();
    }

}

