package com.attendance.services;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.attendance.data.Attendances;
import com.attendance.data.DayData;
import com.attendance.data.Holiday;
import com.attendance.data.Staff;
import com.attendance.data.StaffData;
import com.attendance.data.StaffShift;
import com.attendance.data.WorkTime;
import com.attendance.models.Attendance;
import com.attendance.repos.HolidayRepository;
import com.attendance.repos.LeaveRepository;
import com.attendance.repos.ShiftRepository;
import com.attendance.repositories.AttendanceRepository;
import com.attendance.repositories.StaffRepositoies;
import com.attendance.repositories.StaffShiftRepo;
import com.attendance.repositories.WorkTimeRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class AttendanceService {
	@Autowired
	AttendanceRepository attendanceRepository;

	@Value("${spring.application.name}")
	String appName;

	@Autowired
	JdbcTemplate template;

	@Value("${app.upload.file.location}")
	String folderLocation;

	@Autowired
	StaffRepositoies staffRepository;

	@Autowired
	ShiftRepository shiftRepository;

	@Autowired
	HolidayRepository holidayRepo;
	@Autowired
	StaffShiftRepo shiftRepos;

	@Autowired
	LeaveRepository leaveRepository;

	@Autowired
	WorkTimeRepository workTimeRepository;

	public List<Attendances> getStaffAttendanceInMonth(int year, int month, long staff) {

		final String sql = "SELECT * FROM attendance.attendance where staffid=? and month(date)=? and year(date)=? order by date";
		SqlRowSet rs = template.queryForRowSet(sql, staff, month, year);
		List<Attendances> attendances = new ArrayList<Attendances>();
		while (rs.next()) {
			Attendances attendance = new Attendances();
			attendance.setDate(rs.getString(8));
			attendance.setClosedEarly((rs.getString(9) == null) ? "Absent" : rs.getString(9));
			attendance.setId(rs.getString(2));
			attendance.setTimeOut((rs.getString(4) == null) ? "Absent" : rs.getString(4));
			attendance.setTimeIn((rs.getString(3) == null) ? "Absent" : rs.getString(3));
			attendance.setLabel((rs.getString(5) == null) ? "Absent" : rs.getString(5));
			attendance.setHoursWorked(rs.getLong(7));
			attendance.setDeficit(rs.getLong(12));
			attendance.setLateness(rs.getLong(6));
			attendances.add(attendance);

		}
		return attendances;
	}

	public Map<Long, StaffData> getStaffShift(int year, int month) {
		List<Staff> staffer = staffRepository.findAll();
		Map<Long, Staff> idMap = new HashMap<Long, Staff>();
		for (Staff staff : staffer) {
			idMap.put(staff.getId(), staff);

		}

		List<StaffShift> daga = shiftRepos.findShiftsByMonthAndYear(month, year);
		Map<Long, StaffData> latter = new HashMap<Long, StaffData>();
		for (StaffShift s : daga) {
			if (!latter.keySet().contains(s.getStaffid())) {
				StaffData staffData = new StaffData();
				staffData.setId(s.getStaffid());

				DayData dayData = new DayData();
				dayData.setDay(s.getDate());
				dayData.setTimeIn(s.getWorkTime().getStartstring());
				dayData.setTimeOut(s.getWorkTime().getClosestring());

				Map<String, DayData> ddData = new HashMap<String, DayData>();
				ddData.put(s.getDate(), dayData);
				staffData.setDayData(ddData);
				try {
					latter.put(idMap.get(s.getStaffid()).getBioid(), staffData);
				} catch (Exception e) {
					// TODO: handle exception
					System.err.println(s.getStaffid() + " idddddd");

				}
			} else {
				DayData dayData = new DayData();
				dayData.setDay(s.getDate());
				dayData.setTimeIn(s.getWorkTime().getStartstring());
				dayData.setTimeOut(s.getWorkTime().getClosestring());
				try {
					latter.get(idMap.get(s.getStaffid()).getBioid()).getDayData().put(s.getDate(), dayData);
				} catch (Exception e) {
					StaffData staffData = new StaffData();
					staffData.setId(s.getStaffid());

					DayData dayData1 = new DayData();
					dayData1.setDay(s.getDate());
					dayData1.setTimeIn(s.getWorkTime().getStartstring());
					dayData1.setTimeOut(s.getWorkTime().getClosestring());

					Map<String, DayData> ddData = new HashMap<String, DayData>();
					ddData.put(s.getDate(), dayData1);
					staffData.setDayData(ddData);
					try {
						latter.put(idMap.get(s.getStaffid()).getBioid(), staffData);
					} catch (Exception es) {
						// TODO: handle exception
						System.err.println(s.getStaffid() + "=== error");
					}
				}
			}

		}
		System.err.println(latter.keySet() + "///////////////////");
		return latter;
	}

	public String createAttendanceReport(int month, int year, Staff staff) {
		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());
		System.err.println(month + "/" + year + " ---------" + staff.getId());
		List<Attendances> attendance = getStaffAttendanceInMonth(year, month, (staff.getBioid()));
		long timeWorked = attendance.stream().mapToLong(Attendances::getHoursWorked).sum();
		long lateness = attendance.stream().mapToLong(Attendances::getLateness).sum();
		long deficit = attendance.stream().mapToLong(Attendances::getDeficit).sum();

		System.err.println(" lists " + attendance.size() + "\t" + timeWorked);
		String fileName = System.currentTimeMillis() + staff.getOtherNames() + staff.getBioid() + ".pdf";
		File file = new File(folderLocation+appName + fileName);

		Document document = new Document(PageSize.LEGAL);
		// document.setMargins(0.0f, 0.0f, 15.0f, 2.0f);
		try {
			Image jpg = Image.getInstance("unnamed.png");
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			BaseFont helvetica = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED);
			Font font = new Font(helvetica, 10, Font.NORMAL);

			// step 3: we open the document
			document.open();
			document.add(jpg);
			PdfPTable aggreTable = new PdfPTable(5);
			aggreTable.setSpacingBefore(30);
			aggreTable.setWidthPercentage(100);
			aggreTable.addCell(new Paragraph("Name :" + staff.getOtherNames() + " " + staff.getName(), font));
			aggreTable.addCell(new Paragraph(start.getMonth().name() + " / " + end.getYear(), font));
			aggreTable.addCell(
					new Paragraph("Time worked:" + timeWorked / 60 + "h :" + timeWorked % 60 + " m", font));
			PdfPCell latenessDisplay = new PdfPCell(
					new Paragraph("Lateness:" + lateness / 60 + "h :" + lateness % 60 + " m", font));
			if (lateness > 0) {
				latenessDisplay.setBackgroundColor(new Color(0xff, 0x50, 0x50));
			} else {
				latenessDisplay.setBackgroundColor(new Color(0x00, 0xff, 0x10));

			}

			PdfPCell deficitDisplay = new PdfPCell(
					new Paragraph("Deficit:" + deficit / 60 + "h:" + deficit % 60 + " m", font));
			if (deficit < 0) {
				deficitDisplay.setBackgroundColor(new Color(0xff, 0x50, 0x50));
			} else {
				deficitDisplay.setBackgroundColor(new Color(0x00, 0xff, 0x10));

			}
			aggreTable.addCell(latenessDisplay);

			aggreTable.addCell(deficitDisplay);
			document.add(aggreTable);

			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			table.setSpacingBefore(15f);
			PdfPCell cell = new PdfPCell();

			cell.setColspan(6);

			table.addCell(new Paragraph("Date", font));
			table.addCell(new Paragraph("Time In", font));
			table.addCell(new Paragraph("Time Out", font));
			table.addCell(new Paragraph("Time Worked", font));
			table.addCell(new Paragraph("Minutes Late", font));
			table.addCell(new Paragraph("Deficit", font));

			for (Attendances a : attendance) {

				cell = new PdfPCell(new Paragraph(a.getDate().toString(), font));

				if (a.getLabel().contains("Late")) {
					cell.setBackgroundColor(new Color(0xff, 0x50, 0x50));
				}

				table.addCell(cell);
				helvetica = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.EMBEDDED);
				font = new Font(helvetica, 10, Font.NORMAL);
				table.addCell(new Paragraph(a.getTimeIn(), font));
				table.addCell(new Paragraph(a.getTimeOut(), font));
				table.addCell(new Paragraph(String.valueOf(a.getHoursWorked()), font));
				table.addCell(new Paragraph(String.valueOf(a.getLateness()), font));
				table.addCell(new Paragraph(String.valueOf(a.getDeficit()), font));

			}

			document.add(table);

			// step 4: we grab the ContentByte and do some stuff with it
			PdfContentByte cb = writer.getDirectContent();
			cb.closePath();
			// cb.

		} catch (DocumentException | IOException e) {
			System.err.println(e.getMessage());
		}

		// step 5: we close the document
		document.close();

		// file.delete();
		return fileName;

	}

	public String createExcel(int month, int year, long deptId) {
		List<Staff> getStaff = staffRepository.findByDepartment(deptId);
		System.err.println(getStaff.size());
		List<WorkTime> getDeptShifts = workTimeRepository.findByDepartmentId(deptId);
		LocalDate start = LocalDate.of(LocalDate.now().getYear(), month, 1);
		LocalDate end = LocalDate.of(LocalDate.now().getYear(), month, start.getMonth().minLength());
		List<String> dates = Utilities.getDaysinDays(start.toString(), end.toString());

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
		String fileName = folderLocation+appName+ System.currentTimeMillis() + "-"
				+ deptId + ".xlsx";

		try {
			out = new FileOutputStream(fileName);
			workbook.write(out);
			workbook.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	}

	public String boop(String fileName) {
		String dateLine = null;
		List<String> lines = null;
		try {
			Path path = Paths.get(fileName);

			lines = Files.readAllLines(path);
			dateLine = lines.get(0).split("\t")[1].split(" ")[0];
			System.err.println(dateLine + "00000000000000000000");

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Attendance> attendances = new ArrayList<Attendance>();
		int month = Integer.parseInt(dateLine.split("-")[1]);
		int year = Integer.parseInt(dateLine.split("-")[0]);
		List<String> holidays = holidayRepo.getHolidays(year, month).stream().map(Holiday::getRealHoliday)
				.collect(Collectors.toList());

		List<Staff> staffer = staffRepository.findAll();

		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());

		// setting up days of the month to remove the holidays
		Set<String> dates = (Utilities.getDateinDays(start.toString(), end.toString()));
		dates.removeAll(holidays);
		Set<String> ids = new HashSet<String>();
		HashMap<Long, StaffData> staffD = new HashMap<Long, StaffData>();
		HashMap<Long, Long> staffBioIDs = new HashMap<Long, Long>();
		HashMap<Long, StaffData> staffShift = new HashMap<Long, StaffData>();

		for (Staff s : staffer) {
			// setting biodids for other thiNgs
			staffBioIDs.put(s.getId(), s.getBioid());

			// creating a set to contain date to remove leaves

			Set<String> datesStaff = new HashSet<String>();
			// setting up the dates of the month
			datesStaff.addAll(dates);
			// removing leave days
			datesStaff.removeAll(leaveRepository.getLeavesDatesForAttendance(s.getId(), month, year));
			StaffData sd = new StaffData();
			sd.setId(s.getBioid());
			sd.setType(s.getStatus().getId());
			Map<String, DayData> dt = new HashMap<String, DayData>();
			for (String date : datesStaff) {
				DayData data = new DayData();
				data.setDay(date);
				dt.putIfAbsent(date, data);
			}
			sd.setDayData(dt);
			staffD.put(s.getBioid(), sd);
			//

		}

		// getting staff shift for the month

		shiftRepos.findShiftsByMonthAndYear(month, year).forEach(e -> {
			long bioId = staffBioIDs.get(e.getStaffid());
			if (staffShift.containsKey(bioId)) {

				// adding date if attendance is recorded despite holiday

				DayData data = new DayData();
				data.setDay(e.getDate());
				data.setTimeIn(e.getWorkTime().getStartstring());
				data.setTimeOut(e.getWorkTime().getClosestring());
				staffShift.get(bioId).getDayData().put(e.getDate(), data);

			} else {

				StaffData staffDetails = new StaffData();
				staffDetails.setId(bioId);
				staffDetails.setDayData(new HashMap<String, DayData>());
				DayData data = new DayData();
				data.setDay(e.getDate());
				data.setTimeIn(e.getWorkTime().getStartstring());
				data.setTimeOut(e.getWorkTime().getClosestring());
				staffDetails.getDayData().put(e.getDate(), data);
				staffShift.put(bioId, staffDetails);
			}

		});

		/*
		 * Set<Long> regIds = staffer.stream().filter(e -> e.getTypeId() ==
		 * 0).map(Staff::getBioID) .collect(Collectors.toSet());
		 */

		// staffD.remove(0l);
		System.err.println(staffD.size() + "---------");
		Set<Long> st = new HashSet<Long>();

		Set<Long> abs = new HashSet<Long>();
		// System.err.println(regIds.size() + "-----------");

		for (String s : lines) {

			ids.add(s.split("\t")[0]);
		}
		System.err.println("Distinct id s " + ids.size());
		System.err.println(ids);

		for (int i = 0; i < lines.size(); i++) {

			String line = lines.get(i);
			String[] rowData = line.split("\t");

			try {
				String day = rowData[1].split(" ")[0].strip();
				String time = rowData[1].split(" ")[1].strip();
				long staff = Long.parseLong(rowData[0].strip());

				if (Integer.parseInt(day.split("-")[1]) == month) {
					if (staffD.containsKey(staff)) {
						st.add(staff);
						// adding date if attendance is recorded despite holiday
						if (staffD.get(staff).getDayData().get(day).getDay() == null) {
							DayData data = new DayData();
							data.setDay(day);
							data.setTimeIn(time);
							staffD.get(staff).getDayData().put(day, data);
						}

						if (staffD.get(staff).getDayData().get(day).getTimeIn() != null) { //
							staffD.get(staff).getDayData().get(day).setTimeOut(time);

						} else {
							staffD.get(staff).getDayData().get(day).setTimeIn(time);

						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		for (long s : staffD.keySet()) {
			long staffType = staffD.get(s).getType();
			for (String k : staffD.get(s).getDayData().keySet()) {
				Attendance attendance = new Attendance();
				attendance.setDate(k);
				attendance.setStaffid(s);
				String timeIn = staffD.get(s).getDayData().get(k).getTimeIn();
				// System.err.println(timeIn);
				String timeOut = staffD.get(s).getDayData().get(k).getTimeOut();
				attendance.setTimein(timeIn);
				long timeInMinutes = 0;
				try {
					timeInMinutes = Utilities.stringToMinutes(timeIn);
				} catch (Exception e) {

				}
				attendance.setTimeinmins(timeInMinutes);
				if (timeOut == null) {
					timeOut = timeIn;
				}
				attendance.setTimeout(timeOut);
				long timeOutMinutes = 0;
				try {
					timeOutMinutes = Utilities.stringToMinutes(timeOut);
				} catch (Exception e) {
					// TODO: handle exception
					timeOutMinutes = 0;

				}
				attendance.setTimeoutmins(timeOutMinutes);
				long timeWorked = 0;
				try {
					timeWorked = timeOutMinutes - timeInMinutes;
					attendance.setHoursWorked(timeWorked);

					if (staffType == 0) {

						long startTime = Utilities.stringToMinutes("08:00");
						long closetTime = Utilities.stringToMinutes("17:00");
						long timeToWork = closetTime - startTime;
						attendance.setHoursToWork(timeToWork);
						String late = "";
						String run = "";
						try {
							late = (Utilities.stringToMinutes(timeIn) - startTime) > 0 ? "Late" : "Early";
							run = (Utilities.stringToMinutes(timeOut) - closetTime) > 0 ? "Y" : "N";
						} catch (Exception e) {
							// TODO: handle exception
						}
						attendance.setClosedEarly(run);
						attendance.setIslate(late);
						attendance.setLateness((Utilities.stringToMinutes(timeIn) - startTime));
						long deficit = timeWorked - timeToWork;

						attendance.setDeficit(deficit);
						attendances.add(attendance);

						/*
						 * System.out.println(s + "\t" + k + "\t" + timeIn + "\t" + timeOut + "\t" +
						 * timeWorked + "\t" + timeToWork + "\t" + deficit + "\t" + deficit / 60 + "\t "
						 * + late + "\t" + run + "\t");
						 */
					} else {

						if (staffShift.keySet().contains(s)) {
							String shiftTime = staffShift.get(s).getDayData().get(k).getTimeIn();
							if (!shiftTime.contains("0:00") & (timeIn != null)) {
								System.err.println(shiftTime + "\t" + s);

								attendance.setLateness(
										(Utilities.stringToMinutes(timeIn) - Utilities.stringToMinutes(shiftTime)));
								attendance.setIslate(attendance.getLateness() > 0 ? "Late" : "Early");
								attendances.add(attendance);

							} else {
								abs.add(s);

							}
						}
					}
					// System.out.println(s + "\t" + k + "\t" + timeIn + "\t" + timeOut + "\t" +
					// timeWorked);

				} catch (Exception e) {
					// TODO: handle exception
				}
				// ;

			}

		}
		attendanceRepository.saveAll(attendances);
		return "done";
	}

	public String boop2(String fileName) {
		String dateLine = null;
		List<String> lines = null;
		try {
			Path path = Paths.get(fileName);

			lines = Files.readAllLines(path);
			dateLine = lines.get(0).strip().split("\t")[1].strip().split(" ")[0];

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		List<Attendance> attendances = new ArrayList<Attendance>();
		// getting month days
		int month = Integer.parseInt(dateLine.split("-")[1]);
		int year = Integer.parseInt(dateLine.split("-")[0]);
		List<String> holidays = holidayRepo.getHolidays(year, month).stream().map(Holiday::getRealHoliday)
				.collect(Collectors.toList());

		List<Staff> staffer = staffRepository.findAll();
		Map<Long, Staff> staffMap = new HashMap<Long, Staff>();

		staffer.stream().forEach(e -> {
			if (e.getBioid() != 0) {
				staffMap.put(e.getBioid(), e);
			}
		});

		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = LocalDate.of(year, month, start.lengthOfMonth());

		// setting up days of the month to remove the holidays
		Set<String> dates = (Utilities.getDateinDays(start.toString(), end.toString()));
		// dates.removeAll(holidays);
		HashMap<Long, StaffData> staffShift = new HashMap<Long, StaffData>();

		Set<Long> dataBioid = new HashSet<Long>();

		Map<Long, StaffData> loger = new HashMap<Long, StaffData>();

		// initialising the map

		for (String line : lines) {
			String[] rowData = line.strip().split("\t");

			long bioid = Long.parseLong(rowData[0].strip());
			if (!dataBioid.contains(bioid)) {
				dataBioid.add(bioid);
				StaffData staffData = new StaffData();
				Map<String, DayData> dayMap = new HashMap<String, DayData>();
				dates.stream().forEach(e -> {
					DayData dayData = new DayData();
					dayData.setDay(e);
					dayMap.put(e, dayData);
				});
				staffData.setDayData(dayMap);
				loger.put(bioid, staffData);

				try {
					loger.get(bioid).setType(staffMap.get(bioid).getStatus().getId());
				} catch (Exception e) {
					// TODO: handle exception
					loger.get(bioid).setType(0);
					System.out.println("error BIOID id no found in list of staff");
				}

			}

		}

		System.err.println(loger.keySet());

		for (String line : lines) {
			String[] rowData = line.strip().split("\t");

			String day = rowData[1].split(" ")[0].strip();

			String time = rowData[1].split(" ")[1].strip();
			long bioid = Long.parseLong(rowData[0].strip());

			try {
				loger.get(bioid).getDayData().get(day).dataAdd(time);
			} catch (Exception e) { // TODO: handle exception

				Map<String, DayData> dataset = new HashMap<String, DayData>();
				DayData dayData = new DayData();
				dayData.setDay(day);
				dayData.dataAdd(time);
				dataset.put(day, dayData);
				loger.get(bioid).setDayData(dataset);

				System.err.println(line);

			}
		}

		shiftRepos.findShiftsByMonthAndYear(month, year).forEach(e -> {
			long bioId = staffMap.get(e.getStaffid()).getBioid();
			if (staffShift.containsKey(bioId)) {

				// adding date if attendance is recorded despite holiday

				DayData data = new DayData();
				data.setDay(e.getDate());
				data.setTimeIn(e.getWorkTime().getStartstring());
				data.setTimeOut(e.getWorkTime().getClosestring());
				staffShift.get(bioId).getDayData().put(e.getDate(), data);

			} else {

				StaffData staffDetails = new StaffData();
				staffDetails.setId(bioId);
				staffDetails.setDayData(new HashMap<String, DayData>());
				DayData data = new DayData();
				data.setDay(e.getDate());
				data.setTimeIn(e.getWorkTime().getStartstring());
				data.setTimeOut(e.getWorkTime().getClosestring());
				staffDetails.getDayData().put(e.getDate(), data);
				staffShift.put(bioId, staffDetails);
			}

		});

		for (long id : loger.keySet()) {
			// removing days to remove from days of themonth less holiday for
			StaffData staffAttData = loger.get(id);
			Map<String, DayData> daysData = staffAttData.getDayData();
			if (staffMap.get(id) != null) {
				List<String> daysToRemove = leaveRepository.getLeavesDatesForAttendance(staffMap.get(id).getId(), month,
						year);

				for (String dayToRemove : daysToRemove) {
					daysData.remove(dayToRemove);
				}
			}
			for (String day : daysData.keySet()) {
				if (daysData.get(day).getTimeIn() != null) {
					Attendance attendance = new Attendance();
					attendance.setDate(day);
					attendance.setStaffid(id);
					String timeIn = daysData.get(day).getTimeIn(); //
					System.err.println(timeIn);
					String timeOut = daysData.get(day).getTimeOut();
					attendance.setTimein(timeIn);
					long timeInMinutes = 0;
					timeInMinutes = Utilities.stringToMinutes(timeIn);
					attendance.setTimeinmins(timeInMinutes);
					if (timeOut == null) {
						timeOut = timeIn;
					}
					attendance.setTimeout(timeOut);
					long timeOutMinutes = 0;
					try {
						timeOutMinutes = Utilities.stringToMinutes(timeOut);
					} catch (Exception e) {
						// TODO: handleexception timeOutMinutes = 0;

					}
					attendance.setTimeoutmins(timeOutMinutes);
					long timeWorked = 0;
					attendance.setHoursWorked(timeWorked);
					attendances.add(attendance);
					timeWorked = timeOutMinutes - timeInMinutes;
					attendance.setHoursWorked(timeWorked);

					try {
						if (staffMap.get(id).getStatus().getId() == 0) {
							long startTime = Utilities.stringToMinutes("08:00");
							long closetTime = Utilities.stringToMinutes("17:00");
							long timeToWork = closetTime - startTime;
							attendance.setHoursToWork(timeToWork);
							String late = "";
							String run = "";
							try {
								late = (Utilities.stringToMinutes(timeIn) - startTime) > 0 ? "Late" : "Early";
								run = (Utilities.stringToMinutes(timeOut) - closetTime) > 0 ? "Y" : "N";
							} catch (Exception e) {
								// TODO: handle exception }
							}
							attendance.setClosedEarly(run);
							attendance.setIslate(late);
							attendance.setLateness((Utilities.stringToMinutes(timeIn) - startTime));
							long deficit = timeWorked - timeToWork;

							attendance.setDeficit(deficit);
							attendances.add(attendance);
						} else {
							if (staffShift.containsKey(id)) {
								String shiftTime = staffShift.get(id).getDayData().get(day).getTimeIn();
								if (!shiftTime.contains("0:00") & (timeIn != null)) {

									attendance.setLateness(
											(Utilities.stringToMinutes(timeIn) - Utilities.stringToMinutes(shiftTime)));
									attendance.setIslate(attendance.getLateness() > 0 ? "Late" : "Early");
									attendances.add(attendance);

								} else {
									attendances.add(attendance);

								}

							}

						}
					} catch (Exception e) {
						attendances.add(attendance);

					}
				}

			}

		}
		attendanceRepository.saveAll(attendances);

		return "done";
	}

	public void readFile(String fileLocation) {
		int month = 0, year = 0;

		List<StaffShift> shits = new ArrayList<StaffShift>();
		try {

			File excel = new File(fileLocation);
			FileInputStream fis = new FileInputStream(excel);
			XSSFWorkbook book = new XSSFWorkbook(fis);
			XSSFSheet sheet = book.getSheet("Sheet1");

			Iterator<Row> itr = sheet.iterator();

			// Iterating over Excel file in Java

			while (itr.hasNext()) {

				Row row = itr.next();
				if (row.getRowNum() == 0) {
					String dateInit = (row.getCell(2).getStringCellValue());
					month = Integer.parseInt(dateInit.split("-")[1]);
					year = Integer.parseInt(dateInit.split("-")[0]);
				}
				LocalDate start = LocalDate.of(year, month, 1);
				LocalDate end = LocalDate.of(year, month, start.getMonth().minLength());
				List<String> dates = Utilities.getDaysinDays(start.toString(), end.toString());

				if (row.getRowNum() > 0) {
					for (int i = 2; i < dates.size() + 2; i++) {
						try {
							StaffShift shift = new StaffShift();
							shift.setStaffid((long) row.getCell(0).getNumericCellValue());
							shift.setDate(dates.get(i - 2));
							System.err.println(row.getCell(i).getStringCellValue());

							// System.err.println(row.getCell(i).getStringCellValue().split("\\|")[1]);
							String type = row.getCell(i).getStringCellValue().split("\\|")[1];
							shift.setShiftType(Long.parseLong(type));
							shift.setMonth(month);
							shift.setYear(year);
							shits.add(shift);
						} catch (Exception e) {
							// TODO: handle exception
							StaffShift shift = new StaffShift();
							shift.setStaffid((long) row.getCell(0).getNumericCellValue());
							shift.setShiftType(0);
							shift.setDate(dates.get(i - 2));
							shift.setMonth(month);
							shift.setYear(year);
							shits.add(shift);
						}
					}

				}

			}
			book.close();
			shiftRepos.saveAll(shits);
			// }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
