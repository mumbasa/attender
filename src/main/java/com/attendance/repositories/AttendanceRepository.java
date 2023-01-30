package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.models.Attendance;





@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
List<Attendance> findAttendanceByStaffid(long id);
}
