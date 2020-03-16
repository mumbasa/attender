// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.dao;

import com.attendance.data.Holiday;
import java.util.List;

public interface HolidayDAO
{
    List<Holiday> getHolidays(final int year);
    
    List<Holiday> getHolidaysInMonth(final int month, final int year);
    
    void addHolidays(final List<Holiday> holidays);
    
    int addHoliday(final Holiday holiday);
    
    void addBreaks(final String t, final String f, final String type, final String name);
    
    int deleteHoliday(final int holiday);
    
    int updateHoliday(final Holiday holiday);
    
    boolean checkDataExist(final int year);
}
