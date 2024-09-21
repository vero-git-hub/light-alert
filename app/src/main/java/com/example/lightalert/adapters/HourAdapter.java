package com.example.lightalert.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lightalert.R;
import com.example.lightalert.data.Schedule;

public class HourAdapter extends BaseAdapter {
    private Context context;
    private Schedule schedule;
    private String day;

    public HourAdapter(Context context, Schedule schedule, String day) {
        this.context = context;
        this.schedule = schedule;
        this.day = day;
    }

    @Override
    public int getCount() {
        return 24;
    }

    @Override
    public Object getItem(int position) {
        return String.format("%02d", position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.hour_item, parent, false);
        }

        TextView hourText = convertView.findViewById(R.id.hourText);
        View topIndicator = convertView.findViewById(R.id.topIndicator);
        View bottomIndicator = convertView.findViewById(R.id.bottomIndicator);

        String hour = String.format("%02d", position);
        hourText.setText(hour);

        String status = schedule.getStatus(day, hour);
        if (status != null) {
            String[] states = status.split(",\\s*");
            int colorTop = getColorByStatus(states[0]);
            int colorBottom = getColorByStatus(states[1]);

            topIndicator.setBackgroundColor(colorTop);
            bottomIndicator.setBackgroundColor(colorBottom);
        } else {
            topIndicator.setBackgroundColor(Color.TRANSPARENT);
            bottomIndicator.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    private int getColorByStatus(String status) {
        switch (status.trim().toLowerCase()) {
            case "light":
                return Color.parseColor("#00FF00");
            case "maybe":
                return Color.parseColor("#FFFF00");
            case "no":
                return Color.parseColor("#FF0000");
            default:
                return Color.TRANSPARENT;
        }
    }
}
