package com.example.lightalert.ui.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lightalert.ui.dashboard.ScheduleFragment;

import org.json.JSONObject;

import java.util.List;

public class SchedulePagerAdapter extends FragmentStateAdapter {

    private final JSONObject scheduleData;
    private final List<String> daysOfWeek;

    public SchedulePagerAdapter(@NonNull FragmentActivity fragmentActivity, JSONObject scheduleData, List<String> daysOfWeek) {
        super(fragmentActivity);
        this.scheduleData = scheduleData;
        this.daysOfWeek = daysOfWeek;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String day = daysOfWeek.get(position);
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString("day", day);
        args.putString("scheduleData", scheduleData.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return daysOfWeek.size();
    }
}
