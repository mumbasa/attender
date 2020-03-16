// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.StaffDisplay;
import org.springframework.jdbc.core.RowMapper;

public class StaffDisplayAggMapper implements RowMapper<StaffDisplay>
{
    public StaffDisplay mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final StaffDisplay d = new StaffDisplay();
        d.setStaffName((rs.getString(1) == null) ? "" : rs.getString(1).toLowerCase());
        d.setBioId(rs.getString(2));
        d.setData(rs.getInt(3));
        return d;
    }
}
