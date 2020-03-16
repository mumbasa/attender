package com.attendance.data;


public class LeaveType {
	private long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getMaxDaysToTake() {
		return maxDaysToTake;
	}
	public void setMaxDaysToTake(int maxDaysToTake) {
		this.maxDaysToTake = maxDaysToTake;
	}
	private String leave;
	private int days;
	private int maxDaysToTake;

}
