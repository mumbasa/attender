// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Setter
@Table
@Entity
@Getter
public class Departments implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
    private long id;
    private String department;
    @Transient
    private long size;
    @Transient
    private int supervisor;
   
}
