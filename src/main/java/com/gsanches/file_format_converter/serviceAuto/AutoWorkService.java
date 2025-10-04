package com.gsanches.file_format_converter.serviceAuto;

import com.gsanches.file_format_converter.dtos.AllWorkDto;
import org.springframework.web.multipart.MultipartFile;

public interface AutoWorkService {
    void makeEverythingWorks(MultipartFile file, AllWorkDto allWorkDto);

}
