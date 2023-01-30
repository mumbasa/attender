// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Attendances;
import org.springframework.jdbc.core.RowMapper;

public class AttendanceMapper implements RowMapper<Attendances>
{
    public Attendances mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Attendances attendance = new Attendances();
        attendance.setDate(rs.getString(8));
        attendance.setClosedEarly((rs.getString(9) == null) ? "Absent" : rs.getString(9));
        attendance.setId(rs.getString(2));
        attendance.setTimeOut((rs.getString(4) == null) ? "Absent" : rs.getString(4));
        attendance.setTimeIn((rs.getString(3) == null) ? "Absent" : rs.getString(3));
        attendance.setLabel((rs.getString(5) == null) ? "Absent" : rs.getString(5));
        attendance.setHoursWorked(rs.getLong(7));
        attendance.setDeficit(rs.getLong(12));
        attendance.setLateness(rs.getLong(6));
        return attendance;
    }
}
