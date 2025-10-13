package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.services.BasicOperationsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class BasicOperationsServiceImpl implements BasicOperationsService {

    @Value("${storage}")
    private String storagePath;

    @Value("${storage.converted}")
    private String convertedStoragePath;

    @Value("${storage.uploads}")
    private String uploadsStoragePath;

    public String uploadFile(MultipartFile file){
        try {
            File uploadDir = new File(uploadsStoragePath);

            File destination = new File(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));

            file.transferTo(destination);

            return destination.toString();

        } catch (IOException e) {
            System.err.println("error " + e);

            return null;
        }
    }


    public ResponseEntity<Resource> downloadFile(String absoluteDownloadFilePath){
        //TODO: For the download part, adjust the type of return!

        System.out.println("on the downloadFile " + absoluteDownloadFilePath);

        if(!absoluteDownloadFilePath.startsWith(convertedStoragePath + "/") && !absoluteDownloadFilePath.startsWith(uploadsStoragePath + "/")){
            throw new RuntimeException("Path should start with correct folder path, actual path -> " + absoluteDownloadFilePath);
        }

        try {
            Path filePath = Paths.get(absoluteDownloadFilePath);

            byte[] data = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            String filename;

            if(absoluteDownloadFilePath.startsWith(convertedStoragePath + "/")){
                filename = absoluteDownloadFilePath.substring(convertedStoragePath.length() + "/".length());

            } else {
                filename = absoluteDownloadFilePath.substring(uploadsStoragePath.length() + "/".length());
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void downloadFiles(List<String> absoluteDownloadFilePaths){
        for (String absoluteDownloadFilePath: absoluteDownloadFilePaths) {
            downloadFile(absoluteDownloadFilePath);
        }
    }



    public void deleteFile(String filePath){
        File file = new File(filePath);

        if(filePath.isBlank()){
            throw new RuntimeException("FilePath must be not blank");
        }

        if(!filePath.contains(storagePath)){
            throw new RuntimeException("The delete only should occur inside of the storage folder");
        }

        if(!filePath.contains(convertedStoragePath) || !filePath.contains(uploadsStoragePath)){
            throw new RuntimeException("filePath to be deleted must be inside of the convertedStorage or uploadsStorage");
        }

        //Certificate that the file exists
        if(!file.exists()){
            throw new RuntimeException("File doesn't exists");
        }

        //Delete the file
        if(!file.delete()){
            throw new RuntimeException("Was not possible to delete the file");
        }
    }

    @Override
    public void deleteFiles(List<String> filePaths) {
        for (String filePath: filePaths) {
            downloadFile(filePath);
        }
    }

}