// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.attendance.data.Staff;
import com.attendance.data.User;
import org.springframework.ui.Model;
import com.attendance.repos.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.attendance.repos.UserRepo;
import org.springframework.stereotype.Controller;

@Controller
public class UserController
{
    @Autowired
    UserRepo repo;
    @Autowired
    StaffRepository staffRepo;
    
    @RequestMapping( "/admin/enrol/user" )
    public String getDashwe( Model model) {

        model.addAttribute("roles", repo.getRoles());
        model.addAttribute("staff", staffRepo.getStaff());
        model.addAttribute("depts", staffRepo.getDepartments());
        return "/admin/adduser";
    }
    
    @RequestMapping( "/admin/all/users" )
    public String getUsers( Model model) {
         List<User> users = this.repo.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", repo.getRoles());

        return "/admin/allusers";
    }
    
  
    
    @RequestMapping( "/admin/change/password" )
    public String changePassword() {
        
        return "/admin/changepass";
    }
    
    
    
    
    @RequestMapping( "/get/allusers" )
    public List<User> getUserss() {
         List<User> users = this.repo.getUsers();
        System.out.println(users.get(0).getName());
        return users;
    }
    
	/*
	 * @RequestMapping( "/admin/delete/user/{id}" ) public String
	 * delUser(@PathVariable("id") String email) { System.out.println(email);
	 * this.repo.deleteUser(email); return "redirect:/admin/allusers"; }
	 */
    
    @PostMapping( "/admin/adduser/{pic}" )
    @ResponseBody
    public int addUser(@PathVariable("pic") String pic, @RequestParam("staff") int staff,@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("type") String role,@RequestParam("department") String dept,@RequestParam("name") String name) {
       int stat=0;
    	if(staff >0) {
    	  Staff worker = staffRepo.getStaffByID(staff);
    	  User user = new User();
          user.setEmail(worker.getEmail());
          user.setRole(role);
          user.setPassword(password);
          user.setDepartment(dept);
          user.setName(worker.getName());
          user.setPic(worker.getPicture());
          stat=repo.addUser(user);
          
       }else {
    	
       User user = new User();
       user.setEmail(email);
       user.setRole(role);
       user.setPassword(password);
       user.setDepartment(dept);
       user.setName(name);
       user.setPic(pic);

       stat=repo.addUser(user);
       }
        return stat;
    }
    
    

	@ResponseBody
	@RequestMapping("/admin/update/password/{id}")
	public int updatePassword(@PathVariable("id") long id, @RequestParam("password") String password) {
		return staffRepo.changePassword(id,password);

	}
	
	@ResponseBody
	@RequestMapping("/admin/update/password/")
	public int updatePassword(Principal principal, @RequestParam("password") String password) {
		
		return staffRepo.changePassword(principal.getName(),password);

	}
	
	
	@ResponseBody
	@RequestMapping("/admin/update/role/{id}")
	public int changeUserRole(@PathVariable("id") long id, @RequestParam("role") String password) {
		return staffRepo.changeUserRole(id,password);

	}
    

	@ResponseBody
	@RequestMapping("/admin/activate/user/{id}")
	public int suspendUserRole(@PathVariable("id") long id, @RequestParam("status") String status) {
		return staffRepo.toggleUser(id,status);

	}
    

	@ResponseBody
	@RequestMapping("/admin/delete/user/{id}")
	public int deleteUserRole(@PathVariable("id") long id) {
		return staffRepo.deleteUser(id);

	}
    
	
    
    @PostMapping( "/admin/adduser/" )
    @ResponseBody
    public int addUser2( @RequestParam("staff") int staff,@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("type") String role,@RequestParam("department") String dept,@RequestParam("name") String name) {
       int stat=0;
    	if(staff >0) {
    	  Staff worker = staffRepo.getStaffByID(staff);
    	  User user = new User();
          user.setEmail(worker.getEmail());
          user.setRole(role);
          user.setPassword(password);
          user.setDepartment(dept);
          user.setName(worker.getName());
          user.setPic(worker.getPicture());
          stat=repo.addUser(user);
          
       }else {
    	
       User user = new User();
       user.setEmail(email);
       user.setRole(role);
       user.setPassword(password);
       user.setDepartment(dept);
       user.setName(name);
      

       stat=repo.addUser(user);
       }
        return stat;
    }

}
