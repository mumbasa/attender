package com.attendance.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceAttendance {
	private String date;
	private String bioId;
	private String timeIn;
	private String timeOut;
	private String name;
	private long timeWorked;
	private String department;
}
