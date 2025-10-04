package com.gsanches.file_format_converter.dtos;

import com.gsanches.file_format_converter.enums.ConversionEnum;

public record AllWorkDto(
        ConversionEnum wantedConversion

) {
}
