package com.example.lightalert.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.example.lightalert.R;
import com.example.lightalert.adapters.HourAdapter;
import com.example.lightalert.data.Schedule;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DayFragment extends Fragment {
    private String day;
    private Schedule schedule;
    private ListView hourListView;
    private View currentTimeLine;

    public static DayFragment newInstance(String day, Schedule schedule) {
        DayFragment fragment = new DayFragment();
        fragment.day = day;
        fragment.schedule = schedule;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day, container, false);
        hourListView = view.findViewById(R.id.hourListView);
        currentTimeLine = view.findViewById(R.id.currentTimeLine);

        HourAdapter adapter = new HourAdapter(getContext(), schedule, day);
        hourListView.setAdapter(adapter);

        updateCurrentTimeLine();

        startTimer();

        return view;
    }

    private void updateCurrentTimeLine() {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        String[] days = getResources().getStringArray(R.array.days_of_week);
        if (days[currentDayOfWeek].equals(day)) {
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            int position = currentHour;
            View itemView = hourListView.getChildAt(position);

            if (itemView != null) {
                float itemTop = itemView.getTop();
                float itemHeight = itemView.getHeight();
                float offset = itemHeight * (currentMinute / 60f);

                currentTimeLine.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) currentTimeLine.getLayoutParams();
                params.topMargin = (int) (itemTop + offset);
                currentTimeLine.setLayoutParams(params);
            }
        } else {
            currentTimeLine.setVisibility(View.GONE);
        }
    }

    private void startTimer() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    updateCurrentTimeLine();
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);
    }
}
