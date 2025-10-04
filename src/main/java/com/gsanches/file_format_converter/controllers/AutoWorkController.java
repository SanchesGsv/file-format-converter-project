package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.dtos.AllWorkDto;
import com.gsanches.file_format_converter.serviceAuto.AutoWorkService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AutoWorkController {

    private final AutoWorkService autoWorkService;

    public AutoWorkController(AutoWorkService autoWorkService) {
        this.autoWorkService = autoWorkService;
    }
    //TODO: Maybe now is a good idea to upload the archives to github! Remember to take off this method!

    @PostMapping(value = "/work", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart ("file") MultipartFile file,
            @RequestPart AllWorkDto allWorkDto
    ) {

        autoWorkService.makeEverythingWorks(file, allWorkDto);

        return ResponseEntity.ok().build();
    }

}
