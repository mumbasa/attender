package com.attendance.repos;

import com.attendance.data.Holiday;
import java.util.HashSet;
import com.attendance.services.AttendanceExtractor;
import com.attendance.data.YearMonthData;
import com.attendance.repositories.StaffRepositoies;
import com.attendance.data.StaffDisplay;
import com.attendance.data.MonthAggregate;
import com.attendance.data.Staff;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.attendance.services.Utilities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import com.attendance.data.Attendances;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.attendance.dao.AttendanceDAO;

@Repository
public class AttendancesRepository implements AttendanceDAO {
	@Autowired
	JdbcTemplate template;
	@Autowired
	HolidayRepository holiRepo;
	@Autowired
	StaffRepositoies staffRepo;
	@Autowired
	LeaveRepository leaveRepo;
	@Autowired
	ShiftRepository shiftRepository;
	

	public void saveAttendance(List<Attendances> a) {
		final String sql = "INSERT INTO attendance(`staffid`,`timein`,`timeout`,`islate`,`minuteslate`,`timeworked`,`date`,`closed early`,timeinmins,timeoutmins,deficit,workhours) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement rs, int i) throws SQLException {
				// TODO Auto-generated method stub
				rs.setString(1, a.get(i).getId());
				rs.setString(2, a.get(i).getTimeIn());
				rs.setString(3, a.get(i).getTimeOut());
				rs.setString(4, a.get(i).getLabel());
				rs.setLong(5, a.get(i).getLateness());
				rs.setLong(6, a.get(i).getHoursWorked());
				rs.setString(7, a.get(i).getDate());
				rs.setString(8, a.get(i).getClosedEarly());
				rs.setLong(9, a.get(i).getTimeInMins());
				rs.setLong(10, a.get(i).getTimeOuMins());
				rs.setLong(11, a.get(i).getDeficit());
				rs.setLong(12, a.get(i).getHoursToWork());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return a.size();
			}
		});
	}

	public List<String> getStaffYearlyAggregateSummary(final int year, final long staff) {
		final List<String> data = new ArrayList<String>();
		final String sql = "SELECT sum(latecount),sum(absentcount),avg(avgtimein),sum(deficit) FROM attendance.monthlyagg where staffid=? and year=?";
		final SqlRowSet set = this.template.queryForRowSet(sql, new Object[] { staff, year });
		if (set.next()) {
			data.add(set.getString(1));
			data.add(set.getString(2));
			if (set.getString(3) != null) {
				data.add(Utilities.stringToTime(Long.parseLong(set.getString(3).split("\\.")[0])));
			} else {
				data.add("0");
			}
			data.add(set.getString(4));
		}
		return data;
	}

	public void saveAbsentees(final List<Attendances> a) {
		final String sql = "INSERT INTO attendance(`staffid`,`timeworked`,`date`,deficit) VALUES(?,?,?,?)";
		this.template.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				rs.setString(1, (a.get(arg1).getId()));
				rs.setLong(2, a.get(arg1).getHoursWorked());
				rs.setString(3, (a.get(arg1).getDate()));
				rs.setLong(4, -1*a.get(arg1).getAverageWorkingHours());
			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return a.size();
			}
		});
	}

	public int deleteAttendance(final int year, final int month) {
		return 0;
	}

	public List<Attendances> getAttendanceInMonth(final int year, final int month) {
		final String sql = "SELECT * FROM attendance.attendance where and month(date)=? and year(date)=?";
		SqlRowSet rs = template.queryForRowSet(sql,month,year);
		List<Attendances> attendances = new ArrayList<Attendances>();
		while(rs.next()) {
			Attendances attendance = new Attendances();
	        attendance.setDate(rs.getString(8));
	        attendance.setClosedEarly((rs.getString(9) == null) ? "Absent" : rs.getString(9));
	        attendance.setId(rs.getString(2));
	        attendance.setTimeOut((rs.getString(4) == null) ? "Absent" : rs.getString(4));
	        attendance.setTimeIn((rs.getString(3) == null) ? "Absent" : rs.getString(3));
	        attendance.setLabel((rs.getString(5) == null) ? "Absent" : rs.getString(5));
	        attendance.setHoursWorked(rs.getLong(7));
	        attendance.setDeficit(rs.getLong(12));
	        attendance.setLateness(rs.getLong(6));
	        attendances.add(attendance);
			
		}
		return attendances;
	}

	public List<Attendances> getStaffAttendanceInMonth(final int year, final int month, final String staff) {
		final String sql = "SELECT * FROM attendance.attendance where staffid=? and month(date)=? and year(date)=?";
		SqlRowSet rs = template.queryForRowSet(sql,staff,month,year);
		List<Attendances> attendances = new ArrayList<Attendances>();
		while(rs.next()) {
			Attendances attendance = new Attendances();
	        attendance.setDate(rs.getString(8));
	        attendance.setClosedEarly((rs.getString(9) == null) ? "Absent" : rs.getString(9));
	        attendance.setId(rs.getString(2));
	        attendance.setTimeOut((rs.getString(4) == null) ? "Absent" : rs.getString(4));
	        attendance.setTimeIn((rs.getString(3) == null) ? "Absent" : rs.getString(3));
	        attendance.setLabel((rs.getString(5) == null) ? "Absent" : rs.getString(5));
	        attendance.setHoursWorked(rs.getLong(7));
	        attendance.setDeficit(rs.getLong(12));
	        attendance.setLateness(rs.getLong(6));
	        attendances.add(attendance);
			
		}
		return attendances;
	}

	public List<String> getStaffAttendanceInMonthDays(final int year, final int month, final String staff) {
		final String sql = "SELECT date FROM attendance.attendance where staffid=? and month(date)=? and year(date)=?";
		List<String> days = new ArrayList<String>();
		SqlRowSet set = template.queryForRowSet(sql,staff, month, year);
		while(set.next()) {
			days.add(set.getString(1));
		}
		
		return  days;
	}

	public List<Attendances> getStaffAttendanceInYear( int year,  String staff) {
		final String sql = "SELECT * FROM attendance.attendance where staffid=? and year(date)=?";
		SqlRowSet rs = template.queryForRowSet(sql,staff,year);
		List<Attendances> attendances = new ArrayList<Attendances>();
		while(rs.next()) {
			Attendances attendance = new Attendances();
	        attendance.setDate(rs.getString(8));
	        attendance.setClosedEarly((rs.getString(9) == null) ? "Absent" : rs.getString(9));
	        attendance.setId(rs.getString(2));
	        attendance.setTimeOut((rs.getString(4) == null) ? "Absent" : rs.getString(4));
	        attendance.setTimeIn((rs.getString(3) == null) ? "Absent" : rs.getString(3));
	        attendance.setLabel((rs.getString(5) == null) ? "Absent" : rs.getString(5));
	        attendance.setHoursWorked(rs.getLong(7));
	        attendance.setDeficit(rs.getLong(12));
	        attendance.setLateness(rs.getLong(6));
	        attendances.add(attendance);
			
		}
		return attendances;
			
	}

	public List<MonthAggregate> getStaffYearAggregate(final int year, final long staff) {
		List<MonthAggregate>  data = new ArrayList<MonthAggregate>();
		final String sql = "SELECT * FROM attendance.monthlyagg where staffid=? and year=?";
		SqlRowSet rs =template.queryForRowSet(sql,staff,year);
		while(rs.next()) {
			MonthAggregate agg = new MonthAggregate();
	        agg.setStaffid(rs.getLong(2));
	        agg.setAvgTimeIn(Utilities.stringToTime(rs.getLong(5)));
	        agg.setAvgTimeOut(Utilities.stringToTime(rs.getLong(6)));
	        agg.setDeficit(rs.getLong(7));
	        agg.setLatenessSummary(rs.getString(8));
	        agg.setMonth(rs.getInt(9));
	        agg.setYear(rs.getInt(10));
	        agg.setLatenessount(rs.getInt(3));
	        agg.setAbsents(rs.getInt(4));
			data.add(agg);
		}
		return data;
	}
	
	
	
	public List<MonthAggregate> getDepartmentAgg( long dept) {
		List<MonthAggregate> data = new ArrayList<MonthAggregate>();
		final String sql = "SELECT concat(name,' ',othernames),gender,(SELECT type from stafftype where id=s.status),(SELECT count(*) from attendance as ad where ad.staffid=s.bioid and ad.islate='Early' group by ad.staffid), (SELECT count(*) from attendance as af where af.staffid=s.bioid and af.islate ='Late' group by af.staffid) , (SELECT count(*) from attendance as af where af.staffid=s.bioid and af.islate is null group by af.staffid) ,(SELECT sum(deficit) from attendance as af where af.staffid=s.bioid and af.islate is null group by af.staffid) , (SELECT sum(minuteslate) from attendance as af where af.staffid=s.bioid group by af.staffid),s.bioid FROM attendance.staff as s where department=?";
		SqlRowSet set= this.template.queryForRowSet(sql, dept);
		while (set.next()) {
			MonthAggregate m = new MonthAggregate();
			m.setStaffName(set.getString(1));
			m.setGender(set.getString(2));
			m.setStafftype(set.getString(3));
			m.setEarlyCount((long)set.getDouble(4));
			m.setLatenessount(set.getInt(5));
			m.setAbsents(set.getInt(6));
			m.setDeficit((long)set.getDouble(7));
			m.setLatenessMins((long)set.getDouble(8));
			m.setStaffid(set.getLong(9));
			data.add(m);
		}
		return data;
	}
	


	public List<MonthAggregate> getBranchAgg( long dept) {
		List<MonthAggregate> data = new ArrayList<MonthAggregate>();
		String sql = "SELECT name,gender,(SELECT type from stafftype where id=s.status),(SELECT count(*) from attendance as ad where ad.staffid=s.bioid and ad.islate='Early' group by ad.staffid), (SELECT count(*) from attendance as af where af.staffid=s.bioid and af.islate ='Late' group by af.staffid) , (SELECT count(*) from attendance as af where af.staffid=s.bioid and af.islate is null group by af.staffid) ,(SELECT sum(deficit) from attendance as af where af.staffid=s.bioid and af.islate is null group by af.staffid) , (SELECT sum(minuteslate) from attendance as af where af.staffid=s.bioid group by af.staffid),s.bioid FROM attendance.staff as s where department IN (SELECT id from departments where branch =?)";
		SqlRowSet set= this.template.queryForRowSet(sql, dept);
		while (set.next()) {
			MonthAggregate m = new MonthAggregate();
			m.setStaffName(set.getString(1));
			m.setGender(set.getString(2));
			m.setStafftype(set.getString(3));
			m.setEarlyCount((long)set.getDouble(4));
			m.setLatenessount(set.getInt(5));
			m.setAbsents(set.getInt(6));
			m.setDeficit((long)set.getDouble(7));
			m.setLatenessMins((long)set.getDouble(8));
			m.setStaffid(set.getLong(9));
			data.add(m);
		}
		return data;
	}
	

	
	
	public List<MonthAggregate> yearAggregate(final int year) {
		final String sql = "SELECT month, sum(latecount), sum(absentcount),sum(deficit),year,concat(floor(avg(avgtimein)/60),':',round(avg(avgtimein)%60)) FROM attendance.monthlyagg where year=? group by month";
		List<MonthAggregate> data = new ArrayList<MonthAggregate>();
		SqlRowSet set= this.template.queryForRowSet(sql, year);
		while (set.next()) {
			MonthAggregate m = new MonthAggregate();
			m.setStaffName(set.getString(1));
			m.setGender(set.getString(2));
			m.setStafftype(set.getString(3));
			m.setEarlyCount((long)set.getDouble(4));
			m.setLatenessount(set.getInt(5));
			//changed for getInt(6)
			m.setAbsents(0);
			//m.setDeficit((long)set.getDouble(7));
		//	m.setLatenessMins((long)set.getDouble(8));
			m.setStaffid(set.getLong(2));
			data.add(m);
		}
		return data;

		
		
	}

	public List<StaffDisplay> getTopLateness(final int year) {
		 String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(latecount) as t,(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t desc limit 10";
		List<StaffDisplay> displays = new ArrayList<StaffDisplay>();
		SqlRowSet rs = template.queryForRowSet(sql,year);
		while (rs.next()) {
			StaffDisplay d = new StaffDisplay();
	        d.setStaffName((rs.getString(1) == null) ? "" : rs.getString(1).toLowerCase());
	        d.setBioId(rs.getString(2));
	        d.setData(rs.getInt(3));
			displays.add(d);
		}
		 return displays;
	}

	public List<StaffDisplay> getTopAbsent(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(absentcount) as t,\n(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t desc limit 10;";
		List<StaffDisplay> displays = new ArrayList<StaffDisplay>();
		SqlRowSet rs = template.queryForRowSet(sql,year);
		while (rs.next()) {
			StaffDisplay d = new StaffDisplay();
	        d.setStaffName((rs.getString(1) == null) ? "" : rs.getString(1).toLowerCase());
	        d.setBioId(rs.getString(2));
	        d.setData(rs.getInt(3));
			displays.add(d);
		}
		 return displays;
	}

	public List<StaffDisplay> getTopDeficit(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(deficit) as t,\n(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t asc limit 10;";
		List<StaffDisplay> displays = new ArrayList<StaffDisplay>();
		SqlRowSet rs = template.queryForRowSet(sql,year);
		while (rs.next()) {
			StaffDisplay d = new StaffDisplay();
	        d.setStaffName((rs.getString(1) == null) ? "" : rs.getString(1).toLowerCase());
	        d.setBioId(rs.getString(2));
	        d.setData(rs.getInt(3));
			displays.add(d);
		}
		 return displays;
	}

	public List<StaffDisplay> getTopPerformance(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(latecount)+sum(absentcount) as t,\n(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t asc limit 10";
		List<StaffDisplay> displays = new ArrayList<StaffDisplay>();
		SqlRowSet rs = template.queryForRowSet(sql,year);
		while (rs.next()) {
			StaffDisplay d = new StaffDisplay();
	        d.setStaffName((rs.getString(1) == null) ? "" : rs.getString(1).toLowerCase());
	        d.setBioId(rs.getString(2));
	        d.setData(rs.getInt(3));
			displays.add(d);
		}
		 return displays;
	}

	public int[] getMaxDate() {
		final int[] date = new int[2];
		final String sql = "SELECT month(max(date)),year(max(date)) as d FROM attendance";
		final SqlRowSet set = this.template.queryForRowSet(sql);
		if (set.next()) {
			date[0] = set.getInt(1);
			date[1] = set.getInt(2);
		}
		return date;
	}

	public List<Attendances> getDeptAttendanceInYear(final int year, final int dept) {
		return null;
	}

	public List<YearMonthData> getDataCountFrommonth() {
		List<YearMonthData> data = new ArrayList<YearMonthData>();
		final String sql = "SELECT month(date),year(date),monthname(date), count(*) FROM attendance.attendance group by year(date),month(date),monthname(date)";
		SqlRowSet rs = template.queryForRowSet(sql);
		while(rs.next()) {
			YearMonthData d = new YearMonthData();
	        d.setMonthId(rs.getInt(1));
	        d.setYear(rs.getInt(2));
	        d.setMonthName(rs.getString(3));
	        d.setValueOne(rs.getLong(4));
			data.add(d);
		}
		return data;
	}

	public List<Attendances> getDeptAttendanceInMonth(final int year, final int month, final int dept) {
		return null;
	}

	public void populateAggregate(final int month, final int year, final long staff) {
		final String sql = "INSERT INTO monthlyagg (staffid, latecount, absentcount, avgtimein, avgtimeout,  deficit, lateness, month, year) "
				+ "SELECT staffid,(SELECT count(islate) from attendance as t where t.staffid=a.staffid and islate='Late' and month(date)="
				+ month + " and year(date)=" + year + "),"
				+ "(SELECT count(*) from attendance as b where b.staffid=a.staffid and timein is NULL and month(date)="
				+ month + " and year(date)=" + year + ")"
				+ ",round(avg(timeinmins)),round(avg(timeoutmins)),sum((deficit)),"
				+ "(SELECT Concat(round(sum(minuteslate) /480),'days ',round((sum(minuteslate) %480)/60),'hrs ',round((sum(minuteslate) %480)%60),' mins')from attendance as b where b.staffid=a.staffid and minuteslate>0)\n"
				+ "," + month + "," + year + " from attendance  as a where a.staffid=" + staff + " and year(date)="
				+ year + " and month(date)=" + month;
		System.out.println(sql);
		this.template.execute(sql);
	}

	public int deleteData(int year,  int month) {
		final String sql = "DELETE FROM attendance where year(date)=? and month(date)=?";
		final String sql2 = "DELETE FROM monthlyagg where year=? and month=?";
		return this.template.update(sql, new Object[] { year, month })
				+ this.template.update(sql2, new Object[] { year, month });
	}

	public Map<Long, Staff> getStaffMap() {
		List<Staff> staffs = staffRepo.findAll();
		Map<Long, Staff> staffData = new HashMap<Long, Staff>();
		for (Staff s : staffs) {
			staffData.put(s.getBioid(), s);
		}
		return staffData;

	}
	
	
	public List<Staff> getAllStaff() {

		return staffRepo.findAll();
	}

	public void addAttendanceBatch( String file) {
		Map<Long, Staff> staffs = getStaffMap();
		AttendanceExtractor ex = new AttendanceExtractor(file, AttendanceExtractor.Institutes.IIR,
				staffs);
		ex.extract();
		this.saveAttendance(ex.getAttendance());
		List<Long> staffID = staffRepo.findAll().stream().map(Staff::getId).toList();

		final List<Holiday> hdays = this.holiRepo.getHolidaysInMonth(ex.getMonth(), ex.getYear());
		System.err.println(hdays + "----------------------");
		final HashSet<String> days = ex.getDaysLessHolidays(hdays);
		System.err.println("______" + hdays);
		for (long staff : staffID) {
			Staff staffNow = staffs.get(staff) ;
			if (staffNow.getStatus().getType() != null) {
				final HashSet<String> stafffDays = new HashSet<String>(days);
				final HashSet<String> leaveDays = this.leaveRepo.getLeaveDates(staff, ex.getMonth(), ex.getYear());
				if (!leaveDays.isEmpty()) {
					System.out.println(String.valueOf(staff) + "\t" + leaveDays.size());
					stafffDays.removeAll(leaveDays);
				}
				final List<String> actualAttendanceInDB = this.getStaffAttendanceInMonthDays(ex.getYear(),
						ex.getMonth(), new StringBuilder(String.valueOf(staff)).toString());
				stafffDays.removeAll(actualAttendanceInDB);
				System.out.println(ex.getWeekEnds().size()+"----sdd");
			 if(!staffNow.isWeekendWorker()) {
				 stafffDays.removeAll(ex.getWeekEnds());
			 }
				
				
				final List<Attendances> absent = new ArrayList<Attendances>();
				for (final String sDays : stafffDays) {
					final Attendances a = new Attendances(new StringBuilder().append(staff).toString(), sDays, 0L,staffNow);
					a.setLabel("Absent");
					absent.add(a);
				}
				this.saveAbsentees(absent);
				this.populateAggregate(ex.getMonth(), ex.getYear(), staff);
			}
		}
	}
}
