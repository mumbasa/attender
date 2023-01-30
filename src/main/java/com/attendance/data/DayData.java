package com.attendance.data;

public class DayData {
private String day;
private String timeIn;
private String timeOut;
private WorkTime workTime;

public String getDay() {
	return day;
}
public void setDay(String day) {
	this.day = day;
}
public String getTimeIn() {
	return timeIn;
}
public void setTimeIn(String timeIn) {
	this.timeIn = timeIn;
}
public String getTimeOut() {
	return timeOut;
}
public void setTimeOut(String timeOut) {
	this.timeOut = timeOut;
}
public WorkTime getWorkTime() {
	return workTime;
}
public void setWorkTime(WorkTime workTime) {
	this.workTime = workTime;
}
public void dataAdd(String time) {
	if (timeIn==null ) {
		timeIn=time;
	}else {
		timeOut=time;
	}
}
}
