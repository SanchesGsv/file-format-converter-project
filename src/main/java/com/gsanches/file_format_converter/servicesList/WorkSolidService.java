package com.gsanches.file_format_converter.servicesList;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkSolidService {
    void workSolid(List<MultipartFile> files, FileConversionEnum conversion);
}
