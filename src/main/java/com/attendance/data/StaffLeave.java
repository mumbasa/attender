// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

public class StaffLeave
{
    private String from;
    private String to;
    private int leaveTypeid;
    private String staffId;
    
    public String getFrom() {
        return this.from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public String getTo() {
        return this.to;
    }
    
    public void setTo(final String to) {
        this.to = to;
    }
    
    public int getLeaveTypeid() {
        return this.leaveTypeid;
    }
    
    public void setLeaveTypeid(final int leaveTypeid) {
        this.leaveTypeid = leaveTypeid;
    }
    
    public String getStaffId() {
        return this.staffId;
    }
    
    public void setStaffId(final String staffId) {
        this.staffId = staffId;
    }
}
