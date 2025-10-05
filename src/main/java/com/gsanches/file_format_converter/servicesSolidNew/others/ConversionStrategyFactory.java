package com.gsanches.file_format_converter.servicesSolidNew.others;

import com.gsanches.file_format_converter.enums.FileConversionEnum;
import com.gsanches.file_format_converter.servicesSolidNew.conversionsRelated.FileConversionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConversionStrategyFactory {

    private final Map<FileConversionEnum, FileConversionStrategy> strategyMap;

    public ConversionStrategyFactory(List<FileConversionStrategy> strategies) {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(FileConversionStrategy::getSupportedConversion, Function.identity()));
    }

    public FileConversionStrategy getStrategy(FileConversionEnum type) {
        return strategyMap.get(type);
    }
}
