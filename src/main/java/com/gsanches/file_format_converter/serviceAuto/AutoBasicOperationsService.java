package com.gsanches.file_format_converter.serviceAuto;

import com.gsanches.file_format_converter.dtos.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AutoBasicOperationsService {
    String uploadFile(MultipartFile file);

    ResponseEntity<Resource> downloadFile(String absoluteDownloadFilePath);
    void downloadFiles(List<String> absoluteDownloadFilePaths);

    ResponseEntity<Void> deleteFile(FileDto fileDto);
    void deleteFiles(List<String> absoluteDownloadFilePaths);

}
