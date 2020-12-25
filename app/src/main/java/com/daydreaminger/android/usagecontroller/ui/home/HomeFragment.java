package com.daydreaminger.android.usagecontroller.ui.home;

import android.app.usage.UsageStats;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.daydreaminger.android.usagecontroller.AppHolder;
import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.databinding.HomeFragmentHomeBinding;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseFragment;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;
import com.daydreaminger.android.usagecontroller.viewmodel.UsageAnalyzerViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * home fragment page.
 *
 * @author : daydreaminger
 * @date : 2020/10/22 21:07
 */
public class HomeFragment extends AppBaseFragment<HomeFragmentHomeBinding> {
    public static final String TAG = "HomeFragment";

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    private UsageAnalyzerViewModel mAnalyzerViewModel;

    //view
    private UsageAdapter mAdapterApps;

    public HomeFragment() {
        super(R.layout.home_fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        mRootViewDataBinding.rvUsage.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapterApps = new UsageAdapter();
        mRootViewDataBinding.rvUsage.setAdapter(mAdapterApps);
        mAdapterApps.setOnItemClickListener((adapter, view1, position) -> {
            //start next fragment.
            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_detailFragment);
        });
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mToolbarViewModel.setTitleData("屏幕时间管理");
        mToolbarViewModel.setUpperIconStatusData(false);

        mAnalyzerViewModel = mViewModelProvider.get(UsageAnalyzerViewModel.class);
        mAnalyzerViewModel.getAnalyzeResult().observe(getViewLifecycleOwner(), usageStats -> {
            //update view
            mAdapterApps.setInfoList(usageStats);
            mAdapterApps.notifyDataSetChanged();
        });
        mAnalyzerViewModel.getTodayAppUsage();
    }

    public static class UsageAdapter extends BaseQuickAdapter<UsageStats, UsageHolder> {

        public UsageAdapter() {
            this(null);
        }

        public UsageAdapter(List<UsageStats> list) {
            super(R.layout.home_item_usage_data, list == null ? new ArrayList<>() : list);
        }

        public void setInfoList(@NonNull List<UsageStats> list) {
            setList(list);
        }

        @Override
        protected void convert(@NotNull UsageHolder holder, UsageStats info) {
            AppUtils.AppInfo appInfo = AppUtils.getAppInfo(info.getPackageName());
            if (appInfo != null) {
                Glide.with(holder.ivAppIcon).load(appInfo.getIcon())
                        .into(holder.ivAppIcon);
                holder.tvApp.setText(appInfo.getName());
            }
            holder.tvUsage.setText(getUsageCount(info));
        }

        private String getUsageCount(UsageStats info) {
            String value = "";

            //hour
            int hour = (int) (info.getTotalTimeInForeground() / TimeUtils.TIME_HOUR);
            int minute = (int) (info.getTotalTimeInForeground() % TimeUtils.TIME_HOUR / TimeUtils.TIME_MINUTE);
            int second = (int) (info.getTotalTimeInForeground() % TimeUtils.TIME_MINUTE / TimeUtils.TIME_SECOND);


            value += hour == 0 ? "" : AppHolder.getAppContext().getString(R.string.format_hour, hour);
            value += minute == 0 ? "" : AppHolder.getAppContext().getString(R.string.format_minute, minute);
            value += second == 0 ? "" : AppHolder.getAppContext().getString(R.string.format_second, second);
            if (TextUtils.isEmpty(value)) {
                value = "0";
            }

            return AppHolder.getAppContext().getString(R.string.usage_use_time, value);
        }
    }

    protected static class UsageHolder extends BaseViewHolder {

        ImageView ivAppIcon;
        TextView tvApp, tvUsage;

        public UsageHolder(@NonNull View itemView) {
            super(itemView);
            ivAppIcon = itemView.findViewById(R.id.iv_icon);
            tvApp = itemView.findViewById(R.id.tv_app);
            tvUsage = itemView.findViewById(R.id.tv_usage);
        }
    }
}
