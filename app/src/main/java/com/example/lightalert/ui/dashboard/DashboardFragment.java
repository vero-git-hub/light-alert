package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lightalert.R;
import com.example.lightalert.ui.adapters.SchedulePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private ViewPager2 viewPager;
    private SchedulePagerAdapter schedulePagerAdapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);

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
                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> tab.setText(daysOfWeek.get(position))
                ).attach();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return root;
    }
}