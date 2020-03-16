// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

import java.util.List;
import java.util.Map;

public class Staff
{
    private String name;
    private String otherNames;
    private long bioID;
    private String department;
    private int departmentId;
    private long id;
    private String staffType;
    private boolean weekendWorker;
    private List<ShiftType> shifts;
    private Map<String, Shift> staffShifts;
    private long typeId;
    private String dob;
    private String mobile;
    private String address;
    private String address2;
    private String tinNumber;
    private String picture;
    private String nationality;
    private String gender;
    private String email;
    private String dateJoined;
    private String birthPlace;
    private String category;
    private String highestQualification;
    private String relationshipStatus;
    private String socialSecurityNumber;
    private String staffId;
    private String religion;
    private int categoryId;
    private List<Kin> kins;
    private List<Education> education;
    private List<Kin> emergencyContacts;
    private String bank;
    private int bankid;
    private String accNumber;
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public long getBioID() {
        return this.bioID;
    }
    
    public void setBioID(final long bioID) {
        this.bioID = bioID;
    }
    
    public String getDepartment() {
        return this.department;
    }
    
   

	public void setDepartment(final String department) {
        this.department = department;
    }
    
    public String getStaffId() {
        return this.staffId;
    }
    
    public void setStaffId( String staffId) {
        this.staffId = staffId;
    }

	public String getStaffType() {
		return staffType;
	}

	public void setStaffType(String staffType) {
		this.staffType = staffType;
	}

	public List<ShiftType> getShifts() {
		return shifts;
	}

	public void setShifts(List<ShiftType> shifts) {
		this.shifts = shifts;
	}

	public boolean isWeekendWorker() {
		return weekendWorker;
	}

	public void setWeekendWorker(boolean weekendWorker) {
		this.weekendWorker = weekendWorker;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(String dateJoined) {
		this.dateJoined = dateJoined;
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getHighestQualification() {
		return highestQualification;
	}

	public void setHighestQualification(String highestQualification) {
		this.highestQualification = highestQualification;
	}

	public String getRelationshipStatus() {
		return relationshipStatus;
	}

	public void setRelationshipStatus(String relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public List<Kin> getKins() {
		return kins;
	}

	public void setKins(List<Kin> kins) {
		this.kins = kins;
	}

	public List<Education> getEducation() {
		return education;
	}

	public void setEducation(List<Education> education) {
		this.education = education;
	}

	public List<Kin> getEmergencyContacts() {
		return emergencyContacts;
	}

	public void setEmergencyContacts(List<Kin> emergencyContacts) {
		this.emergencyContacts = emergencyContacts;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getOtherNames() {
		return otherNames;
	}

	public void setOtherNames(String otherNames) {
		this.otherNames = otherNames;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public Map<String, Shift> getStaffShifts() {
		return staffShifts;
	}

	public void setStaffShifts(Map<String, Shift> staffShifts) {
		this.staffShifts = staffShifts;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAccNumber() {
		return accNumber;
	}

	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}

	public int getBankid() {
		return bankid;
	}

	public void setBankid(int bankid) {
		this.bankid = bankid;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	
}
