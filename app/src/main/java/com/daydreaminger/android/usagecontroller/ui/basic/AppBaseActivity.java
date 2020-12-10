package com.daydreaminger.android.usagecontroller.ui.basic;

import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;

import com.daydreaminger.android.usagecontroller.R;

/**
 * App activity 基类
 *
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public abstract class AppBaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected T rootViewDataBinding;
    protected Toolbar mToolbar;
    protected FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
    }

    protected void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            return;
        }

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    protected void setToolbarTitle(@StringRes int title) {
        mToolbar.setTitle(title);
    }

    protected void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    protected void setToolbarTitle(Spannable title) {
        if (mToolbar != null) mToolbar.setTitle(title);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        rootViewDataBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        rootViewDataBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
