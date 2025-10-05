package com.gsanches.file_format_converter.services;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import org.springframework.web.multipart.MultipartFile;

public interface AutoWorkService {
    void makeEverythingWorks(MultipartFile file, FileConversionEnum wantedConversion);
}
