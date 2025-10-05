package com.gsanches.file_format_converter.servicesList.impl;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoBasicOperationsService;
import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import com.gsanches.file_format_converter.servicesList.WorkSolidService;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkSolidServiceImpl implements WorkSolidService {

    private final AutoBasicOperationsService autoBasicOperationsService;
    private final AutoFileFormatConverterService autoFileFormatConverterService;

    public WorkSolidServiceImpl(AutoBasicOperationsService autoBasicOperationsService, AutoFileFormatConverterService autoFileFormatConverterService) {
        this.autoBasicOperationsService = autoBasicOperationsService;
        this.autoFileFormatConverterService = autoFileFormatConverterService;
    }

    @Override
    public void workSolid(List<MultipartFile> files, FileConversionEnum conversion) {
        System.out.println("SOLID PART!!!");

        List<String> allDownloadPaths = new ArrayList<>();

        for (MultipartFile file : files) {
            String uploadDestination = autoBasicOperationsService.uploadFile(file);
            String mimeType = checkFileFormat(file);

            List<String> convertedPaths = new ArrayList<>();

            switch (conversion) {
                case PDF_TO_IMAGE:
                    if (!mimeType.equals("application/pdf")) {
                        throw new RuntimeException(("Expected PDF, got " + mimeType));
                    }

                    convertedPaths.addAll(autoFileFormatConverterService.pdfToJpg(uploadDestination));
                    break;

                case JPG_TO_PDF:
                    if (!mimeType.equals("image/jpeg")) {
                        throw new RuntimeException(("Expected image/jpeg, got " + mimeType));
                    }

                    convertedPaths.addAll(autoFileFormatConverterService.jpgToPdf(uploadDestination));
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported conversion type.");
            }

            allDownloadPaths.addAll(convertedPaths);

        }
        autoBasicOperationsService.downloadFiles(allDownloadPaths);
    }

    private String checkFileFormat(MultipartFile file) {
        Tika tika = new Tika();
        try {
            return tika.detect(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}