package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.services.AutoWork;
import com.gsanches.file_format_converter.enums.FileConversionEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AutoWorkController {

    private final AutoWork autoWork;

    public AutoWorkController(AutoWork autoWork) {
        this.autoWork = autoWork;
    }

    @PostMapping("/pdf-to-jpg")
    public ResponseEntity<Void> pdfToImage(@RequestPart("files") List<MultipartFile> files) {
        autoWork.autoWork(files, FileConversionEnum.PDF_TO_JPG);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/jpg-to-pdf")
    public ResponseEntity<Void> jpgToPdf(@RequestPart("files") List<MultipartFile> files){
        autoWork.autoWork(files, FileConversionEnum.JPG_TO_PDF);

        return ResponseEntity.ok().build();
    }

}
