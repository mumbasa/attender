package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.data.Kin;
import com.attendance.data.Staff;

public interface KinRepository extends JpaRepository<Kin, Long>{
	List<Kin> findKinByStaff(Staff staff);

}
