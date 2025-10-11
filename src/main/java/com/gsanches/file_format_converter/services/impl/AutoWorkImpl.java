package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.factory.ConversionStrategyFactory;
import com.gsanches.file_format_converter.services.BasicOperationsService;
import com.gsanches.file_format_converter.services.AutoWork;
import com.gsanches.file_format_converter.strategy.FileConversionStrategy;
import com.gsanches.file_format_converter.validator.FileFormatValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutoWorkImpl implements AutoWork {

    @Value("${storage.converted}")
    private String convertedFileFolder;

    private final BasicOperationsService basicOperations;
    private final FileFormatValidator validator;
    private final ConversionStrategyFactory strategyFactory;

    public AutoWorkImpl(BasicOperationsService basicOperations, FileFormatValidator validator, ConversionStrategyFactory strategyFactory) {
        this.basicOperations = basicOperations;
        this.validator = validator;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public void autoWork(List<MultipartFile> files, FileConversionEnum conversionType) {

        //Grab the strategy according to the file type
        //Eg:
        //JpgToPdfStrategy
        //PdfToImageStrategy
        FileConversionStrategy strategy = strategyFactory.getStrategy(conversionType);

        //For each send file
        for (MultipartFile file : files) {

            //Grab basically the file extension
            //application/pdf
            //image/jpeg
            String mimeType = validator.detectMimeType(file);

            //Certificate that the strategy is the same as the file extension
            if (!strategy.supportsMimeType(mimeType)) {
                throw new IllegalArgumentException("Invalid MIME type: " + mimeType);
            }

            //Upload the original file
            String pathOfOriginalUploadedFile = basicOperations.uploadFile(file);

            //Convert the original file and put on the converted folder
            strategy.convert(pathOfOriginalUploadedFile);
        }

        System.out.println("converted Files -> " + grabAllConvertedFiles());

        //TODO: Make the download part works!
        basicOperations.downloadFiles(grabAllConvertedFiles());
    }


    private List<String> grabAllConvertedFiles() {
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
