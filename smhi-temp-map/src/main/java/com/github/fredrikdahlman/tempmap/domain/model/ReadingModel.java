package com.github.fredrikdahlman.tempmap.domain.model;

import java.time.Instant;

public record ReadingModel(
    Long id,
    StationModel station,
    Double temperature,
    Instant timestamp
) {}