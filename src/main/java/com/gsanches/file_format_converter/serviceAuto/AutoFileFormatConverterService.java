package com.gsanches.file_format_converter.serviceAuto;

import java.util.List;

public interface AutoFileFormatConverterService {
    //TODO: See if image is jpg, if yes change the method name.
    List<String> pdfToImage(String absoluteCurrentFileLocation);

    List<String> jpgToPdf(String absoluteCurrentFileLocation);

}
