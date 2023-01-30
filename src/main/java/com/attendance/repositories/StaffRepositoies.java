package com.attendance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.attendance.data.Staff;

@Repository
public interface StaffRepositoies extends JpaRepository<Staff, Long> {
	Staff findByEmail(String email);

	@Query(value = "SELECT * FROM staff as s where (month(s.dob)-month(curdate()))<=2 and day(curdate())<= day(s.dob)", nativeQuery = true)
	List<Staff> findBirthdays();

	@Query(value = "SELECT * FROM staff as s WHERE s.department=?1 and s.id !=?2", nativeQuery = true)
	List<Staff> findByDepartmentAndNotStaff(long deptId, long staffId);
	
	@Query(value = "SELECT * FROM staff as s WHERE s.department=?1", nativeQuery = true)
	List<Staff> findByDepartment(long deptId);

	@Query(value = "SELECT * FROM staff as s  where s.id NOT IN (SELECT staff_id from supervisors);", nativeQuery = true)
	List<Staff> findStaffNoSupervisor();

	
	@Query(value = "SELECT * FROM staff as s where s.id IN (SELECT staff_id FROM supervisors where supervisor_id=?)", nativeQuery = true)
	List<Staff> findSupervisorStaff(long bioid);

	Staff findByBioid(long bioid);
	
	@Query(value="SELECT * FROM attendance.staff  as s  s.staff_id NOT IN (SELECT l.staffid from staff_leave as l where start_date<=?2 and end_date>=?2 and hr_approve=1) and s.status=2 and s.id  IN (SELECT staff_id FROM supervisors where supervisor_id=?1);",nativeQuery = true)
	List<Staff> findByStaffShiftReady(long staffId, String date);
	
	@Query(value="SELECT * FROM attendance.staff  as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.staff_id NOT IN (SELECT l.staffid from staff_leave as l where start_date<=?1 and end_date>=?1) and s.status=1 and s.id not in (SELECT h.staffid from staff_shift as h where h.date=?1);",nativeQuery = true)
	List<Staff> findByStaffShiftReady(String date);
}
