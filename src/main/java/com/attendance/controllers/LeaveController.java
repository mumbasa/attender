
package com.attendance.controllers;

import com.attendance.data.KeyValue;
import com.attendance.data.Leave;
import com.attendance.data.Staff;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import com.attendance.data.StaffLeave;
import com.attendance.repos.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.attendance.repos.LeaveRepository;
import org.springframework.stereotype.Controller;

@Controller
public class LeaveController
{
    @Autowired
    LeaveRepository leaveRepository;
    @Autowired
    StaffRepository staffRepo;
    
    
    
    @RequestMapping("/get/server/ip")
	@ResponseBody
	public String ip() {
		try {
			DatagramSocket socketIP = new DatagramSocket();
			socketIP.connect(InetAddress.getByName("8.8.8.8"), 10002);
			String ips = socketIP.getLocalAddress().getHostAddress();
			socketIP.close();
			return ips;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
    @ResponseBody
    @PostMapping( "/add/leave" )
    public String addLeave(@RequestParam("staff") String id,@RequestParam("start") String from,@RequestParam("days") int days,@RequestParam("type") int type,@RequestParam("weekend") boolean wk) {
         List<StaffLeave> staffLeaves = new ArrayList<StaffLeave>();
        LocalDate endDate = LeaveRepository.getLeaveEndDate(from, days, wk);
         Map<String, List<String>> data = leaveRepository.getLeaveIntervals(from, endDate.toString());
        for ( List<String> a : data.values()) {
             StaffLeave staff = new StaffLeave();
            staff.setFrom(a.get(0));
            staff.setTo(a.get(1));
            staff.setStaffId(id);
            staff.setLeaveTypeid(type);
            staffLeaves.add(staff);
        }
        leaveRepository.insertStaffLeave(staffLeaves);
        return endDate.toString();
    }
    
    
    @ResponseBody
    @PostMapping( "/add/staff/leave" )
    public String staffApplyLeave(Principal principal,@RequestParam("start") String from,@RequestParam("days") int days,@RequestParam("type") int type,@RequestParam("weekend") boolean wk) {
         List<StaffLeave> staffLeaves = new ArrayList<StaffLeave>();
         Staff staff  = staffRepo.getStaffByEmail(principal.getName());
        LocalDate endDate = LeaveRepository.getLeaveEndDate(from, days, wk);
         Map<String, List<String>> data = leaveRepository.getLeaveIntervals(from, endDate.toString());
        for ( List<String> a : data.values()) {
             StaffLeave staffLeave = new StaffLeave();
             staffLeave.setFrom(a.get(0));
             staffLeave.setTo(a.get(1));
             staffLeave.setStaffId(staff.getId()+"");
             staffLeave.setLeaveTypeid(type);
            staffLeaves.add(staffLeave);
        }
        leaveRepository.insertStaffLeave(staffLeaves);
        return endDate.toString();
    }
    
    
    @ResponseBody
    @PostMapping( "/admin/add/staff/leave" )
    public int staffApplyLeave(Principal principal,@RequestParam("start") String from,@RequestParam("days") int days,@RequestParam("type") int type) {
        Staff staff  = staffRepo.getStaffByEmail(principal.getName());
        LocalDate endDate = LeaveRepository.getLeaveEndDate(from,days,staff.isWeekendWorker());

        
        return leaveRepository.saveStaffLeave(String.valueOf(staff.getId()), from,endDate.toString(), days,type);
    }
    
    
    
    @ResponseBody
    @RequestMapping( "/admin/hod/approve/leave" )
    public int hodApprove(@RequestParam("id") long id,@RequestParam("decide") String status) {
    	
          return leaveRepository.saveHODApproveLeave(id, status);
    }
    
    
    @ResponseBody
    @PostMapping( "/admin/query/leave" )
    public List<Leave> hodApprove(@RequestParam("start") String start,@RequestParam("end") String to) {
    	
          return leaveRepository.getStaffLeaveQuery(start, to);
    }

    @ResponseBody
    @RequestMapping( "/admin/hr/approve/leave" )
    public int hRApprove(@RequestParam("id") long id,@RequestParam("decide") int status) {
    	
    	if(status==1) {
    		Leave leave = leaveRepository.getLeaveById(id);
            List<StaffLeave> staffLeaves = new ArrayList<StaffLeave>();

        	Map<String, List<String>> data = leaveRepository.getLeaveIntervals(leave.getStart(), leave.getEnd());
            for ( List<String> a : data.values()) {
                 StaffLeave staffLeave = new StaffLeave();
                 staffLeave.setFrom(a.get(0));
                 staffLeave.setTo(a.get(1));
                 staffLeave.setStaffId(String.valueOf(leave.getStaffid()));
                 staffLeave.setLeaveTypeid(leave.getLeaveTypeId());
                staffLeaves.add(staffLeave);
            }
            leaveRepository.insertStaffLeave(staffLeaves,leave.getId());
    	}
          return leaveRepository.saveHRpproveLeave(id, ""+status);
    
    }
    
    @RequestMapping( "/admin/new/leave" )
    public String newLeave( Model model) {
         StaffLeave h = new StaffLeave();
        model.addAttribute("staffleave", h);
        model.addAttribute("stafflist", staffRepo.getStaff());
        model.addAttribute("leaves", leaveRepository.getLeaveTypes());
        System.out.println(staffRepo.getIds().size());
        return "/admin/addleave";
    }
    
    @RequestMapping( "/admin/add/leave" )
    public String newLeaves( Model model) {
         StaffLeave h = new StaffLeave();
        model.addAttribute("staffleave", h);
        model.addAttribute("stafflist", staffRepo.getStaff());
        model.addAttribute("leaves", leaveRepository.getLeaves());
        System.out.println(staffRepo.getIds().size());
        return "/admin/staffleave2";
    }
    
    @RequestMapping( "/admin/staff/on/leave" )
    public String staffOnLeaves( Model model) {
        model.addAttribute("leaves", leaveRepository.getStaffOnLeave());
        return "/admin/staffonleave";
    }
    
    @RequestMapping( "/admin/staff/pending/leave" )
    public String staffLeaves( Model model) {
        model.addAttribute("leaves", leaveRepository.getStaffPendingLeave());
        return "/admin/staffonleave";
    }
    
    
    @RequestMapping( "/admin/leave/data" )
    public String staffLeavesData( Model model) {
        return "/admin/leavequery";
    }
    
    
    
    @RequestMapping( "/admin/staff/apply/leave" )
    public String newstaffLeaves(Principal principal, Model model) {
    	Staff staff  = staffRepo.getStaffByEmail(principal.getName());
       model.addAttribute("days", leaveRepository.getStaffLeaveDataSummary(staff.getId()));
        model.addAttribute("leaves", leaveRepository.getLeaveTypes());
        return "/admin/staffapplyleave";
    }
    
    
    
    
    @RequestMapping( "/admin/my/leaves" )
    public String deleteLeaves(Principal principal,Model model) {
    	Staff staff  = staffRepo.getStaffByEmail(principal.getName());
        model.addAttribute("leaves", leaveRepository.getStaffLeavesApplications(staff.getId()));
        model.addAttribute("leavesum", leaveRepository.getStaffLeaveYears(staff.getId()));

        
        return "/admin/staffleaveapp";
    }
    
    
    @RequestMapping( "/admin/delete/leave" )
    public String deleteLeaves(@RequestParam("id")  int id) {
        leaveRepository.deleteLeave(id);
        return "redirect:/admin/get/leaves";
    }
    
    @RequestMapping( "/admin/staff/leaves" )
    public String getLeaves( Model model) {
         List<Leave> leaves = leaveRepository.getStaffHODApprovedLeavesApplications();
        model.addAttribute("staffleave", leaves);
        return "/admin/leaves";
    }
    
    
    
    
    @RequestMapping( "/admin/my/department/leave/applications" )
    public String getLeavesInDept(Principal principal, Model model) {
         Staff staff  = staffRepo.getStaffByEmail(principal.getName());
         List<Leave> leaves = leaveRepository.getStaffLeavesInDept(staff.getDepartmentId());

         model.addAttribute("staffleave", leaves);
        return "/admin/leaves";
    }
    
    @RequestMapping( "/admin/my/department/pending/leave/applications" )
    public String getLeavesInDeptPending(Principal principal, Model model) {
         Staff staff  = staffRepo.getStaffByEmail(principal.getName());
         List<Leave> leaves = leaveRepository.getStaffLeavesInDeptPending(staff.getDepartmentId());

         model.addAttribute("staffleave", leaves);
        return "/admin/leaves";
    }
    
    @RequestMapping( "/admin/my/staff/pending/leave/applications" )
    public String getLeavesStaffSupervisorPending(Principal principal, Model model) {
         Staff staff  = staffRepo.getStaffByEmail(principal.getName());
         List<Leave> leaves = leaveRepository.getStaffLeavesofSupertvisorPending(staff.getId());

         model.addAttribute("staffleave", leaves);
        return "/admin/supervisorleaveapprove";
    }
    
    @RequestMapping( "/admin/my/staff/on/leave" )
    public String getLeavesSupervisorStaffOnLeave(Principal principal, Model model) {
         Staff staff  = staffRepo.getStaffByEmail(principal.getName());
         List<Leave> leaves = leaveRepository.getMyStaffOnLeave(staff.getId());

         model.addAttribute("staffleave", leaves);
        return "/admin/staffOnleaveDays";
    }
    
    
	/*
	 * @RequestMapping( "/admin/my/staff/pending/leave/applications" ) public String
	 * getLeavesStaffSupervisorPending(Principal principal, Model model) { Staff
	 * staff = staffRepo.getStaffByEmail(principal.getName()); List<Leave> leaves =
	 * leaveRepository.getStaffLeavesofSupertvisorPending(staff.getId());
	 * 
	 * model.addAttribute("staffleave", leaves); return "/admin/leaves"; }
	 */
    
    @RequestMapping("/admin/mount/leaves")
    @ResponseBody
    public int mountLeaves() {
    	leaveRepository.mountLeaves();
    return 1;	
    }
    
    
    @RequestMapping("/admin/staff/leave/type/days")
    @ResponseBody
    public KeyValue mountLeaves(Principal principal,@RequestParam("id") long id) {
    	System.out.println("---"+id);
    	return leaveRepository.getLeaveTypeDays(id, staffRepo.getStaffByEmail(principal.getName()).getId());
    
    }
    
    
    @RequestMapping("/admin/get/staff/{id}/approved/leaves")
    @ResponseBody
    public List<Leave> getStaffApprovedLeaves(@PathVariable("id") long id) {
    return	leaveRepository.getStaffLeavesApproved(id);
    
    }
    
    
    
    
    
    
    @RequestMapping("/admin/staff/sum/leaves")
    public String mountLeave(Model model) {
    	model.addAttribute("days", leaveRepository.getLeaveDataSummary());
    return	"/admin/leavesdays";
    
    }
    
   
}
