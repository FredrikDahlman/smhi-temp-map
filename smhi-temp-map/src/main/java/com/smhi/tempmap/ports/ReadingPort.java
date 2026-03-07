package com.smhi.tempmap.ports;

import com.smhi.tempmap.domain.model.Reading;
import java.util.List;

public interface ReadingPort {
    List<Reading> findLatestByStation();
    List<Reading> findByStation(Long stationId, int limit);
    Reading save(Reading reading);
}