// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.dao;

import com.attendance.data.Staff;
import java.util.List;

public interface StaffDAO
{
    List<Staff> getStaff();
    
    int saveStaff(final Staff e);
    
    int deleteStaff(final long e);
    
    void insertBatchStaff(final List<Staff> e);
    
    int updateStaff(final Staff e);
    
    boolean checkStaffExist(final Staff e);
    
    Staff getStaffByID(final long id);
}
