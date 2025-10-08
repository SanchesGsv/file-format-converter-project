package com.gsanches.file_format_converter.dto;

import com.gsanches.file_format_converter.enums.FolderLocationEnum;

public record String(
        java.lang.String filename,
        FolderLocationEnum deleteLocal

) {
}
