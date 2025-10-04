package com.gsanches.file_format_converter.services;

import com.gsanches.file_format_converter.dtos.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BasicOperationsService {
    ResponseEntity<Resource> downloadFile(String filename);

    ResponseEntity<String> uploadFile(MultipartFile file);

    ResponseEntity<Void> deleteFile(FileDto fileDto);

}
