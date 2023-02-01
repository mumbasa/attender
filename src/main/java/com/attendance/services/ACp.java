package com.attendance.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ACp {
	  private final Logger logger = LoggerFactory.getLogger(ACp.class);

	  
	  public static void main(String[] args) {
		Path path = Paths.get("C:\\Users\\bryan.laryea\\Downloads\\Telegram Desktop\\attendance\\6160052003966_attlog.dat");
		try {
			List<String> lines= Files.readAllLines(path);
			Set<String> data = new HashSet<String>();
			int count=0;
			for (String line : lines) {
				data.add( line.strip().split("\t")[0]);
				count++;
				
			}
			System.err.println(" count "+data.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	}
	    public void listener(String message){
	        logger.info("Message received {} ", message);
	    }
}
