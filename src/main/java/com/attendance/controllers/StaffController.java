
package com.attendance.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.attendance.data.Attendance;
import com.attendance.data.KeyValue;
import com.attendance.data.MonthAggregate;
import com.attendance.data.Shift;
import com.attendance.data.Staff;
import com.attendance.repos.AttendanceRepository;
import com.attendance.repos.LeaveRepository;
import com.attendance.repos.OvertimeRepository;
import com.attendance.repos.SchoolRepository;
import com.attendance.repos.ShiftRepository;
import com.attendance.repos.StaffRepository;
import com.attendance.repos.UserRepo;
import com.attendance.services.StaffExtractor;
import com.attendance.services.Utilities;

@Controller
public class StaffController {
	@Autowired
	StaffRepository staffRepo;
	@Autowired
	UserRepo repo;
	@Autowired
	ShiftRepository shiftRepo;

	@Autowired
	SchoolRepository schoolRepo;

	@Autowired
	OvertimeRepository overtimeRepo;

	private static String UPLOADED_FOLDER;
	@Autowired
	AttendanceRepository attendanceRepo;
	@Autowired
	LeaveRepository leaveeRepo;

	static {
		StaffController.UPLOADED_FOLDER = "/home/bryan/data/";
	}

	@RequestMapping("/admin/all/staff")
	public String getUsers(Model model) {
		List<Staff> staff = staffRepo.getStaff();
		List<String> gender = new ArrayList<String>();
		gender.add("Male");
		gender.add("Female");
		System.err.println(staff.size());
		model.addAttribute("staff", staff);
		model.addAttribute("depts", staffRepo.getDepartments());
		model.addAttribute("types", staffRepo.getStaffTypes());
		model.addAttribute("cat", staffRepo.getStaffCategory());
		model.addAttribute("rel", staffRepo.getReligion());
		model.addAttribute("rela", staffRepo.getRelation());
		model.addAttribute("marry", staffRepo.getRelationship());
		model.addAttribute("cert", staffRepo.getCert());
		model.addAttribute("gender", gender);

		model.addAttribute("countries", staffRepo.getCountries());

		return "/admin/allstaff";
	}

