package com.github.fredrikdahlman.tempmap.ports;

import com.github.fredrikdahlman.tempmap.domain.model.ReadingModel;
import java.util.List;

public interface ReadingPort {
    List<ReadingModel> findLatestByStation();
    List<ReadingModel> findByStation(Long stationId, int limit);
    ReadingModel save(ReadingModel reading);
}