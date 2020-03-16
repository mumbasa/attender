package com.attendance.controllers;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.attendance.data.Departments;
import com.attendance.data.KeyValue;
import com.attendance.data.Staff;
import com.attendance.data.StaffDisplay;
import com.attendance.repos.AttendanceRepository;
import com.attendance.repos.ConfgurationRepository;
import com.attendance.repos.HolidayRepository;
import com.attendance.repos.LeaveRepository;
import com.attendance.repos.StaffRepository;

@Controller


public class PageController {
	
	@Autowired
	HolidayRepository holiRepo;
	@Autowired
	StaffRepository staffRepo;
	@Autowired
	LeaveRepository leaveRepo;
	@Autowired
	AttendanceRepository attendanceRepo;
	@Autowired
	ConfgurationRepository configRepo;
	
	@RequestMapping("/admin/dashboard" )
	public String getDash(Model model) {
		List<StaffDisplay> top = attendanceRepo.getTopLateness(Calendar.getInstance().get(1)-3);
		List<StaffDisplay> abs = attendanceRepo.getTopAbsent(Calendar.getInstance().get(1)-3);
		List<StaffDisplay> def = attendanceRepo.getTopDeficit(Calendar.getInstance().get(1)-3);
		List<StaffDisplay> perf = attendanceRepo.getTopPerformance(Calendar.getInstance().get(1)-3);
		model.addAttribute("toplate", top);
		model.addAttribute("abs", abs);
		model.addAttribute("def", def);
		model.addAttribute("perf", perf);
		model.addAttribute("size", staffRepo.getStaffSize());
		model.addAttribute("deptsize", staffRepo.getDeptSize());
		model.addAttribute("leavesize", staffRepo.StaffOnLeaveSize());
		model.addAttribute("genstat", staffRepo.getStaffGendrRAtio());
		model.addAttribute("dbays", staffRepo.getStaffUpcomingBirtday());
		model.addAttribute("depperformance", staffRepo.getDeptStat());
		return "/admin/dashboard";
		
	}

	
	@RequestMapping("/admin/department/{id}/dashboard" )
	public String getDeptDash(@PathVariable("id") long id,Model model) {
		Departments dept= staffRepo.getDepartment(id); 
		model.addAttribute("dept", dept);
		model.addAttribute("deptatt", attendanceRepo.getDepartmentAgg(id));
		model.addAttribute("genstat", staffRepo.getStaffGendrRAtiobyDept(id));
		model.addAttribute("leavesize", staffRepo.staffOnLeaveSizeByDept(id));
		model.addAttribute("stafonleave", leaveRepo.getStaffOnLeveDAteResume(id));
		model.addAttribute("avgage", staffRepo.getDeptAvgAge(id));

		return "/admin/deptdash";
		
	}

	@RequestMapping("/admin/my/department/dashboard" )
	public String getDeptDash(Principal principal,Model model) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		System.out.println(staff.getDepartmentId());
		Departments dept= staffRepo.getDepartment(staff.getDepartmentId()); 
		model.addAttribute("dept", dept);
		model.addAttribute("deptatt", attendanceRepo.getDepartmentAgg(staff.getDepartmentId()));
		model.addAttribute("genstat", staffRepo.getStaffGendrRAtiobyDept(staff.getDepartmentId()));
		model.addAttribute("leavesize", staffRepo.staffOnLeaveSizeByDept(staff.getDepartmentId()));
		model.addAttribute("stafonleave", leaveRepo.getStaffOnLeveDAteResume(staff.getDepartmentId()));
		model.addAttribute("avgage", staffRepo.getDeptAvgAge(staff.getDepartmentId()));

		return "/admin/deptdash";
		
	}

	
	
	
	@RequestMapping("/admin/add/staff/category" )
	public String addStaffCategory() {
				return "/admin/addstaffType";
		
	}

	@RequestMapping("/admin/new/department" )
	public String addDepartment(Model model) {
		List<KeyValue> kv = configRepo.getBranches();
		model.addAttribute("branches", kv);
				return "/admin/adddepartment";
		
	}

	@RequestMapping("/admin/new/branch" )
	public String addBranch() {
				return "/admin/addbranch";
		
	}
	
	@ResponseBody
	@RequestMapping("/add/new/branch" )
	public int addBranch(@RequestParam("branch") String branch,@RequestParam("location") String location) {
				return configRepo.saveBranch(branch, location);
		
	}


	@ResponseBody
	@RequestMapping("/add/new/department" )
	public int addDepartment(@RequestParam("branch") String branch,@RequestParam("department") String department) {
				return configRepo.saveDepartment(department, branch);
		
	}
	

	@RequestMapping("/admin/new/staff/category")
	public String newCaetgory() {
		return "/admin/addstaffType";
	}
	
	 @RequestMapping( "/admin/staff/leave/arrears" )
	    public String newLeave( Model model) {

	        model.addAttribute("stafflist", staffRepo.getAllStaff());
	        System.out.println(staffRepo.getIds().size());
	        return "/admin/leaveinit";
	    }
	
	@ResponseBody
	@RequestMapping("/add/new/category" )
	public int addCategory(@RequestParam("category") String category,@RequestParam("days") String days) {
				return configRepo.saveCategory(category, days);
		
	}

	@ResponseBody
	@RequestMapping("/add/leave/arrears" )
	public int addCategory(@RequestParam("staff") String staffid,@RequestParam("days") String days,@RequestParam("year") String year) {
				return configRepo.saveStaffLeveArrears(staffid, days, year);
		
	}
	
}
