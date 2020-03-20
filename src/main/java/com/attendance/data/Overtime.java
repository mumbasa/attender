package com.attendance.data;

import com.attendance.services.Utilities;

public class Overtime {
private  long id;
private String name;
private Staff staff;
private String date;
private String start;
private String end;
private int duration;
private boolean approved;
private String approvalDate;
private boolean verified;
private int difference;
private String reason;
private String decision;
private long staffId;
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDate() {
	return Utilities.dateConvert(date);
}
public void setDate(String date) {
	this.date = date;
}
public String getStart() {
	return start;
}
public void setStart(String start) {
	this.start = start;
}
public String getEnd() {
	return end;
}
public void setEnd(String end) {
	this.end = end;
}
public int getDuration() {
	return duration;
}
public void setDuration(int duration) {
	this.duration = duration;
}
public boolean isApproved() {
	return approved;
}
public void setApproved(boolean approved) {
	this.approved = approved;
}
public String getApprovalDate() {
	return approvalDate;
}
public void setApprovalDate(String approvalDate) {
	this.approvalDate = approvalDate;
}
public boolean isVerified() {
	return verified;
}
public void setVerified(boolean verified) {
	this.verified = verified;
}
public int getDifference() {
	return difference;
}
public void setDifference(int difference) {
	this.difference = difference;
}
public String getReason() {
	return reason;
}
public void setReason(String reason) {
	this.reason = reason;
}
public String getDecision() {
	return decision;
}
public void setDecision(String decision) {
	this.decision = decision;
}
public Staff getStaff() {
	return staff;
}
public void setStaff(Staff staff) {
	this.staff = staff;
}
public long getStaffId() {
	return staffId;
}
public void setStaffId(long staffId) {
	this.staffId = staffId;
}
	
}
