// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<User>
{
    public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final User u = new User();
        u.setId(rs.getLong(1));
        u.setDepartment(rs.getString(10));
        u.setEmail(rs.getString("email"));
        u.setName(rs.getString("name"));
        u.setRole(rs.getString(9));
        u.setStatus(rs.getInt(8));
        u.setPassword(rs.getString("password"));
        return u;
    }
}
