package com.gsanches.file_format_converter.strategy;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import org.springframework.stereotype.Service;

@Service
public class PdfToImageStrategy implements FileConversionStrategy {

    private final AutoFileFormatConverterService converter;

    public PdfToImageStrategy(AutoFileFormatConverterService converter) {
        this.converter = converter;
    }

    @Override
    public FileConversionEnum getSupportedConversion() {
        return FileConversionEnum.PDF_TO_JPG;
    }

    @Override
    public boolean supportsMimeType(String mimeType) {
        return "application/pdf".equals(mimeType);
    }

    @Override
    public void convert(String filePath) {
        converter.pdfToJpg(filePath);
    }
}
