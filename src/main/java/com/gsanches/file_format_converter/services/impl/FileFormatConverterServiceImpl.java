package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.dtos.FileDto;
import com.gsanches.file_format_converter.services.FileFormatConverterService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class FileFormatConverterServiceImpl implements FileFormatConverterService {

    private static final String ORIGINAL_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/originalFile";
    private static final String CONVERTED_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/convertedFile";

    public void pdfToImage(FileDto fileDto){
        System.out.println("pdfToImage Service!");

        String filename = fileDto.filename();

        String pdfPath = ORIGINAL_FILE_FOLDER + "/" + filename;
        File pdfFile = new File(pdfPath);

        if (!pdfFile.exists()) {
            System.out.println("PDF file not found: " + pdfPath);
            return;
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) { // For each page
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                String imageName = pdfFile.getName().replace(".pdf", "") + "_page_" + (i + 1) + ".jpg";
                File imageFile = new File(CONVERTED_FILE_FOLDER, imageName);
                ImageIO.write(image, "jpg", imageFile);
                System.out.println("Saved: " + imageFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Error converting PDF: " + e.getMessage());
        }
    }

}
