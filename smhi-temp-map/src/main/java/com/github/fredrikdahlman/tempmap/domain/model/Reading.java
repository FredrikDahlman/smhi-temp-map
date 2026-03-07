package com.github.fredrikdahlman.tempmap.domain.model;

import java.time.Instant;

public record Reading(
    Long id,
    Station station,
    Double temperature,
    Instant timestamp
) {}