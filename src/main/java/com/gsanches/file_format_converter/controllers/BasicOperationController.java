package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.dtos.FileDto;
import com.gsanches.file_format_converter.services.BasicOperationsService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BasicOperationController {

    private final BasicOperationsService basicOperationsService;

    public BasicOperationController(BasicOperationsService basicOperationsService) {
        this.basicOperationsService = basicOperationsService;
    }

    //Add a @RequestBody with the filename
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(){

        return basicOperationsService.downloadFile("Document.pdf");
    }

    // For this one, use with cmd (curl), and with Post!
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {

        return basicOperationsService.uploadFile(file);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(@RequestBody FileDto fileDto){

        return basicOperationsService.deleteFile(fileDto);
    }

}
