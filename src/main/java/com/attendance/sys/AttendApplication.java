package com.attendance.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@ComponentScan("com.*")
@EnableJms
public class AttendApplication  extends SpringBootServletInitializer{

	
	public static void main(String[] args) {
		SpringApplication.run(AttendApplication.class, args);
	}
	 @Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	        return bCryptPasswordEncoder;
	    }
}
