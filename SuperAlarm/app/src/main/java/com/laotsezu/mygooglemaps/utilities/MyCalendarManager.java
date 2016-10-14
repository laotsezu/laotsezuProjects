package com.laotsezu.mygooglemaps.utilities;


import java.util.Calendar;

import static java.util.Calendar.MINUTE;

/**
 * Created by Laotsezu on 10/12/2016.
 */

public class MyCalendarManager {
    private Calendar calendar;
    public MyCalendarManager(){
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
    }
    public Calendar getCalendar(){
        return calendar;
    }
    public void setHour(int hour){
        calendar.set(Calendar.HOUR_OF_DAY,hour);
    }
    public void setMinute(int minute){
        calendar.set(MINUTE,minute);
    }
    public int getHourOfDay(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public int getMinute(){
        return calendar.get(MINUTE);
    }
    public long getTimeInMillis(){
        return calendar.getTimeInMillis();
    }
    public void addDayOfMonth(int dayOfMonth){
        calendar.add(Calendar.DAY_OF_MONTH,dayOfMonth);
    }
}
