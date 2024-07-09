package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lightalert.R;

import org.json.JSONObject;
import java.util.Iterator;

public class ScheduleFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_SCHEDULE = "schedule";

    public static ScheduleFragment newInstance(String day, JSONObject schedule) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY, day);
        args.putString(ARG_SCHEDULE, schedule.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ScheduleFragment", "onCreate: " + getArguments().getString(ARG_DAY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ScheduleFragment", "onCreateView: " + getArguments().getString(ARG_DAY));
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        TextView scheduleTextView = view.findViewById(R.id.text_schedule);

        if (getArguments() != null) {
            String day = getArguments().getString(ARG_DAY);
            String scheduleString = getArguments().getString(ARG_SCHEDULE);
            try {
                Log.d("ScheduleFragment", "Day: " + day + ", Schedule: " + scheduleString);
                JSONObject schedule = new JSONObject(scheduleString);
                StringBuilder scheduleText = new StringBuilder(day + "\n\n");
                Iterator<String> keys = schedule.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = schedule.getString(key);
                    scheduleText.append(key).append(": ").append(value).append("\n");
                }
                scheduleTextView.setText(scheduleText.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ScheduleFragment", "Failed to parse schedule for " + day, e);
            }
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ScheduleFragment", "onResume: " + getArguments().getString(ARG_DAY));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ScheduleFragment", "onPause: " + getArguments().getString(ARG_DAY));
    }
}
