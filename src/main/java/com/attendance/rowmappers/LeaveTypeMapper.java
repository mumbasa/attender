// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Leave;
import org.springframework.jdbc.core.RowMapper;

public class LeaveTypeMapper implements RowMapper<Leave>
{
    public Leave mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Leave l = new Leave();
        l.setId(rs.getInt(1));
        l.setLeave(rs.getString(2));
        return l;
    }
}
