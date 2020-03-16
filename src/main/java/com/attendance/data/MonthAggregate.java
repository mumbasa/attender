// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

public class MonthAggregate
{
    private int month;
    private int year;
    private long staffid;
    private int latenessount;
    private int absents;
    private long deficit;
    private String latenessSummary;
    private long latenessMins;
    private long earlyCount;
    private String avgTimeIn;
    private String avgTimeOut;
    private String staffName;
    private String gender;
    private String stafftype;
    
    public int getMonth() {
        return this.month;
    }
    
    public void setMonth(final int month) {
        this.month = month;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public void setYear(final int year) {
        this.year = year;
    }
    
    public long getStaffid() {
        return this.staffid;
    }
    
    public void setStaffid(final long staffid) {
        this.staffid = staffid;
    }
    
    public int getLatenessount() {
        return this.latenessount;
    }
    
    public void setLatenessount(final int latenessount) {
        this.latenessount = latenessount;
    }
    
    public int getAbsents() {
        return this.absents;
    }
    
    public void setAbsents(final int absents) {
        this.absents = absents;
    }
    
    public long getDeficit() {
        return this.deficit;
    }
    
    public void setDeficit(final long deficit) {
        this.deficit = deficit;
    }
    
    public String getLatenessSummary() {
        return this.latenessSummary;
    }
    
    public void setLatenessSummary(final String latenessSummary) {
        this.latenessSummary = latenessSummary;
    }
    
    public long getLatenessMins() {
        return this.latenessMins;
    }
    
    public void setLatenessMins(final long latenessMins) {
        this.latenessMins = latenessMins;
    }
    
    public String getAvgTimeIn() {
        return this.avgTimeIn;
    }
    
    public void setAvgTimeIn(final String avgTimeIn) {
        this.avgTimeIn = avgTimeIn;
    }
    
    public String getAvgTimeOut() {
        return this.avgTimeOut;
    }
    
    public void setAvgTimeOut(final String avgTimeOut) {
        this.avgTimeOut = avgTimeOut;
    }

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStafftype() {
		return stafftype;
	}

	public void setStafftype(String stafftype) {
		this.stafftype = stafftype;
	}

	public long getEarlyCount() {
		return earlyCount;
	}

	public void setEarlyCount(long earlyCount) {
		this.earlyCount = earlyCount;
	}
}
