package com.gsanches.file_format_converter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileDownloadDto {
    private String filename;
    private byte[] content;
    private long size;
    private String contentType;

}
