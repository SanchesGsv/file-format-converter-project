package com.gsanches.file_format_converter.servicesSolidNew.serviceAndImpl.impl;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoBasicOperationsService;
import com.gsanches.file_format_converter.servicesSolidNew.others.ConversionStrategyFactory;
import com.gsanches.file_format_converter.servicesSolidNew.conversionsRelated.FileConversionStrategy;
import com.gsanches.file_format_converter.servicesSolidNew.others.FileFormatValidator;
import com.gsanches.file_format_converter.servicesSolidNew.serviceAndImpl.WorkSolService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkSolServiceImpl implements WorkSolService {

    private final AutoBasicOperationsService basicOperations;
    private final FileFormatValidator validator;
    private final ConversionStrategyFactory strategyFactory;

    public WorkSolServiceImpl(AutoBasicOperationsService basicOperations, FileFormatValidator validator, ConversionStrategyFactory strategyFactory) {
        this.basicOperations = basicOperations;
        this.validator = validator;
        this.strategyFactory = strategyFactory;
    }


    @Override
    public void workSol(List<MultipartFile> files, FileConversionEnum conversionType) {
        System.out.println("SOL");

        FileConversionStrategy strategy = strategyFactory.getStrategy(conversionType);
        List<String> allConvertedPaths = new ArrayList<>();

        if (conversionType == FileConversionEnum.MERGE_PDF) {
            List<String> uploadPaths = files.stream()
                    .map(basicOperations::uploadFile)
                    .collect(Collectors.toList());

            //TODO: Use this method (under)
//            allConvertedPaths = basicOperations.mergePdfFiles(uploadPaths);

            allConvertedPaths = uploadPaths(uploadPaths);

            System.out.println(" workSol " + uploadPaths);

        } else {
            for (MultipartFile file : files) {
                String mimeType = validator.detectMimeType(file);
                if (!strategy.supportsMimeType(mimeType)) {
                    throw new IllegalArgumentException("Invalid MIME type: " + mimeType);
                }

                String uploadPath = basicOperations.uploadFile(file);

                System.out.println("before -> " + uploadPath);

                allConvertedPaths.addAll(strategy.convert(uploadPath));
            }
        }

        basicOperations.downloadFiles(allConvertedPaths);
    }

    //todo: Take off this from here, and also add logic!
    private List<String> uploadPaths(List<String> paths){
        return paths;
    }

}
