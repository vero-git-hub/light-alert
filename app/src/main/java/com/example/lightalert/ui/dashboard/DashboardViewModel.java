package com.example.lightalert.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lightalert.util.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class DashboardViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> scheduleData = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<JSONObject> getScheduleData() {
        return scheduleData;
    }

    public void loadScheduleData(Context context) {
        try {
            String json = FileUtil.loadJSONFromAsset(context, "schedule.json");
            if (json != null) {
                JSONObject data = new JSONObject(json).getJSONObject("schedule");
                scheduleData.setValue(data);
            } else {
                scheduleData.setValue(new JSONObject());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            scheduleData.setValue(new JSONObject());
        }
    }
}