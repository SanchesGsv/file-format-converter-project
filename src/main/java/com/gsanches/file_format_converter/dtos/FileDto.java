package com.gsanches.file_format_converter.dtos;

import com.gsanches.file_format_converter.enums.FolderLocationEnum;

public record FileDto(
    String filename,
    FolderLocationEnum deleteLocal

) {
}
