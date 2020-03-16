// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Leave;
import org.springframework.jdbc.core.RowMapper;

public class LeaveMapping implements RowMapper<Leave>
{
    public Leave mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Leave leave = new Leave();
        leave.setId(rs.getInt(1));
        leave.setLeave(rs.getString(9));
        leave.setStaffName(rs.getString(8));
        leave.setStart(rs.getString(3));
        leave.setEnd(rs.getString(4));
        return leave;
    }
}
