// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.dao;

import com.attendance.data.Attendance;
import java.util.List;

public interface AttendanceDAO
{
    void saveAttendance(final List<Attendance> a);
    
    int deleteAttendance(final int year, final int month);
    
    List<Attendance> getAttendanceInMonth(final int year, final int month);
    
    List<Attendance> getStaffAttendanceInMonth(final int year, final int month, final String staff);
    
    List<Attendance> getStaffAttendanceInYear(final int year, final String staff);
    
    List<Attendance> getDeptAttendanceInYear(final int year, final int dept);
    
    List<Attendance> getDeptAttendanceInMonth(final int year, final int month, final int dept);
}
