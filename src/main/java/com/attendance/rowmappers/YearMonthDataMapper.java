// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.YearMonthData;
import org.springframework.jdbc.core.RowMapper;

public class YearMonthDataMapper implements RowMapper<YearMonthData>
{
    public YearMonthData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final YearMonthData d = new YearMonthData();
        d.setMonthId(rs.getInt(1));
        d.setYear(rs.getInt(2));
        d.setMonthName(rs.getString(3));
        d.setValueOne(rs.getLong(4));
        return d;
    }
}
