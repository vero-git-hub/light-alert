package com.example.lightalert.ui.dashboard;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lightalert.util.FileUtil;

import org.json.JSONObject;

public class DashboardViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> mSchedule;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        mSchedule = new MutableLiveData<>();
        loadSchedule(application);
    }

    public LiveData<JSONObject> getSchedule() {
        return mSchedule;
    }

    private void loadSchedule(Application application) {
        new Thread(() -> {
            try {
                JSONObject json = FileUtil.loadJSONFromAsset(application, "schedule.json");
                mSchedule.postValue(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}