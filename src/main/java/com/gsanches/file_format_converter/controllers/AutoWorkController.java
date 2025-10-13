package com.gsanches.file_format_converter.controllers;

import com.gsanches.file_format_converter.dto.FileDownloadDto;
import com.gsanches.file_format_converter.services.AutoWork;
import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.OtherOperations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<ByteArrayResource> downloadConvertedFiles(){

        //TODO: WORKING, BUT ADJUST FOR DOWNLOAD MORE THAN ONE AT ONCE, AND ON THE CODE BELOW, ADJUST FOR NOT ONLY getFirst()!

        List<FileDownloadDto> files = otherOperations.downloadAllConvertedFiles();

        //ADJUST FOR NOT GET ONLY THE FIRST ONE!
        ByteArrayResource resources = new ByteArrayResource(files.getFirst().getContent());;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + files.getFirst().getFilename() + "\"")
                .contentType(MediaType.parseMediaType(files.getFirst().getContentType()))
                .contentLength(files.getFirst().getSize())
                .body(resources);
    }


}
