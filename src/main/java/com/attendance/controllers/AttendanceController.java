
package com.attendance.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.attendance.data.MonthAggregate;
import com.attendance.data.Staff;
import com.attendance.data.YearMonthData;
import com.attendance.repos.AttendancesRepository;
import com.attendance.repos.HolidayRepository;

@Controller
public class AttendanceController
{
    @Autowired
    AttendancesRepository attendance;
    
    
   
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
        List<Staff> staff = attendance.getAllStaff();
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
