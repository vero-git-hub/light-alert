package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lightalert.R;
import com.example.lightalert.ui.adapters.SchedulePagerAdapter;
import com.example.lightalert.util.DateUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private ViewPager2 viewPager;
    private SchedulePagerAdapter schedulePagerAdapter;
    private TabLayout tabLayout;
    private TextView weekRangeTextView;
    private TextView clockTextView;
    private TextView notesTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        weekRangeTextView = root.findViewById(R.id.weekRangeTextView);
        clockTextView = root.findViewById(R.id.clockTextView);
        notesTextView = root.findViewById(R.id.notesTextView);

        String weekRange = DateUtil.getCurrentWeekRange();
        weekRangeTextView.setText(weekRange);

        int currentDayIndex = DateUtil.getDayOfWeek();

        dashboardViewModel.getSchedule().observe(getViewLifecycleOwner(), scheduleJson -> {
            try {
                JSONObject scheduleData = scheduleJson.getJSONObject("schedule");
                List<String> daysOfWeek = new ArrayList<>();
                Iterator<String> keys = scheduleData.keys();
                while (keys.hasNext()) {
                    daysOfWeek.add(keys.next());
                }

                schedulePagerAdapter = new SchedulePagerAdapter(requireActivity(), scheduleData, daysOfWeek);
                viewPager.setAdapter(schedulePagerAdapter);

                setDayOfWeek(currentDayIndex);

                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setText(daysOfWeek.get(position))
                ).attach();

                updateCurrentDaySchedule(scheduleData, daysOfWeek.get(currentDayIndex));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return root;
    }

    private void updateCurrentDaySchedule(JSONObject scheduleData, String currentDay) {
        try {
            JSONObject currentDaySchedule = scheduleData.getJSONObject(currentDay);
            StringBuilder clockBuilder = new StringBuilder();
            StringBuilder notesBuilder = new StringBuilder();

            Iterator<String> keys = currentDaySchedule.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                clockBuilder.append(key).append("\n");
                notesBuilder.append(currentDaySchedule.getString(key)).append("\n");
            }

            clockTextView.setText(clockBuilder.toString().trim());
            notesTextView.setText(notesBuilder.toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Setting the current day of the week in TabLayout and ViewPager:
     * - setting the current page in ViewPager;
     * - scroll to current tab in TabLayout.
     * @param currentDayIndex
     */
    private void setDayOfWeek(int currentDayIndex) {
        viewPager.setCurrentItem(currentDayIndex, false);
        tabLayout.setScrollPosition(currentDayIndex, 0f, true);
    }
}