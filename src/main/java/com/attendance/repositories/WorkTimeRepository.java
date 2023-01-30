package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.data.WorkTime;
@Repository
public interface WorkTimeRepository extends JpaRepository<WorkTime, Long> {
	List<WorkTime> findByDepartmentId(long id);
}
