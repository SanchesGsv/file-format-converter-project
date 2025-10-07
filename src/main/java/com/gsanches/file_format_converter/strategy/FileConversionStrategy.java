package com.gsanches.file_format_converter.strategy;

import com.gsanches.file_format_converter.enums.FileConversionEnum;

import java.util.List;

public interface FileConversionStrategy {
    FileConversionEnum getSupportedConversion();
    boolean supportsMimeType(String mimeType);
    List<String> convert(String filePath);
}