	@RequestMapping("/admin/my/department/staff")
	public String getDepartmenttaff(Principal principal, Model model) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		List<Staff> staffs = staffRepo.getStaffInDepartment(staff.getDepartmentId());
		model.addAttribute("staff", staffs);
		return "/admin/allstaff";
	}


	
	
	
	@RequestMapping("/admin/my/staff")
	public String getSuperviceortaff(Principal principal, Model model) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		List<Staff> staffs = staffRepo.getStaffOfSupervisor(staff.getId());
		model.addAttribute("staff", staffs);
		return "/supervisor/allstaff";
	}

	
	
	@RequestMapping("/admin/apply/overtime")
	public String applyOvertime(Model model) {
		List<Staff> staff = staffRepo.getStaff();

		model.addAttribute("staff", staff);
		return "/admin/newovertime";
	}

	@RequestMapping("/admin/my/biodata")
	public String mybiodata(Model model, Principal principal) {

		model.addAttribute("staff", staffRepo.getStaffByEmail(principal.getName()));
		return "/admin/biodata";
	}

	@RequestMapping("/admin/my/overtime")
	public String myOvertime(Model model, Principal principal) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		model.addAttribute("overtimes", overtimeRepo.getOvertimes(staff.getId()));
		return "/admin/myovertime";
	}

	@RequestMapping("/admin/my/kins")
	public String myKins(Model model, Principal principal) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		model.addAttribute("kins", staffRepo.getKin(staff.getId()));
		return "/admin/kins";
	}

	@RequestMapping("/admin/my/qualification")
	public String myQualification(Model model, Principal principal) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		model.addAttribute("edu", staffRepo.getEducation(staff.getId()));
		return "/admin/qualification";
	}
	
	@RequestMapping("/admin/supervisor/staff")
	public String supervisorStaff(Model model, Principal principal) {
	
		model.addAttribute("staff", staffRepo.getUnassignedStaff());
		return "/admin/supervisor";
	}
	
	
	@PostMapping("/admin/add/supervisor/staff")
	@ResponseBody
	public void addSupervisorStaff(@RequestParam("superid") long superId,@RequestParam("staffid") List<Long> staff) {
		staffRepo.addSupervisor(staff, superId);
	
	}

	@RequestMapping("/admin/my/emergency/contact")
	public String myEmergencyContact(Model model, Principal principal) {
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		model.addAttribute("contacts", staffRepo.getContacts(staff.getId()));
		return "/admin/emergency";
	}

	@RequestMapping("/admin/staff/{id}/set/shifts")
	public String unavailableShifts(Model model, @PathVariable("id") long id) {
		model.addAttribute("id", id);
		model.addAttribute("shifts", shiftRepo.getStaffUnavbailableShifts(id));
		return "/admin/setstaffshift";
	}

	@ResponseBody
	@RequestMapping("/admin/profile/user")
	public String delteUser(@RequestParam("id") String email) {
		System.out.println(email);
		return new StringBuilder().append(repo.deleteUser(email)).toString();
	}

	@ResponseBody
	@RequestMapping("/get/all/staff")
	public List<Staff> getAllStaff() {
		return staffRepo.getStaff();
	}

	@ResponseBody
	@RequestMapping("/admin/get/{id}/unavailable/shifts")
	public List<Shift> getStaffUnvailble(@PathVariable("id") String email) {
		Staff staff = staffRepo.getStaffByEmail(email);
		return shiftRepo.getStaffUnavbailableShifts(staff.getId());
	}

	@RequestMapping("/admin/profile")
	public String getLogss(@RequestParam("id") long id, Model model) {
		int[] dates = attendanceRepo.getMaxDate();
		Staff staff = staffRepo.getStaffByID(id);
		String summary = "";
		List<Attendance> att = attendanceRepo.getStaffAttendanceInYear(dates[1],
				new StringBuilder(String.valueOf(id)).toString());
		List<MonthAggregate> agg = attendanceRepo.getStaffYearAggregate(dates[1], id);
		List<String> sum = attendanceRepo.getStaffYearlyAggregateSummary(dates[1], id);
		double deficit = 0.0;
		try {
			summary = Utilities.stringToTime(Long.parseLong(sum.get(3).split("\\.")[0]));
			deficit = Double.parseDouble(sum.get(3));
		} catch (Exception e) {
			summary = "";
		}
		model.addAttribute("monthagg", agg);
		model.addAttribute("attendance", att);
		model.addAttribute("staff", staff);
		model.addAttribute("year", dates[1]);
		model.addAttribute("month", dates[0]);
		model.addAttribute("late", sum.get(0));
		model.addAttribute("age", staffRepo.getStaffAgeAge(id));
		model.addAttribute("year", staffRepo.getStaffYearsSpent(id));

		model.addAttribute("abs", sum.get(1));
		model.addAttribute("summary", summary);
		model.addAttribute("def", deficit);
		model.addAttribute("in", sum.get(2));
		return "/profile/profile";
	}

	@RequestMapping("/admin/my/profile")
	public String getLogss(Principal principal, Model model) {
		int[] dates = attendanceRepo.getMaxDate();
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		String summary = "";
		List<Attendance> att = attendanceRepo.getStaffAttendanceInYear(dates[1],
				new StringBuilder(String.valueOf(staff.getBioID())).toString());
		List<MonthAggregate> agg = attendanceRepo.getStaffYearAggregate(dates[1], staff.getId());
		List<String> sum = attendanceRepo.getStaffYearlyAggregateSummary(dates[1], staff.getId());
		double deficit = 0.0;
		try {
			summary = Utilities.stringToTime(Long.parseLong(sum.get(3).split("\\.")[0]));
			deficit = Double.parseDouble(sum.get(3));
		} catch (Exception e) {
			summary = "";
		}
		model.addAttribute("monthagg", agg);
		model.addAttribute("attendance", att);
		model.addAttribute("staff", staff);
		model.addAttribute("year", dates[1]);
		model.addAttribute("month", dates[0]);
		model.addAttribute("late", sum.get(0));
		model.addAttribute("age", staffRepo.getStaffAgeAge(staff.getId()));
		model.addAttribute("year", staffRepo.getStaffYearsSpent(staff.getId()));

		model.addAttribute("abs", sum.get(1));
		model.addAttribute("summary", summary);
		model.addAttribute("def", deficit);
		model.addAttribute("in", sum.get(2));
		return "/profile/profile";
	}

	@RequestMapping("/admin/my/attendance")
	public String getAttendance(Principal principal, Model model) {
		int[] dates = attendanceRepo.getMaxDate();
		Staff staff = staffRepo.getStaffByEmail(principal.getName());
		String summary = "";
		List<Attendance> att = attendanceRepo.getStaffAttendanceInYear(dates[1],
				new StringBuilder(String.valueOf(staff.getBioID())).toString());
		List<MonthAggregate> agg = attendanceRepo.getStaffYearAggregate(dates[1], staff.getId());
		List<String> sum = attendanceRepo.getStaffYearlyAggregateSummary(dates[1], staff.getId());
		double deficit = 0.0;
		try {
			summary = Utilities.stringToTime(Long.parseLong(sum.get(3).split("\\.")[0]));
			deficit = Double.parseDouble(sum.get(3));
		} catch (Exception e) {
			summary = "";
		}
		model.addAttribute("monthagg", agg);
		model.addAttribute("attendance", att);
		model.addAttribute("staff", staff);
		model.addAttribute("year", dates[1]);
		model.addAttribute("month", dates[0]);
		model.addAttribute("late", sum.get(0));
		model.addAttribute("age", staffRepo.getStaffAgeAge(staff.getId()));
		model.addAttribute("year", staffRepo.getStaffYearsSpent(staff.getId()));

		model.addAttribute("abs", sum.get(1));
		model.addAttribute("summary", summary);
		model.addAttribute("def", deficit);
		model.addAttribute("in", sum.get(2));
		return "/admin/attendance";
	}

	@RequestMapping("/admin/delete/staff")
	public String deleteUser(@RequestParam("id") long id) {
		System.err.println(staffRepo.deleteStaff(id));
		return "redirect:/admin/all/staff";
	}

	@ResponseBody
	@RequestMapping("/get/{staff}/agg/{year}")
	public List<MonthAggregate> getStaffMonthlyAggregate(@PathVariable("staff") long id,
			@PathVariable("year") int year) {
		System.out.println("----------------");
		return attendanceRepo.getStaffYearAggregate(year, id);
	}

	@PostMapping("/upload/staffbatch")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		System.err.println(file.getOriginalFilename());
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(String.valueOf(StaffController.UPLOADED_FOLDER) + file.getOriginalFilename(),
					new String[0]);
			Files.write(path, bytes, new OpenOption[0]);
			redirectAttributes.addFlashAttribute("message",
					("You successfully uploaded '" + file.getOriginalFilename() + "'"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		StaffExtractor ex = new StaffExtractor();
		ex.extract(String.valueOf(StaffController.UPLOADED_FOLDER) + file.getOriginalFilename());
		staffRepo.insertBatchStaff(ex.getStaffs());
		return "redirect:/admin/dashboard";
	}

	@RequestMapping("/admin/staff/batch")
	public String getLogs() {
		return "/admin/batch";
	}

	@RequestMapping("/admin/staff/{id}/profile")
	public String getProfile(@PathVariable("id") long id, Model model) {
		int[] dates = attendanceRepo.getMaxDate();
		model.addAttribute("staffid", id);
		Staff staff = null;
		try {
			staff = staffRepo.getStaffByID(id);
		} catch (Exception e) {
			staff = staffRepo.getStaffByBoid(id);

		}

		String summary = "";
		List<Attendance> att = attendanceRepo.getStaffAttendanceInYear(dates[1],
				new StringBuilder(String.valueOf(staff.getBioID())).toString());
		List<MonthAggregate> agg = attendanceRepo.getStaffYearAggregate(dates[1], staff.getBioID());
		List<String> sum = attendanceRepo.getStaffYearlyAggregateSummary(dates[1], staff.getBioID());
		long deficit = 0;
		try {
			summary = Utilities.stringToTime(Long.parseLong(sum.get(3).split("\\.")[0]));
			deficit = Long.parseLong(sum.get(3).split("\\.")[0]);

			model.addAttribute("late", Integer.parseInt(sum.get(0).split("\\.")[0]));
			model.addAttribute("abs", Integer.parseInt(sum.get(1).split("\\.")[0]));

		} catch (Exception e) {
			summary = "";
		}
		model.addAttribute("staff", staff);
		model.addAttribute("monthagg", agg);
		model.addAttribute("attendance", att);
		model.addAttribute("year", dates[1]);
		model.addAttribute("month", dates[0]);
		model.addAttribute("summary", summary);
		model.addAttribute("def", (deficit));
		model.addAttribute("in", sum.get(2));
		model.addAttribute("staffs", staffRepo.getStaffInDept(staff.getId()));
		model.addAttribute("leaves", leaveeRepo.getStaffLeaves(staff.getId()));
		model.addAttribute("age", staffRepo.getStaffAgeAge(staff.getId()));
		model.addAttribute("year", staffRepo.getStaffYearsSpent(staff.getId()));
		model.addAttribute("countries", staffRepo.getCountries());
		model.addAttribute("contacts", staffRepo.getContacts(staff.getId()));
		model.addAttribute("kins", staffRepo.getKin(staff.getId()));
		model.addAttribute("edu", staffRepo.getEducation(staff.getId()));
		model.addAttribute("schools", staffRepo.getSchools());

		return "/admin/profile";
	}
	
	@RequestMapping("/admin/my/staff/{id}/profile")
	public String getStaffDataProfile(@PathVariable("id") long id, Model model) {
		int[] dates = attendanceRepo.getMaxDate();
		model.addAttribute("staffid", id);
		Staff staff = null;
		try {
			staff = staffRepo.getStaffByID(id);
		} catch (Exception e) {
			staff = staffRepo.getStaffByBoid(id);

		}

		String summary = "";
		List<Attendance> att = attendanceRepo.getStaffAttendanceInYear(dates[1],
				new StringBuilder(String.valueOf(staff.getBioID())).toString());
		List<MonthAggregate> agg = attendanceRepo.getStaffYearAggregate(dates[1], staff.getBioID());
		List<String> sum = attendanceRepo.getStaffYearlyAggregateSummary(dates[1], staff.getBioID());
		long deficit = 0;
		try {
			summary = Utilities.stringToTime(Long.parseLong(sum.get(3).split("\\.")[0]));
			deficit = Long.parseLong(sum.get(3).split("\\.")[0]);

			model.addAttribute("late", Integer.parseInt(sum.get(0).split("\\.")[0]));
			model.addAttribute("abs", Integer.parseInt(sum.get(1).split("\\.")[0]));

		} catch (Exception e) {
			summary = "";
		}
		model.addAttribute("staff", staff);
		model.addAttribute("monthagg", agg);
		model.addAttribute("attendance", att);
		model.addAttribute("year", dates[1]);
		model.addAttribute("month", dates[0]);
		model.addAttribute("summary", summary);
		model.addAttribute("def", (deficit));
		model.addAttribute("in", sum.get(2));
		model.addAttribute("leaves", leaveeRepo.getStaffLeaves(staff.getId()));
		model.addAttribute("countries", staffRepo.getCountries());
		model.addAttribute("contacts", staffRepo.getContacts(staff.getId()));

		return "/admin/staffdataprofile";
	}

	@RequestMapping("/admin/get/staff/shifts")
	public String setStaffWorkTimes(Model model) {
		model.addAttribute("types", staffRepo.getStaffShifts());

		return "admin/staffshift";
	}

	@RequestMapping("/admin/staff/unavailable/shifts")
	public String setUnavailable(Model model) {
		model.addAttribute("types", staffRepo.getStaffShifts());

		return "admin/unavailable";
	}

	@RequestMapping("/admin/staff/apply/overtime")
	public String staffApplyOvertime(Model model) {
		model.addAttribute("types", staffRepo.getStaffShifts());

		return "admin/newstaffovertime";
	}

	@RequestMapping("/admin/get/shifts")
	public String setWorkTimes(Model model) {
		model.addAttribute("types", staffRepo.getStaffShifts());

		return "admin/shifts";
	}

	@RequestMapping("/admin/enrolstaff")
	public String getDashwer(Model model) {
		Staff staff = new Staff();
		model.addAttribute("staff", staff);
		model.addAttribute("depts", staffRepo.getDepartments());
		model.addAttribute("types", staffRepo.getStaffTypes());
		model.addAttribute("cat", staffRepo.getStaffCategory());
		model.addAttribute("countries", staffRepo.getCountries());
		model.addAttribute("religions", staffRepo.getReligions());
		model.addAttribute("banks", staffRepo.getBanks());
		return "/admin/enrollstaff";
	}

	@PostMapping("/admin/add/staff/{pic}")
	@ResponseBody
	public int addHoliday(@PathVariable("pic") String pic, @RequestParam("dob") String dob,
			@RequestParam("lname") String lname, @RequestParam("type") String type, @RequestParam("mobile") String mob,
			@RequestParam("address") String address, @RequestParam("fname") String fname, @RequestParam("tin") String tinNumber,
			@RequestParam("bioid") long bioid, @RequestParam("weekend") boolean weekendWorker, @RequestParam("address2") String add2,
			@RequestParam("email") String email, @RequestParam("cat") int cat, @RequestParam("sex") String sex, @RequestParam("accno") String accNo,
			@RequestParam("country") String country, @RequestParam("dept") String dept, @RequestParam("emp") String emp,
			@RequestParam("cert") String cert, @RequestParam("relation") String relation, @RequestParam("bank") int bankid,
			@RequestParam("ssn") String ssn, @RequestParam("religion") String rel,
			@RequestParam("staffid") String staffid) {
		Staff staff = new Staff();
		staff.setAddress(address);
		staff.setDob(dob);
		staff.setPicture(pic);
		staff.setDepartment(dept);
		staff.setMobile(mob);
		staff.setNationality(country);
		staff.setGender(sex);
		staff.setEmail(email);
		staff.setWeekendWorker(weekendWorker);
		staff.setName(lname);
		staff.setOtherNames(fname);
		staff.setStaffType(type);
		staff.setDateJoined(emp);
		staff.setReligion(rel);
		staff.setHighestQualification(cert);
		staff.setRelationshipStatus(relation);
		staff.setSocialSecurityNumber(ssn);
		staff.setStaffId(staffid);
		staff.setBioID(bioid);
		staff.setCategoryId(cat);
		staff.setTinNumber(tinNumber);
		staff.setAddress2(add2);
		staff.setBankid(bankid);
		staff.setAccNumber(accNo);
		return staffRepo.saveStaff(staff);

	}

	@PostMapping("/admin/update/staff/{id}")
	@ResponseBody
	public int updateStaff(@PathVariable("id") long id, @RequestParam("dob") String dob,
			@RequestParam("lname") String lname, @RequestParam("type") String type, @RequestParam("mobile") String mob,
			@RequestParam("address") String address, @RequestParam("fname") String fname,
			@RequestParam("bioid") long bioid, @RequestParam("weekend") boolean weekendWorker,
			@RequestParam("email") String email, @RequestParam("cat") int cat, @RequestParam("sex") String sex,
			@RequestParam("country") String country, @RequestParam("dept") String dept, @RequestParam("emp") String emp,
			@RequestParam("cert") String cert, @RequestParam("relation") String relation,
			@RequestParam("ssn") String ssn, @RequestParam("religion") String rel,
			@RequestParam("staffid") String staffid) {
		Staff staff = new Staff();
		staff.setAddress(address);
		staff.setDob(dob);

		staff.setDepartment(dept);
		staff.setMobile(mob);
		staff.setNationality(country);
		staff.setGender(sex);
		staff.setEmail(email);
		staff.setWeekendWorker(weekendWorker);
		staff.setName(lname);
		staff.setOtherNames(fname);
		staff.setStaffType(type);
		staff.setDateJoined(emp);
		staff.setReligion(rel);
		staff.setHighestQualification(cert);
		staff.setRelationshipStatus(relation);
		staff.setSocialSecurityNumber(ssn);
		staff.setStaffId(staffid);
		staff.setBioID(bioid);
		staff.setCategoryId(cat);
		return staffRepo.updateStaff(staff, id);

	}

	@ResponseBody
	@PostMapping("/admin/add/shift")
	public void addHoliday(@RequestParam("start") String start, @RequestParam("color") String color,
			@RequestParam("close") String close, @RequestParam("type") String type, @RequestParam("name") String name,
			@RequestParam("hours") int hours) {
		staffRepo.saveshift(name, Utilities.stringToMinutes(start), Utilities.stringToMinutes(close), type, hours,
				start, close, color);
	}

	@ResponseBody
	@RequestMapping("/add/staff/pic")
	public String homdes(@RequestParam("webcam") MultipartFile data) {
		System.err.println(data.getContentType());
		String filename = null;
		try {
			filename = System.currentTimeMillis() + ".jpg";
			byte[] bytes = data.getBytes();
			Path path = Paths.get("/home/bryan/Downloads/akoo/" + filename);
			Files.write(path, bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return filename;
	}

	@ResponseBody
	@RequestMapping("/admin/get/by/gender")
	public List<KeyValue> getGenderProportion() {
		return staffRepo.getStaffByGender();

	}

	@ResponseBody
	@RequestMapping("/admin/dept/{id}/by/gender")
	public List<KeyValue> getGenderProportion(@PathVariable("id") long id) {
		return staffRepo.getDeptStaffByGender(id);

	}

	@ResponseBody
	@RequestMapping("/admin/get/by/age")
	public List<KeyValue> getAgeProportion() {
		return staffRepo.getByAges();

	}

	@ResponseBody
	@RequestMapping("/admin/get/by/age/by/dept/{id}")
	public List<KeyValue> getAgeProportions(@PathVariable("id") long dept) {
		return staffRepo.getByAges(dept);

	}

	@ResponseBody
	@RequestMapping("/admin/dept/lateness/{id}")
	public List<KeyValue> getLateness(@PathVariable("id") long dept) {
		return staffRepo.getDeprtmentlatenessCount(dept);

	}

	@ResponseBody
	@RequestMapping("/admin/staff/delete/kin/{id}")
	public int deleteKin(@PathVariable("id") long id) {
		return staffRepo.deleteKin(id);

	}

	@ResponseBody
	@RequestMapping("/admin/staff/delete/contact/{id}")
	public int deleteCont(@PathVariable("id") long id) {
		return staffRepo.deleteContact(id);

	}

	@ResponseBody
	@RequestMapping("/admin/delete/staff/{id}")
	public int deleteStaffData(@PathVariable("id") long id) {
		return staffRepo.deleteStaff(id);

	}

	@ResponseBody
	@RequestMapping("/admin/staff/update/contact/{id}")
	public int updateContact(@PathVariable("id") long id, @RequestParam("name") String name,
			@RequestParam("contact") String contact) {
		return staffRepo.updateContact(id, name, contact);

	}

	@ResponseBody
	@RequestMapping("/admin/staff/update/kin/{id}")
	public int updsateKin(@PathVariable("id") long id, @RequestParam("name") String name,
			@RequestParam("contact") String contact, @RequestParam("gender") String gender,
			@RequestParam("relation") String relation) {
		return staffRepo.updateKin(id, name, relation, gender, contact);

	}

	@ResponseBody
	@RequestMapping("/admin/get/by/deficit")
	public List<KeyValue> getMonthlyDefcit() {
		return staffRepo.getAttendanceDeficitsMonthly();

	}

	@ResponseBody
	@RequestMapping("/admin/get/dept/{id}/deficit")
	public List<KeyValue> getMonthlyDefcitDepy(@PathVariable("id") long id) {
		return staffRepo.getAttendanceDeficitsMonthly(id);

	}

	@ResponseBody
	@PostMapping("/admin/staff/add/kin")
	public int addKin(@RequestParam("name") String name, @RequestParam("contact") String contact, @RequestParam("id") String id,
			@RequestParam("relation") String relationship, @RequestParam("gender") String gender, Principal principal) {
		return staffRepo.addKin(name, relationship, gender, contact, id);

	}

	@ResponseBody
	@PostMapping("/admin/staff/add/emergency/")
	public int addContact(@RequestParam("name") String name, @RequestParam("contact") String contact,@RequestParam("relation") String relationship,
			@RequestParam("id") String id) {
	
		return staffRepo.addContact(name, contact, id,relationship);

	}

	@ResponseBody
	@PostMapping("/admin/staff/add/education")
	public int addQualification(@RequestParam("name") String name, @RequestParam("cert") String cert,
			@RequestParam("start") String start, @RequestParam("end") String to,@RequestParam("id") String id) {
	
		return staffRepo.addQualification(name, cert, start, to, id);

	}

	@ResponseBody
	@RequestMapping("/admin/staff/delete/education/{id}")
	public int deleteQualification(@PathVariable("id") long id) {
		return staffRepo.deleteEducation(id);

	}

	@ResponseBody
	@RequestMapping("/add/schools")
	public int schoolRepo() {
		schoolRepo.insertHighSchool();
	
		return 1;
	}

	@ResponseBody
	@RequestMapping("/add/universities")
	public int tertiary() {
		schoolRepo.insertUniversity();
		return 1;
	}
	
	@ResponseBody
	@RequestMapping("/add/bank")
	public int banksAdd() {
		schoolRepo.insertBank();
		return 1;
	}

	@ResponseBody
	@RequestMapping("/admin/staff/update/education/{id}")
	public int updateEducation(@PathVariable("id") long id, @RequestParam("name") String name,
			@RequestParam("start") String start, @RequestParam("end") String end, @RequestParam("cert") String cert) {
		return staffRepo.updateEducation(id, name, cert, start, end);

	}

	@ResponseBody
	@RequestMapping("/admin/get/staff/{id}/deficit")
	public List<KeyValue> getMonthlyDefcit(@PathVariable("id") long id) {
		return staffRepo.getStaffAttendanceDeficitsMonthly(id);

	}

	@ResponseBody
	@RequestMapping("/admin/get/staff/{id}/attendance/label")
	public List<KeyValue> getMonthlyAttendanceLabel(@PathVariable("id") long id) {
		return staffRepo.getAttendanceStackMonthly(id);

	}
}
