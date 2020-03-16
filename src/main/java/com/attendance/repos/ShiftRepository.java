package com.attendance.repos;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.attendance.data.Shift;
import com.attendance.data.ShiftType;
import com.attendance.services.Utilities;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

@Repository
public class ShiftRepository {

	@Autowired
	JdbcTemplate template;

	@Autowired
	LeaveRepository leaveRepository;
	
	@Autowired
	StaffRepository staffRepository;

	public int saveUnwantedShift(JSONArray array, String staffid) {
		List<JSONObject> objs = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			System.out.println(obj);
			if (!obj.isNull("end")) {
				HashSet<String> dates = Utilities.getDateinDays(obj.getString("start").substring(0, 10),
						obj.getString("end").substring(0, 10));
				System.out.println(dates.size());
				for (String date : dates) {
					JSONObject o = new JSONObject();
					o.append("start", date);
					o.append("title", obj.getString("title"));
					objs.add(o);
					System.out.println(date);

				}
			} else {
				System.err.println("yes");
				objs.add(obj);
			}

		}

		// getting map to map shiftype to the array submited by full calendar
		Map<String, ShiftType> shifts = getShiftTypes();
		String sql = "INSERT INTO `date_prefs` (`date`, `staffid`, `shift_type`, `start`, `end`) VALUES (?,?,?,?,?)";
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				// getting calendar events from json uploaded
				JSONObject event = objs.get(arg1);
				String shift = event.getString("title");
				ps.setString(1, event.getString("start").substring(0, 10));
				ps.setString(2, staffid);

