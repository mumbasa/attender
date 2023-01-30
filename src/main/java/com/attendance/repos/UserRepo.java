// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.repos;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.attendance.rowmappers.UserRowMapper;
import com.attendance.data.KeyValue;
import com.attendance.data.User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.attendance.dao.UserDAO;

@Repository
public class UserRepo implements UserDAO {
	@Autowired
	JdbcTemplate template;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public List<User> getUsers() {
		final String sql = "SELECT *,(SELECT role from role as r where r.id=u.role),(SELECT department from departments as d where d.id=u.dept) FROM attendance.users as u;";
		final UserRowMapper r = new UserRowMapper();
		return this.template.query(sql, r);
	}

	public List<KeyValue> getRoles() {
		String sql = "SELECT * FROM role";
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

	public User getUserbyEmail(final String email, final String password) {
		final String sql = "SELECT * FROM users where email=?";
		final User u = (User) this.template.queryForObject(sql,   new UserRowMapper(),
				email );
		if (this.bCryptPasswordEncoder.matches((CharSequence) password, u.getPassword())) {
			return u;
		}
		return null;
	}

	
	public User getUserbyEmail(Principal principal) {
		String sql = "SELECT * FROM users where email=?";
		User u = null;
		SqlRowSet rs = template.queryForRowSet(sql, principal.getName());
		if (rs.next()) {
			 u = new User();
			u.setId(rs.getLong(1));
			u.setDepartmentId(rs.getLong(6));
			u.setEmail(rs.getString("email"));
			u.setName(rs.getString("name"));
		

	
		
		}
		return u;
	}
	public int addUser(final User e) {
		final String insert = "INSERT INTO users(email,password,name,role,dept) VALUES(?,?,?,?,?)";
		return this.template.update(insert,
				new Object[] { e.getEmail(), this.bCryptPasswordEncoder.encode((CharSequence) e.getPassword()),
						e.getName(), e.getRole(), e.getDepartment() });
	}

	public int deleteUser(final User e) {
		final String insert = "DELETE FROM users where email=?";
		return this.template.update(insert, new Object[] { e.getEmail() });
	}

	public int deleteUser(final String e) {
		final String insert = "DELETE FROM users where id=?";
		return this.template.update(insert, new Object[] { e });
	}

	public int updateUser(final User e) {
		return 0;
	}

	public boolean userExists(final String email) {
		final String sql = "SELECT * FROM users where email=?";
		return this.template.queryForRowSet(sql).next();
	}
}
