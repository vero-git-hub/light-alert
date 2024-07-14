package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lightalert.R;
import com.example.lightalert.util.StatusColorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_SCHEDULE = "schedule";
    private static final String TAG = "ScheduleFragment";

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        if (getArguments() != null) {
            String day = getArguments().getString(ARG_DAY);
            String scheduleString = getArguments().getString(ARG_SCHEDULE);
            try {
                JSONObject schedule = new JSONObject(scheduleString);
                List<HourStatus> hourStatuses = parseSchedule(schedule);
                displaySchedule(view, hourStatuses);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error parsing schedule JSON", e);
            }
        }

        return view;
    }

    private List<HourStatus> parseSchedule(JSONObject schedule) throws JSONException {
        List<HourStatus> hourStatuses = new ArrayList<>();

        for (int i = 0; i <= 24; i++) {
            String startHour = String.format("%02d", i);
            String endHour = String.format("%02d", i + 1);
            String key = startHour + "-" + endHour;
            String status = schedule.optString(key, "none");

            String topPart = "none";
            String bottomPart = "none";

            if (schedule.has(key)) {
                bottomPart = schedule.optString(key);
            }

            String prevKey = String.format("%02d", i - 1) + "-" + startHour;
            if (schedule.has(prevKey)) {
                topPart = schedule.optString(prevKey);
            }

            hourStatuses.add(new HourStatus(i, topPart, bottomPart));
        }

        return hourStatuses;
    }

    private void displaySchedule(View view, List<HourStatus> hourStatuses) {
        LinearLayout hoursContainer = view.findViewById(R.id.hours_container);

        for (HourStatus hourStatus : hourStatuses) {
            View hourView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_item, hoursContainer, false);

            TextView timeTextView = hourView.findViewById(R.id.timeTextView);
            View topPartView = hourView.findViewById(R.id.topPartView);
            View bottomPartView = hourView.findViewById(R.id.bottomPartView);

            timeTextView.setText(String.valueOf(hourStatus.getHour()));

            int topColor = StatusColorUtil.getColorForStatus(getContext(), hourStatus.getTopPart());
            topPartView.setBackgroundColor(topColor);

            int bottomColor = StatusColorUtil.getColorForStatus(getContext(), hourStatus.getBottomPart());
            bottomPartView.setBackgroundColor(bottomColor);

            hoursContainer.addView(hourView);
        }

        View fakeBottomView = new View(getContext());
        LinearLayout.LayoutParams fakeBottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200
        );
        fakeBottomView.setLayoutParams(fakeBottomParams);
        hoursContainer.addView(fakeBottomView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
