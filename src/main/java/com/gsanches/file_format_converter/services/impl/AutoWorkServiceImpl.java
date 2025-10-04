package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.dtos.AllWorkDto;
import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoBasicOperationsService;
import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import com.gsanches.file_format_converter.services.AutoWorkService;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AutoWorkServiceImpl implements AutoWorkService {

    private final AutoBasicOperationsService autoBasicOperationsService;
    private final AutoFileFormatConverterService autoFileFormatConverterService;

    public AutoWorkServiceImpl(AutoBasicOperationsService autoBasicOperationsService, AutoFileFormatConverterService autoFileFormatConverterService) {
        this.autoBasicOperationsService = autoBasicOperationsService;
        this.autoFileFormatConverterService = autoFileFormatConverterService;
    }

    public void makeEverythingWorks(MultipartFile file, AllWorkDto allWorkDto){

        String uploadDestination = autoBasicOperationsService.uploadFile(file);

        FileConversionEnum wantedConversion = allWorkDto.wantedConversion();

        List<String> absoluteDownloadFilePaths = new ArrayList<>();



        if(wantedConversion.equals(FileConversionEnum.PDF_TO_IMAGE)){
            if(!checkFileFormat(file).equals("application/pdf")){
                throw new RuntimeException("File format entry is not correct, method for pdf as an entry, but receiving " + checkFileFormat(file) + " instead.");
            }
            absoluteDownloadFilePaths = autoFileFormatConverterService.pdfToJpg(uploadDestination);
        } else if(wantedConversion.equals(FileConversionEnum.JPG_TO_PDF)) {
            if(!checkFileFormat(file).equals("image/jpeg")){
                throw new RuntimeException("File format entry is not correct, method for image/jpg as an entry, but receiving " + checkFileFormat(file) + " instead.");
            }
            absoluteDownloadFilePaths = autoFileFormatConverterService.jpgToPdf(uploadDestination);
        } else if(wantedConversion.equals(FileConversionEnum.MERGE_PDF)){
            if(!checkFileFormat(file).equals("")) {
                throw new RuntimeException("File format entry is not correct, method for / as an entry, but receiving " + checkFileFormat(file) + " instead.");
            }
            absoluteDownloadFilePaths = autoFileFormatConverterService.listMergePdf(Collections.singletonList(uploadDestination));



        }

        autoBasicOperationsService.downloadFiles(absoluteDownloadFilePaths);

        //TODO: Take off the comments from above.
//        List<String> allFiles = Collections.singletonList(absoluteDownloadFilePaths + uploadDestination);
//        autoBasicOperationsService.deleteFiles(allFiles);




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
