package com.attendance.sys;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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
public class AttendApplication extends SpringBootServletInitializer {

	@Value("${app.upload.file.location}")
	String UPLOAD_FOLDER;



	public static void main(String[] args) {
		SpringApplication.run(AttendApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Bean
	public FileStorageProperties getProp() {
		return new FileStorageProperties();
		
	}
	@PostConstruct
	public void creatFolder() {
		// String folder
		// =ServletUriComponentsBuilder.fromCurrentContextPath().path("/files/download/").toString();
		File file = new File(UPLOAD_FOLDER);
		if (!file.exists()) {
			System.out.println("Does not Exit. Creaitng folder");
			file.mkdir();

		} else {
			System.out.println("Does  Exit. ");

		}
	}
}
