package com.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.data.Departments;

public interface DepartmentRepository extends JpaRepository<Departments, Long>{
	

}
