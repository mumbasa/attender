package com.attendance.data;

public class LeaveSummary {
	private long leaveDaysSum;
	private int currentTaken;
	private long daysTakenSum;
	private Staff staff;
	public long getLeaveDaysSum() {
		return leaveDaysSum;
	}
	public void setLeaveDaysSum(long leaveDaysSum) {
		this.leaveDaysSum = leaveDaysSum;
	}
	public int getCurrentTaken() {
		return currentTaken;
	}
	public void setCurrentTaken(int currentTaken) {
		this.currentTaken = currentTaken;
	}
	public long getDaysTakenSum() {
		return daysTakenSum;
	}
	public void setDaysTakenSum(long daysTakenSum) {
		this.daysTakenSum = daysTakenSum;
	}
	public Staff getStaff() {
		return staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}

}
