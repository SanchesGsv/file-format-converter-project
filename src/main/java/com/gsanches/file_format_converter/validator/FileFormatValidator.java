package com.gsanches.file_format_converter.validator;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileFormatValidator {

    public String detectMimeType(MultipartFile file) {
        try {
            return new Tika().detect(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to detect MIME type", e);
        }
    }
}
