// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.services;

import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import com.attendance.data.Holiday;
import com.attendance.repos.HolidayRepository;

import java.util.List;

@Service
public class HolidayExtractor
{
	@Autowired
    HolidayRepository holidayRepository;
	
	private List<Holiday> holidays;
    
    public HolidayExtractor() {
        this.holidays = new ArrayList<Holiday>();
    }
    
    public void Extact(final String year) {
        final Connection soup = Jsoup.connect("https://www.officeholidays.com/countries/ghana/" + year);
        try {
            final Document d = soup.get();
            final Elements rows = d.getElementsByClass("country-table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
            int rowCount = 0;
            for (final Element e : rows) {
                Thread.sleep(10L);
          
                final Elements columns = e.getElementsByTag("td");
                String day = columns.get(0).text();
                String hday = columns.get(2).text();
                String date = (e.getElementsByTag("time").first().attr("datetime"));
                String type= "Public";
              
					System.err.println(day);
			
					 final Holiday h = new Holiday(date, day, hday,type); 
					 this.holidays.add(h);
					
                
          System.err.println(holidays.size());
            }
       holidayRepository.addHolidays(holidays);
        }
        catch (IOException | InterruptedException ex2) {
      
            ex2.printStackTrace();
        }
    }
    
    public List<Holiday> getHolidays() {
        return this.holidays;
    }
    
    public void setHolidays(final List<Holiday> holidays) {
        this.holidays = holidays;
    }
}
