package com.attendance.repos;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import com.attendance.rowmappers.DepartmentMapper;
import com.attendance.rowmappers.EducationMapper;
import com.attendance.rowmappers.EmergencyMapper;
import com.attendance.rowmappers.KinMapper;
import com.attendance.rowmappers.ShiftMapper;
import com.attendance.data.Departments;
import com.attendance.data.Education;
import com.attendance.data.KeyValue;
import com.attendance.data.Kin;
import com.attendance.data.ShiftType;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.attendance.rowmappers.StaffMapper;
import com.attendance.data.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.attendance.dao.StaffDAO;

@Repository
public class StaffRepository implements StaffDAO {
	@Autowired
	JdbcTemplate template;
	
	@Value("${app.upload.file.location}")
	String location;

	@Autowired
	BCryptPasswordEncoder encoder;
	

	
	public List<Staff> getStaff() {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id";
		List<Staff> staffs = template.query(sql, new StaffMapper());
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			s.setKins(getKin(s.getId()));
			s.setEducation(getEducation(s.getId()));
			s.setEmergencyContacts(getContacts(s.getId()));
			if(s.getStaffType().equalsIgnoreCase("shift")) {
			}else {
				}
		}
		return staffs;
	}
	
	
	
	public List<Staff> getUnassignedStaff() {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.id NOT IN (SELECT staff_id from supervisors);";
		List<Staff> staffs = template.query(sql, new StaffMapper());
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			s.setKins(getKin(s.getId()));
			s.setEducation(getEducation(s.getId()));
			s.setEmergencyContacts(getContacts(s.getId()));
			if(s.getStaffType().equalsIgnoreCase("shift")) {
			}else {
				}
		}
		return staffs;
	}
	
	public List<Staff> getAllStaff() {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id";
		List<Staff> staffs = template.query(sql, new StaffMapper());
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			s.setKins(getKin(s.getId()));
			s.setEducation(getEducation(s.getId()));
			s.setEmergencyContacts(getContacts(s.getId()));
		}
		return staffs;
	}
	public List<Staff> getStaff(boolean cat) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.category is not null";
		List<Staff> staffs = template.query(sql, new StaffMapper());
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			s.setKins(getKin(s.getId()));
			s.setEducation(getEducation(s.getId()));
			s.setEmergencyContacts(getContacts(s.getId()));
		}
		return staffs;
	}
	
	
	
	public List<Staff> getStaffUpcomingBirtday() {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where month(curdate())<= month(s.dob) and day(curdate())<= day(s.dob)";
		List<Staff> staffs = template.query(sql, new StaffMapper());
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
		}
		return staffs;
	}
	
	
	

	public List<Staff> getStaffInDept(long id) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.department=(SELECT department from staff as f where f.id=?) and s.id !=?";
		List<Staff> staffs = template.query(sql, new StaffMapper(),id,id);
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
		}
		return staffs;
	}
	
	public List<Staff> getStaffInDepartment(long id) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.department=?";
		List<Staff> staffs = template.query(sql, new StaffMapper(),id);
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			s.setKins(getKin(s.getId()));
			s.setEducation(getEducation(s.getId()));
			s.setEmergencyContacts(getContacts(s.getId()));
		}
		return staffs;
	}
	
	
	public List<Staff> getStaffOfSupervisor(long id) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.id IN (SELECT staff_id FROM supervisors where supervisor_id=?)";
		List<Staff> staffs = template.query(sql, new StaffMapper(),id);
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			s.setKins(getKin(s.getId()));
			s.setEducation(getEducation(s.getId()));
			s.setEmergencyContacts(getContacts(s.getId()));
		}
		return staffs;
	}
	
	
	
	public List<Staff> getShiftStaffInDepartment(long id) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.department=? and s.status=2";
		List<Staff> staffs = template.query(sql, new StaffMapper(),id);
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			
		}
		return staffs;
	}
	
	public List<Staff> getStaffShiftReady(String date) {
		String sql = "SELECT * FROM attendance.staff  as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.staff_id NOT IN (SELECT l.staffid from staff_leave as l where start_date<=? and end_date>=?) and s.status=1 and s.id not in (SELECT h.staffid from staff_shift as h where h.date=?);";
		List<Staff> staffs = template.query(sql, new StaffMapper(),date,date,date);
	
		return staffs;
	}
	
	public List<Staff> getStaffShiftReady(long staffID,String date) {
		String sql = "SELECT * FROM attendance.staff  as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.staff_id NOT IN (SELECT l.staffid from staff_leave as l where start_date<=? and end_date>=? and hr_approve=1) and s.status=2 and s.id  IN (SELECT staff_id FROM supervisors where supervisor_id=?);";
		List<Staff> staffs = template.query(sql, new StaffMapper(),date,date,staffID);
	
		return staffs;
	}
	

	public Map<Long,Staff> getStaffMap() {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id";
		List<Staff> staffs = template.query(sql, new StaffMapper());
		Map<Long, Staff> staffData = new HashMap<Long, Staff>();
		for (Staff s : staffs) {
			s.setShifts(getStaffShifts(s.getTypeId()));
			staffData.put(s.getBioID(), s);
		}
		System.err.println("getting staff");
		return staffData;
		
	}

	
	
	
	public List<ShiftType> getStaffShifts(long type) {
		String sql = "SELECT * FROM worktimes where staff_type=?;";
		return template.query(sql, new ShiftMapper(), type);

	}
	public List<ShiftType> getStaffShifts() {
		String sql = "SELECT * FROM worktimes";
		return template.query(sql, new ShiftMapper());

	}

	public int saveStaff(Staff e) {
		String sql = "INSERT INTO staff(bioid,name,department,status,weekends,`mobile`,`address`,`email`,`gender`,`dob`,`nationality`,picture,date_joined,category,`relationship`,`highest_qualification`,`ssn`,`religion`,`staff_id`,othernames,`tin`,`address2`,`bank`,`accno` ) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return template.update(sql, new Object[] { e.getBioID(), e.getName(), e.getDepartment(),e.getStaffType(),e.isWeekendWorker(),
		e.getMobile(),e.getAddress(),e.getEmail(),e.getGender(),e.getDob(),e.getNationality(),e.getPicture(),e.getDateJoined(),e.getCategoryId(),e.getRelationshipStatus(),e.getHighestQualification(),e.getSocialSecurityNumber(),e.getReligion(),e.getStaffId(),e.getOtherNames(),e.getAddress2(),e.getTinNumber(),e.getBank(),e.getAccNumber()		
		});
	}
	
	public int updateStaff(Staff e,long id) {
		String sql = "UPDATE  staff set bioid=?,name=?,department=?,status=?,weekends=?,`mobile`=?,`address`=?,`email`=?,`gender`=?,`dob`=?,`nationality`=?,date_joined=?,category=?,`relationship`=?,`highest_qualification`=?,`ssn`=?,`religion`=?,`staff_id`=?,othernames=? where id=?";
		return template.update(sql, new Object[] { e.getBioID(), e.getName(), e.getDepartment(),e.getStaffType(),e.isWeekendWorker(),
		e.getMobile(),e.getAddress(),e.getEmail(),e.getGender(),e.getDob(),e.getNationality(),e.getDateJoined(),e.getCategoryId(),e.getRelationshipStatus(),e.getHighestQualification(),e.getSocialSecurityNumber(),e.getReligion(),e.getStaffId(),e.getOtherNames(),id		
		});
	}
	
	public int saveshift(String name,long start,long close,String type,int hours,String startString,String closeString,String color) {
		String sql = "INSERT INTO `worktimes` (name,`start`, `end`, `staff_type`, `hours`,startstring,closestring,color) VALUES (?,?,?,?,?,?,?,?);";
		return template.update(sql,name,start,close,type,hours,startString,closeString,color);
	}
	

	public int addDept(Departments e) {
		String sql = "INSERT INTO departments(department) VALUES(?)";
		return template.update(sql, e.getDepartment());
	}
	//education operation

	public int addQualification(String name,String cert,String from,String to,String staff) {
		String sql = "INSERT INTO `attendance`.`education` (`institution`, `award`, `start`, `end`, staff_id,`date_added`) VALUES (?,?,?,?,?,curdate());";
		return template.update(sql, name,cert,from,to,staff);
	}
	
	public List<Education> getEducation(long id){
		String sql="SELECT * FROM education where staff_id=?";
		return template.query(sql, new EducationMapper(),id);
	}
	
	

	public int deleteEducation(long id) {
		String sql = "DELETE FROM education where id=?";
		return template.update(sql,id);
	}
	

	
	public int updateEducation(long id,String name,String award,String start,String to) {
		String sql = "UPDATE education set institution=?,award=?,start=?,end=? where id=?";
		return template.update(sql,name,award,start,to,id);
	}
	
	//end edicaion
	
	
	
	public int addKin(String name,String relationship,String gender,String contact,String staff) {
		String sql = "INSERT INTO  `kin` (`name`, `contact`, `relation`, `date_added`, `staff_id`, `gender`) VALUES (?,?,?,curdate(),?,?);";
		return template.update(sql, name,contact,relationship,staff,gender);
	}
	
	
	
	
	public int deleteKin(long id) {
		String sql = "DELETE FROM kin where id=?";
		return template.update(sql,id);
	}

	
	public int updateKin(long id,String name,String relationship,String gender,String contact) {
		String sql = "UPDATE kin set name=?,contact=?,gender=?,relation=? where id=?";
		return template.update(sql,name,contact,gender,relationship,id);
	}
	
	public List<Kin> getKin(long id){
		String sql="SELECT * FROM kin where staff_id=?";
		return template.query(sql, new KinMapper(),id);
	}
	
	
	public int addContact(String name,String contact,String staff,String relationship) {
		String sql = "INSERT INTO `attendance`.`contact` (`name`, `contact`,staff_id, `date_added`,relation) VALUES (?,?,?,curdate(),?)";
		return template.update(sql, name,contact,staff,relationship);
	}
	

	public List<Kin> getContacts(long id){
		String sql="SELECT * FROM contact where staff_id=?";
		return template.query(sql, new EmergencyMapper(),id);
	}
	
	
	public List<String> getReligion(){
		String sql="SELECT *  FROM religion";
		return template.queryForList(sql, String.class);
	}
	
	public List<String> getRelation(){
		String sql="SELECT *  FROM relation";
		return template.queryForList(sql, String.class);
	}
	
	public List<String> getRelationship(){
		String sql="SELECT *  FROM relationship";
		return template.queryForList(sql, String.class);
	}
	
	public List<String> getCert(){
		String sql="SELECT *  FROM certification";
		return template.queryForList(sql, String.class);
	}
	
	

	public List<String> getCountries(){
		String sql="SELECT country_name  FROM countries";
		return template.queryForList(sql, String.class);
	}
	
	public List<String> getReligions(){
		String sql="SELECT * FROM religion;";
		return template.queryForList(sql, String.class);
	}
	
	public int updateContact(long id,String name,String contact) {
		String sql = "UPDATE contact set name=?,contact=? where id=?";
		return template.update(sql,name,contact,id);
	}
	
	public int deleteContact(long id) {
		String sql = "DELETE FROM contact where id=?";
		return template.update(sql,id);
	}
	
	public long getDeptAvgAge(long id) {
		String sql = "SELECT round(avg(datediff(curdate(),dob)/365)) FROM staff where department=? and dob is not null";
			
		return template.queryForObject(sql,long.class,id);
	}
	
	public long getStaffAgeAge(long id) {
		String sql = "SELECT round((datediff(curdate(),dob)/365)) FROM staff where id=?";
		return template.queryForObject(sql,long.class,id);
	}
	
	

	public int changePassword(long id,String password) {
		String sql = "UPDATE users set password=? where id=?";
		return template.update(sql,encoder.encode(password),id);
	}
	
	public int changePassword(String email,String password) {
		String sql = "UPDATE users set password=? where email=?";
		return template.update(sql,encoder.encode(password),email);
	}
	
	
	public int changeUserRole(long id,String role) {
		String sql = "UPDATE users set role=? where id=?";
		return template.update(sql,role,id);
	}
	
	
	public int deleteUser(long id) {
		String sql = "DELETE FROM users  where id=?";
		return template.update(sql,id);
	}
	
	
	public int toggleUser(long id,String status) {
		String sql = "UPDATE users set status=? where id=?";
		return template.update(sql,status,id);
	}
	
	public long getStaffYearsSpent(long id) {
		String sql = "SELECT round((datediff(curdate(),date_joined)/365)) FROM staff where id=?";
		return template.queryForObject(sql,long.class,id);
	}
	

	public List<Departments> getDepartments() {
		String sql = "SELECT *,(SELECT count(*) from staff as s where s.department=d.id) FROM departments as d;";
		return (List<Departments>) template.query(sql, new DepartmentMapper());
	}
	
	public Departments getDepartment(long id) {
		String sql = "SELECT *,(SELECT count(*) from staff as s where s.department=d.id) FROM departments as d where d.id=?";
		return  template.queryForObject(sql, new DepartmentMapper(),id);
	}

	public int deleteStaff(long e) {
		String sql = "DELETE FROM staff where id=?";
		return template.update(sql, new Object[] { e });
	}

	public void insertBatchStaff(List<Staff> e) {
		String sql = "INSERT INTO staff(bioid,staffid,name,department) VALUES(?,?,?,?)";
		template.batchUpdate(sql, (BatchPreparedStatementSetter) new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, e.get(i).getBioID());
				ps.setString(2, e.get(i).getStaffId());
				ps.setString(3, e.get(i).getName());
				ps.setString(4, e.get(i).getDepartment());
			}

			public int getBatchSize() {
				return e.size();
			}
		});
	}

	

	public boolean checkStaffExist(Staff e) {
		return false;
	}

	public Staff getStaffByID(long id) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.id=?";
		Staff staff= template.queryForObject(sql, new StaffMapper(), id);
		//staff.setShifts(getStaffShifts(staff.getTypeId()));
		return staff;
	}

	public Staff getStaffByBoid(long id) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.bioid=?";
		Staff staff= template.queryForObject(sql, new StaffMapper(), id);
		staff.setShifts(getStaffShifts(staff.getTypeId()));
		staff.setKins(getKin(staff.getId()));
		staff.setEducation(getEducation(staff.getId()));
		staff.setEmergencyContacts(getContacts(staff.getId()));
		return staff;
	}

	
	public Staff getStaffByEmail(String email) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.email=?";
		Staff staff= template.queryForObject(sql, new StaffMapper(), email);
		staff.setShifts(getStaffShifts(staff.getTypeId()));
		staff.setKins(getKin(staff.getId()));
		staff.setEducation(getEducation(staff.getId()));
		staff.setEmergencyContacts(getContacts(staff.getId()));
		return staff;
	}
	public Staff getStaffByStaffId(String staffId) {
		String sql = "SELECT * FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id left join category as c on s.category=c.id where s.staff_id=?";
		Staff staff= template.queryForObject(sql, new StaffMapper(), staffId);
		staff.setShifts(getStaffShifts(staff.getTypeId()));
		staff.setKins(getKin(staff.getId()));
		staff.setEducation(getEducation(staff.getId()));
		staff.setEmergencyContacts(getContacts(staff.getId()));
		
		return staff;
	}

	
	
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public List<KeyValue> getStaffTypes() {
		String sql = "SELECT * FROM stafftype";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(1));
			v.setValue(set.getString(2));
			values.add(v);

		}
		return values;
	}
	
	
	
	public List<KeyValue> getDeptStat() {
		String sql = "SELECT d.department,floor(avg(timeworked)),floor(avg(minuteslate)),floor(avg(deficit)) as def FROM staff as s left join departments as d on s.department=d.id left join stafftype as st on s.status=st.id join attendance as a on s.bioid=a.staffid where deficit is not null group by s.department;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(Long.parseLong(set.getString(2).split("\\.")[0]));
			v.setValue(set.getString(1));
			v.setQuantity(Long.parseLong((""+set.getDouble(3)).split("\\.")[0]));
			v.setQuantity2(Long.parseLong((""+set.getDouble(4)).split("\\.")[0]));
			values.add(v);
		}
		return values;
	}
	
	public List<KeyValue> getByAges() {
		String sql = "SELECT floor(datediff(curdate(),dob)/365) as age ,count(*) from staff where dob is not null group by age;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setValue(set.getString(1));
			values.add(v);

		}
		return values;
	}
	
	
	public List<KeyValue> getBanks() {
		String sql = "SELECT * FROM banks;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(1));
			v.setValue(set.getString(2));
			values.add(v);

		}
		return values;
	}
	
	public List<KeyValue> getSchools() {
		String sql = "SELECT * FROM schools order by school asc";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(1));
			v.setValue(set.getString(2));
			values.add(v);

		}
		return values;
	}
	
	
	public List<KeyValue> getByAges(long dept) {
		String sql = "SELECT floor(datediff(curdate(),dob)/365) as age ,count(*) from staff where dob is not null and department=? group by age;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,dept);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setValue(set.getString(1));
			values.add(v);

		}
		return values;
	}
	
	public List<KeyValue> getDeprtmentlatenessCount(long dept) {
		String sql = "SELECT month(date) as m , count(*) FROM attendance where staffid IN(SELECT bioid from staff where department=?)  and minuteslate >0 group by m;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,dept);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(1));
			v.setQuantity(set.getLong(2));
			values.add(v);

		}
		return values;
	}
	
	public List<KeyValue> getStaffByGender() {
		String sql = "SELECT gender,count(*) FROM staff where gender is not null group by gender;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setValue(set.getString(1));
			values.add(v);

		}
		return values;
	}
	
	public List<KeyValue> getStaffCategory() {
		String sql = "SELECT * FROM category";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(1));
			v.setValue(set.getString(2));
			values.add(v);

		}
		return values;
	}
	
	public List<KeyValue> getDeptStaffByGender(long id) {
		String sql = "SELECT gender,count(*) FROM staff where gender is not null and department=? group by gender;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,id);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setValue(set.getString(1));
			values.add(v);

		}
		return values;
	}
	
	
	public List<KeyValue> getAttendanceDeficitsMonthly() {
		String sql = "SELECT floor(sum(deficit)),month(date) as m FROM attendance group by m;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setQuantity((long) set.getDouble(1));
			values.add(v);

		}
		return values;
	}
	
	public List<KeyValue> getAttendanceDeficitsMonthly(long dept) {
		String sql = "SELECT floor(sum(deficit)),month(date) as m FROM attendance where staffid IN (SELECT bioid from staff where department=?) group by m;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,dept);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setQuantity((long) set.getDouble(1));
			values.add(v);

		}
		return values;
	}
	
	
	public List<KeyValue> getAttendanceStackMonthly(long id) {
		String sql = "SELECT month(a.date) as m,\n" + 
				"(SELECT count(*) from attendance as ad where ad.staffid=a.staffid and ad.islate='Early' and month(ad.date)=m group by ad.staffid), \n" + 
				"(SELECT count(*) from attendance as af where af.staffid=a.staffid  and  af.islate ='Late' and month(af.date)=m  group by af.staffid) , \n" + 
				"(SELECT count(*) from attendance as af where af.staffid=a.staffid  and  af.islate is null and month(af.date)=m  group by af.staffid)  \n" + 
				"from attendance as  a where a.staffid=? group by month(date)";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,id);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setValue(set.getString(1));
			v.setQuantity(Long.parseLong(set.getString(3).split("\\.")[0]));
			v.setQuantity2(Long.parseLong(set.getString(4).split("\\.")[0]));

			values.add(v);

		}
		return values;
	}
	
	
	public List<KeyValue> getStaffAttendanceDeficitsMonthly(long id) {
		String sql = "SELECT floor(sum(deficit)),month(date) as m FROM attendance  where staffid=? group by m";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,id);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(2));
			v.setQuantity(Long.parseLong(set.getString(1).split("\\.")[0]));
			values.add(v);

		}
		return values;
	}

	public List<KeyValue> getDepartments(long id) {
		String sql = "SELECT *,(SELECT count(*) from staff as s where s.department=d.id) FROM departments as d;";
		List<KeyValue> values = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql,id);
		while (set.next()) {
			KeyValue v = new KeyValue();
			v.setId(set.getLong(1));
			v.setValue(set.getString(2));
			v.setQuantity(Long.parseLong(set.getString(3).split("\\.")[0]));
			values.add(v);

		}
		return values;
	}

	
	
	@Override
	public String toString() {
		return super.toString();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public List<Long> getIds() {
		String sql = "SELECT distinct bioid FROM staff";
		return  template.queryForList(sql, Long.class);
	}
	
	
	public Long getStaffSize() {
		String sql = "SELECT count(*) FROM staff";
		return  template.queryForObject(sql, Long.class);
	}


	public String getStaffGendrRAtio() {
		String stat="";
		String sql = "SELECT (SELECT count(*) from staff where gender='Male'),(SELECT count(*) from staff where gender='Female') from staff LIMIT 1;";
		SqlRowSet set =  template.queryForRowSet(sql);
		if(set.next()) {
			stat= set.getString(1)+":"+set.getString(2);
			
		}
		return stat;
	}
	
	public String getStaffGendrRAtiobyDept(long id) {
		String stat="";
		String sql = "SELECT (SELECT count(*) from staff where gender='Male'),(SELECT count(*) from staff where gender='Female') from staff where department=? LIMIT 1;";
		SqlRowSet set =  template.queryForRowSet(sql,id);
		if(set.next()) {
			stat= set.getString(1)+":"+set.getString(2);
			
		}
		return stat;
	}

	
	public Long getDeptSize() {
		String sql = "SELECT count(*) FROM departments";
		return  template.queryForObject(sql, Long.class);
	}

	
	public Long StaffOnLeaveSize() {
		String sql = "SELECT count(distinct staffid) FROM leaves where curdate()<= endDate;";
		return  template.queryForObject(sql, Long.class);
	}
	

	public void addSupervisor(List<Long> staff,long superId) {
		String sql = "INSERT INTO `attendance`.`supervisors`(`supervisor_id`,`staff_id`,`date_added`)VALUES(?,?,curdate());";
		  template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setLong(1, superId);
				ps.setLong(2, staff.get(i));
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return staff.size();
			}
		});
	}


	public Long staffOnLeaveSizeByDept(long id) {
		String sql = "SELECT count(distinct staffid) FROM leaves where curdate()<= endDate and staffid IN (SELECT bioid from staff where department=?);";
		return  template.queryForObject(sql, Long.class,id);
	}
	
	public void setupJson() {
		List<Staff> staff = this.getStaff();
		ObjectMapper m = new ObjectMapper();
		FileWriter writeer = null;
		try {
			String json = m.writeValueAsString((Object) staff);
			System.err.println(json);
			File file = new File("/src/main/resources/static/assets/search.json");
			writeer = new FileWriter(file);
			writeer.write(json);
			writeer.flush();
			writeer.close();
			System.out.println(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dataFolderSetup() {
		File file = new File(this.location);
		if (file.exists()) {
			if (!file.isDirectory()) {
				file.mkdir();
			} else {
				System.out.println("Folder exists");
			}
		} else {
			file.mkdir();
		}
	}

	@Override
	public int updateStaff(Staff e) {
		// TODO Auto-generated method stub
		return 0;
	}
}
