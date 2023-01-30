package com.attendance.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.attendance.data.Staff;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(name="contact")
@Entity
public class EmergencyContact implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String gender;
	private String contact;
	private String relation;
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	private String dateAdded;
	@ManyToOne
	@JoinColumn(name ="staff_id",insertable = false,updatable = false)
	Staff staff;

}
