package com.gsanches.file_format_converter.servicesSolidNew.serviceAndImpl;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkSolService {
    void workSol(List<MultipartFile> files, FileConversionEnum conversionType);

}
