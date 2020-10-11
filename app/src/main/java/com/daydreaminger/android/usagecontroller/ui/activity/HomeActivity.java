package com.daydreaminger.android.usagecontroller.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.daydreaminger.android.usagecontroller.AppHolder;
import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.model.UsageInfo;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;
import com.daydreaminger.android.usagecontroller.vm.UsageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class HomeActivity extends AppBaseActivity {
    private static final String TAG = "HomeActivity";

    TextView tvDataEndTime;
    RecyclerView rvUsage;
    UsageAdapter adapter;
    List<UsageInfo> infoList = new ArrayList<>();

    UsageViewModel mUsageViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_home);
        initToolbar();
        initViews();

        mUsageViewModel = new ViewModelProvider(this).get(UsageViewModel.class);

        updateDateEndTime(System.currentTimeMillis());
        mUsageViewModel.getUsageStates()
                .observe(this, usageStatsData -> {
                    //after get data.
                    Log.i(TAG, "onChanged: " + usageStatsData.usageInfoList.size());
                    Log.i(TAG, "onChanged: " + usageStatsData.usageInfoList.toString());
                    infoList.clear();
                    infoList.addAll(usageStatsData.usageInfoList);
                    if (adapter != null) {
                        adapter.setAppInfoMap(usageStatsData.appInfoMap);
                        adapter.notifyDataSetChanged();
                    }
                });
        mUsageViewModel.asyncGetUsageState();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("屏幕时间管理");
    }

    private void initViews() {
        tvDataEndTime = findViewById(R.id.tv_data_end_time);
        rvUsage = findViewById(R.id.rv_apps);
        rvUsage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsageAdapter(this, infoList);
        rvUsage.setAdapter(adapter);
    }

    private void updateDateEndTime(long endTime) {
        tvDataEndTime.setText(getString(R.string.data_end_time, TimeUtils.format(endTime, TimeUtils.FORMAT_PATTERN_YMDHM)));
    }

    public static class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.UsageHolder> {

        private ArrayMap<String, AppUtils.AppInfo> appInfoMap;
        private List<UsageInfo> infoList;
        private LayoutInflater layoutInflater;

        public UsageAdapter(@NonNull Context context, @NonNull List<UsageInfo> list) {
            layoutInflater = LayoutInflater.from(context);
            infoList = list;
        }

        public void setAppInfoMap(ArrayMap<String, AppUtils.AppInfo> appInfo) {
            appInfoMap = appInfo;
        }

        @NonNull
        @Override
        public UsageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UsageHolder(layoutInflater.inflate(R.layout.home_item_usage_data, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull UsageHolder holder, int position) {
            UsageInfo info = infoList.get(position);
            AppUtils.AppInfo appInfo = appInfoMap.get(info.mPackageName);
            if (appInfo != null) {
                Glide.with(holder.ivAppIcon).load(appInfo.getIcon())
                        .into(holder.ivAppIcon);
                holder.tvApp.setText(appInfo.getName());
            }
            holder.tvUsage.setText(getUsageCount(info));
        }

        private String getUsageCount(UsageInfo info) {
            String value = "";

            //hour
            int hour = (int) (info.mTotalTimeInForeground / TimeUtils.TIME_HOUR);
            int minute = (int) (info.mTotalTimeInForeground % TimeUtils.TIME_HOUR / TimeUtils.TIME_MINUTE);
            int second = (int) (info.mTotalTimeInForeground % TimeUtils.TIME_MINUTE / TimeUtils.TIME_SECOND);


            value += hour == 0 ? "" : AppHolder.getAppContext().getString(R.string.format_hour, hour);
            value += minute == 0 ? "" : AppHolder.getAppContext().getString(R.string.format_minute, minute);
            value += second == 0 ? "" : AppHolder.getAppContext().getString(R.string.format_second, second);
            if (TextUtils.isEmpty(value)) {
                value = "0";
            }

            return AppHolder.getAppContext().getString(R.string.usage_use_time, value);
        }

        @Override
        public int getItemCount() {
            return infoList == null ? 0 : infoList.size();
        }

        private static class UsageHolder extends RecyclerView.ViewHolder {

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
}
