package com.example.lightalert.ui.dashboard;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.List;
import org.json.JSONObject;

public class SchedulePagerAdapter extends FragmentStateAdapter {

    private final List<String> days;
    private final JSONObject scheduleData;
    private int currentDayIndex;

    public SchedulePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> days, JSONObject scheduleData, int currentDayIndex) {
        super(fragmentActivity);
        this.days = days;
        this.scheduleData = scheduleData;
        this.currentDayIndex = currentDayIndex;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String day = days.get(position);
        JSONObject daySchedule = scheduleData.optJSONObject(day);
        if (daySchedule == null) {
            daySchedule = new JSONObject();
        }

        return ScheduleFragment.newInstance(day, daySchedule, currentDayIndex, position);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }
}
