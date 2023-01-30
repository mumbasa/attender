package com.attendance.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table

public class StaffShift {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "staffshift")
	private long id;

	private long staffid;
	private long shiftType;
	private String date;
	private int year;
	private int month;
	@ManyToOne
	@JoinColumn(name ="shiftType",updatable = false,insertable = false)
	private WorkTime workTime;


}
