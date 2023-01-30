package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.data.StaffShift;

@Repository
public interface StaffShiftRepo extends JpaRepository<StaffShift, Long> {
	List<StaffShift> findShiftsByMonthAndYear(int month,int year);	
}
