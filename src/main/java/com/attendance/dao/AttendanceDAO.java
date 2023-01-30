// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.dao;

import com.attendance.data.Attendances;
import java.util.List;

public interface AttendanceDAO
{
    void saveAttendance(final List<Attendances> a);
    
    int deleteAttendance(final int year, final int month);
    
    List<Attendances> getAttendanceInMonth(final int year, final int month);
    
    List<Attendances> getStaffAttendanceInMonth(final int year, final int month, final String staff);
    
    List<Attendances> getStaffAttendanceInYear(final int year, final String staff);
    
    List<Attendances> getDeptAttendanceInYear(final int year, final int dept);
    
    List<Attendances> getDeptAttendanceInMonth(final int year, final int month, final int dept);
}
