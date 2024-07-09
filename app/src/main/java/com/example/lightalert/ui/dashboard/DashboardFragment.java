package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lightalert.databinding.FragmentDashboardBinding;
import com.example.lightalert.R;
import com.example.lightalert.util.DateUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private List<String> days;
    private int currentDayIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        days = Arrays.asList(getResources().getStringArray(R.array.days_of_week));
        currentDayIndex = DateUtil.getCurrentDayIndex();

        setCurrentWeekDates();

        dashboardViewModel.loadScheduleData(getContext());

        dashboardViewModel.getScheduleData().observe(getViewLifecycleOwner(), new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject scheduleData) {
                if (scheduleData != null) {
                    SchedulePagerAdapter adapter = new SchedulePagerAdapter(getActivity(), days, scheduleData);
                    ViewPager2 viewPager = binding.viewPager;
                    viewPager.setAdapter(adapter);

                    Log.d("DashboardFragment", "Today: " + currentDayIndex);
                    // Set the current tab to today of the week
                    viewPager.setCurrentItem(currentDayIndex, false);
                    if (currentDayIndex == viewPager.getCurrentItem()) {
                        String currentTime = DateUtil.getCurrentTime();
                        Log.d("DashboardFragment", "Initial load - Current time: " + currentTime);
                    }

                    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            String selectedDay = days.get(position);
                            Log.d("DashboardFragment", "Selected day: " + selectedDay);

                            if (position == currentDayIndex) {
                                Log.d("DashboardFragment", "Selected day is today.");

                                String currentTime = DateUtil.getCurrentTime();
                                Log.d("DashboardFragment", "Current time: " + currentTime);
                            } else {
                                Log.d("DashboardFragment", "Selected day is not today.");
                            }
                        }
                    });

                    TabLayout tabLayout = binding.tabLayout;
                    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(days.get(position))).attach();
                }
            }
        });

        return root;
    }

    private void setCurrentWeekDates() {
        String weekDates = DateUtil.getCurrentWeekDates();
        TextView weekDatesTextView = binding.textWeekDates;
        weekDatesTextView.setText(weekDates);
        weekDatesTextView.setPadding(0, 24, 0, 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}