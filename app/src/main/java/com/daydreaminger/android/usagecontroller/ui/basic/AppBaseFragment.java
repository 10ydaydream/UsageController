package com.daydreaminger.android.usagecontroller.ui.basic;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * @author : daydreaminger
 * @date : 2020/10/22 21:05
 */
public class AppBaseFragment<T extends ViewDataBinding> extends Fragment {
    private static final String TAG = "BaseFragment";

    /**
     * instance created after method {@link #onViewCreated(View, Bundle)} execute.
     */
    protected T rootViewDataBinding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootViewDataBinding = DataBindingUtil.bind(view);
    }
}
