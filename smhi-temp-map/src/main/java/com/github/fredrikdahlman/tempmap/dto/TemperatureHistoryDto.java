package com.github.fredrikdahlman.tempmap.dto;

import com.github.fredrikdahlman.tempmap.domain.model.Reading;
import java.util.List;

public class TemperatureHistoryDto {
    public Long stationId;
    public String stationName;
    public List<TemperaturePoint> readings;

    public static TemperatureHistoryDto from(Long stationId, String stationName, List<Reading> readings) {
        TemperatureHistoryDto dto = new TemperatureHistoryDto();
        dto.stationId = stationId;
        dto.stationName = stationName;
        dto.readings = readings.stream()
            .map(r -> new TemperaturePoint(r.temperature(), r.timestamp().toString()))
            .toList();
        return dto;
    }

    public record TemperaturePoint(Double temperature, String timestamp) {}
}