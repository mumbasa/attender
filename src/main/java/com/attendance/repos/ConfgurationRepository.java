package com.attendance.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.attendance.data.Departments;
import com.attendance.data.KeyValue;

@Repository
public class ConfgurationRepository {

	@Autowired
	JdbcTemplate template;
	
	public int saveBranch(String branch,String location) {
		String sql="INSERT INTO `attendance`.`branch` (`branch`, `location`) VALUES (?,?);";
		return template.update(sql, branch,location);
		
	}
	
	public int saveBank(String bank) {
		String sql="INSERT INTO `banks` (`bank`) VALUES (?);";
		return template.update(sql, bank);
		
	}
	
	public int saveQualif(String bank) {
		String sql="INSERT INTO `certification` (`certs`) VALUES (?);";
		return template.update(sql, bank);
		
	}
	
	public int saveSchool(String school,String type) {
		String sql="INSERT INTO `schools` (`school`,type) VALUES (?,?);";
		return template.update(sql, school,type);
		
	}
	
	
	public int saveDepartment(String department,String branch) {
		String sql="INSERT INTO `attendance`.`departments` (`department`, `branch`) VALUES (?,?);";
		return template.update(sql,department, branch);
		
	}
	
	public int deleteDepartment(long id) {
		String sql="DELETE from departments where id=?";
		return template.update(sql,id);
		
	}
	

	public int deleteHoliday(long id) {
		String sql="DELETE from holidays where id=?";
		return template.update(sql,id);
		
	}
	
	public int deleteQualifications(long id) {
		String sql="DELETE from certification where id=?";
		return template.update(sql,id);
		
	}
	

	public int deleteSchool(long id) {
		String sql="DELETE from schools where id=?";
		return template.update(sql,id);
		
	}
	
	public int saveCategory(String category,String days,String emp) {
		String sql="INSERT INTO `attendance`.`category` (`category`, emptype) VALUES (?,?)";
		KeyHolder key = new GeneratedKeyHolder();
		 template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// TODO Auto-generated method stub
				PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, category);
				ps.setString(2, emp);
				return ps;
			}
		},key);
		
		
		String sql2 ="INSERT INTO `category_leave_days`(`days`,`date`,`category_id`)VALUES(?,curdate(),?)";
		return template.update(sql2,days,key.getKey());
	}
	
	
	public int saveStaffLeveArrears(String staffid,String days,String year) {
		String sql="INSERT INTO `attendance`.`leave_arrears` (`staffid`, `days`, `year`) VALUES (?,?,?);";
		return template.update(sql,staffid, days,year);
		
	}
	
	public int saveHoliday(String holiday,String date) {
		String sql="INSERT INTO `attendance`.`holidays` (`holiday`, `date`, `realdate`,year) VALUES (?,?,?,?);";
		return template.update(sql,holiday, date,date,date.split("-")[0]);
		
	}

	public List<KeyValue> getBranches() {
		String sql="SELECT * FROM `attendance`.`branch`";
		List<KeyValue> branches = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while(set.next()) {
			KeyValue kv = new KeyValue();
			kv.setId(set.getLong(1));
			kv.setValue(set.getString(2));
			branches.add(kv);
		}
		return branches;
		
	}
	
	public List<KeyValue> getHolidays() {
		String sql="SELECT * FROM `attendance`.`holidays` where year=year(curdate())";
		List<KeyValue> branches = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while(set.next()) {
			KeyValue kv = new KeyValue();
			kv.setId(set.getLong(1));
			kv.setValue(set.getString(2));
			kv.setValue2(set.getString(4));
			branches.add(kv);
		}
		return branches;
		
	}
	
	public List<KeyValue> getQualification() {
		String sql="SELECT * FROM `attendance`.`certification`";
		List<KeyValue> branches = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while(set.next()) {
			KeyValue kv = new KeyValue();
			kv.setId(set.getLong("id"));
			kv.setValue(set.getString("certs"));
			branches.add(kv);
		}
		return branches;
		
	}
	
	public List<KeyValue> getSchools() {
		String sql="SELECT * FROM `attendance`.`schools`";
		List<KeyValue> branches = new ArrayList<KeyValue>();
		SqlRowSet set = template.queryForRowSet(sql);
		while(set.next()) {
			KeyValue kv = new KeyValue();
			kv.setId(set.getLong(1));
			kv.setValue(set.getString(2));
			branches.add(kv);
		}
		return branches;
		
	}
	
	public List<Departments> getDepartments() {
		String sql="SELECT * FROM attendance.departments d left join branch b on b.id=d.branch;";
		List<Departments> branches = new ArrayList<Departments>();
		SqlRowSet set = template.queryForRowSet(sql);
		while(set.next()) {
			Departments kv = new Departments();
			kv.setId(set.getLong(1));
			kv.setDepartment(set.getString(2));
			branches.add(kv);
		}
		return branches;
		
	}
	public List<Departments> getAllDepartments() {
		String sql="SELECT * FROM attendance.departments d";
		List<Departments> branches = new ArrayList<Departments>();
		SqlRowSet set = template.queryForRowSet(sql);
		while(set.next()) {
			Departments kv = new Departments();
			kv.setId(set.getLong(1));
			kv.setDepartment(set.getString(2));
			branches.add(kv);
		}
		return branches;
		
	}
	
	
	
}
