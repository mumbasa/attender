package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.data.Education;
import com.attendance.data.Staff;

public interface EducationRepository extends JpaRepository<Education, Long>{
List<Education> findByStaff(Staff staff);
}
