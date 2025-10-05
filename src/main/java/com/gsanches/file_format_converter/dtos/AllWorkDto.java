package com.gsanches.file_format_converter.dtos;

import com.gsanches.file_format_converter.enums.FileConversionEnum;

public record AllWorkDto(
        FileConversionEnum wantedConversion

) {
}
