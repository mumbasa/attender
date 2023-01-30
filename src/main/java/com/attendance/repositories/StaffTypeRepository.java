package com.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.data.StaffType;
@Repository
public interface StaffTypeRepository extends JpaRepository<StaffType, Long> {

}
