package com.daydreaminger.android.usagecontroller.ui.activity;

import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daydreaminger.android.usagecontroller.R;

/**
 * App activity 基类
 *
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class AppBaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
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
        mToolbar.setTitle(title);
    }

    protected void setToolbarTitle(Spannable title) {
        mToolbar.setTitle(title);
    }
}
