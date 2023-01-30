package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.attendance.data.Staff;
import com.attendance.models.EmergencyContact;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, Long>{
 List<EmergencyContact> findByStaff(Staff staff);
}
