package com.gsanches.file_format_converter.services;

import java.util.List;

public interface AutoFileFormatConverterService {
    //One input
    List<String> pdfToJpg(String absoluteCurrentFileLocation);
    List<String> jpgToPdf(String absoluteCurrentFileLocation);

    //Multiple Inputs
    List<String> listPdfToJpg(List<String> absoluteCurrentFileLocationList);
    List<String> listJpgToPdf(List<String> absoluteCurrentFileLocationList);

    List<String> listMergePdf(List<String> absoluteCurrentFileLocationList);

}
