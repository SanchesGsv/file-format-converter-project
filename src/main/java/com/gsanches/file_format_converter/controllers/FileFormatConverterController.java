package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.dtos.FileDto;
import com.gsanches.file_format_converter.services.FileFormatConverterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileFormatConverterController {

    private final FileFormatConverterService fileFormatConverterService;

    public FileFormatConverterController(FileFormatConverterService fileFormatConverterService) {
        this.fileFormatConverterService = fileFormatConverterService;
    }

    @GetMapping("/pdfToImage")
    public ResponseEntity<Void> pdfToImage(@RequestBody FileDto fileDto){
        fileFormatConverterService.pdfToImage(fileDto);

        return null;
    }

}
