package com.attendance.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "worktimes")
public class WorkTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "worktimes")
	private long id;
	private long departmentId;
	private String name; 
	private String code;
	private String color;
	private long start;
	private long end;
	private int hours;
	//@Column(name="startstring")
	private String startstring;

	//@Column(name="closeString")
	private String closestring;
	
	private int staffType;
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="departmentId",updatable = false,insertable = false) private
	 * Department department;
	 */
}
