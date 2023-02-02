package com.attendance.controllers;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.attendance.data.Departments;
import com.attendance.data.KeyValue;
import com.attendance.data.Staff;
import com.attendance.data.StaffDisplay;
import com.attendance.repos.AttendancesRepository;
import com.attendance.repos.ConfgurationRepository;
import com.attendance.repos.HolidayRepository;
import com.attendance.repos.LeaveRepository;
import com.attendance.repos.StaffService;

@Controller

public class PageController {

	@Autowired
	HolidayRepository holiRepo;
	@Autowired
	StaffService staffRepo;
	@Autowired
	LeaveRepository leaveRepo;
	@Autowired
	AttendancesRepository attendanceRepo;
	@Autowired
	ConfgurationRepository configRepo;

	@RequestMapping("/admin/dashboard")
	public String getDash(Model model) {
		List<StaffDisplay> top = attendanceRepo.getTopLateness(Calendar.getInstance().get(1) - 3);
		List<StaffDisplay> abs = attendanceRepo.getTopAbsent(Calendar.getInstance().get(1) - 3);
		List<StaffDisplay> def = attendanceRepo.getTopDeficit(Calendar.getInstance().get(1) - 3);
		List<StaffDisplay> perf = attendanceRepo.getTopPerformance(Calendar.getInstance().get(1) - 3);
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
		return "admin/dashboard";

	}

	@RequestMapping("/admin/department/{id}/dashboard")
	public String getDeptDash(@PathVariable("id") long id, Model model) {
		Departments dept = staffRepo.getDepartment(id);
		model.addAttribute("dept", dept);
		model.addAttribute("deptatt", attendanceRepo.getDepartmentAgg(id));
		model.addAttribute("genstat", staffRepo.getStaffGendrRAtiobyDept(id));
		model.addAttribute("leavesize", staffRepo.staffOnLeaveSizeByDept(id));
		model.addAttribute("stafonleave", leaveRepo.getStaffOnLeveDAteResume(id));
		model.addAttribute("avgage", staffRepo.getDeptAvgAge(id));

		return "admin/deptdash";

	}

	@RequestMapping("/admin/my/department/dashboard")
	public String getDeptDash(Principal principal, Model model) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		System.out.println(staff.getDepartment().getId());
		Departments dept = staffRepo.getDepartment(staff.getDepartment().getId());
		model.addAttribute("dept", dept);
		model.addAttribute("deptatt", attendanceRepo.getDepartmentAgg(staff.getDepartment().getId()));
		model.addAttribute("genstat", staffRepo.getStaffGendrRAtiobyDept(staff.getDepartment().getId()));
		model.addAttribute("leavesize", staffRepo.staffOnLeaveSizeByDept(staff.getDepartment().getId()));
		model.addAttribute("stafonleave", leaveRepo.getStaffOnLeveDAteResume(staff.getDepartment().getId()));
		model.addAttribute("avgage", staffRepo.getDeptAvgAge(staff.getDepartment().getId()));

		return "admin/deptdash";

	}

	@RequestMapping("/admin/add/staff/category")
	public String addStaffCategory() {
		return "sys/addstaffType";

	}

	@RequestMapping("/admin/add/new/staff/type")
	public String addStaffType() {
		return "sys/addstaffType";

	}

	@RequestMapping("/admin/new/bank")
	public String addBank() {
		return "sys/addbank";

	}

	@RequestMapping("/admin/new/school")
	public String addSchool(Model model) {
		model.addAttribute("schools", configRepo.getSchools());
		return "sys/school";

	}

	@ResponseBody
	@RequestMapping("/delete/school/{id}")
	public int deleteSchool(@PathVariable("id") long id) {
		return configRepo.deleteSchool(id);

	}

	@RequestMapping("/admin/new/department")
	public String addDepartment(Model model) {
		List<KeyValue> kv = configRepo.getBranches();
		model.addAttribute("branches", kv);
		return "sys/adddepartment";

	}

	@RequestMapping("/admin/departments")
	public String addDepart(Model model) {
		List<Departments> kv = configRepo.getDepartments();
		model.addAttribute("branches", kv);
		return "admin/depts";

	}

	@RequestMapping("/admin/new/branch")
	public String addBranch() {
		return "sys/addbranch";

	}

	@ResponseBody
	@RequestMapping("/add/new/branch")
	public int addBranch(@RequestParam("branch") String branch, @RequestParam("location") String location) {
		return configRepo.saveBranch(branch, location);

	}

	@ResponseBody
	@RequestMapping("/add/new/department")
	public int addDepartment(@RequestParam("branch") String branch, @RequestParam("department") String department) {
		return configRepo.saveDepartment(department, branch);

	}

	@ResponseBody
	@GetMapping("/admin/delete/department/{id}")
	public int addDepartment(@PathVariable("id") int id) {
		return configRepo.deleteDepartment(id);

	}

	@RequestMapping("/admin/new/staff/category")
	public String newCaetgory() {
		return "sys/addstaffType";
	}

	@RequestMapping("/admin/staff/leave/arrears")
	public String newLeave(Model model) {

		model.addAttribute("stafflist", staffRepo.getAllStaff());
		System.out.println(staffRepo.getIds().size());
		return "admin/leaveinit";
	}

	@ResponseBody
	@RequestMapping("/add/new/category")
	public int addCategory(@RequestParam("category") String category, @RequestParam("days") String days,
			@RequestParam("emptype") String empType) {
		return configRepo.saveCategory(category, days, empType);

	}

	@ResponseBody
	@RequestMapping("/add/new/school")
	public int addSchool(@RequestParam("school") String school, @RequestParam("type") String type) {
		return configRepo.saveSchool(school, type);

	}

	@ResponseBody
	@RequestMapping("/add/new/bank")
	public int addCategory(@RequestParam("bank") String bank) {
		return configRepo.saveBank(bank);

	}

	@ResponseBody
	@RequestMapping("/add/new/qualification")
	public int addQualify(@RequestParam("cert") String cert) {
		return configRepo.saveQualif(cert);

	}

	@ResponseBody
	@RequestMapping("/delete/qualification/{id}")
	public int deleteQualification(@PathVariable("id") long id) {
		return configRepo.deleteQualifications(id);

	}

	@ResponseBody
	@RequestMapping("/add/leave/arrears")
	public int addLeaveArrears(@RequestParam("staff") String staffid, @RequestParam("days") String days,
			@RequestParam("year") String year) {
		return configRepo.saveStaffLeveArrears(staffid, days, year);

	}

	@RequestMapping("/admin/new/holiday")
	public String addholiday(Model model) {
		model.addAttribute("holidays", configRepo.getHolidays());
		return "sys/holiday";

	}

	@ResponseBody
	@RequestMapping("/add/new/holiday")
	public int addLeaveArrears(@RequestParam("holiday") String holiday, @RequestParam("date") String date) {
		return configRepo.saveHoliday(holiday, date);

	}

	@ResponseBody
	@GetMapping("/admin/delete/holidays/{id}")
	public int deleteHoliday(@PathVariable("id") int id) {
		return configRepo.deleteHoliday(id);

	}

	@RequestMapping("/admin/new/qualification")
	public String addq(Model model) {
		model.addAttribute("qualification", configRepo.getQualification());

		return "sys/addqualification";

	}
}
