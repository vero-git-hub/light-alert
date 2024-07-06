package com.example.lightalert.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightalert.R;
import com.example.lightalert.util.StatusColorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final JSONObject schedule;
    private final String[] times;
    private final String[] statuses;
    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_PADDING = 1;
    private final Context context;

    public ScheduleAdapter(Context context, JSONObject schedule) {
        this.context = context;
        this.schedule = schedule;
        int size = schedule.length();
        this.times = new String[size + 1]; // +1 for a fake element (padding from footer)
        this.statuses = new String[size + 1];
        Iterator<String> keys = schedule.keys();
        int index = 0;
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                this.times[index] = key;
                this.statuses[index] = schedule.getString(key);
                Log.d("ScheduleAdapter", "Read time: " + key + ", status: " + schedule.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            index++;
        }
        // Adding a fake element
        this.times[size] = "";
        this.statuses[size] = "";
    }

    @Override
    public int getItemViewType(int position) {
        if (position == times.length - 1) {
            return VIEW_TYPE_PADDING;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = new View(parent.getContext());
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) parent.getContext().getResources().getDimension(R.dimen.bottom_padding)
            );
            view.setLayoutParams(layoutParams);
            return new PaddingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.timeTextView.setText(times[position]);
            itemHolder.statusTextView.setText(statuses[position]);
            Log.d("ScheduleAdapter", "Binding view for time: " + times[position] + ", status: " + statuses[position]);

            itemHolder.statusTextView.setBackgroundColor(StatusColorUtil.getStatusColor(context, statuses[position]));
        }
    }

    @Override
    public int getItemCount() {
        return times.length;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;
        TextView statusTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }

    static class PaddingViewHolder extends RecyclerView.ViewHolder {
        PaddingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
