package com.attendance.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.attendance.data.KeyValue;
import com.attendance.data.Overtime;
import com.attendance.data.Staff;
import com.attendance.repos.OvertimeRepository;
import com.attendance.repos.StaffRepository;

@Controller
public class OvertimeController {

	@Autowired
	OvertimeRepository overtimeRepo;
	@Autowired
	StaffRepository staffRepo;
	
	@PostMapping("/admin/add/staff/overtime")
	@ResponseBody
	public int addStaffOvertime(@RequestParam("staff") long staffId,@RequestParam("reason") String reason,@RequestParam("date") String date,@RequestParam("starttime") String starttime,@RequestParam("endtime") String endtime) {
		
		return overtimeRepo.saveOvertime(staffId, staffRepo.getStaffByID(staffId).getDepartment(), reason,date,starttime.replace("T", " "),endtime.replace("T", " "));
		
	}
	
	@PostMapping("/admin/staff/apply/overtime")
	@ResponseBody
	public int addStaffOvertimeapply(Principal principal , @RequestParam("reason") String reason,@RequestParam("date") String date,@RequestParam("starttime") String starttime,@RequestParam("endtime") String endtime) {
	
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		return overtimeRepo.saveOvertime(staff.getId(), staff.getDepartment(), reason,date,starttime.replace("T", " "),endtime.replace("T", " "));
		
	}
	
	
	/*
	 * @RequestMapping("/admin/my/department/overtime/applications") public String
	 * myOvertime(Model model,Principal principal) { Staff staff =
	 * staffRepo.getStaffByEmail(principal.getName());
	 * model.addAttribute("overtimes",overtimeRepo.getOvertimesDeptPending(staff.
	 * getDepartmentId()) ); return "/admin/overtimeapps"; }
	 */
	
	@RequestMapping("/admin/my/staff/overtime/applications")
	public String myStaffOvertime(Model model,Principal principal) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		model.addAttribute("overtimes",overtimeRepo.getOvertimesStaffPending(staff.getId()) );
		return "/admin/overtimeapps";
	}
	
	
	@RequestMapping("/admin/overtime/applications")
	public String myOvertime(Model model) {
		
		model.addAttribute("overtimes",overtimeRepo.getOvertimesApproved() );
		return "/admin/overtimeapplications";
	}
	
	
	@RequestMapping("/admin/overtime/agg/data")
	public String myOvertimes() {
		
		return "/admin/otquery";
	}
	
	
	@RequestMapping("/admin/overtime/agg/month")
	public String myMonthOvertimes(Model model) {
		model.addAttribute("agg", overtimeRepo.getOvertimesMonthAggraegate());
		return "/admin/otmonth";
	}
	
	@RequestMapping("/admin/my/department/overtime/query")
	public String deptOertime(Model model,Principal principal) {
		return "/admin/queryovertime";
	}

	@ResponseBody
	@RequestMapping("/admin/approve/overtime")
	public int go(@RequestParam("id") long id,@RequestParam("decide") String decide){
		System.err.println(id);
		return overtimeRepo.decide(id, decide);
	}
	
	
	@ResponseBody
	@RequestMapping("/admin/query/overtime")
	public List<Overtime> go(@RequestParam("from") String from,@RequestParam("to") String to,Principal principal){
		Staff staff = staffRepo.getStaffByEmail(principal.getName());

		return overtimeRepo.getOvertimesDeptApproved(staff.getDepartmentId(), from,to);
	}
	
	
	@ResponseBody
	@RequestMapping("/admin/query/agg/overtime")
	public List<KeyValue> overtimeQuery(@RequestParam("from") String from,@RequestParam("to") String to,Principal principal){
		

		return overtimeRepo.getOvertimesApprovedAggraegate(from,to);
	}
	
	
}
