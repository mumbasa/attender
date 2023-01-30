package com.attendance.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.attendance.data.Staff;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
public class Attendance {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "attendance")
	private long id;
	private String date;
	private String timein;
	private String timeout;
	@Column(name="minuteslate")

	private long lateness;
	@Column(name="timeworked")

	private long hoursWorked;
	private long deficit;
	private String islate;
	private String closedEarly;
	private long timeoutmins;
	private long timeinmins;
	@Column(name="workhours")
	private long hoursToWork;
	private long staffid;
	@JoinColumn(name="staffid",referencedColumnName = "bioid",updatable = true,insertable = true)
	private Staff staff;
	
	
}
