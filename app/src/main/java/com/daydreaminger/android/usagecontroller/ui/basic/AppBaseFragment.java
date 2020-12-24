package com.daydreaminger.android.usagecontroller.ui.basic;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.daydreaminger.android.usagecontroller.viewmodel.ToolbarViewModel;

/**
 * @author : daydreaminger
 * @date : 2020/10/22 21:05
 */
public class AppBaseFragment<T extends ViewDataBinding> extends Fragment {
    private static final String TAG = "BaseFragment";

    /**
     * instance created after method {@link #onViewCreated(View, Bundle)} execute.
     */
    protected T mRootViewDataBinding;

    protected ViewModelProvider mViewModelProvider;
    protected ToolbarViewModel mToolbarViewModel;


    public AppBaseFragment() {
        super();
    }

    @ContentView
    public AppBaseFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootViewDataBinding = DataBindingUtil.bind(view);
        initViewModel();
    }

    protected void initViewModel() {
        mViewModelProvider = new ViewModelProvider(getActivity());
        mToolbarViewModel = mViewModelProvider.get(ToolbarViewModel.class);
    }
}
