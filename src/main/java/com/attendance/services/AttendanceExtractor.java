package com.attendance.services;

import com.attendance.data.Holiday;
import com.attendance.data.Staff;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.io.IOException;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.HashMap;
import com.attendance.data.Attendance;
import java.util.List;
import java.util.Map;

public class AttendanceExtractor {
	private List<Attendance> attendance;
	private HashMap<String, String> data;
	private HashSet<String> days;
	private HashSet<String> weekEnds;
	private HashSet<String> daysLessHolidays;
	private int year;
	private int month;
	private String file;
	private Institutes institute;
	private Map<Long, Staff> staff;

	public void extract() {
		switch (institute) {
		case IIR: {
			this.setup2(this.file);
			break;
		}
		case INSTI: {
			this.setup(this.file);
			break;
		}
		default:
			break;
		}
	}

	public AttendanceExtractor(final String file, final Institutes inst, Map<Long, Staff> staff) {
		this.data = new HashMap<String, String>();
		this.days = new HashSet<String>();
		weekEnds = new HashSet<String>();
		this.file = file;
		this.institute = inst;
		this.staff = staff;
	}

	public List<Attendance> getAttendance() {
		return this.attendance;
	}

	public void setup(final String file) {
		final Path p = Paths.get(file, new String[0]);
		try {
			final BufferedReader reader = Files.newBufferedReader(p, StandardCharsets.UTF_8);
			int count = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				if (count > 0) {
					final String[] datas = line.replace("/", "-").split(",");
					final String key = String.valueOf(datas[8].split("\\s")[0]) + "/" + datas[2];
					final String dates = datas[8].split("\\s")[0];
					if (LocalDate.parse(dates).getDayOfWeek().getValue() > 5) {
						weekEnds.add(dates);
					}
					this.days.add(dates);

					if (count == 10) {
						final String date = datas[8].split("\\s")[0];
						this.year = Integer.parseInt(date.substring(0, 4));
						this.month = Integer.parseInt(date.substring(5, 7));
					}
					if (data.containsKey(key)) {
						data.put(key, String.valueOf(this.data.get(key)) + "/" + datas[8].split("\\s")[1]);
					} else {
						this.data.put(key, datas[8].split("\\s")[1]);
					}
				}
				++count;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.attendance = new ArrayList<Attendance>();
		for (final String a : data.keySet()) {
			final Attendance attend = new Attendance(a, data.get(a));
			this.attendance.add(attend);
		}
	}

	public void setup2(final String upload) {
		System.err.println(staff.size() + "-size---- i3d");

		attendance = new ArrayList<Attendance>();
		final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		final Path file = Paths.get(upload, new String[0]);
		try {
			final BufferedReader reader = Files.newBufferedReader(file);
			int count = 0;
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.replace("/", "-").split(",");
				if (count > 0) {

					LocalDate lD = LocalDate.parse(fields[2], fmt);
					if (lD.getDayOfWeek().getValue() > 5) {
						weekEnds.add(lD.toString());
					}
					this.days.add(lD.toString());
					final String date = lD.toString();
					long staffId = Long.parseLong(fields[0]);

					if (fields.length == 5) {
						if (fields[3] != null & fields[4] != null) {
							Staff staffer = staff.get(staffId);

							if (staffer != null) {
								if (staffer.getStaffType() != null) {
									// System.out.println(line);

									Attendance att = new Attendance(fields[0], Utilities.hourPadding(fields[3]),
											Utilities.hourPadding(fields[4]), date, staffer);
									// checking for staff without type to test
									System.err.println(att.getStaff().getBioID() + "------ id");

									attendance.add(att);
								}
							}
						}
					} else if (fields.length == 4) {
						final Attendance att = new Attendance(fields[0], Utilities.hourPadding(fields[3]),
								Utilities.hourPadding(fields[3]), date, staff.get(staffId));
						if (att.getStaff().getStaffType() != null) {
							this.attendance.add(att);
						}
					}
					if (count == 10) {
						this.year = lD.getYear();
						this.month = lD.getMonthValue();
					}
				}
				++count;
			}
			reader.close();
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
		}
	}

	public HashSet<String> getDays() {
		return this.days;
	}

	public HashSet<String> getDaysLessHolidays(final List<Holiday> holidays) {
		this.daysLessHolidays = new HashSet<String>(this.days);
		if (!holidays.isEmpty()) {
			for (final Holiday h : holidays) {
				System.out.println(
						String.valueOf(h.getRealHoliday()) + " from " + holidays.size() + " days " + this.days.size());
				this.daysLessHolidays.remove(h.getRealHoliday());
				System.err.println("------" + this.daysLessHolidays.size());
			}
		}
		return this.daysLessHolidays;
	}

	public int getYear() {
		return this.year;
	}

	public int getMonth() {
		return this.month;
	}

	public enum Institutes {
		IIR, INSTI, FRI, ARI, WRI, SoRI;
	}

	public HashSet<String> getWeekEnds() {
		return weekEnds;
	}

	public void setWeekEnds(HashSet<String> weekEnds) {
		this.weekEnds = weekEnds;
	}
}
