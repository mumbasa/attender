package com.attendance.data;

public class Shift {
	private long id;
	private ShiftType type;
	private Staff staff;
	private String date;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ShiftType getType() {
		return type;
	}
	public void setType(ShiftType type) {
		this.type = type;
	}
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	

}
