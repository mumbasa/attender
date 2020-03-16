// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

public class Departments
{
    private String id;
    private String department;
    private long size;
    private String supervisor;
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getDepartment() {
        return this.department;
    }
    
    public void setDepartment(final String department) {
        this.department = department;
    }

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
}
