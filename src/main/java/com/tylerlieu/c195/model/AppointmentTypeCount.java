package com.tylerlieu.c195.model;

import java.time.Month;

/**
 * Represents a record that shows the number of appointments from each type and month.
 * Specifically used for one of the three report tables.
 * Simplified to be easy to display in a TableView.
 */
public class AppointmentTypeCount {
    /**
     * Year of counting
     */
    private final int year;
    /**
     * Month of counting
     */
    private final Month month;
    /**
     * Appointment type being counted
     */
    private final String type;
    /**
     * Total number of appointments of this type on this month of this year
     */
    private final int total;

    /**
     * Constructs an AppointmentTypeCount record
     * @param year Year of counting
     * @param month Month of counting
     * @param type Type being counted
     * @param total Total number of appointments
     */
    public AppointmentTypeCount(int year, int month, String type, int total) {
        this.year = year;
        this.month = Month.of(month);
        this.type = type;
        this.total = total;
    }

    /**
     * Returns this AppointmentTypeCount's year
     * @return AppointmentTypeCount's year
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns this AppointmentTypeCount's month
     * @return AppointmentTypeCount's month
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Returns this AppointmentTypeCount's type
     * @return AppointmentTypeCount's type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns this AppointmentTypeCount's total
     * @return AppointmentTypeCount's total
     */
    public int getTotal() {
        return total;
    }
}
