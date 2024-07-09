package com.example.lightalert.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

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

    public static String getCurrentTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");
        Calendar calendar = Calendar.getInstance(timeZone);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    /**
     * Set the calendar to the beginning of the week (Monday)
     * Add 6 days to get the end of the week (Sunday)
     * @return date in format "day.month - day.month"
     */
    public static String getCurrentWeekDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = sdf.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = sdf.format(calendar.getTime());

        return startDate + " - " + endDate;
    }
}
