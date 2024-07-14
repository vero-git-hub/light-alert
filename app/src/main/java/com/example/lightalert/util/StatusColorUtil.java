package com.example.lightalert.util;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.example.lightalert.R;

public class StatusColorUtil {

    public static int getColorForStatus(Context context, String status) {
        if (status.equals(context.getString(R.string.status_light_on))) {
            return ContextCompat.getColor(context, R.color.pastel_green);
        } else if (status.equals(context.getString(R.string.status_light_off))) {
            return ContextCompat.getColor(context, R.color.pastel_red);
        } else if (status.equals(context.getString(R.string.status_possible_shutdown))) {
            return ContextCompat.getColor(context, R.color.pastel_yellow);
        } else {
            return Color.TRANSPARENT;
        }
    }
}
