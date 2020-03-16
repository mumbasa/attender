package com.attendance.repos;

import com.attendance.data.Holiday;
import java.util.HashSet;
import com.attendance.services.AttendanceExtractor;
import com.attendance.rowmappers.YearMonthDataMapper;
import com.attendance.data.YearMonthData;
import com.attendance.rowmappers.StaffDisplayAggMapper;
import com.attendance.data.StaffDisplay;
import com.attendance.rowmappers.YearAggMapper;
import com.attendance.rowmappers.MontlyAggMapper;
import com.attendance.data.MonthAggregate;
import com.attendance.data.Staff;
import com.attendance.rowmappers.AttendanceMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.attendance.services.Utilities;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import com.attendance.data.Attendance;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.attendance.dao.AttendanceDAO;

@Repository
public class AttendanceRepository implements AttendanceDAO {
	@Autowired
	JdbcTemplate template;
	@Autowired
	HolidayRepository holiRepo;
	@Autowired
	StaffRepository staffRepo;
	@Autowired
	LeaveRepository leaveRepo;

	public void saveAttendance(List<Attendance> a) {
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

	public void saveAbsentees(final List<Attendance> a) {
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

	public List<Attendance> getAttendanceInMonth(final int year, final int month) {
		return null;
	}

	public List<Attendance> getStaffAttendanceInMonth(final int year, final int month, final String staff) {
		final String sql = "SELECT * FROM attendance.attendance where staffid=? and month(date)=? and year(date)=?";
		return (List<Attendance>) this.template.query(sql, new AttendanceMapper(), new Object[] { staff, month, year });
	}

	public List<String> getStaffAttendanceInMonthDays(final int year, final int month, final String staff) {
		final String sql = "SELECT date FROM attendance.attendance where staffid=? and month(date)=? and year(date)=?";
		return (List<String>) this.template.queryForList(sql, String.class, new Object[] { staff, month, year });
	}

	public List<Attendance> getStaffAttendanceInYear(final int year, final String staff) {
		final String sql = "SELECT * FROM attendance.attendance where staffid=? and year(date)=?";
		return (List<Attendance>) this.template.query(sql, new AttendanceMapper(), new Object[] { staff, year });
	}

	public List<MonthAggregate> getStaffYearAggregate(final int year, final long staff) {
		final String sql = "SELECT * FROM attendance.monthlyagg where staffid=? and year=?";
		return (List<MonthAggregate>) this.template.query(sql, new MontlyAggMapper(), new Object[] { staff, year });
	}
	
	
	public List<MonthAggregate> getDepartmentAgg( long dept) {
		List<MonthAggregate> data = new ArrayList<MonthAggregate>();
		final String sql = "SELECT name,gender,(SELECT type from stafftype where id=s.status),(SELECT count(*) from attendance as ad where ad.staffid=s.bioid and ad.islate='Early' group by ad.staffid), (SELECT count(*) from attendance as af where af.staffid=s.bioid and af.islate ='Late' group by af.staffid) , (SELECT count(*) from attendance as af where af.staffid=s.bioid and af.islate is null group by af.staffid) ,(SELECT sum(deficit) from attendance as af where af.staffid=s.bioid and af.islate is null group by af.staffid) , (SELECT sum(minuteslate) from attendance as af where af.staffid=s.bioid group by af.staffid),s.bioid FROM attendance.staff as s where department=?";
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
		return (List<MonthAggregate>) this.template.query(sql, new YearAggMapper(), new Object[] { year });
	}

	public List<StaffDisplay> getTopLateness(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(latecount) as t,(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t desc limit 10";
		return (List<StaffDisplay>) this.template.query(sql, new StaffDisplayAggMapper(), new Object[] { year });
	}

	public List<StaffDisplay> getTopAbsent(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(absentcount) as t,\n(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t desc limit 10;";
		return (List<StaffDisplay>) this.template.query(sql, new StaffDisplayAggMapper(), new Object[] { year });
	}

	public List<StaffDisplay> getTopDeficit(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(deficit) as t,\n(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t asc limit 10;";
		return (List<StaffDisplay>) this.template.query(sql, new StaffDisplayAggMapper(), new Object[] { year });
	}

	public List<StaffDisplay> getTopPerformance(final int year) {
		final String sql = "SELECT (select name from staff where bioid=m.staffid),staffid,sum(latecount)+sum(absentcount) as t,\n(select department from staff where bioid=m.staffid)  FROM attendance.monthlyagg  as m  where year=? group by m.staffid order by t asc limit 10";
		return (List<StaffDisplay>) this.template.query(sql, new StaffDisplayAggMapper(), new Object[] { year });
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

	public List<Attendance> getDeptAttendanceInYear(final int year, final int dept) {
		return null;
	}

	public List<YearMonthData> getDataCountFrommonth() {
		final String sql = "SELECT month(date),year(date),monthname(date), count(*) FROM attendance.attendance group by year(date),month(date),monthname(date)";
		return (List<YearMonthData>) this.template.query(sql, new YearMonthDataMapper());
	}

	public List<Attendance> getDeptAttendanceInMonth(final int year, final int month, final int dept) {
		return null;
	}

	public void populateAggregate(final int month, final int year, final long staff) {
		final String sql = "INSERT INTO monthlyagg (staffid, latecount, absentcount, avgtimein, avgtimeout,  deficit, lateness, month, year) \n SELECT staffid,\n(SELECT count(islate) from attendance as t where t.staffid=a.staffid and islate='Late' and month(date)="
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

	public void addAttendanceBatch( String file) {
		Map<Long, Staff> staffs = staffRepo.getStaffMap();
		final AttendanceExtractor ex = new AttendanceExtractor(file, AttendanceExtractor.Institutes.IIR,
				staffRepo.getStaffMap());
		ex.extract();
		this.saveAttendance(ex.getAttendance());
		final List<Long> staffID = this.staffRepo.getIds();

		final List<Holiday> hdays = this.holiRepo.getHolidaysInMonth(ex.getMonth(), ex.getYear());
		System.err.println(hdays + "----------------------");
		final HashSet<String> days = ex.getDaysLessHolidays(hdays);
		System.err.println("______" + hdays);
		for (long staff : staffID) {
			Staff staffNow = staffs.get(staff) ;
			if (staffNow.getStaffType() != null) {
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
				
				
				final List<Attendance> absent = new ArrayList<Attendance>();
				for (final String sDays : stafffDays) {
					final Attendance a = new Attendance(new StringBuilder().append(staff).toString(), sDays, 0L,staffNow);
					a.setLabel("Absent");
					absent.add(a);
				}
				this.saveAbsentees(absent);
				this.populateAggregate(ex.getMonth(), ex.getYear(), staff);
			}
		}
	}
}
