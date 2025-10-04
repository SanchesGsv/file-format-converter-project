package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.dtos.FileDto;
import com.gsanches.file_format_converter.enums.FolderLocationEnum;
import com.gsanches.file_format_converter.services.BasicOperationsService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class BasicOperationsServiceImpl implements BasicOperationsService {

    private static final String ORIGINAL_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/originalFile";
    private static final String CONVERTED_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/convertedFile";

    public ResponseEntity<Resource> downloadFile(String filename){

        try {
            String downloadFilePath = CONVERTED_FILE_FOLDER;

            Path filePath = Paths.get(downloadFilePath, filename);

            byte[] data = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseEntity<String> uploadFile(MultipartFile file){
        System.out.println("service!! fileUpload");
        try {
            File uploadDir = new File(ORIGINAL_FILE_FOLDER);

            File destination = new File(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));
            file.transferTo(destination);

            return ResponseEntity.ok("File uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }

    }

    public ResponseEntity<Void> deleteFile(FileDto fileDto){
        String storagePath = "";

        if(fileDto.deleteLocal() == FolderLocationEnum.ORIGINAL_FOLDER){
            storagePath = ORIGINAL_FILE_FOLDER;
        }
        else if(fileDto.deleteLocal() == FolderLocationEnum.CONVERTED_FOLDER){
            storagePath = CONVERTED_FILE_FOLDER;
        }
        else{
            System.out.println("Not a valid folder location " + fileDto.deleteLocal());
        }

        File file = new File(storagePath + "/" + fileDto.filename());
        System.out.println("--- path -> " + storagePath + "/" + fileDto.filename());

        if(!file.exists()){
            System.out.println("!file.exists()");
        }
        if(!file.delete()){
            System.out.println("!file.delete()");
        }

        return ResponseEntity.ok().build();
    }


}
