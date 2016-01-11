/**
 * Copyright (C) 2013 by
 * Mathias Markl and Kerrim Abd El Hamed
 *
 * This program is free software!
 * You are allowed to redistribute it and/or modify it
 * under the terms of the GNU General Public License, version 2.
 * For details of the GNU General Public License see
 * http://www.gnu.org/licenses/gpl-2.0.html
 */

package at.mukprojects.vuniapp.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Model eines TaskDates.
 * 
 * @author Mathias
 */
public class TaskDate implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer           year;
    private Integer           month;
    private Integer           day;
    private Integer           hour;
    private Integer           minute;

    public TaskDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public TaskDate(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * @param year
     *            the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @param month
     *            the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @param day
     *            the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * @param hour
     *            the hour to set
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * @param minute
     *            the minute to set
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Date getDate() {
        if (hour == null || minute == null) {
            return new Date(year, month, day);
        }
        return new Date(year, month, day, hour, minute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return day + "/" + month + "/" + year + "  " + hour + ":" + minute;
    }

}
