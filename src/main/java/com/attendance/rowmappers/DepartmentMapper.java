// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Departments;
import org.springframework.jdbc.core.RowMapper;

public class DepartmentMapper implements RowMapper<Departments>
{
    public Departments mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Departments dep = new Departments();
        dep.setId(rs.getLong(1));
        dep.setDepartment(rs.getString(2));
        dep.setSize(rs.getLong(3));
        
        return dep;
    }
}
