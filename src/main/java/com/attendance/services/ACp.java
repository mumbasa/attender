package com.attendance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@EnableJms
public class ACp {
	  private final Logger logger = LoggerFactory.getLogger(ACp.class);

	    @JmsListener(destination = "test")
	    public void listener(String message){
	        logger.info("Message received {} ", message);
	    }
}
