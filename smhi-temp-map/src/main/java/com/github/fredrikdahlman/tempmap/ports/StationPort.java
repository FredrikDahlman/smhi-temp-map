package com.github.fredrikdahlman.tempmap.ports;

import com.github.fredrikdahlman.tempmap.domain.model.StationModel;
import java.util.List;
import java.util.Optional;

public interface StationPort {
    Optional<StationModel> findByStationId(String stationId);
    List<StationModel> findAll();
    StationModel save(StationModel station);
}