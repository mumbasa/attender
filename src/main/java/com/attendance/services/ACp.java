package com.attendance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ACp {
	  private final Logger logger = LoggerFactory.getLogger(ACp.class);

	    public void listener(String message){
	        logger.info("Message received {} ", message);
	    }
}
