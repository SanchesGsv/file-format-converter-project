package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.dtos.FileAutoBasicOperationsDto;
import com.gsanches.file_format_converter.enums.FolderLocationEnum;
import com.gsanches.file_format_converter.services.AutoBasicOperationsService;
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
public class AutoBasicOperationsServiceImpl implements AutoBasicOperationsService {

    private static final String ORIGINAL_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/originalFile";
    private static final String CONVERTED_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/convertedFile";

//    private final

    public String uploadFile(MultipartFile file){
        try {
            File uploadDir = new File(ORIGINAL_FILE_FOLDER);

            File destination = new File(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));

            file.transferTo(destination);

            return destination.toString();

        } catch (IOException e) {
            System.err.println("error " + e);
            return null;
        }

    }


    public ResponseEntity<Resource> downloadFile(String absoluteDownloadFilePath){
        try {
            Path filePath = Paths.get(absoluteDownloadFilePath);

            byte[] data = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            System.out.println("before of the return (downloadFile)");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "putTheCorrectName" + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void downloadFiles(List<String> absoluteDownloadFilePaths){
        for (String absoluteDownloadFilePath : absoluteDownloadFilePaths) {
            downloadFile(absoluteDownloadFilePath);
        }
    }



    public ResponseEntity<Void> deleteFile(FileAutoBasicOperationsDto fileAutoBasicOperationsDto){
        String storagePath = "";

        if(fileAutoBasicOperationsDto.deleteLocal().equals(FolderLocationEnum.ORIGINAL_FOLDER)){
            storagePath = ORIGINAL_FILE_FOLDER;
        }
        else if(fileAutoBasicOperationsDto.deleteLocal().equals(FolderLocationEnum.CONVERTED_FOLDER)){
            storagePath = CONVERTED_FILE_FOLDER;
        }
        else{
            System.out.println("Not a valid folder location " + fileAutoBasicOperationsDto.deleteLocal());
        }

        File file = new File(storagePath + "/" + fileAutoBasicOperationsDto.filename());
        System.out.println("--- path -> " + storagePath + "/" + fileAutoBasicOperationsDto.filename());

        if(!file.exists()){
            System.out.println("!file.exists()");
        }
        if(!file.delete()){
            System.out.println("!file.delete()");
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public void deleteFiles(List<String> absoluteDeleteFilePaths) {
        for (String absoluteDeleteFilePath : absoluteDeleteFilePaths) {
            downloadFile(absoluteDeleteFilePath);
        }
    }

}
