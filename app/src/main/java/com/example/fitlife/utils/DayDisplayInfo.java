package com.example.fitlife.utils;

public class DayDisplayInfo {
    public final String dayName;
    public final String date;
    public final boolean isToday;

    public DayDisplayInfo(String dayName, String date, boolean isToday) {
        this.dayName = dayName;
        this.date = date;
        this.isToday = isToday;
    }
}
