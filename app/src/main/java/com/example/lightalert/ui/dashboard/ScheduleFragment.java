package com.example.lightalert.ui.dashboard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.lightalert.R;
import com.example.lightalert.util.StatusColorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.TimeZone;

public class ScheduleFragment extends Fragment {

    private static final String ARG_DAY = "day";
    private static final String ARG_SCHEDULE = "schedule";
    private static final String ARG_CURRENT_DAY_INDEX = "current_day_index";
    private static final String TAG = "ScheduleFragment";

    private ImageView marker;
    private View timeline;
    private View horizontalLine;
    private Handler handler = new Handler();
    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            updateMarkerPosition();
            handler.postDelayed(this, 60000);
        }
    };

    public static ScheduleFragment newInstance(String day, JSONObject schedule, int currentDayIndex, int position) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY, day);
        args.putString(ARG_SCHEDULE, schedule.toString());
        args.putInt(ARG_CURRENT_DAY_INDEX, currentDayIndex);
        args.putInt("position", position);
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
        horizontalLine = view.findViewById(R.id.horizontal_line);

        if (getArguments() != null) {
            String day = getArguments().getString(ARG_DAY);
            String scheduleString = getArguments().getString(ARG_SCHEDULE);
            int currentDayIndex = getArguments().getInt(ARG_CURRENT_DAY_INDEX);
            int position = getArguments().getInt("position");

            if (position != currentDayIndex) {
                marker.setVisibility(View.GONE);
                timeline.setVisibility(View.GONE);
                horizontalLine.setVisibility(View.GONE);
            }

            try {
                JSONObject schedule = new JSONObject(scheduleString);
                List<HourStatus> hourStatuses = parseSchedule(schedule);
                displaySchedule(view, hourStatuses);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error parsing schedule JSON", e);
            }
        }

        if (marker.getVisibility() == View.VISIBLE) {
            timeline.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    timeline.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    updateMarkerPosition();
                    handler.post(updateTimeTask);
                }
            });
        }

        return view;
    }

    private List<HourStatus> parseSchedule(JSONObject schedule) throws JSONException {
        List<HourStatus> hourStatuses = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
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
        LinearLayout colorsContainer = view.findViewById(R.id.colors_container);

        for (HourStatus hourStatus : hourStatuses) {
            LinearLayout hourLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.schedule_item, null);
            TextView timeTextView = hourLayout.findViewById(R.id.timeTextView);
            timeTextView.setText(String.format("%02d:00", hourStatus.getHour()));

            View topPartView = hourLayout.findViewById(R.id.topPartView);
            topPartView.setBackgroundColor(StatusColorUtil.getColorForStatus(getContext(), hourStatus.getTopPart()));

            View bottomPartView = hourLayout.findViewById(R.id.bottomPartView);
            bottomPartView.setBackgroundColor(StatusColorUtil.getColorForStatus(getContext(), hourStatus.getBottomPart()));

            hoursContainer.addView(hourLayout);
        }

        Space bottomSpace = new Space(getContext());
        LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                125
        );
        bottomSpace.setLayoutParams(spaceParams);
        hoursContainer.addView(bottomSpace);
    }

    private void updateMarkerPosition() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kiev"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        float percentageOfHour = minute / 60f;

        LinearLayout hoursContainer = getView().findViewById(R.id.hours_container);
        int totalChildren = hoursContainer.getChildCount();

        int containerHeight = timeline.getHeight();
        if (containerHeight == 0) {
            return;
        }

        int elementHeight = totalChildren > 0 ? containerHeight / totalChildren : 0;

        if (elementHeight > 0) {
            int hourHeight = hour * elementHeight;
            int minuteHeight = (int) (percentageOfHour * elementHeight);

            int newMarginTop = hourHeight + minuteHeight;
            newMarginTop -= 60;

            ConstraintLayout.LayoutParams markerParams = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
            markerParams.leftMargin = 20;
            markerParams.topMargin = newMarginTop - 10;
            marker.setLayoutParams(markerParams);

            ConstraintLayout.LayoutParams lineParams = (ConstraintLayout.LayoutParams) horizontalLine.getLayoutParams();
            lineParams.topMargin = newMarginTop;
            lineParams.leftMargin = 20;
            lineParams.width = 810;
            horizontalLine.setLayoutParams(lineParams);
        } else {
            Log.e(TAG, "Element height is zero or less, cannot update marker position.");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(updateTimeTask);
    }
}