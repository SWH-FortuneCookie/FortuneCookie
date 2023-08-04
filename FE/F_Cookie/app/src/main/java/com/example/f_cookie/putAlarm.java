package com.example.f_cookie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class putAlarm {
    @SerializedName("name")
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("days")
    public List days;
    public List getDays() {
        return days;
    }
    public void setDays(List days) {
        this.days = days;
    }

    @SerializedName("hour")
    public int hour;
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }

    @SerializedName("minute")
    public int minute;
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public putAlarm(String name, List days, int hour, int minute) {
        this.name = name;
        this.days = days;
        this.hour = hour;
        this.minute = minute;
    }
}
