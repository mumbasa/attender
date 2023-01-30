package com.attendance.repos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.attendance.data.KeyValue;
import com.attendance.data.Overtime;
import com.attendance.data.Staff;
import com.attendance.repositories.StaffRepositoies;
import com.attendance.rowmappers.OvertimeMapper;

@Repository
public class OvertimeRepository {

	@Autowired
	JdbcTemplate template;

	@Autowired
	StaffRepositoies staffRepository;

	public int saveOvertime(long id, long dept, String reason, String date, String starttime, String entime) {
		String sql = "INSERT INTO `attendance`.`overtime` (`reason`, `staff`, `dept`, overtime_date,`date_applied`,start_time,end_time,duration) VALUES (?,?,?,?,curdate(),?,?,timediff(?,?))";
		return template.update(sql, reason, id, getStaffById(id).getDepartment().getId(), date, starttime, entime, entime, starttime);

	}

	public int decide(long id, String decide) {
		String sql = "UPDATE overtime set date_approved=curdate(),approver=? where id=?";
		return template.update(sql, decide, id);

	}

	public List<Overtime> getOvertimes(long id) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where staff=? order by id desc";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), id);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;
	}

	public List<Overtime> getOvertimesFromTo(String from, String to) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where overtime_date >=? and overtime_date <=?";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), from, to);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;
	}

	public List<Overtime> getOvertimesPending(long id) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where date_approved is null";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper());
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}

	public List<Overtime> getOvertimesDeptPending(long id) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where date_approved is null and dept=?";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), id);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}

	public List<Overtime> getOvertimesStaffPending(long id) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where date_approved is null and o.staff IN (SELECT staff_id FROM supervisors where supervisor_id=?)";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), id);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}

	public List<Overtime> getOvertimesDeptApproved(int dept) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where approver='1' and dept=?  ";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), dept);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;
	}

	public List<Overtime> getOvertimesApproved() {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where approver='1'  ";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper());

		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}

	public List<Overtime> getOvertimesDeptApprovedAgrreage(String from, String to) {
		String sql = "SELECT ( SELECT name from staff as s where s.bioid=o.staff),sum(duration) FROM attendance.overtime as o  where approver=1 and overtime_date >=? and overtime_date <=? group by staff;";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), from, to);

		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}

	public List<Overtime> getOvertimesDeptApproved(long dept, String start, String end) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where  approver='1' and dept=? and overtime_date>=? and overtime_date<=? ";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), dept, start, end);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}

	public List<KeyValue> getOvertimesApprovedAggraegate(String start, String end) {
		List<KeyValue> kvs = new ArrayList<KeyValue>();
		String sql = "SELECT ( SELECT name from staff as s where s.bioid=o.staff),sum(duration) FROM attendance.overtime as o  where  approver='1'  and overtime_date>=? and overtime_date<=?  group by o.staff ";
		SqlRowSet set = template.queryForRowSet(sql, start, end);

		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(Math.round(set.getDouble(2)));
			v.setValue(set.getString(1));
			kvs.add(v);
		}
		return kvs;

	}

	public List<KeyValue> getOvertimesMonthAggraegate() {
		List<KeyValue> kvs = new ArrayList<KeyValue>();
		String sql = "SELECT concat(monthname(overtime_date),'-',year(overtime_date)) as m,sum(duration) FROM attendance.overtime where approver=1 group by m;";
		SqlRowSet set = template.queryForRowSet(sql);

		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(Math.round(set.getDouble(2)));
			v.setValue(set.getString(1));
			kvs.add(v);
		}
		return kvs;

	}

	public List<Staff> getAllStaff() {

		return staffRepository.findAll();

	}

	public Staff getStaffByEmail(String email) {

		return staffRepository.findByEmail(email);

	}

	public Staff getStaffById(long id) {

		return staffRepository.findById(id).get();
	}

	public List<Overtime> getOvertimesDApproved(String start, String end) {
		String sql = "SELECT *,( SELECT name from staff as s where s.bioid=o.staff) FROM attendance.overtime as o  where  approver='1' and overtime_date>=? and overtime_date<=? ";
		List<Overtime> overtimes = template.query(sql, new OvertimeMapper(), start, end);
		for (Overtime ot : overtimes) {
			ot.setStaff(staffRepository.findById(ot.getStaffId()).get());

		}
		return overtimes;

	}
}
