package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoBasicOperationsService;
import com.gsanches.file_format_converter.factory.ConversionStrategyFactory;
import com.gsanches.file_format_converter.strategy.FileConversionStrategy;
import com.gsanches.file_format_converter.validator.FileFormatValidator;
import com.gsanches.file_format_converter.services.AutoWork;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutoWorkImpl implements AutoWork {

    private final AutoBasicOperationsService basicOperations;
    private final FileFormatValidator validator;
    private final ConversionStrategyFactory strategyFactory;

    public AutoWorkImpl(AutoBasicOperationsService basicOperations, FileFormatValidator validator, ConversionStrategyFactory strategyFactory) {
        this.basicOperations = basicOperations;
        this.validator = validator;
        this.strategyFactory = strategyFactory;
    }


    @Override
    public void autoWork(List<MultipartFile> files, FileConversionEnum conversionType) {


        //TODO: See what should return of here (above)
        FileConversionStrategy strategy = strategyFactory.getStrategy(conversionType);

        System.out.println("strategy (FileConversionStrategy) " + strategy);

        List<String> allConvertedPaths = new ArrayList<>();

//        if (conversionType == FileConversionEnum.MERGE_PDF) {
//            List<String> uploadPaths = files.stream()
//                    .map(basicOperations::uploadFile)
//                    .collect(Collectors.toList());
//
//            //TODO: Use this method (under)
////            allConvertedPaths = basicOperations.mergePdfFiles(uploadPaths);
//
//            allConvertedPaths = uploadPaths(uploadPaths);
//
//            System.out.println(" workSol " + uploadPaths);
//
//        } else {

        for (MultipartFile file : files) {
            //TODO: Verify if the place is correct (put the code here!)
            System.out.println("file -> !! " + file);

            String mimeType = validator.detectMimeType(file);
            if (!strategy.supportsMimeType(mimeType)) {
                throw new IllegalArgumentException("Invalid MIME type: " + mimeType);
            }

            String uploadPath = basicOperations.uploadFile(file);

            System.out.println("before -> " + uploadPath);

            allConvertedPaths.addAll(strategy.convert(uploadPath));
        }
//    }

        System.out.println("allConvertedPaths " + allConvertedPaths);
        basicOperations.downloadFiles(allConvertedPaths);
    }

    //todo: Take off this from here, and also add logic!
    private List<String> uploadPaths(List<String> paths) {
        return paths;
    }

}
