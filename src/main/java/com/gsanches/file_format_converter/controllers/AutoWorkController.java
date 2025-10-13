package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.services.AutoWork;
import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.OtherOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AutoWorkController {

    private final AutoWork autoWork;
    private final OtherOperations otherOperations;

    public AutoWorkController(AutoWork autoWork, OtherOperations otherOperations) {
        this.autoWork = autoWork;
        this.otherOperations = otherOperations;
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

    @GetMapping("/download-converted-files")
    public ResponseEntity<Void> downloadConvertedFiles(){
        System.out.println("Before");
        otherOperations.downloadAllConvertedFiles();
        System.out.println("After");

        return ResponseEntity.ok().build();
    }


}
