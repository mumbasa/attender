
package com.attendance.controllers;

import com.attendance.data.YearMonthData;
import com.attendance.data.Staff;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.attendance.data.MonthAggregate;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import com.attendance.repos.HolidayRepository;
import com.attendance.repos.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.attendance.repos.AttendanceRepository;
import org.springframework.stereotype.Controller;

@Controller
public class AttendanceController
{
    @Autowired
    AttendanceRepository attendance;
    @Autowired
    StaffRepository staffRepo;
    @Autowired
    HolidayRepository rep;
    
    @ResponseBody
    @RequestMapping( "/get/attendance/year/{id}" )
    public List<MonthAggregate> getYearAttendanceSum(@PathVariable("id") final int id) {
        return this.attendance.yearAggregate(id);
    }
    
    @ResponseBody
    @RequestMapping( "/admin/delete/holiday/{id}" )
    public int deleteHoliday(@PathVariable("id") final int id) {
        System.err.println(String.valueOf(id) + "========");
        return this.rep.deleteHoliday(id);
    }
    
    @RequestMapping( "/admin/view/report" )
    public String getUsers(final Model model) {
        final List<Staff> staff = this.staffRepo.getStaff();
        System.err.println(staff.size());
        model.addAttribute("staff", (Object)staff);
        return "/admin/report";
    }
    
    @RequestMapping( "/admin/view/data" )
    public String getDta(final Model model) {
        final List<YearMonthData> att = this.attendance.getDataCountFrommonth();
        model.addAttribute("att", (Object)att);
        return "/admin/operation";
    }
    
    @RequestMapping( "/admin/delete/data/{month}/{year}" )
    public String geDelete(@PathVariable("year") final int year, @PathVariable("month") final int month) {
        this.attendance.deleteData(year, month);
        return "/admin/data";
    }
}
