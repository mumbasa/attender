// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Leave;
import org.springframework.jdbc.core.RowMapper;

public class StaffLeaveMapping implements RowMapper<Leave>
{
    public Leave mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Leave leave = new Leave();
        leave.setId(rs.getLong(1));
        leave.setLeave(rs.getString(11));
        leave.setDaysMore(rs.getInt(12));
        leave.setLeaveTypeId(rs.getInt(6));
        leave.setStaffName(rs.getString(10));
        leave.setStaffid(rs.getLong(2));
        leave.setStart(rs.getString(3));
        leave.setEnd(rs.getString(5));
        leave.setDays(rs.getInt(4));
        leave.setApprovalDate(rs.getString(7));
        return leave;
    }
}
