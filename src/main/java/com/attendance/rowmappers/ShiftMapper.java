package com.attendance.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.attendance.data.ShiftType;

public class ShiftMapper  implements RowMapper<ShiftType>{

	@Override
	public ShiftType mapRow(ResultSet arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		ShiftType type = new ShiftType();
		type.setId(arg0.getLong(1));
		type.setStart(arg0.getLong(2));
		type.setClose(arg0.getLong(3));
		type.setShiftType(arg0.getString(6));
		type.setHours(arg0.getLong(5));
		type.setStartString(arg0.getString(7));
		type.setCloseString(arg0.getString(8));
	
		return type;
	}

}
