package com.attendance.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.attendance.data.Shift;
import com.attendance.data.Staff;
import com.attendance.repos.ShiftRepository;
import com.attendance.repos.StaffRepository;
import com.attendance.services.Utilities;

import kong.unirest.json.JSONArray;

@Controller
public class ShiftController {

	@Autowired
	ShiftRepository shiftRepository;
	@Autowired
	StaffRepository staffRepository;
	
	@ResponseBody
	@RequestMapping("/admin/get/staff/{id}/shifts")
	public List<Shift> getStaffShift(@PathVariable("id") long id){
		
			return shiftRepository.getStaffShifts(id);
		
	}
	
	
	@ResponseBody
	@PostMapping("/admin/get/dept/shifts")
	public List<Shift> getStaffShift(Principal principal,@RequestParam("startDate") String start,@RequestParam("endDate") String end){
		Staff staff = staffRepository.getStaffByEmail(principal.getName());

			return shiftRepository.getDeptShifts(staff.getDepartmentId(),start,end);
		
	}
	
	@RequestMapping("/admin/staff/{id}/get/shifts")
	public String setStaffWorkTimes(Model model,@PathVariable("id") long id) {
		Staff staff = staffRepository.getStaffByID(id);
		model.addAttribute("staff", staff);
		model.addAttribute("shifts", shiftRepository.getStaffShifts(id));
		return "admin/staffshift";
	}
	
	
	
	@ResponseBody
	@PostMapping("/admin/add/staff/{id}/shiftdate")
	public int setUnavailableDays(@PathVariable("id") long id, @RequestParam("dates") JSONArray dates){
		
		return shiftRepository.saveStaffShift(dates, String.valueOf(id));
		
	}
	
	
	@ResponseBody
	@GetMapping("/admin/staff/delete/staffpref/{id}")
	public int delUnavailableDays(@PathVariable("id") long id){
		
		return shiftRepository.deletePrefDateShift(id);
		
	}
	
	
	@ResponseBody
	@PostMapping("/admin/add/staff/unavailable")
	public int setUnavailableDays(Principal principal, @RequestParam("dates") JSONArray dates) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());
		return shiftRepository.saveUnwantedShift(dates, String.valueOf(staff.getId()));

	}

	@RequestMapping("/admin/new/shift")
	public String newShift(Model model) {
		model.addAttribute("stafftypes", staffRepository.getStaffTypes());
		return "/admin/newshift";
	}
	
	@RequestMapping("/admin/put/shift")
	public String putShift(Model model,@RequestParam("date") String date,Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());

		model.addAttribute("staff", staffRepository.getStaffShiftReady(staff.getId(),date));
		model.addAttribute("shift",shiftRepository.getShiftTypes());
		model.addAttribute("date", Utilities.dateConvert(date) +" -");
		return "/admin/putstaffshit";
	}


	@ResponseBody
	@PostMapping("/admin/put/staff/shift")
	public void putShiftStaff(Model model,@RequestParam("date") String date,@RequestParam("staff") List<String> staff,@RequestParam("shift") int shiftType) {
		shiftRepository.saveStaffShifts(shiftType, staff, date);
		
	}


	
	
	@RequestMapping("/admin/set/staff/shift")
	public String newStaffShift(Model model) {
		model.addAttribute("stafftypes", staffRepository.getStaffTypes());
		return "/admin/setstaffshift";
	}

	@RequestMapping("/admin/department/staff/roster")
	public String newStaffShift(Model model, Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());

		model.addAttribute("staff", staffRepository.getShiftStaffInDepartment(staff.getDepartmentId()));
		return "/admin/shiftworkers";
	}

	@RequestMapping("/admin/my/unavailable/shifts")
	public String unavailableShifts(Model model, Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());
		model.addAttribute("shifts", shiftRepository.getStaffUnavbailableShifts(staff.getId()));
		return "/admin/unavailableshift";
	}

	
	@RequestMapping("/admin/my/staff/set/shifts")
	public String upertunavailableShifts(Model model, Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());
		model.addAttribute("shifts", shiftRepository.getStaffUnavbailableShifts(staff.getId()));
		return "/admin/dateshift";
	}

	
	
	@RequestMapping("/admin/department/roster")
	public String setDeptStaffWorkTimes(Model model,Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());
		model.addAttribute("staff", staff);
		model.addAttribute("shifts", shiftRepository.getDeptShifts(staff.getDepartmentId()));
		return "admin/staffshiftquery";
	}
	
	@ResponseBody
	@RequestMapping("/admin/get/staff/{id}/unavailable/shifts")
	public List<Shift> getStaffUnvailble(@PathVariable("id") long id) {
		return shiftRepository.getStaffUnavbailableShifts(id);
	}
	
	@ResponseBody
	@RequestMapping("/admin/delete/shift/{id}")
	public int deleteShift(@PathVariable("id") long id) {
		return shiftRepository.deleteShift(id);
	}
	
	@ResponseBody
	@RequestMapping("/admin/staff/shifts")
	public List<Shift> myStaffWorkTimes(Model model,Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());
		
		
		return shiftRepository.getDeptShifts();
	}
	@RequestMapping("/admin/my/shifts")
	public String setStaffWorkTimes(Model model,Principal principal) {
		Staff staff = staffRepository.getStaffByEmail(principal.getName());
		model.addAttribute("shifts", shiftRepository.getStaffShifts(staff.getId()));
		model.addAttribute("staff", staff);

		return "admin/staffshift";
	}
	
}
