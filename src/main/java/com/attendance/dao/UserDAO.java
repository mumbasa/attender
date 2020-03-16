// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.dao;

import com.attendance.data.User;
import java.util.List;

public interface UserDAO
{
    List<User> getUsers();
    
    User getUserbyEmail(final String email, final String password);
    
    int addUser(final User e);
    
    int deleteUser(final User e);
    
    int updateUser(final User e);
    
    boolean userExists(final String email);
}
