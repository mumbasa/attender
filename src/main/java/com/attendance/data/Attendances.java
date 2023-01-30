// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.attendance.Tester;
import com.attendance.services.Utilities;

public class Attendances {
	private String date;
	private String id;
	private String timeIn;
	private String timeOut;
	private long lateness;
	private long hoursWorked;
	private long deficit;
	private String label;
	private String closedEarly;
	private long timeOuMins;
	private long timeInMins;
	private Staff staff;
	private long hoursToWork;

	public String getDate() {
		return Utilities.dateConvert(date);
	}

	public Attendances() {
	}

	public Attendances(String keys, String value) {
		String[] dateID = keys.split("/");
		String[] times = value.split("/");
		this.id = dateID[1];
		this.date = dateID[0];
		try {
			this.timeIn = times[0];
			this.timeOut = times[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			this.timeIn = times[0];
			this.timeOut = times[0];
		}
		this.setUp();
	}

	public void setUp() {
		this.timeInMins = Utilities.stringToMinutes(this.timeIn);
		this.timeOuMins = Utilities.stringToMinutes(this.timeOut);
		System.out.println(staff.getShifts().size() + "shift size");
		lateness = Tester.getLateness(staff.getShifts(), timeInMins, Attendances.this);
		label = (lateness > 0 ? "Late" : "Early");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-dd HH:mm:ss");
		// g LocalDateTime dateTime1 = LocalDateTime.parse(String.valueOf(this.date) + "
		// " + "08:30:00", formatter);
		// LocalDateTime dateTime2 = LocalDateTime.parse(String.valueOf(this.date) + " "
		// + this.timeIn, formatter);
		// this.lateness = Duration.between(dateTime1, dateTime2).toMinutes();
		LocalDateTime d1 = LocalDateTime.parse(String.valueOf(this.date) + " " + this.timeIn, formatter);
		LocalDateTime d2 = LocalDateTime.parse(String.valueOf(this.date) + " " + this.timeOut, formatter);
		// LocalDateTime closingTime = LocalDateTime.parse(String.valueOf(this.date) + "
		// " + "16:30:00", formatter);
		this.hoursWorked = Duration.between(d1, d2).toMinutes();
		this.deficit = getHoursWorked() - (hoursToWork * 60);
		this.closedEarly = (Tester.getRun(staff.getShifts(), timeOuMins) < 0 ? "Y" : "N");
	}

	public Attendances(String id, String date, long hours, Staff staff) {
		this.date = date;
		this.id = id;
		this.hoursWorked = hours;
		this.staff = staff;
	}

	public Attendances(String id, String timeIn, String timeOut, String date, Staff staff) {
		this.id = id;
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.date = date;
		this.staff = staff;
		this.setUp();
	}

	public Attendances(int id, String timeIn, String timeOut, String date, Staff staff) {
		this.id = String.valueOf(id);
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.date = date;
		this.staff = staff;
		this.hoursWorked = 0L;
		this.lateness = 0L;
	}

	public String getId() {
		return this.id;
	}

	public String getTimeIn() {
		return this.timeIn;
	}

	public String getTimeOut() {
		return this.timeOut;
	}

	public long getLateness() {
		return this.lateness;
	}

	public long getHoursWorked() {
		return this.hoursWorked;
	}

	public String getLabel() {
		return this.label;
	}

	public long getDeficit() {

		return deficit;
	}

	public String getClosedEarly() {
		return this.closedEarly;
	}

	public void setClosedEarly(String closedEarly) {
		this.closedEarly = closedEarly;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public void setLateness(long lateness) {
		this.lateness = lateness;
	}

	public void setHoursWorked(long hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public void setDeficit(long deficit) {
		this.deficit = deficit;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public long getTimeOuMins() {
		return this.timeOuMins;
	}

	public void setTimeOuMins(long timeOuMins) {
		this.timeOuMins = timeOuMins;
	}

	public long getTimeInMins() {
		return this.timeInMins;
	}

	public void setTimeInMins(long timeInMins) {
		this.timeInMins = timeInMins;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public long getHoursToWork() {
		return hoursToWork;
	}

	public void setHoursToWork(long hoursToWork) {
		this.hoursToWork = hoursToWork;
	}

	public long getAverageWorkingHours() {
		long hours = 0;

		for (ShiftType t : staff.getShifts()) {
			hours += (t.getHours() * 60);
		}
		return hours / staff.getShifts().size();

	}
}
