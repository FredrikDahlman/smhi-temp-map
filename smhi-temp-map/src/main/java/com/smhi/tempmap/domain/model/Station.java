package com.smhi.tempmap.domain.model;

public record Station(
    Long id,
    String stationId,
    String name,
    Double latitude,
    Double longitude
) {}