// 
// Decompiled by Procyon v0.5.36
// 

package com.attendance.services;

import java.io.BufferedReader;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ArrayList;
import com.attendance.data.Staff;
import java.util.List;

public class StaffExtractor
{
    private List<Staff> staffs;
    
    public void extract(final String fileLoation) {
        this.staffs = new ArrayList<Staff>();
        final HashMap<String, String> att = new HashMap<String, String>();
        final Path file = Paths.get(fileLoation, new String[0]);
        try {
            final BufferedReader reader = Files.newBufferedReader(file);
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (count > 0) {
                    final String[] fields = line.split(",");
                    if (!att.containsKey(fields[0])) {}
                    att.put(fields[0], fields[1]);
                }
                ++count;
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        for (final String a : att.keySet()) {
            final Staff staff = new Staff();
            staff.setBioid(Long.parseLong(a));
            staff.setName((String)att.get(a));
            this.staffs.add(staff);
        }
    }
    
    public List<Staff> getStaffs() {
        return this.staffs;
    }
    
    public void setStaffs(final List<Staff> staffs) {
        this.staffs = staffs;
    }
}
