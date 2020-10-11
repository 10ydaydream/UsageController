package com.daydreaminger.android.usagecontroller.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        mUsageViewModel.getUsageStates()
                .observe(this, usageWrappers -> {
                    //after get data.
                    Log.i(TAG, "onChanged: " + usageWrappers.size());
                    Log.i(TAG, "onChanged: " + usageWrappers.toString());
                    infoList.clear();
                    infoList.addAll(usageWrappers);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("屏幕时间管理");
    }

    private void initViews() {
        rvUsage = findViewById(R.id.rv_apps);
        rvUsage.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsageAdapter(this, infoList);
        rvUsage.setAdapter(adapter);
    }

    public static class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.UsageHolder> {

        private Context context;
        private List<UsageInfo> infoList;
        private LayoutInflater layoutInflater;

        public UsageAdapter(@NonNull Context context, @NonNull List<UsageInfo> list) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            infoList = list;
        }

        @NonNull
        @Override
        public UsageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UsageHolder(layoutInflater.inflate(R.layout.home_item_usage_data, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull UsageHolder holder, int position) {
            UsageInfo info = infoList.get(position);
            holder.tvApp.setText(info.mAppInfo.getName());
            holder.tvUsage.setText(getUsageCount(info));
        }

        private String getUsageCount(UsageInfo info) {
            String value = "";

            //hour
            int hour = (int) (info.mTotalTimeInForeground / TimeUtils.TIME_HOUR);
            int minute = (int) (info.mTotalTimeInForeground % TimeUtils.TIME_HOUR / TimeUtils.TIME_MINUTE);
            int second = (int) (info.mTotalTimeInForeground % TimeUtils.TIME_MINUTE / TimeUtils.TIME_SECOND);


            value += hour == 0 ? "" : context.getString(R.string.format_hour, hour);
            value += minute == 0 ? "" : context.getString(R.string.format_minute, minute);
            value += second == 0 ? "" : context.getString(R.string.format_second, second);
            if (TextUtils.isEmpty(value)) {
                value = "N/A";
            }

            return value;
        }

        @Override
        public int getItemCount() {
            return infoList == null ? 0 : infoList.size();
        }

        private static class UsageHolder extends RecyclerView.ViewHolder {

            TextView tvApp, tvUsage;

            public UsageHolder(@NonNull View itemView) {
                super(itemView);
                tvApp = itemView.findViewById(R.id.tv_app);
                tvUsage = itemView.findViewById(R.id.tv_usage);
            }
        }
    }
}
