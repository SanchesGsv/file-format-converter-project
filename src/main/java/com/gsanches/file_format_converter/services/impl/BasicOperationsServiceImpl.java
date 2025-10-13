package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.dto.FileDownloadDto;
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
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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


    //TODO: Adjust, put this type of return on the Controller instead of here.
    public FileDownloadDto downloadFile(String absoluteDownloadFilePath){
        System.out.println("on the downloadFile " + absoluteDownloadFilePath);

        if (!absoluteDownloadFilePath.startsWith(convertedStoragePath + "/") &&
                !absoluteDownloadFilePath.startsWith(uploadsStoragePath + "/")) {
            throw new IllegalArgumentException("Invalid path: " + absoluteDownloadFilePath);
        }

        try {
            Path filePath = Paths.get(absoluteDownloadFilePath);
            byte[] data = Files.readAllBytes(filePath);

            String filename = absoluteDownloadFilePath.startsWith(convertedStoragePath + "/")
                    ? absoluteDownloadFilePath.substring(convertedStoragePath.length() + 1)
                    : absoluteDownloadFilePath.substring(uploadsStoragePath.length() + 1);

            return new FileDownloadDto(filename, data, data.length, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file: " + absoluteDownloadFilePath, e);
        }
    }



    public List<FileDownloadDto> downloadFiles(List<String> absoluteDownloadFilePaths){
        List<FileDownloadDto> fileDownloadDtoList = new ArrayList<>();

        for (String absoluteDownloadFilePath: absoluteDownloadFilePaths) {
            fileDownloadDtoList.add(downloadFile(absoluteDownloadFilePath));
        }

        return fileDownloadDtoList;
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