				if (!shift.equals("All Day")) {
					ps.setLong(3, shifts.get(shift).getId());
					ps.setString(4, shifts.get(shift).getStartString());
					ps.setString(5, shifts.get(shift).getCloseString());
				} else {

					ps.setLong(3, 0);
					ps.setString(4, "00:00");
					ps.setString(5, "23:59");
				}

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return objs.size();
			}
		});
		return 1;

	}

	public int saveStaffShift(JSONArray array, String staffid) {
		List<JSONObject> objs = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			
			//removng dates in the database from the calendar dates array submitted. the : character separates the new from the old
			if(obj.getString("title").contains(":")) {
				System.out.println(obj);

			if (!obj.isNull("end")) {
				HashSet<String> dates = Utilities.getDateinDays(obj.getString("start").substring(0, 10),
						obj.getString("end").substring(0, 10));
				System.out.println(dates.size());
				for (String date : dates) {
					JSONObject o = new JSONObject();
					o.append("start", date);
					o.append("title", obj.getString("title"));
					objs.add(o);
					System.out.println(date);

				}
			} else {
				System.err.println("yes");
				objs.add(obj);
			}

		}}

		// getting map to map shiftype to the array submited by full calendar
		Map<String, ShiftType> shifts = getShiftTypes();
		String sql = "INSERT INTO `staff_shift` (`date`, `staffid`, `shift_type`, `start`, `end`) VALUES (?,?,?,?,?)";
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				// getting calendar events from json uploaded
				JSONObject event = objs.get(arg1);
				String shift = event.getString("title").replace(":", "");
				ps.setString(1, event.getString("start").substring(0, 10));
				ps.setString(2, staffid);

				if (!shift.equals("Off Day")) {

					ps.setLong(3, shifts.get(shift).getId());
					ps.setString(4, shifts.get(shift).getStartString());
					ps.setString(5, shifts.get(shift).getCloseString());
				} else {
					leaveRepository.saveStaffLeave(staffid, event.getString("start").substring(0, 10),
							event.getString("start").substring(0, 10), 1, 13);

					ps.setLong(3, 0);
					ps.setString(4, "00:00");
					ps.setString(5, "23:59");
				}

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return objs.size();
			}
		});
		return 1;

	}

	public Map<String, ShiftType> getShiftTypes() {
		String sql = "SELECT * FROM worktimes where staff_type=2";
		Map<String, ShiftType> shifts = new HashMap<String, ShiftType>();

		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			ShiftType types = new ShiftType();
			types.setId(set.getLong(1));
			types.setStartString(set.getString(7));
			types.setCloseString(set.getString(8));
			types.setShiftType(set.getString(6));
			types.setColour(set.getString(9));
			shifts.put(set.getString(6), types);
			
		}
		return shifts;
	}

	public ShiftType getShiftType(int id) {
		String sql = "SELECT * FROM worktimes where id=?";
		ShiftType shiftType = new ShiftType();
		SqlRowSet set = template.queryForRowSet(sql, id);
		while (set.next()) {

			shiftType.setId(set.getLong(1));
			shiftType.setShiftType(set.getString(6));
			shiftType.setStartString(set.getString(7));
			shiftType.setCloseString(set.getString(8));
			shiftType.setColour(set.getString(9));

		}

		return shiftType;

	}
	public void saveStaffShifts(int shiftType,List<String> staff,String date) {
		String sql ="INSERT INTO `staff_shift` (`date`, `staffid`, `shift_type`, `start`, `end`) VALUES (?,?,?,?,?)";
		ShiftType t = getShiftType(shiftType);
	
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, date);
				ps.setString(2, staff.get(i));
				ps.setInt(3, shiftType);
				ps.setString(4, t.getStartString());
				ps.setString(5, t.getCloseString());
				
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return staff.size();
			}
		});
		
	}

	public List<Shift> getStaffUnavbailableShifts(long staff) {
		String sql = "SELECT * FROM date_prefs where staffid=?";
		List<Shift> shifts = new ArrayList<Shift>();
		SqlRowSet set = template.queryForRowSet(sql, staff);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setId(set.getLong(1));
			shift.setDate(set.getString(2));
			shift.setStaff(staffRepository.getStaffByID(set.getLong(3)));

			shift.setType(getShiftType(set.getInt(4)));
			shifts.add(shift);
		}
		return shifts;

	}
	
	
	public List<Shift> getStaffShifts(String from,String end) {
		String sql = "SELECT * FROM date_prefs where date between ? and ?";
		List<Shift> shifts = new ArrayList<Shift>();
		SqlRowSet set = template.queryForRowSet(sql, from,end);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setStaff(staffRepository.getStaffByID(set.getLong(3)));
			shift.setId(set.getLong(1));
			shift.setDate(set.getString(2));
			shift.setType(getShiftType(set.getInt(4)));
			shifts.add(shift);
		}
		return shifts;

	}

	
	public List<Shift> getStaffShifts(long staff) {
		String sql = "SELECT * FROM staff_shift where staffid=?";
		List<Shift> shifts = new ArrayList<Shift>();
		SqlRowSet set = template.queryForRowSet(sql, staff);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setId(set.getLong(1));
			shift.setStaff(staffRepository.getStaffByID(staff));
			shift.setDate(set.getString(2));
			shift.setType(getShiftType(set.getInt(4)));
			shifts.add(shift);
		}
		return shifts;

	}
	
	public Map<String,Shift> getStaffShiftsMap(long staff) {
		String sql = "SELECT * FROM staff_shift where staffid=?";
		 Map<String,Shift> shifts = new HashMap<String, Shift>();
		SqlRowSet set = template.queryForRowSet(sql, staff);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setId(set.getLong(1));
			shift.setStaff(staffRepository.getStaffByID(staff));
			shift.setDate(set.getString(2));
			shift.setType(getShiftType(set.getInt(4)));
			shifts.put(set.getString(2),shift);
		}
		return shifts;

	}
	
	
	public List<Shift> getDeptShifts(long dept,String start,String end) {
		String sql = "SELECT * FROM staff_shift where staffid IN (SELECT bioid from staff where department=?) and date>=? and date<=?";
		List<Shift> shifts = new ArrayList<Shift>();
		SqlRowSet set = template.queryForRowSet(sql, dept,start,end);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setId(set.getLong(1));
			shift.setStaff(staffRepository.getStaffByID(set.getLong(3)));

			shift.setDate(set.getString(2));
			shift.setType(getShiftType(set.getInt(4)));
			shifts.add(shift);
		}
		return shifts;

	}
	
	public List<Shift> getDeptShifts(long dept) {
		String sql = "SELECT * FROM staff_shift where staffid IN (SELECT bioid from staff where department=?)";
		List<Shift> shifts = new ArrayList<Shift>();
		SqlRowSet set = template.queryForRowSet(sql, dept);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setId(set.getLong(1));
			shift.setStaff(staffRepository.getStaffByID(set.getLong(3)));

			shift.setDate(set.getString(2));
			shift.setType(getShiftType(set.getInt(4)));
			shifts.add(shift);
		}
		return shifts;

	}
	
	
	public List<Shift> getDeptShifts() {
		String sql = "SELECT * FROM staff_shift where staffid";
		List<Shift> shifts = new ArrayList<Shift>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			Shift shift = new Shift();
			shift.setId(set.getLong(1));
			shift.setStaff(staffRepository.getStaffByID(set.getLong(3)));

			shift.setDate(set.getString(2));
			shift.setType(getShiftType(set.getInt(4)));
			shifts.add(shift);
		}
		return shifts;

	}
	
	public int deletePrefDateShift(long id) {
	String sql="DELETE FROM date_prefs where id=?";
	return template.update(sql,id);
	}
		
	public int deleteShift(long id) {
		String sql="DELETE FROM staff_shift where id=?";
		return template.update(sql,id);
		}

}
