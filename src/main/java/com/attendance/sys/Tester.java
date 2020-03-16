// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.sys;

import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.attendance.data.Attendance;
import com.attendance.data.ShiftType;

import java.io.IOException;
import java.time.LocalDate;

public class Tester {
	public static void main(final String[] args) {

		/*
		 * LocalDate date = LocalDate.parse("2019-10-12"); int days = 5; int
		 * weekendCounts = 0; for (int a = 0; a <= days; a++) { LocalDate d =
		 * date.plusDays(a); if (d.getDayOfWeek() == DayOfWeek.SATURDAY |
		 * d.getDayOfWeek() == DayOfWeek.SUNDAY) { System.err.println(true + "\t" +
		 * d.toString()); weekendCounts++; } } System.out.println(date.plusDays(days +
		 * weekendCounts).toString());
		 */
		getBank();
	}

	public static List<String> getUni() {

		List<String> schools = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_universities_in_Ghana").get();
			Elements newsHeadlines = doc.select(".wikitable tr");
			for (Element headline : newsHeadlines) {
				Elements k = headline.getElementsByTag("td");
				
				if (k.size() > 1) {
					String data = k.first().text().split("\\[")[0].replace("[", "");
					System.out.println(data);
					schools.add(data);
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(schools.size());
		return schools;
		

	}
	
	public static List<String> getSHS2() {

		List<String> schools = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_senior_high_schools_in_Ghana").get();
			Elements newsHeadlines = doc.select(".wikitable tr");
			for (Element headline : newsHeadlines) {
				Elements k = headline.getElementsByTag("td");
				
				if (k.size() > 1) {
					String data = k.first().text().split("\\[")[0].replace("[", "");
					System.out.println(data);
					if(data.length()>0) {
					schools.add(data);
					}
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(schools.size());
		return schools;
		

	}
	
	public static List<String> getBank() {

		List<String> schools = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_financial_institutions_in_Ghana").get();
			Elements newsHeadlines = doc.select(".wikitable tr");
			//System.out.println(newsHeadlines.text());
			for (Element headline : newsHeadlines) {
				Elements k = headline.getElementsByTag("td");
				
				if (k.size() > 1) {
					String data = k.get(1).text().split("\\[")[0].replace("[", "");
					
			if(data.contains("ank") & data.length()>4) {
					System.out.println(data.split("\\(")[0].replace("(", ""));
					schools.add(data);
				}
			}
			}


		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(schools.size());
		return schools;
		

	}

	public static long getLateness(List<Long> starttimes, List<Long> endtimes, long time) {

		double f = 400;
		int index = 0;
		for (int i = 0; i < starttimes.size(); i++) {

			if ((starttimes.get(i) - 360) < f && f < (endtimes.get(i))) {
				index = i;
				break;
			}

		}

		return time - starttimes.get(index);

	}

	public static long getLateness(List<ShiftType> times, long timeIn, Attendance attendance) {
		System.err.println("-----------computing");
		int index = 0;
		for (int i = 0; i < times.size(); i++) {

			if ((times.get(i).getStart() - 360) < timeIn && timeIn < (times.get(i).getClose())) {
				index = i;
				break;
			}

		}
		attendance.setHoursToWork(times.get(index).getHours());
		return timeIn - times.get(index).getStart();

	}

	public static long getRun(List<ShiftType> times, long timeOut) {

		int index = 0;
		for (int i = 0; i < times.size(); i++) {

			if ((times.get(i).getClose() - 120) < timeOut && timeOut < (times.get(i).getClose() + 120)) {
				index = i;
				break;
			}

		}

		return timeOut - times.get(index).getClose();

	}

	public static int getMonths(final LocalDate a, final LocalDate b) {
		final int years = b.getYear() - a.getYear();
		final int month = b.getMonthValue() - a.getMonthValue();
		return years * 12 + month;
	}

	public static Map<String, List<String>> getLeaveIntervals(final String start, final String end) {
		final Map<String, List<String>> dates = new HashMap<String, List<String>>();
		final LocalDate d = LocalDate.parse(start);
		final LocalDate f = LocalDate.parse(end);
		for (int months = getMonths(d, f), a = 0; a <= months; ++a) {
			final ArrayList<String> range = new ArrayList<String>();
			if (a == 0) {
				range.add(d.toString());
				range.add(d.with(TemporalAdjusters.lastDayOfMonth()).toString());
				dates.put(String.valueOf(d.getYear()) + "-" + d.getMonth().name(), range);
			} else {
				final LocalDate cur = d.plusMonths(a).with(TemporalAdjusters.lastDayOfMonth());
				if (f.isAfter(cur)) {
					range.add(cur.with(TemporalAdjusters.firstDayOfMonth()).toString());
					range.add(cur.toString());
					dates.put(String.valueOf(cur.getYear()) + "-" + cur.getMonth().name(), range);
				} else {
					range.add(cur.with(TemporalAdjusters.firstDayOfMonth()).toString());
					range.add(f.toString());
					dates.put(String.valueOf(f.getYear()) + "-" + f.getMonth().name(), range);
				}
			}
		}
		return dates;
	}
}
