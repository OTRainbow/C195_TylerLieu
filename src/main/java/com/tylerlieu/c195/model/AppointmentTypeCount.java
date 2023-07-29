package com.tylerlieu.c195.model;

import java.time.Month;

public class AppointmentTypeCount {
    private int year;
    private Month month;
    private String type;
    private int total;
    public AppointmentTypeCount(int year, int month, String type, int total) {
        this.year = year;
        this.month = Month.of(month);
        this.type = type;
        this.total = total;
    }

    public int getYear() {
        return year;
    }

    public Month getMonth() {
        return month;
    }

    public String getType() {
        return type;
    }

    public int getTotal() {
        return total;
    }
}
