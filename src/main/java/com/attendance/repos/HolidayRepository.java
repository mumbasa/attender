// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.repos;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import com.attendance.services.HolidayExtractor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import com.attendance.rowmappers.HolidayMapper;
import com.attendance.data.Holiday;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.attendance.dao.HolidayDAO;

@Repository
public class HolidayRepository implements HolidayDAO
{
    @Autowired
    JdbcTemplate template;
    
    public List<Holiday> getHolidays(final int year) {
        final String sql = "SELECT *  FROM holidays where year=?";
        return template.query(sql, new HolidayMapper(), new Object[] { year });
    }
    
    
    public Map<String,Holiday> getHolidaysMao(final int year) {
        final String sql = "SELECT *  FROM holidays where year=?";
        Map<String, Holiday> holidays= new HashMap<String, Holiday>();
        SqlRowSet set= template.queryForRowSet(sql,year);
        while (set.next()) {
        	Holiday holiday = new Holiday();
        	holiday.setHoliday(set.getString(2));
        	holiday.setRealDay(set.getString(4));
        	holiday.setType(set.getString(7));
        	holidays.put(set.getString(4), holiday);
        }
        
        return holidays;
    }
    
    public List<Holiday> getHolidaysInMonth(final int month, final int year) {
        final String sql = "SELECT *  FROM holidays where year(realdate)=? and month(realdate)=? ";
        return template.query(sql, new HolidayMapper(), new Object[] { year, month });
    }
    
    public void addHolidays(final List<Holiday> holidays) {
        System.out.println("Holiday Extracting " + holidays.size());
        final String sql = "INSERT INTO holidays(date,holiday,realdate,realday,year,type) VALUES(?,?,?,?,?,?)";
        template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				rs.setString(1, holidays.get(arg1).getDate());
				rs.setString(2, holidays.get(arg1).getHoliday());
				rs.setString(3, holidays.get(arg1).getRealHoliday());
				rs.setString(4, holidays.get(arg1).getRealDay());
				rs.setInt(5, holidays.get(arg1).getYear());
				rs.setString(6, holidays.get(arg1).getType());
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return holidays.size();
			}
		});
    }
    
    public int addHoliday(final Holiday holiday) {
        final String sql = "INSERT INTO holidays(date,holiday,realdate,realday,year,type) VALUES(?,?,?,?,?,?)";
        return template.update(sql, new Object[] { holiday.getDate(), holiday.getHoliday(), holiday.getRealHoliday(), holiday.getRealDay(), holiday.getYear(), "Institutional Break" });
    }
    
    public List<Holiday> getHolidays(final int year,int month) {
        final String sql = "SELECT * FROM attendance.holidays where year =? and month(realdate)=?;";
        return template.query(sql, new HolidayMapper(), year,month);
    }
    
    public void addBreaks(final String f, final String t, final String type, final String name) {
        final String sql = "INSERT INTO holidays(date,holiday,realdate,realday,year,type) VALUES(?,?,?,?,?,?)";
        final List<Holiday> holidays = getHolidaysFromDate(getDatesBetween(f, t), type, name);
        System.out.println(String.valueOf(f) + "=====");
        template.batchUpdate(sql,new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement rs, int arg1) throws SQLException {
				// TODO Auto-generated method stub
				rs.setString(1, holidays.get(arg1).getDate());
				rs.setString(2, holidays.get(arg1).getHoliday());
				rs.setString(3, holidays.get(arg1).getRealHoliday());
				rs.setString(4, holidays.get(arg1).getRealDay());
				rs.setInt(5, holidays.get(arg1).getYear());
				rs.setString(6, holidays.get(arg1).getType());
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return holidays.size();
			}
		});
    }
    
    public static List<LocalDate> getDatesBetween(final String from, final String to) {
        final LocalDate startDate = LocalDate.parse(from);
        final LocalDate endDate = LocalDate.parse(to);
        final long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        final List<LocalDate> dates = IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween).
        		mapToObj(i -> startDate.plusDays(i)).filter(i -> i.getDayOfWeek().getValue() < 6).collect(Collectors.toList());
        return dates;
    }
    
    public List<Holiday> getHolidaysFromDate(final List<LocalDate> dates, final String type, final String name) {
        final List<Holiday> holidays = new ArrayList<Holiday>();
        for (final LocalDate d : dates) {
            final Holiday holiday = new Holiday(d.toString(), d.toString(), name, type, d.getYear());
            holiday.setRealDay(d.getDayOfWeek().name());
            holidays.add(holiday);
        }
        return holidays;
    }
    
    public int deleteHoliday(final int holiday) {
        final String sql = "DELETE FROM holidays where id=?";
        return template.update(sql, new Object[] { holiday });
    }
    
    public int updateHoliday(final Holiday holiday) {
        return 0;
    }
    
    public boolean checkDataExist(final int year) {
        final String sql = "SELECT * FROM holidays where year=? and type='Public'";
        template.queryForList(sql, new Object[] { year });
        if (template.queryForRowSet(sql, new Object[] { year }).next()) {
            return true;
        }
        final HolidayExtractor ex = new HolidayExtractor();
        ex.Extact(new StringBuilder(String.valueOf(year)).toString());
        addHolidays(ex.getHolidays());
        return false;
    }
    
    public HashSet<String> getDaysWithoutHoliday(final String file) {
        final Path p = Paths.get("/home/bryan/data/" + file, new String[0]);
        final List<String> lines = new ArrayList<String>();
        final HashSet<String> days = new HashSet<String>();
        try {
            final BufferedReader r = Files.newBufferedReader(p);
            int count = 0;
            String line;
            while ((line = r.readLine()) != null) {
                if (count > 0) {
                    final String[] data = line.split(",");
                    lines.add(String.valueOf(data[2]) + "," + data[8].replace("/", "-"));
                }
                ++count;
            }
            r.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final String a : lines) {
            days.add(a.split(",")[1].split("\\s")[0]);
        }
        for (final Holiday h : getHolidaysInMonth(2, 2017)) {
            days.remove(h.getRealHoliday());
        }
        return days;
    }
}
