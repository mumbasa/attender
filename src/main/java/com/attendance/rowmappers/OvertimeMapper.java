package com.attendance.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.attendance.data.Overtime;

public class OvertimeMapper implements RowMapper<Overtime>{

	@Override
	public Overtime mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		Overtime overtime = new Overtime();
		overtime.setId(rs.getLong(1));
		overtime.setDate(rs.getString(8));
		overtime.setStart(rs.getString(9));
		overtime.setEnd(rs.getString(10));
		overtime.setStaffId(rs.getLong(3));
		overtime.setApprovalDate(rs.getString(6));
		overtime.setDecision(rs.getInt(7)==0?"Rejected":"Approved");
		overtime.setReason(rs.getString(2));
		overtime.setName(rs.getString(12));
		String[] times = rs.getString(11).split(":");
		int minutes=0;
		if(times.length==3) {
			int hours= Integer.parseInt(times[0])*60;
			int mins= Integer.parseInt(times[1]);
			minutes=mins+hours;
		}else {
			minutes=Integer.parseInt(times[0])*60;
		}
	overtime.setDuration(minutes);
		return overtime;
	}

}
