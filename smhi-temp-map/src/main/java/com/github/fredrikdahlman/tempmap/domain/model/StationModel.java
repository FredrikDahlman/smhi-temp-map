package com.github.fredrikdahlman.tempmap.domain.model;

public record StationModel(
    Long id,
    String stationId,
    String name,
    Double latitude,
    Double longitude
) {}