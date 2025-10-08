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

    @Value("${storage.uploads}")
    private String originalFileFolder;

    @Value("${storage.converted}")
    private String convertedFileFolder;

    @Value("${storage}")
    private String storagePath;

    @Value("${storage.converted}")
    private String convertedStoragePath;

    @Value("${storage.uploads}")
    private String uploadsStoragePath;

    public String uploadFile(MultipartFile file){
        try {
            File uploadDir = new File(originalFileFolder);

            File destination = new File(uploadDir, Objects.requireNonNull(file.getOriginalFilename()));

            file.transferTo(destination);

            return destination.toString();

        } catch (IOException e) {
            System.err.println("error " + e);

            return null;
        }
    }


    //TODO: Fix the download part here (singular and plural), on the autoWork service impl, and also add the throw's
    public ResponseEntity<Resource> downloadFile(String absoluteDownloadFilePath){
        //TODO: Make sure that the file to be downloaded is on the converted or uploads folder
        //Maybe do a single place for put all of these exceptions

        //TODO: Add the / here and on the others places!!!
        //Make sure to use the / for be sure that is inside of a folder
        if(!absoluteDownloadFilePath.startsWith(convertedStoragePath + "/") && !absoluteDownloadFilePath.startsWith(uploadsStoragePath + "/")){
            throw new RuntimeException("Path should start with correct folder path, actual path -> " + absoluteDownloadFilePath);
        }


        try {
            Path filePath = Paths.get(absoluteDownloadFilePath);

            byte[] data = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);


            //TODO: see if should I put the correct name for these things.
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "putTheCorrectName" + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //TODO: See if should I create another class, only for the multiple tasks (for example downloadsFiles[plural] on the anther class, and downloadFile on this class[singular])
    public void downloadFiles(List<String> absoluteDownloadFilePaths){
        for (String absoluteDownloadFilePath : absoluteDownloadFilePaths) {
            downloadFile(absoluteDownloadFilePath);
        }
    }



    //TODO: Adjust the return type. make a return different than ResponseEntity here (ServiceImpl).
    public void deleteFile(String filePath){
        File file = new File(filePath);

        //TODO: Organize the Exceptions (see if should I put on an centralized place).

        System.out.println("deleteFile part " + filePath);

        if(filePath.isBlank()){
            throw new RuntimeException("FilePath must be not blank");
        }

        if(!filePath.contains(storagePath)){
            throw new RuntimeException("The delete only should occur inside of the storage folder");
        }

        if(!filePath.contains(convertedStoragePath) || !filePath.contains(uploadsStoragePath)){
            throw new RuntimeException("filePath to be deleted must be inside of the convertedStorage or uploadsStorage");
        }

        //TODO: Certificate that the file to be deleted is not inside another folder inside of the uploads or converted! Maybe is not super necessary!

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