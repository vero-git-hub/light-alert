package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lightalert.R;
import com.example.lightalert.ui.adapters.ScheduleAdapter;

import org.json.JSONObject;

public class ScheduleDayFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_SCHEDULE = "schedule";

    private String day;
    private JSONObject schedule;

    public static ScheduleDayFragment newInstance(String day, JSONObject schedule) {
        ScheduleDayFragment fragment = new ScheduleDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY, day);
        args.putString(ARG_SCHEDULE, schedule.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getString(ARG_DAY);
            try {
                schedule = new JSONObject(getArguments().getString(ARG_SCHEDULE));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule_day, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ScheduleAdapter(schedule));
        return view;
    }
}
