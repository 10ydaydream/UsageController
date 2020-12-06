package com.daydreaminger.android.usagecontroller.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.daydreaminger.android.usagecontroller.AppHolder;
import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.model.UsageInfo;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseFragment;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;
import com.daydreaminger.android.usagecontroller.viewmodel.UsageViewModel;

import java.util.List;

/**
 * @author : daydreaminger
 * @date : 2020/10/22 21:07
 */
public class HomeFragment extends AppBaseFragment {
    public static final String TAG = "HomeFragment";

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ViewModelProvider(getActivity()).get(UsageViewModel.class).calDayTotalUsage();
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
