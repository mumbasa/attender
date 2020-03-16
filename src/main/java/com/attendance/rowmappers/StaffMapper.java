// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.rowmappers;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.attendance.data.Staff;
import org.springframework.jdbc.core.RowMapper;

public class StaffMapper implements RowMapper<Staff>
{
    public Staff mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Staff staff = new Staff();
        staff.setId(rs.getLong(1));
        staff.setDob(rs.getString(13));
        staff.setMobile(rs.getString(9));
        staff.setGender(rs.getString(12));
        staff.setAddress(rs.getString(10));
        staff.setEmail(rs.getString(11));
        staff.setBioID(rs.getLong(6));
        staff.setName(rs.getString(3));
        staff.setCategoryId(rs.getInt(15));
        staff.setHighestQualification(rs.getString(17));
        staff.setRelationshipStatus(rs.getString(16));
        staff.setSocialSecurityNumber(rs.getString(18));
        staff.setReligion(rs.getString(19));
        staff.setStaffId(rs.getString(20));
        staff.setOtherNames(rs.getString(21));
        staff.setDepartment(rs.getString(27)+"-"+rs.getString(28));
        staff.setPicture(rs.getString(5));
        staff.setDateJoined(rs.getString(2));
        staff.setStaffType(rs.getString(27));
        staff.setNationality(rs.getString(14));
        staff.setTypeId(rs.getLong(7));
        staff.setDepartmentId(rs.getInt(4));
        staff.setTinNumber(rs.getString(23));
        staff.setAddress2(rs.getString(22));
        staff.setCategory(rs.getString(29));
        staff.setWeekendWorker(rs.getInt(8)>0?true:false);
        return staff;
    }
}
