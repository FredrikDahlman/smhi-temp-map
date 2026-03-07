package com.github.fredrikdahlman.tempmap.dto;

import com.github.fredrikdahlman.tempmap.domain.model.Station;

public class StationDto {
    public Long id;
    public String stationId;
    public String name;
    public Double latitude;
    public Double longitude;
    public Double temperature;
    public String timestamp;

    public static StationDto from(Station station) {
        StationDto dto = new StationDto();
        dto.id = station.id();
        dto.stationId = station.stationId();
        dto.name = station.name();
        dto.latitude = station.latitude();
        dto.longitude = station.longitude();
        return dto;
    }

    public static StationDto from(com.github.fredrikdahlman.tempmap.entity.Station station) {
        StationDto dto = new StationDto();
        dto.id = station.id;
        dto.stationId = station.stationId;
        dto.name = station.name;
        dto.latitude = station.latitude;
        dto.longitude = station.longitude;
        return dto;
    }

    public static StationDto from(Station station, Double temperature, String timestamp) {
        StationDto dto = from(station);
        dto.temperature = temperature;
        dto.timestamp = timestamp;
        return dto;
    }
    
    public static StationDto from(com.github.fredrikdahlman.tempmap.entity.Station station, Double temperature, String timestamp) {
        StationDto dto = from(station);
        dto.temperature = temperature;
        dto.timestamp = timestamp;
        return dto;
    }
}
