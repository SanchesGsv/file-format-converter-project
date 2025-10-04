package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AutoFileFormatConverterServiceImpl implements AutoFileFormatConverterService {

    private static final String ORIGINAL_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/originalFile";
    private static final String CONVERTED_FILE_FOLDER = "/home/sanches/Desktop/file-format-converter/src/main/java/com/gsanches/file_format_converter/storage/convertedFile";

    //TODO: Adjust the returns, add throw

    public List<String> pdfToJpg(String absoluteCurrentFileLocation){

        System.out.println("absoluteCurrentFileLocation" + absoluteCurrentFileLocation);

        File pdfFile = new File(absoluteCurrentFileLocation);

        if (!pdfFile.exists()) {
            System.out.println("PDF file not found: " + absoluteCurrentFileLocation);
            return null;
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);

            List<String> savedPaths = new java.util.ArrayList<>(List.of());

            for (int i = 0; i < document.getNumberOfPages(); i++) { // For each page
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                String imageName = pdfFile.getName().replace(".pdf", "") + "_page_" + (i + 1) + ".jpg";
                File imageFile = new File(CONVERTED_FILE_FOLDER, imageName);
                ImageIO.write(image, "jpg", imageFile);
                System.out.println("Saved: " + imageFile.getAbsolutePath());
                savedPaths.add(imageFile.getAbsolutePath());
            }

        //TODO: Verify if I need the catch part!

        return savedPaths;

        } catch (IOException e) {
            System.err.println("Error converting PDF: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> jpgToPdf(String absoluteCurrentFileLocation) {

        try {
            System.out.println("absoluteCurrentFileLocation jpgToPdf " + absoluteCurrentFileLocation);

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDImageXObject image = PDImageXObject.createFromFile(absoluteCurrentFileLocation, doc);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page);

            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();

            float pageWidth = page.getMediaBox().getWidth();
            float pageHeight = page.getMediaBox().getHeight();

            float scale = Math.min(pageWidth / imgWidth, pageHeight / imgHeight) * 0.9f;

            float x = (pageWidth - imgWidth * scale) / 2;
            float y = (pageHeight - imgHeight * scale) / 2;

            contentStream.drawImage(image, x, y, imgWidth * scale, imgHeight * scale);
            contentStream.close();

            String filename = "newPdf.pdf";
            File outputFolder = new File(CONVERTED_FILE_FOLDER);

            doc.save(new File(outputFolder, filename));
            doc.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JPG to PDF", e);
        }


        //TODO: May return a list even if on the catch part! Make sure to fix this things (even if is on another place)
        return List.of();
    }

    @Override
    public List<String> listMergePdf(List<String> absoluteCurrentFileLocationList) {
        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            for (String path: absoluteCurrentFileLocationList) {
                merger.addSource(new File(path));
            }
            merger.setDestinationFileName(CONVERTED_FILE_FOLDER);
            merger.mergeDocuments(null); // null = default memory settings

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //TODO: Change the return
        return new ArrayList<>();
    }

    @Override
    public List<String> listPdfToJpg(List<String> absoluteCurrentFileLocationList) {
        List<String> destinationPaths = new ArrayList<>();

        for(String path: absoluteCurrentFileLocationList){
            //TODO: Certificate if the line above works
            destinationPaths.addAll(pdfToJpg((path)));
        }
        return destinationPaths;
    }

    @Override
    public List<String> listJpgToPdf(List<String> absoluteCurrentFileLocationList) {
        List<String> destinationPaths = new ArrayList<>();

        for(String path: absoluteCurrentFileLocationList){
            //TODO: Certificate if the line above works
            destinationPaths.addAll(jpgToPdf((path)));
        }
        return destinationPaths;
    }

}
