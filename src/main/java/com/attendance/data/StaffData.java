package com.attendance.data;

import java.util.HashMap;
import java.util.Map;

public class StaffData {
	private long id;
	private String name;
	private long type;
	private Map<String,DayData> dayData;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Map<String, DayData> getDayData() {
		return dayData;
	}
	public void setDayData(Map<String, DayData> dayData) {
		this.dayData = dayData;
	}

	public StaffData() {
		// TODO Auto-generated constructor stub
		this.dayData = new HashMap<String, DayData>();
	}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
