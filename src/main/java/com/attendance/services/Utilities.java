package com.attendance.services;

import java.time.Period;
import java.time.LocalDate;
import java.util.HashSet;

public class Utilities
{
    public static HashSet<String> getDateForLeave(final String a, final String b) {
        final HashSet<String> daysData = new HashSet<String>();
        final LocalDate d = LocalDate.parse(a.replace("/", "-"));
        final LocalDate f = LocalDate.parse(b.replace("/", "-"));
        for (int days = Period.between(d, f).getDays(), i = 0; i <= days; ++i) {
            final LocalDate n = d.plusDays(i);
            if (n.getDayOfWeek().getValue() < 6) {
                daysData.add(n.toString());
            }
        }
        return daysData;
    }
    
    public static HashSet<String> getDateinDays(final String a, final String b) {
        final HashSet<String> daysData = new HashSet<String>();
        final LocalDate d = LocalDate.parse(a.replace("/", "-"));
        final LocalDate f = LocalDate.parse(b.replace("/", "-"));
        for (int days = Period.between(d, f).getDays(), i = 0; i <days; ++i) {
            final LocalDate n = d.plusDays(i);
            
                daysData.add(n.toString());
           
        }
        return daysData;
    }
    
    public static long stringToMinutes(final String time) {
        final String[] times = time.split(":");
        return Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
    }
    
    public static String stringToTime(final long time) {
        return String.valueOf(String.format("%2s", new StringBuilder().append(time / 60L).toString()).replace(" ", "0").replace("-", "")) + ":" + String.format("%2s", new StringBuilder().append(time % 60L).toString()).replace(" ", "0").replace("-", "");
    }
    
    public static String hourPadding(final String time) {
        final String[] times = time.split(":");
        final String hour = String.format("%2s", times[0]).replace(" ", "0");
        return String.valueOf(hour) + ":" + times[1] + ":" + "00";
    }
}
