package com.attendance.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.attendance.data.Education;

public class EducationMapper implements RowMapper<Education> {

	@Override
	public Education mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		Education edu = new Education();
		edu.setId(rs.getLong(1));
		edu.setInstitution(rs.getString(2));
		edu.setStart(rs.getString(4));
		edu.setEnd(rs.getString(5));
		edu.setDateAdded(rs.getString(6));
		edu.setCetification(rs.getString(3));
		return edu;
	}
	

}
