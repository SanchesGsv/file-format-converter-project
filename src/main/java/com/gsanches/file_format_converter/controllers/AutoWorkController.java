package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.servicesSolidNew.serviceAndImpl.WorkSolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AutoWorkController {

    private final WorkSolService workSolService;

    public AutoWorkController(WorkSolService workSolService) {
        this.workSolService = workSolService;
    }

    @PostMapping("/pdf-to-image-sol")
    public ResponseEntity<Void> workWithSol(
            @RequestPart("files") List<MultipartFile> files
    ) {
        workSolService.workSol(files, FileConversionEnum.PDF_TO_IMAGE);

        return ResponseEntity.ok().build();
    }

}
