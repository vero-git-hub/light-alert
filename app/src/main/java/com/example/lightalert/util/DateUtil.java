package com.example.lightalert.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String getCurrentWeekRange() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = dateFormat.format(calendar.getTime());

        return startDate + " - " + endDate;
    }

    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek + 5) % 7;
    }

    public static boolean isCurrentTime(String timeRange) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        String[] parts = timeRange.split("-");
        if (parts.length == 2) {
            int startHour = Integer.parseInt(parts[0]);
            int endHour = Integer.parseInt(parts[1]);

            if (endHour == 0) {
                endHour = 24;
            }

            return currentHour >= startHour && currentHour < endHour;
        }

        return false;
    }

    public static int getCurrentDayIndex() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }
}
