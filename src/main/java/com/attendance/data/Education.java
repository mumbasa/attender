package com.attendance.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
public class Education implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	private String institution;
	private String start;
	private String end;
	private String dateAdded;
	@Column(name="award")
	private String cetification;
	@ManyToOne
	@JoinColumn(name="staff_id")
	private Staff staff;
	

}
