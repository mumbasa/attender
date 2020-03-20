// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

import com.attendance.services.Utilities;

public class Leave
{
    private String leave;
    private String staffName;
    private String department;
    private Staff staff;
    private long staffid;
    private long id;
    private int daysMore;
    private long leaveDaysRemaining;
    private int days;
    private boolean approved;
    private String approvalDate;
    private String approver;
    private String start;
    private String end;
    private int leaveTypeId;
    private String file;
    public String getLeave() {
        return this.leave;
    }
    
    public void setLeave(final String leave) {
        this.leave = leave;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getStaffName() {
        return this.staffName;
    }
    
    public void setStaffName(final String staffName) {
        this.staffName = staffName;
    }
    
    public String getStart() {
        return this.start;
    }
    
    public void setStart(final String start) {
        this.start = Utilities.dateConvert(start);
    }
    
    public String getEnd() {
        return this.end;
    }
    
    public void setEnd(final String end) {
        this.end = Utilities.dateConvert(end);
    }

	public int getDaysMore() {
		return daysMore;
	}

	public void setDaysMore(int daysMore) {
		this.daysMore = daysMore;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(String approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public long getStaffid() {
		return staffid;
	}

	public void setStaffid(long staffid) {
		this.staffid = staffid;
	}

	public int getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(int leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public long getLeaveDaysRemaining() {
		return leaveDaysRemaining;
	}

	public void setLeaveDaysRemaining(long leaveDaysRemaining) {
		this.leaveDaysRemaining = leaveDaysRemaining;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
