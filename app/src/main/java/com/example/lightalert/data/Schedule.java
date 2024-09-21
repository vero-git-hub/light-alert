package com.example.lightalert.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Schedule {
    private HashMap<String, HashMap<String, String>> scheduleData;

    public Schedule(JSONObject jsonSchedule) {
        scheduleData = new HashMap<>();

        try {
            Iterator<String> daysIterator = jsonSchedule.keys();

            while (daysIterator.hasNext()) {
                String day = daysIterator.next();
                JSONObject dayObject = jsonSchedule.getJSONObject(day);
                HashMap<String, String> hoursMap = new HashMap<>();
                Iterator<String> hoursIterator = dayObject.keys();

                while (hoursIterator.hasNext()) {
                    String hour = hoursIterator.next();
                    String status = dayObject.getString(hour);

                    hoursMap.put(hour, status);
                }

                scheduleData.put(day, hoursMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getStatus(String day, String hour) {
        HashMap<String, String> daySchedule = scheduleData.get(day);
        if (daySchedule != null) {
            return daySchedule.get(hour);
        }
        return null;
    }
}
