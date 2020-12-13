package com.daydreaminger.android.usagecontroller.ui;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.databinding.HomeActivitySplashBinding;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseActivity;
import com.daydreaminger.android.usagecontroller.viewmodel.ToolbarViewModel;

/**
 * Activity + 多Fragment的方式，就要考虑如何完成Activity和Fragment、以及Fragment和Fragment之间的交互；
 *
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class HomeActivity extends AppBaseActivity<HomeActivitySplashBinding> {
    private static final String TAG = "HomeActivity";

    private ToolbarViewModel mToolbarViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_home);
        initToolbar();
        mToolbarViewModel = new ViewModelProvider(this).get(ToolbarViewModel.class);
        initObserve();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
    }

    private void initObserve() {
        //update toolbar title.
        mToolbarViewModel.getTitleData().observe(this, s -> {
            if (TextUtils.isEmpty(s)) {
                s = "";
            }
            setToolbarTitle(s);
        });
    }
}
