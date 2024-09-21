package com.example.lightalert.data;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ScheduleDataLoader {
    private Context context;

    public ScheduleDataLoader(Context context) {
        this.context = context;
    }

    public JSONObject loadScheduleData() {
        String jsonString = null;
        try {
            InputStream is = context.getAssets().open("schedule.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getJSONObject("schedule");
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
