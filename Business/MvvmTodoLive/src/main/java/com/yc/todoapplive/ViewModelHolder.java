

package com.yc.todoapplive;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Non-UI Fragment used to retain ViewModels.
 */
public class ViewModelHolder<VM> extends Fragment {

    private VM mViewModel;

    public ViewModelHolder() { }

    public static <M> ViewModelHolder createContainer(@NonNull M viewModel) {
        ViewModelHolder<M> viewModelContainer = new ViewModelHolder<>();
        viewModelContainer.setViewModel(viewModel);
        return viewModelContainer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable public VM getViewmodel() {
        return mViewModel;
    }

    public void setViewModel(@NonNull VM viewModel) {
        mViewModel = viewModel;
    }
}
