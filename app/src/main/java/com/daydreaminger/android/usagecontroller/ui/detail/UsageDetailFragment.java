package com.daydreaminger.android.usagecontroller.ui.detail;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.databinding.UsagedetailFragmentMianBinding;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseFragment;

/**
 * @author : daydreaminger
 * @date : 2020/12/18 22:30
 */
public class UsageDetailFragment extends AppBaseFragment<UsagedetailFragmentMianBinding> {
    private static final String TAG = "UsageDetailFragment";


    public UsageDetailFragment() {
        super(R.layout.usagedetail_fragment_mian);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mToolbarViewModel.setTitleData("使用详情");

    }
}
