package com.attendance.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.attendance.data.Kin;

public class EmergencyMapper implements RowMapper<Kin>{

	@Override
	public Kin mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		Kin kin = new Kin();
		kin.setId(rs.getLong(1));
		kin.setName(rs.getString(2));
		kin.setContact(rs.getString(3));

		kin.setDateAdded(rs.getString(4));
		return kin;
	}

}
