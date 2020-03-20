// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.repos;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.attendance.services.MessageService;
import com.attendance.services.Utilities;
import java.util.HashSet;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import com.attendance.data.StaffLeave;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import com.attendance.rowmappers.LeaveTypeMapper;
import com.attendance.rowmappers.StaffLeaveMapping;
import com.attendance.rowmappers.LeaveMapping;
import com.attendance.data.Holiday;
import com.attendance.data.KeyValue;
import com.attendance.data.Leave;
import com.attendance.data.LeaveSummary;
import com.attendance.data.Staff;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class LeaveRepository {
	@Autowired
	JdbcTemplate template;
	@Autowired
	StaffRepository staffRepository;
	@Autowired
	MessageService messagerService;

	@Autowired
	HolidayRepository holidayRepository;
	
	@Value("${app.upload.file.location}")
	String UPLOAD_FOLDER;

	@Value("${spring.application.name}")
	String appName;

	public List<Leave> getLeaves() {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) FROM leaves as l";
		return template.query(sql, new LeaveMapping());
	}

	public List<Leave> getStaffLeaves(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) FROM leaves as l where l.staffid=?";
		return template.query(sql, new LeaveMapping(), id);
	}

	public List<Leave> getStaffLeavesInDept(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where staffid IN(SELECT id from staff where department=?)";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
			// System.out.println(leaveSum.getLeaveDaysSum());
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

		}
		return leaves;
	}

	public List<Leave> getStaffLeavesOfSupervisor(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where staffid IN(SELECT staff_id from supervisors where supervisor_id=?)";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
			// System.out.println(leaveSum.getLeaveDaysSum());
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

		}
		return leaves;
	}

	public List<Leave> getStaffLeavesApproved(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type),datediff(end_date,curdate())  FROM staff_leave as l  where staffid=? and hr_approve=1";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(id));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffLeavesInDeptPending(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where staffid IN(SELECT id from staff where department=?) and hod_approved is null";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffLeavesofSupertvisorPending(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where staffid IN(SELECT staff_id FROM supervisors where supervisor_id=?) and hod_approved is null";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffLeavesApplications(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where staffid=?";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffHODApprovedLeavesApplications(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=1";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffHODApprovedLeavesApplications() {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=1 and hr_approve is null";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping());
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffOnLeave() {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type),datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=1 and hr_approve =1 and end_date>=curdate() ";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping());
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));

		}
		return leaves;
	}

	public List<Leave> getMyStaffOnLeave(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type),datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=1 and hr_approve =1 and end_date>=curdate() and l.staffid IN (SELECT staff_id FROM supervisors where supervisor_id=?)";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));

		}
		return leaves;
	}

	public List<Leave> getStaffPendingLeave() {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type),datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=1 and hr_approve =1 and start_date>=curdate() ";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping());
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));

		}
		return leaves;
	}

	public List<Leave> getStaffLeaveQuery(String start, String to) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type),datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=1 and hr_approve =1 and start_date>=? and start_date <=? ";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), start, to);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));

		}
		return leaves;
	}

	public List<Leave> getStaffHORejectedLeavesApplications(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(end_date,curdate()) FROM staff_leave as l  where hod_approved=0";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffHRApprovedLeavesApplications(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type),datediff(end_date,curdate())  FROM staff_leave as l  where hr_approve=1";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));

			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<Leave> getStaffHRRejectedLeavesApplications(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) ,datediff(endDate,curdate()) FROM staff_leave as l  where hr_approve=0";
		List<Leave> leaves = template.query(sql, new StaffLeaveMapping(), id);
		for (Leave leave : leaves) {
			leave.setStaff(staffRepository.getStaffByID(leave.getStaffid()));
			LeaveSummary leaveSum = getStaffLeaveDataSummary(leave.getStaffid());
			System.out.println(leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum());
			leave.setLeaveDaysRemaining((leaveSum.getLeaveDaysSum() - leaveSum.getDaysTakenSum()));
		}
		return leaves;
	}

	public List<LeaveSummary> getLeaveDataSummary() {
		List<LeaveSummary> leaveSum = new ArrayList<LeaveSummary>();
		final String sql = "SELECT m.staffid, sum(days),(SELECT a.days from leave_arrears as a where a.staffid=m.staffid LIMIT 1),(SELECT sum(l.days) from staff_leave as l where l.staffid=m.staffid and l.type <11 and hr_approve=1), (SELECT sum(l.days) from staff_leave as l where l.staffid=m.staffid and  hr_approve=1 and l.type<11 and year(date_approved)=year(curdate())) FROM leave_mount as m group by m.staffid";
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			LeaveSummary sum = new LeaveSummary();
			sum.setLeaveDaysSum((long) set.getDouble(2) + (long) set.getDouble(3));
			sum.setStaff(staffRepository.getStaffByID(set.getLong(1)));
			sum.setDaysTakenSum((long) set.getDouble(4));
			sum.setCurrentTaken((int) set.getDouble(5));
			leaveSum.add(sum);
		}
		return leaveSum;

	}

	public LeaveSummary getStaffLeaveDataSummary(long id) {
		LeaveSummary sum = null;
		final String sql = "SELECT m.staffid, sum(days),(SELECT (a.days) from leave_arrears as a where a.staffid=? LIMIT 1),(SELECT sum((l.days)) from staff_leave as l where l.staffid=? and l.type<11 and hr_approve=1), (SELECT sum(l.days) from staff_leave as l where l.staffid=? and l.type<11 and year(date_approved)=year(curdate())) FROM leave_mount as m where m.staffid=?";
		SqlRowSet set = template.queryForRowSet(sql, id, id, id, id);
		if (set.next()) {
			System.err.println(set.getDouble(2));
			sum = new LeaveSummary();
			sum.setLeaveDaysSum((long) set.getDouble(2) + (long) set.getDouble(3));
			sum.setStaff(staffRepository.getStaffByID(id));
			sum.setDaysTakenSum((long) set.getDouble(4));
			sum.setCurrentTaken((int) set.getDouble(5));

		}
		return sum;

	}

	public List<Leave> getStaffOnLeveDAteResume() {
		final String sql = "SELECT (SELECT name from staff where id=l.staffid),max(endDate) as m , min(endDate),datediff(max(endDate),curdate()) FROM leaves  as l  where l.endDate>curdate() group by staffid;";
		List<Leave> leaves = new ArrayList<Leave>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			Leave l = new Leave();
			l.setStaff(staffRepository.getStaffByID(l.getStaffid()));

			l.setStaffName(set.getString(1));
			l.setStart(set.getString(3));
			l.setEnd(set.getString(5));
			l.setDaysMore(set.getInt(4));
			l.setStaffid(set.getLong(2));

			leaves.add(l);
		}
		return leaves;
	}

	public Map<Integer, Long> getStaffCategoryLeaves() {
		final String sql = "SELECT * FROM category_leave_days as g where id=(SELECT max(id) from category_leave_days as d where d.category_id=g.category_id) ";
		Map<Integer, Long> data = new HashMap<Integer, Long>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			data.put(set.getInt(4), set.getLong(2));
		}
		return data;
	}

	public void mountLeaves() {
		List<Staff> staff = staffRepository.getStaff(true);
		System.err.println(staff.size());
		Map<Integer, Long> days = getStaffCategoryLeaves();
		String sql = "INSERT INTO `attendance`.`leave_mount` ( `year`, `staffid`, `date`,days) VALUES (year(curdate()),?,curdate(),?)";

		// batch update with staff to get their ids and the category leave days from the
		// map named days
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				Staff employee = staff.get(arg1);
				System.err.println(days.get(employee.getCategoryId()));

				ps.setLong(1, employee.getId());
				ps.setLong(2, days.get(employee.getCategoryId()));
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return staff.size();
			}
		});

	}

	public Leave getLeaveById(long id) {
		final String sql = "SELECT *,(SELECT name from staff where id=l.staffid),(SELECT leavetype from leavetypes as t where t.id=l.type) FROM staff_leave as l  where id=?";
		Leave leaves = null;
		SqlRowSet set = template.queryForRowSet(sql, id);
		if (set.next()) {
			leaves = new Leave();
			leaves.setId(set.getLong(1));
			leaves.setStaff(staffRepository.getStaffByID(leaves.getStaffid()));

			leaves.setStaffName(set.getString(10));
			leaves.setStart(set.getString(3));
			leaves.setEnd(set.getString(5));
			leaves.setDaysMore(set.getInt(4));
			leaves.setStaffid(set.getLong(2));
			leaves.setLeaveTypeId(set.getInt(8));
		}
		return leaves;
	}

	public List<Leave> getStaffOnLeveDAteResume(long dept) {
		final String sql = "SELECT (SELECT name from staff where id=l.staffid),max(endDate) as m , min(endDate),datediff(max(endDate),curdate()) FROM leaves  as l  where l.endDate>curdate() and l.staffid IN (SELECT id from staff where department=?)group by staffid;";
		List<Leave> leaves = new ArrayList<Leave>();
		SqlRowSet set = template.queryForRowSet(sql, dept);
		while (set.next()) {
			Leave l = new Leave();
			l.setStaff(staffRepository.getStaffByID(l.getStaffid()));

			l.setStaffName(set.getString(1));
			l.setStart(set.getString(3));
			l.setEnd(set.getString(2));

			l.setDaysMore(set.getInt(4));
			leaves.add(l);
		}
		return leaves;
	}

	public List<Leave> getLeaveTypes() {
		final String sql = "SELECT * FROM leavetypes;";
		return template.query(sql, new LeaveTypeMapper());
	}

	public List<KeyValue> getStaffLeaveYears(long id) {
		List<KeyValue> value = new ArrayList<KeyValue>();
		final String sql = "SELECT year(start_date) as year,sum(days) as days FROM staff_leave where type <11 and staffid =? and hr_approve=1 group by year;";
		SqlRowSet set = template.queryForRowSet(sql, id);
		while (set.next()) {
			KeyValue key = new KeyValue();
			key.setId(set.getLong(1));
			key.setValue(set.getString(2));
			value.add(key);
		}
		return value;
	}

	public KeyValue getLeaveTypeDays(long id, long staffid) {
		KeyValue value = new KeyValue();
		String sql = null;
		SqlRowSet set = null;
		if (id != 7) {
			sql = "SELECT IFNULL((SELECT maximum_days from leavetypes as l where l.id=?),0),IFNULL(sum(days),0),IFNULL((SELECT isdeducted from leavetypes as d where d.id=?),0) FROM staff_leave as s where s.type=? and year(date_approved)=year(curdate()) and hr_approve=1 and staffid=?";
			set = template.queryForRowSet(sql, id, id, id, staffid);
		} else {
			sql = "SELECT (SELECT days from leave_mount where staffid=? and year=year(curdate())), ifnull(sum(days),0),IFNULL((SELECT isdeducted from leavetypes as d where d.id=?),0) FROM staff_leave where type IN (SELECT id from leavetypes where isdeducted=1) and staffid =? and year(date_approved)=year(curdate());";
			set = template.queryForRowSet(sql, staffid, id, staffid);

		}

		if (set.next()) {
			value = new KeyValue();
			value.setId(set.getLong(1));
			value.setQuantity((long) set.getDouble(2));
			value.setQuantity2((long) set.getDouble(3));

		}
		return value;
	}

	public int saveStaffLeave(String staffid, String startDate, String endDate, int days, int type,MultipartFile file) {
		try {
//			copyFileUsingStream(uploadfile, new File(filename));

			file.transferTo(new File(UPLOAD_FOLDER  + file.getOriginalFilename()));

		} catch (IllegalStateException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final String sql = "INSERT INTO `staff_leave` (`staffid`, `start_date`, `end_date`,`days`,type,file) VALUES (?,?,?,?,?,?)";
		return template.update(sql, staffid, startDate, endDate, days, type,file.getOriginalFilename());
	}
	
	public int saveStaffLeave(String staffid, String startDate, String endDate, int days, int type) {
	

		final String sql = "INSERT INTO `staff_leave` (`staffid`, `start_date`, `end_date`,`days`,type) VALUES (?,?,?,?,?)";
		return template.update(sql, staffid, startDate, endDate, days, type);
	}

	public int saveHODApproveLeave(long id, String decision) {
		final String sql = "UPDATE staff_leave set hod_approved=?,date_approved=curdate() where id=?";
		return template.update(sql, decision, id);
	}

	public int saveHRpproveLeave(long id, String decision) {
		final String sql = "UPDATE staff_leave set hr_approve=?,date_approved=curdate() where id=?";
		Leave leave = getLeaveById(id);
		String message = "Hello " + leave.getStaffName() + " your leave from " + leave.getStart() + " to "
				+ leave.getEnd() + " has been ";
		Staff staff = staffRepository.getStaffByID(leave.getStaffid());
		String decide = (decision.equalsIgnoreCase("1")) ? "approved " : "rejected";
		messagerService.sendSms(message + decide, staff.getMobile());
		return template.update(sql, decision, id);
	}

	public static int getMonths(final LocalDate a, final LocalDate b) {
		final int years = b.getYear() - a.getYear();
		final int month = b.getMonthValue() - a.getMonthValue();
		return years * 12 + month;
	}

	public int deleteLeave(final int leave) {
		final String sql = "DELETE FROM leaves where id=?";
		return this.template.update(sql, new Object[] { leave });
	}

	public void insertStaffLeave(final List<StaffLeave> leaves) {
		
		
		final String sql = "INSERT INTO leaves(staffid,startDate,endDate,type) VALUES(?,?,?,?)";
		this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				rs.setString(1, leaves.get(arg1).getStaffId());
				rs.setString(2, leaves.get(arg1).getFrom());
				rs.setString(3, leaves.get(arg1).getTo());
				rs.setInt(4, leaves.get(arg1).getLeaveTypeid());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return leaves.size();
			}
		});
	}

	public void insertStaffLeave(final List<StaffLeave> leaves, long leaveId) {
		final String sql = "INSERT INTO leaves(staffid,startDate,endDate,type,leave_id) VALUES(?,?,?,?,?)";
		this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				rs.setString(1, leaves.get(arg1).getStaffId());
				rs.setString(2, leaves.get(arg1).getFrom());
				rs.setString(3, leaves.get(arg1).getTo());
				rs.setInt(4, leaves.get(arg1).getLeaveTypeid());
				rs.setLong(5, leaveId);
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return leaves.size();
			}
		});
	}

	public static LocalDate getLeaveEndDate(String start, int days, boolean weekend) {

		LocalDate date = LocalDate.parse(start);
		if (weekend == false) {
			int weekendCounts = 0;
			for (int a = 0; a <= days; a++) {
				LocalDate d = date.plusDays(a);
				if (d.getDayOfWeek() == DayOfWeek.SATURDAY | d.getDayOfWeek() == DayOfWeek.SUNDAY) {
					System.err.println(true + "\t" + d.toString());
					weekendCounts++;
				}
			}
			return date.plusDays(days + weekendCounts);
		} else {
			return date.plusDays(days);
		}

	}

	public LocalDate getLeaveEndDate(String start, int days) {

		LocalDate date = LocalDate.parse(start);
		Map<String, Holiday> holidays = holidayRepository.getHolidaysMao(LocalDate.now().getYear());
		int weekendCounts = 0;
		for (int a = 0; a <= days; a++) {
			LocalDate d = date.plusDays(a);
			if (d.getDayOfWeek() == DayOfWeek.SATURDAY | d.getDayOfWeek() == DayOfWeek.SUNDAY) {
				System.err.println(true + "\t" + d.toString());
				weekendCounts++;
			}
			// checking whether the day is holiday to rollover
			if (holidays.containsKey(d.toString())) {
				weekendCounts++;
			}

		}
		date = date.plusDays(days + weekendCounts);
		while (date.getDayOfWeek() == DayOfWeek.SATURDAY | date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			date = date.plusDays(1);

		}

		return date;

	}

	public Map<String, List<String>> getLeaveIntervals(final String start, final String end) {
		final Map<String, List<String>> dates = new HashMap<String, List<String>>();
		final LocalDate d = LocalDate.parse(start);
		final LocalDate f = LocalDate.parse(end);
		final int months = getMonths(d, f);
		ArrayList<String> range = new ArrayList<String>();
		if (months == 0) {
			range.add(d.toString());
			range.add(f.toString());
			dates.put(String.valueOf(d.getYear()) + "-" + d.getMonth().name(), range);
		} else {
			for (int a = 0; a <= months; ++a) {
				range = new ArrayList<String>();
				if (a == 0) {
					range.add(d.toString());
					range.add(d.with(TemporalAdjusters.lastDayOfMonth()).toString());
					dates.put(String.valueOf(d.getYear()) + "-" + d.getMonth().name(), range);
				} else {
					final LocalDate cur = d.plusMonths(a).with(TemporalAdjusters.lastDayOfMonth());
					if (f.isAfter(cur)) {
						range.add(cur.with(TemporalAdjusters.firstDayOfMonth()).toString());
						range.add(cur.toString());
						dates.put(String.valueOf(cur.getYear()) + "-" + cur.getMonth().name(), range);
					} else {
						range.add(cur.with(TemporalAdjusters.firstDayOfMonth()).toString());
						range.add(f.toString());
						dates.put(String.valueOf(f.getYear()) + "-" + f.getMonth().name(), range);
					}
				}
			}
		}
		return dates;
	}

	public HashSet<String> getLeaveDates(final long staffid, final int month, final int year) {
		final String sql = "SELECT startDate,endDate FROM leaves where year(startDate)=? and month(startDate)=? and staffid=?";
		String sDate = "";
		String eDate = "";
		final HashSet<String> dates = new HashSet<String>();
		final SqlRowSet r = this.template.queryForRowSet(sql, new Object[] { year, month, staffid });
		while (r.next()) {
			sDate = r.getString(1);
			eDate = r.getString(2);
			dates.addAll(Utilities.getDateForLeave(sDate, eDate));
		}
		return dates;
	}
}
