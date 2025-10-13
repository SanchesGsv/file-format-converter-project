package com.gsanches.file_format_converter.services;

import com.gsanches.file_format_converter.dto.FileDownloadDto;

import java.util.List;

public interface OtherOperations {
    List<FileDownloadDto> downloadAllConvertedFiles();

    List<String> grabAllConvertedFiles();

}
