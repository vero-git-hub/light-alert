package com.example.lightalert.ui.dashboard;

import android.graphics.Color;
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

import android.view.Gravity;

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
                displaySchedule(view, schedule);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error parsing schedule JSON", e);
            }
        }

        return view;
    }

    private void displaySchedule(View view, JSONObject schedule) throws JSONException {
        LinearLayout hoursContainer = view.findViewById(R.id.hours_container);

        for (int i = 0; i < 24; i++) {
            String startHour = String.format("%02d", i);
            String endHour = String.format("%02d", i + 1);
            String key = startHour + "-" + endHour;
            String status = schedule.optString(key);

            Log.d(TAG, "Hour: " + i + ", Status: " + status);

            LinearLayout hourLayout = new LinearLayout(getContext());
            hourLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView hourView = new TextView(getContext());
            hourView.setText(String.valueOf(i));
            hourView.setTextSize(18);
            hourView.setTextColor(Color.BLACK);
            hourView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            View statusView = new View(getContext());
            statusView.setBackgroundColor(StatusColorUtil.getStatusColor(getContext(), status));
            Log.d(TAG, "Color for status " + status + ": " + StatusColorUtil.getStatusColor(getContext(), status));

            LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            hourView.setLayoutParams(hourParams);

            LinearLayout.LayoutParams statusParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    4.0f
            );
            statusView.setLayoutParams(statusParams);

            hourLayout.addView(hourView);
            hourLayout.addView(statusView);

            LinearLayout.LayoutParams hourLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            hourLayout.setLayoutParams(hourLayoutParams);

            hoursContainer.addView(hourLayout);

            Log.d(TAG, "Added hour layout for hour: " + i);
        }

        View fakeBottomView = new View(getContext());
        LinearLayout.LayoutParams fakeBottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
        );
        fakeBottomView.setLayoutParams(fakeBottomParams);
        hoursContainer.addView(fakeBottomView);

        Log.d(TAG, "Total child count in hours container: " + hoursContainer.getChildCount());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
