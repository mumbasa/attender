// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.services;

import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import com.attendance.data.Holiday;
import java.util.List;

public class HolidayExtractor
{
    private List<Holiday> holidays;
    
    public HolidayExtractor() {
        this.holidays = new ArrayList<Holiday>();
    }
    
    public void Extact(final String year) {
        final Connection soup = Jsoup.connect("https://www.officeholidays.com/countries/ghana/" + year + ".php");
        try {
            final Document d = soup.get();
            final Element table = d.selectFirst("table.list-table");
            final Elements rows = table.getElementsByTag("tr");
            int rowCount = 0;
            for (final Element e : rows) {
                Thread.sleep(10L);
                final Elements columns = e.getElementsByTag("td");
                int count = 0;
                String day = null;
                String hday = null;
                String date = null;
                for (final Element c : columns) {
                    if (count == 0) {
                        day = c.ownText();
                    }
                    else if (count == 1) {
                        date = c.getElementsByTag("time").first().getElementsByAttribute("datetime").first().ownText();
                    }
                    else if (count == 2) {
                        hday = c.getElementsByTag("a").first().ownText();
                    }
                    ++count;
                }
                if (rowCount > 0) {
                    final Holiday h = new Holiday(date, day, hday);
                    this.holidays.add(h);
                }
                ++rowCount;
            }
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
