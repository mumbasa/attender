package com.attendance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
public class FileStorageProperties {
	
	@Value("${spring.application.name}")
    String appName;

	
    private String uploadDir;

    public String getUploadDir() {
        return "C:\\Users\\bryan.laryea\\Downloads\\Telegram Desktop\\attendance\\";
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = "C:\\Users\\bryan.laryea\\Downloads\\Telegram Desktop\\attendance\\";
    }
}
