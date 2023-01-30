package com.attendance.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.attendance.data.Staff;
import com.attendance.data.User;
import com.attendance.repos.StaffService;
import com.attendance.repos.UploadFileResponse;
import com.attendance.repos.UserRepo;
import com.attendance.repositories.StaffRepositoies;
import com.attendance.services.AttendanceService;
import com.attendance.services.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    StaffService staffRepository;
    
    @Autowired
    AttendanceService attendanceService;
    
    @Autowired
    UserRepo userRepository;
    
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }
    
    @GetMapping("/download/attendance")
	public  ResponseEntity<Resource> getAttendance(HttpServletRequest request,@RequestParam String date,@RequestParam long staffId){
		System.err.println("staffid - "+staffId);
	String[] dates = date.split("-");
		Staff staff = staffRepository.getStaffByStaffId(staffId);
		System.err.println("staffid +++++"+staff.getBioid());
		String fileName =attendanceService.createAttendanceReport(Integer.parseInt(dates[1]), Integer.parseInt(dates[0]), staff);
		return downloadFile(fileName, request);
	}
	
	@GetMapping("/admin/roster/template")
	public  ResponseEntity<Resource> getTemplate(HttpServletRequest request,@RequestParam String date,Principal principal){
	
	String[] dates = date.split("-");
		User staff = userRepository.getUserbyEmail(principal);
		String fileName =attendanceService.createExcel(Integer.parseInt(dates[1]), Integer.parseInt(dates[0]), staff.getDepartmentId());
		return downloadFile(fileName, request);
	}
	

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
