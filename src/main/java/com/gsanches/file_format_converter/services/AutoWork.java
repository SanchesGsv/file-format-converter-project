package com.gsanches.file_format_converter.services;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AutoWork {
    void autoWork(List<MultipartFile> files, FileConversionEnum conversionType);

}
