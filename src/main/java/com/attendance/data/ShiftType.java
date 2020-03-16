package com.attendance.data;

public class ShiftType {
	private long id;
	private String shiftType;
	private long close;
	private long start;
	private long hours;
	private String closeString;
	private String startString;
	private String colour;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getShiftType() {
		return shiftType;
	}
	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}
	
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getClose() {
		return close;
	}
	public void setClose(long close) {
		this.close = close;
	}
	public long getHours() {
		return hours;
	}
	public void setHours(long hours) {
		this.hours = hours;
	}
	public String getCloseString() {
		return closeString;
	}
	public void setCloseString(String closeString) {
		this.closeString = closeString;
	}
	public String getStartString() {
		return startString;
	}
	public void setStartString(String startString) {
		this.startString = startString;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	
}
