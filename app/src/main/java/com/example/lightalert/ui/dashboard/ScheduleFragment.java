package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
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
import android.graphics.Color;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.TimeZone;

public class ScheduleFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_SCHEDULE = "schedule";
    private static final String TAG = "ScheduleFragment";

    private ImageView marker;
    private View timeline;
    private Handler handler = new Handler();
    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            updateMarkerPosition();
            handler.postDelayed(this, 60000);
        }
    };

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

        marker = view.findViewById(R.id.marker);
        timeline = view.findViewById(R.id.timeline);

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

        timeline.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                timeline.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateMarkerPosition();
                handler.post(updateTimeTask);
            }
        });

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
            Log.d(TAG, "Hour: " + hourStatus.getHour() + ", Top Part: " + hourStatus.getTopPart() + ", Bottom Part: " + hourStatus.getBottomPart());

            LinearLayout hourLayout = new LinearLayout(getContext());
            hourLayout.setOrientation(LinearLayout.HORIZONTAL);

            View topPartView = new View(getContext());
            topPartView.setBackgroundColor(StatusColorUtil.getColorForStatus(getContext(), hourStatus.getTopPart()));

            LinearLayout.LayoutParams topPartParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1.0f
            );
            topPartView.setLayoutParams(topPartParams);

            View bottomPartView = new View(getContext());
            bottomPartView.setBackgroundColor(StatusColorUtil.getColorForStatus(getContext(), hourStatus.getBottomPart()));

            LinearLayout.LayoutParams bottomPartParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1.0f
            );
            bottomPartView.setLayoutParams(bottomPartParams);

            TextView hourView = new TextView(getContext());
            hourView.setText(String.valueOf(hourStatus.getHour()));
            hourView.setTextSize(17);
            hourView.setTextColor(Color.BLACK);
            hourView.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.2f
            );
            hourView.setLayoutParams(hourParams);

            LinearLayout verticalContainer = new LinearLayout(getContext());
            verticalContainer.setOrientation(LinearLayout.VERTICAL);
            verticalContainer.addView(topPartView);
            verticalContainer.addView(bottomPartView);

            LinearLayout.LayoutParams verticalContainerParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.8f
            );
            verticalContainer.setLayoutParams(verticalContainerParams);

            hourLayout.addView(hourView);
            hourLayout.addView(verticalContainer);

            LinearLayout.LayoutParams hourLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    1.0f
            );
            hourLayout.setLayoutParams(hourLayoutParams);

            hoursContainer.addView(hourLayout);
        }

        View fakeBottomView = new View(getContext());
        LinearLayout.LayoutParams fakeBottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                200
        );
        fakeBottomView.setLayoutParams(fakeBottomParams);
        hoursContainer.addView(fakeBottomView);

        Log.d(TAG, "Total child count in hours container: " + hoursContainer.getChildCount());
    }

    private void updateMarkerPosition() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        float percentageOfDay = (hour * 60 + minute) / (24f * 60f);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        int parentHeight = timeline.getHeight();
        int newMarginTop = (int) (parentHeight * percentageOfDay);

        params.topMargin = newMarginTop;
        marker.setLayoutParams(params);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateTimeTask);
    }
}
