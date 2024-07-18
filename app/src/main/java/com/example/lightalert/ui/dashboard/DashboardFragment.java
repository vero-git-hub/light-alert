package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lightalert.databinding.FragmentDashboardBinding;
import com.example.lightalert.R;
import com.example.lightalert.util.ColorUtil;
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
    private Handler handler;
    private Runnable updateDayTask;

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
                    SchedulePagerAdapter adapter = new SchedulePagerAdapter(getActivity(), days, scheduleData, currentDayIndex);
                    ViewPager2 viewPager = binding.viewPager;
                    viewPager.setAdapter(adapter);

                    // Set the current tab to today of the week
                    viewPager.setCurrentItem(currentDayIndex, false);
                    if (currentDayIndex == viewPager.getCurrentItem()) {
                        String currentTime = DateUtil.getCurrentTime();
                    }

                    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            String selectedDay = days.get(position);

                            if (position == currentDayIndex) {
                                String currentTime = DateUtil.getCurrentTime();
                            } else {
                                Log.d("DashboardFragment", "Selected day is not today.");
                            }
                        }
                    });

                    TabLayout tabLayout = binding.tabLayout;
                    new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(days.get(position))).attach();

                    ColorUtil.applyHighlightToCurrentDay(tabLayout, currentDayIndex);
                }
            }
        });

        handler = new Handler();
        updateDayTask = new Runnable() {
            @Override
            public void run() {
                int newDayIndex = DateUtil.getCurrentDayIndex();
                if (newDayIndex != currentDayIndex) {
                    currentDayIndex = newDayIndex;
                    applyHighlightToCurrentDay(binding.tabLayout);
                    binding.viewPager.setCurrentItem(currentDayIndex, true);
                }
                handler.postDelayed(this, 60000);
            }
        };
        handler.post(updateDayTask);

        return root;
    }

    private void applyHighlightToCurrentDay(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.view != null) {
                if (i == currentDayIndex) {
                    tab.view.setBackgroundResource(R.drawable.tab_highlight);
                } else {
                    tab.view.setBackgroundResource(0);
                }
            }
        }
    }

    private void setCurrentWeekDates() {
//        String weekDates = DateUtil.getCurrentWeekDates();
//        TextView weekDatesTextView = binding.textWeekDates;
//        weekDatesTextView.setText(weekDates);
//        weekDatesTextView.setPadding(0, 24, 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateDayTask);
        binding = null;
    }
}