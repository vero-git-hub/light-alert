package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lightalert.R;
import com.example.lightalert.ui.adapters.ScheduleAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleFragment extends Fragment {

    private String day;
    private JSONObject scheduleData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            day = getArguments().getString("day");
            try {
                scheduleData = new JSONObject(getArguments().getString("scheduleData"));
                JSONObject daySchedule = scheduleData.getJSONObject(day);
                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(getContext(), daySchedule);
                recyclerView.setAdapter(scheduleAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
