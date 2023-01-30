package com.attendance.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.attendance.models.Category;
import com.attendance.models.Certification;
import com.attendance.models.EmergencyContact;

import lombok.Getter;
import lombok.Setter;

@Table(name = "staff")
@Setter
@Getter
@Entity
public class Staff implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	@Column(name = "othernames")
	private String otherNames;
	private String middleName;
	private long bioid;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@OneToOne
	@JoinColumn(name = "status", referencedColumnName = "id", updatable = true, insertable = true, nullable = true)
	private StaffType status;

	@Column(name = "weekends")
	private boolean weekendWorker;
	private String sortCode;
	private String dob;
	private String mobile;
	private String address;
	private String address2;
	@Column(name = "tin")
	private String tinNumber;
	private String picture;
	private String nationality;
	private String gender;
	private String email;
	@Column(name = "date_joined")
	private String dateJoined;
	@OneToOne
	@JoinColumn(name = "highest_qualification", referencedColumnName = "id", updatable = true, insertable = true, nullable = true)
	private Certification highestQualification;
	@Column(name = "relationship")
	private String relationshipStatus;
	@Column(name = "ssn")
	private String socialSecurityNumber;
	private String staffid;
	private String religion;
	@Column(name = "accno")
	private String accountNumber;

	@Transient
	List<ShiftType> shifts;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "staff")
	private List<Kin> kins;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "staff")
	private List<Education> education;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "staff")
	private List<EmergencyContact> emergencyContacts;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank", referencedColumnName = "id", updatable = true, insertable = true, nullable = true)
	private Bank bank;

	@Transient
	private String birthPlace;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category", referencedColumnName = "id", updatable = true, insertable = true, nullable = true)
	private Category category;

	@OneToOne
	@JoinColumn(name = "department", referencedColumnName = "id", updatable = true, insertable = true, nullable = false)
	private Departments department;
}
