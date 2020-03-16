// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

import java.time.LocalDate;

public class Holiday
{
    private String day;
    private String hDate;
    private String holiday;
    private String realHoliday;
    private String realDay;
    private int year;
    private long id;
    private String type;
    private String from;
    private String to;
    
    public String getDay() {
        return this.day;
    }
    
    public Holiday(final String date, final String day, final String holiday) {
        this.hDate = date;
        this.day = day;
        this.holiday = holiday;
        this.setRealHoliday();
    }
    
    public Holiday() {
    }
    
    public Holiday(final String date, final String realDate, final String holiday, final String type, final int year) {
        this.hDate = date;
        this.realHoliday = realDate;
        this.holiday = holiday;
        this.year = year;
        this.type = type;
    }
    
    public void setDay(final String day) {
        this.day = day;
    }
    
    public String getDate() {
        return this.hDate;
    }
    
    public String getHoliday() {
        return this.holiday;
    }
    
    public void setHoliday(final String holiday) {
        this.holiday = holiday;
    }
    
    public String getRealHoliday() {
        return this.realHoliday;
    }
    
    public String getRealDay() {
        return this.realDay;
    }
    
    public void setRealHoliday() {
        LocalDate date = LocalDate.parse(this.hDate);
        if (date.getDayOfWeek().getValue() == 6) {
            date = date.plusDays(2L);
        }
        else if (date.getDayOfWeek().getValue() == 7) {
            date = date.plusDays(1L);
        }
        this.realHoliday = date.toString();
        this.realDay = date.getDayOfWeek().toString().toLowerCase();
        this.year = date.getYear();
    }
    
    public int getYear() {
        return this.year;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getFrom() {
        return this.from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public void setYear(final int year) {
        this.year = year;
    }
    
    public String getTo() {
        return this.to;
    }
    
    public void setTo(final String to) {
        this.to = to;
    }
    
    public void setRealDay(final String realDay) {
        this.realDay = realDay;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
}
