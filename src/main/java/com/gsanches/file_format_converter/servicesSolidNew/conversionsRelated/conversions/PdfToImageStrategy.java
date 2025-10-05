package com.gsanches.file_format_converter.servicesSolidNew.conversionsRelated.conversions;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import com.gsanches.file_format_converter.servicesSolidNew.conversionsRelated.FileConversionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfToImageStrategy implements FileConversionStrategy {

    private final AutoFileFormatConverterService converter;

    public PdfToImageStrategy(AutoFileFormatConverterService converter) {
        this.converter = converter;
    }

    @Override
    public FileConversionEnum getSupportedConversion() {
        return FileConversionEnum.PDF_TO_IMAGE;
    }

    @Override
    public boolean supportsMimeType(String mimeType) {
        return "application/pdf".equals(mimeType);
    }

    @Override
    public List<String> convert(String filePath) {
        return converter.pdfToJpg(filePath);
    }
}
