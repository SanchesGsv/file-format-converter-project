package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.dto.FileDownloadDto;
import com.gsanches.file_format_converter.services.AutoWork;
import com.gsanches.file_format_converter.services.BasicOperationsService;
import com.gsanches.file_format_converter.services.OtherOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OtherOperationsImpl implements OtherOperations {

    @Value("${storage.converted}")
    private String convertedFileFolder;

    private final BasicOperationsService basicOperationsService;

    public OtherOperationsImpl(BasicOperationsService basicOperationsService) {
        this.basicOperationsService = basicOperationsService;
    }

    @Override
    public List<FileDownloadDto> downloadAllConvertedFiles() {
        List<String> convertedFiles = grabAllConvertedFiles();
        System.out.println("convertedFiles " + convertedFiles);

        return basicOperationsService.downloadFiles(grabAllConvertedFiles());

    }

    //TODO: Maybe have a place for put things such as grabAllConvertedFiles, things like this. And another for OtherOperations such as downloadAllConvertedFiles, deleteAllConvertedFiles...

    public List<String> grabAllConvertedFiles() {
        Path path = Paths.get(convertedFileFolder);

        try {
            return
                    Files.list(path)
                            .filter(Files::isRegularFile)
                            .map(p -> convertedFileFolder + "/" + p.getFileName().toString())
                            .collect(Collectors.toList()
                            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
