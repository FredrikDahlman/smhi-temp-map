package com.github.fredrikdahlman.tempmap.ports;

import com.github.fredrikdahlman.tempmap.domain.model.Reading;
import java.util.List;

public interface ReadingPort {
    List<Reading> findLatestByStation();
    List<Reading> findByStation(Long stationId, int limit);
    Reading save(Reading reading);
}