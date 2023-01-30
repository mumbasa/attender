package com.attendance.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.attendance.data.Departments;
import com.attendance.data.Holiday;
import com.attendance.repos.HolidayRepository;
import com.attendance.repos.LeaveRepository;
import com.attendance.repos.StaffService;
import com.attendance.repos.UserRepo;
import com.attendance.repositories.AttendanceRepository;
import com.attendance.services.AttendanceService;

@Controller
public class RegistrationController {
	@Autowired
	UserRepo repo;
	@Autowired
	HolidayRepository holiRepo;
	@Autowired
	StaffService staffRepo;
	@Autowired
	LeaveRepository leaveRepo;
	@Autowired
	AttendanceRepository attendanceRepo;
	@Value("${spring.application.name}")
	String appName;
	@Autowired
	AttendanceService attendanceService;
	@Value("${app.upload.file.location}")
	private String UPLOADED_FOLDER;

	@RequestMapping("/" )
	public String getHome() {
		return "redirect:/admin/dashboard";
	}

	

	

	public String getDash(Model model) {
		
		return "admin/dashboard";
	}

	

	@RequestMapping("/login" )
	public String getCalendar() {
		return "admin/login";
	}

	@RequestMapping("/admin/holidays/current" )
	public String holidays(Model model) {
		model.addAttribute("holidays", holiRepo.getHolidays(LocalDate.now().getYear()));
		model.addAttribute("year", LocalDate.now().getYear());
		return "admin/holidays";
	}

	@RequestMapping("/admin/add/department" )
	public String addDepartment(Model model) {
		Departments dept = new Departments();
		model.addAttribute("dept", dept);
		return "admin/adddepartment";
	}

	@RequestMapping("/admin/all/departments" )
	public String departments(Model model) {
		model.addAttribute("dept", staffRepo.getDepartments());
		return "admin/departments";
	}

	@RequestMapping("/admin/upload/rosters" )
	public String rosters(Model model) {
	
		return "admin/uploadroster";
	}
	
	@RequestMapping("/admin/newholiday" )
	public String newHoliday(Model model) {
		Holiday h = new Holiday();
		model.addAttribute("holiday", h);
		return "admin/newholiday";
	}

	@PostMapping("/addholiday" )
	public String addHoliday(@ModelAttribute("holiday") Holiday day) {
		holiRepo.addBreaks(day.getFrom(), day.getTo(), day.getType(), day.getHoliday());
		return "admin/dashboard";
	}

	@PostMapping("/add/department" )
	public String addDept(@ModelAttribute("dept") Departments day) {
		staffRepo.addDept(day);
		return "redirect:/admin/dashboard";
	}

	@RequestMapping("/admin/upload/attendance" )
	public String uploadAttendance() {
		return "admin/uploadreport";
	}

	@PostMapping("/upload" )
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		System.err.println(file.getOriginalFilename());
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(String.valueOf(UPLOADED_FOLDER) + file.getOriginalFilename(), new String[0]);
			Files.write(path, bytes, new OpenOption[0]);
			redirectAttributes.addFlashAttribute("message",
					("You successfully uploaded '" + file.getOriginalFilename() + "'"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.err.println(holiRepo.getDaysWithoutHoliday(file.getOriginalFilename()).size());
		return "redirect:/admin/dashboard";
	}

	@PostMapping("/upload/staffattendance" )
	public String singleAttendanceUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		System.err.println(file.getOriginalFilename());
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(String.valueOf(UPLOADED_FOLDER) + appName+"/"+file.getOriginalFilename(), new String[0]);
			Files.write(path, bytes, new OpenOption[0]);
			redirectAttributes.addFlashAttribute("message",
					("You successfully uploaded '" + file.getOriginalFilename() + "'"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//attendanceRepo.addAttendanceBatch(String.valueOf(UPLOADED_FOLDER) + file.getOriginalFilename());
		attendanceService.boop2(String.valueOf(UPLOADED_FOLDER) + appName+"/"+file.getOriginalFilename());

		return "redirect:/admin/dashboard";
	}
	
	
	@PostMapping("/upload/roster" )
	public String roster(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		System.err.println(file.getOriginalFilename());
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(String.valueOf(UPLOADED_FOLDER)  + appName+"/"+ file.getOriginalFilename(), new String[0]);
			Files.write(path, bytes, new OpenOption[0]);
			redirectAttributes.addFlashAttribute("message",
					("You successfully uploaded '" + file.getOriginalFilename() + "'"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		attendanceService.readFile((String.valueOf(UPLOADED_FOLDER) + appName+"/"+file.getOriginalFilename()));
		return "redirect:/admin/dashboard";
	}
}
