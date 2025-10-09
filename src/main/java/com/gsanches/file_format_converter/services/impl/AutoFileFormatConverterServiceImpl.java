package com.gsanches.file_format_converter.services.impl;

import com.gsanches.file_format_converter.services.AutoFileFormatConverterService;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AutoFileFormatConverterServiceImpl implements AutoFileFormatConverterService {

    @Value("${storage.converted}")
    private String convertedFileFolder;

    @Value("${storage.uploads}")
    private String uploadsFileFolder;

    //TODO: Take off the Sol name!
    public List<String> pdfToJpg(String absoluteCurrentFileLocation) {

        System.out.println("pdfToJpg absoluteCurrentFileLocation " + absoluteCurrentFileLocation);

        //TODO: Put the line above on a try catch

        File pdfFile;
        try {
            pdfFile = new File(absoluteCurrentFileLocation);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }


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
                File imageFile = new File(convertedFileFolder, imageName);
                ImageIO.write(image, "jpg", imageFile);
                System.out.println("Saved: " + imageFile.getAbsolutePath());
                savedPaths.add(imageFile.getAbsolutePath());
            }

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

            String filename = (
                    absoluteCurrentFileLocation
                            .substring(uploadsFileFolder.length() + "/".length(),
                                    absoluteCurrentFileLocation.length() - ".jpg".length()
                            )
                            + ".pdf"
            );

            File outputFolder = new File(convertedFileFolder);


            doc.save(new File(outputFolder, filename));
            doc.close();

            return List.of();

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JPG to PDF", e);
        }
    }

    @Override
    public List<String> listMergePdf(List<String> absoluteCurrentFileLocationList) {
        try {
            PDFMergerUtility merger = new PDFMergerUtility();
            for (String path : absoluteCurrentFileLocationList) {
                merger.addSource(new File(path));
            }
            merger.setDestinationFileName(convertedFileFolder);
            merger.mergeDocuments(null);

            return new ArrayList<>();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listPdfToJpg(List<String> absoluteCurrentFileLocationList) {
        List<String> destinationPaths = new ArrayList<>();

        for (String path : absoluteCurrentFileLocationList) {
            System.out.println("fileLocation AutoFileFormatConverter (path) -> " + path);
            destinationPaths.addAll(pdfToJpg((path)));
        }
        return destinationPaths;
    }

    @Override
    public List<String> listJpgToPdf(List<String> absoluteCurrentFileLocationList) {
        List<String> destinationPaths = new ArrayList<>();

        for (String path : absoluteCurrentFileLocationList) {
            destinationPaths.addAll(jpgToPdf((path)));
        }
        return destinationPaths;
    }

}
