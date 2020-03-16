// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Holiday;
import org.springframework.jdbc.core.RowMapper;

public class HolidayMapper implements RowMapper<Holiday>
{
    public Holiday mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Holiday h = new Holiday(rs.getString(3), rs.getString(4), rs.getString(2), rs.getString(7), rs.getInt(6));
        h.setId(rs.getLong(1));
        return h;
    }
}
