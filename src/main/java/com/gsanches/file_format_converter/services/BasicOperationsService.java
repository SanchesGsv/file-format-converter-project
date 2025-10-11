package com.gsanches.file_format_converter.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BasicOperationsService {
    java.lang.String uploadFile(MultipartFile file);

    ResponseEntity<Resource> downloadFile(String absoluteDownloadFilePath);
    void downloadFiles(List<String> absoluteDownloadFilePaths);

    void deleteFile(String filePath);
    void deleteFiles(List<String> absoluteDownloadFilePaths);
}
