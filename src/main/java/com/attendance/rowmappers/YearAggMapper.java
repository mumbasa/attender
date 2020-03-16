// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.MonthAggregate;
import org.springframework.jdbc.core.RowMapper;

public class YearAggMapper implements RowMapper<MonthAggregate>
{
    public MonthAggregate mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final MonthAggregate agg = new MonthAggregate();
        agg.setMonth(rs.getInt(1));
        agg.setLatenessount(rs.getInt(2));
        agg.setYear(rs.getInt(5));
        agg.setAbsents(rs.getInt(3));
        agg.setDeficit(rs.getLong(4));
        agg.setAvgTimeIn(rs.getString(6));
        return agg;
    }
}
