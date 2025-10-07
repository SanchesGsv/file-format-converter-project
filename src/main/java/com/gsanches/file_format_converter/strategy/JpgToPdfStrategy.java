package com.gsanches.file_format_converter.strategy;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpgToPdfStrategy implements FileConversionStrategy {

    private final AutoFileFormatConverterService converter;

    public JpgToPdfStrategy(AutoFileFormatConverterService converter) {
        this.converter = converter;
    }

    @Override
    public FileConversionEnum getSupportedConversion() {
        return FileConversionEnum.JPG_TO_PDF;
    }

    @Override
    public boolean supportsMimeType(String mimeType) {
        return "image/jpeg".equals(mimeType);
    }

    @Override
    public List<String> convert(String filePath) {
        return converter.jpgToPdf(filePath);
    }

}
