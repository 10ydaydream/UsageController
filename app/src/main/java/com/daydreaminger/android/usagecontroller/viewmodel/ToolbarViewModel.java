package com.daydreaminger.android.usagecontroller.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * control the host activity toolbar status.
 *
 * @author : daydreaminger
 * @date : 2020/12/10 20:30
 */
public class ToolbarViewModel extends ViewModel {
    private static final String TAG = "ToolbarViewModel";

    private final MutableLiveData<String> titleData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> upperIconStatusData = new MutableLiveData();

    public LiveData<String> getTitleData() {
        return titleData;
    }

    public LiveData<Boolean> getUpperIconStatus() {
        return upperIconStatusData;
    }

    public void setTitleData(String title) {
        titleData.setValue(title);
    }

    public void setUpperIconStatusData(boolean status) {
        upperIconStatusData.setValue(status);
    }
}
