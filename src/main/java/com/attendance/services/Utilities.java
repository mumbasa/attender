package com.attendance.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.attendance.data.Staff;
import com.attendance.data.WorkTime;
import com.attendance.repos.ShiftRepository;
import com.attendance.repositories.StaffRepositoies;
import com.attendance.repositories.WorkTimeRepository;

@Component

public class Utilities

{
	@Autowired
	StaffRepositoies staffRepository;

	@Autowired
	ShiftRepository shiftRepository;

	@Autowired
	WorkTimeRepository workTimeRepository;

	public static HashSet<String> getDateForLeave(final String a, final String b) {
		final HashSet<String> daysData = new HashSet<String>();
		final LocalDate d = LocalDate.parse(a.replace("/", "-"));
		final LocalDate f = LocalDate.parse(b.replace("/", "-"));
		for (int days = Period.between(d, f).getDays(), i = 0; i <= days; ++i) {
			final LocalDate n = d.plusDays(i);
			if (n.getDayOfWeek().getValue() < 6) {
				daysData.add(n.toString());
			}
		}
		return daysData;
	}

	public static HashSet<String> getDateinDays(final String a, final String b) {
		final HashSet<String> daysData = new HashSet<String>();
		final LocalDate d = LocalDate.parse(a.replace("/", "-"));
		final LocalDate f = LocalDate.parse(b.replace("/", "-"));
		for (int days = Period.between(d, f).getDays(), i = 0; i <= days; ++i) {
			final LocalDate n = d.plusDays(i);
			System.err.println(n.toString());
			daysData.add(n.toString());

		}
		return daysData;
	}

	public static List<String> getDaysinDays(final String a, final String b) {
		final List<String> daysData = new ArrayList<String>();
		final LocalDate d = LocalDate.parse(a.replace("/", "-"));
		final LocalDate f = LocalDate.parse(b.replace("/", "-"));
		for (int days = Period.between(d, f).getDays(), i = 0; i <= days; ++i) {
			final LocalDate n = d.plusDays(i);
		//	System.err.println(n.toString());
			daysData.add(n.toString());

		}
		return daysData;
	}

	public static long stringToMinutes(final String time) {
		final String[] times = time.split(":");
		return Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
	}

	public static String stringToTime(final long time) {
		return String
				.valueOf(String.format("%2s", new StringBuilder().append(time / 60L).toString()).replace(" ", "0")
						.replace("-", ""))
				+ ":" + String.format("%2s", new StringBuilder().append(time % 60L).toString()).replace(" ", "0")
						.replace("-", "");
	}

	public static String hourPadding(final String time) {
		final String[] times = time.split(":");
		final String hour = String.format("%2s", times[0]).replace(" ", "0");
		return String.valueOf(hour) + ":" + times[1] + ":" + "00";
	}

	public static String dateConvert(String date) {
		String pattern = "dd MMMM yyyy";
		Date oldDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			oldDate = df.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dates = simpleDateFormat.format(oldDate);
		return dates;
	}

	public void createExcel(int month, int deptId) {
		List<Staff> getStaff = staffRepository.findByDepartment(deptId);
		System.err.println(getStaff.size());
		List<WorkTime> getDeptShifts = workTimeRepository.findByDepartmentId(deptId);
		LocalDate start = LocalDate.of(LocalDate.now().getYear(), month, 1);
		LocalDate end = LocalDate.of(LocalDate.now().getYear(), month, start.getMonth().minLength());
		List<String> dates = getDaysinDays(start.toString(), end.toString());

		Workbook workbook = new XSSFWorkbook();

		// hidden sheet for list values
		Sheet sheet = workbook.createSheet("ListSheet");

		Row row;
		Name namedRange;
		String colLetter;
		String reference;

		int c = 0;
		// put the data in
		for (WorkTime key : getDeptShifts) {
			int r = 0;
			row = sheet.getRow(r);
			if (row == null)
				row = sheet.createRow(r);
			r++;
			String item = key.getId() + "";
			row.createCell(c).setCellValue(key.getName() + "|" + item);

			row = sheet.getRow(r);
			if (row == null)
				row = sheet.createRow(r);
			r++;
			row.createCell(c).setCellValue(item);

			// create names for the item list constraints, each named from the current key
			colLetter = CellReference.convertNumToColString(c);
			namedRange = workbook.createName();
			namedRange.setNameName("_" + key.getId());
			reference = "ListSheet!$" + colLetter + "$2:$" + colLetter + "$" + r;
			namedRange.setRefersToFormula(reference);
			c++;
		}

		// create name for Categories list constraint
		colLetter = CellReference.convertNumToColString((c - 1));
		namedRange = workbook.createName();
		namedRange.setNameName("Shifts");
		reference = "ListSheet!$A$1:$" + colLetter + "$1";
		namedRange.setRefersToFormula(reference);

		// unselect that sheet because we will hide it later
		sheet.setSelected(false);

		// visible data sheet
		sheet = workbook.createSheet("Sheet1");
		sheet.createRow(0).createCell(0).setCellValue("Id");
		sheet.getRow(0).createCell(1).setCellValue("Name");
		int cell = 2;
		for (String day : dates) {
			sheet.getRow(0).createCell(cell++).setCellValue(day);

		}

		sheet.setActiveCell(new CellAddress("A2"));

		for (int i = 0; i < getStaff.size(); i++) {
			row = sheet.createRow((short) i + 1);
			row.createCell(0).setCellValue(getStaff.get(i).getId());
			row.createCell(1).setCellValue(getStaff.get(i).getOtherNames() + " " + getStaff.get(i).getName());

		}
		sheet.autoSizeColumn(0, true);
		sheet.autoSizeColumn(1, true);

		// data validations
		DataValidationHelper dvHelper = sheet.getDataValidationHelper();
		// data validation for categories in A2:
		DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("Shifts");
		CellRangeAddressList addressList = new CellRangeAddressList(1, getStaff.size() + 1, 2, dates.size() + 2);
		DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
		sheet.addValidationData(validation);
		/*
		 * // data validation for items of the selected category in B2: dvConstraint =
		 * dvHelper.createFormulaListConstraint("Ranks"); addressList = new
		 * CellRangeAddressList(1, 11000, 5, 5); validation =
		 * dvHelper.createValidation(dvConstraint, addressList);
		 * sheet.addValidationData(validation);
		 */
		// hide the ListSheet
		workbook.setSheetHidden(0, true);
		workbook.setSheetHidden(1, false);

		// set Sheet1 active
		workbook.setActiveSheet(1);

		FileOutputStream out;
		try {
			out = new FileOutputStream("/home/bryan/Documents/nursing.xlsx");
			workbook.write(out);
			workbook.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
