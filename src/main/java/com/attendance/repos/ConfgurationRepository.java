package com.attendance.repos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.attendance.data.KeyValue;

@Repository
public class ConfgurationRepository {

	@Autowired
	JdbcTemplate template;
	
	public int saveBranch(String branch,String location) {
		String sql="INSERT INTO `attendance`.`branch` (`branch`, `location`) VALUES (?,?);";
		return template.update(sql, branch,location);
		
	}
	
	
	public int saveDepartment(String department,String branch) {
		String sql="INSERT INTO `attendance`.`departments` (`department`, `branch`) VALUES (?,?);";
		return template.update(sql,department, branch);
		
	}
	
	
	public int saveCategory(String category,String days) {
		String sql="INSERT INTO `attendance`.`category` (`category`, `leave_days`, `date`) VALUES (?,?,curdate())";
		return template.update(sql,category, days);
		
	}
	
	
	public int saveStaffLeveArrears(String staffid,String days,String year) {
		String sql="INSERT INTO `attendance`.`leave_arrears` (`staffid`, `days`, `year`) VALUES (?,?,?);";
		return template.update(sql,staffid, days,year);
		
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
	
	
	
	
	
}
