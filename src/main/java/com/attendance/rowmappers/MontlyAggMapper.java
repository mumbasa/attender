// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import com.attendance.services.Utilities;
import java.sql.ResultSet;
import com.attendance.data.MonthAggregate;
import org.springframework.jdbc.core.RowMapper;

public class MontlyAggMapper implements RowMapper<MonthAggregate>
{
    public MonthAggregate mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final MonthAggregate agg = new MonthAggregate();
        agg.setStaffid(rs.getLong(2));
        agg.setAvgTimeIn(Utilities.stringToTime(rs.getLong(5)));
        agg.setAvgTimeOut(Utilities.stringToTime(rs.getLong(6)));
        agg.setDeficit(rs.getLong(7));
        agg.setLatenessSummary(rs.getString(8));
        agg.setMonth(rs.getInt(9));
        agg.setYear(rs.getInt(10));
        agg.setLatenessount(rs.getInt(3));
        agg.setAbsents(rs.getInt(4));
        return agg;
    }
}
