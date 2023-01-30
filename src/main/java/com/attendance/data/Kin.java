package com.attendance.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table
@Entity
public class Kin implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String gender;
	private String contact;
	private String relation;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	private String dateAdded;
	@ManyToOne
	@JoinColumn(name = "staff_id", referencedColumnName = "id")
	Staff staff;

}
