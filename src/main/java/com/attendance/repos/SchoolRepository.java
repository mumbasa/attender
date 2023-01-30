package com.attendance.repos;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.attendance.Tester;

@Repository
public class SchoolRepository {

	@Autowired
	JdbcTemplate template;
	
	public void insertHighSchool() {
		String sql ="INSERT INTO `schools`(`school`,`type`)VALUES(?,?);";
		List<String> data = Tester.getSHS2();
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, data.get(i));
				ps.setString(2, "SHS");
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
		});
		
	}
	
	public void insertUniversity() {
		String sql ="INSERT INTO `schools`(`school`,`type`)VALUES(?,?);";
		List<String> data = Tester.getUni();
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, data.get(i));
				ps.setString(2, "tertiary");
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
		});
		
	}
	
	public void insertBank() {
		String sql ="INSERT INTO `banks`(`bank`)VALUES(?);";
		List<String> data = Tester.getBank();
		template.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, data.get(i));
		
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return data.size();
			}
		});
		
	}
	
}
