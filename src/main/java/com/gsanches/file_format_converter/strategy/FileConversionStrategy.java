package com.gsanches.file_format_converter.strategy;

import com.gsanches.file_format_converter.enums.FileConversionEnum;

public interface FileConversionStrategy {
    FileConversionEnum getSupportedConversion();
    boolean supportsMimeType(String mimeType);
    void convert(String filePath);
}
