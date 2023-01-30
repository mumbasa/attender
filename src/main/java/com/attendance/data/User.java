// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User
{
    private long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String department;
    private String pic;
    private int status;
    private long departmentId;
   }
