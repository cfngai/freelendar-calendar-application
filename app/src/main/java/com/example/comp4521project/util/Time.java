package com.example.comp4521project.util;

public class Time {

    private int hour; //0-23
    private int minute; //0-59

    public Time (int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public Time(int time) {
        this.hour = time/60;
        this.minute = time%60;
    }

    public int toInt() {
        return this.hour * 60 + this.minute;
    }

    public boolean isEarlierThan(Time otherTime) {
        if (this.hour < otherTime.hour) return true;
        if (this.hour == otherTime.hour && this.minute < otherTime.minute) return true;
        return false;
    }

    public boolean isLaterThan(Time otherTime) {
        if (this.hour > otherTime.hour) return true;
        if (this.hour == otherTime.hour && this.minute > otherTime.minute) return true;
        return false;
    }

    public boolean isEqualTo(Time otherTime) {
        if (this.hour == otherTime.hour && this.minute == otherTime.minute) return true;
        return false;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
