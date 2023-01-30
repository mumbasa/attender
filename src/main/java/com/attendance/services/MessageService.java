package com.attendance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import kong.unirest.Unirest;

@Service
public class MessageService {
	
	@Autowired
	JavaMailSender emailSender;
	

	
	@Value("${mnotify.key}")
	private String smsApiKey;
	
	public String sendSms(String message,String contact) {
		return 	Unirest.get("https://apps.mnotify.net/smsapi").
				queryString("key", smsApiKey).
				queryString("to", contact).
				queryString("msg",message ).
				queryString("sender_id", "BrieftaTech")
				.asString().getBody();
		}

		

		


}
