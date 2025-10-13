package com.gsanches.file_format_converter.services;

import com.gsanches.file_format_converter.dto.FileDownloadDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BasicOperationsService {
    java.lang.String uploadFile(MultipartFile file);

    FileDownloadDto downloadFile(String absoluteDownloadFilePath);
    List<FileDownloadDto> downloadFiles(List<String> absoluteDownloadFilePaths);

    void deleteFile(String filePath);
    void deleteFiles(List<String> absoluteDownloadFilePaths);
}
