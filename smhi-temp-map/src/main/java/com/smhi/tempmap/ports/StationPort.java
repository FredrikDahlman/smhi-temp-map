package com.smhi.tempmap.ports;

import com.smhi.tempmap.domain.model.Station;
import java.util.List;
import java.util.Optional;

public interface StationPort {
    Optional<Station> findByStationId(String stationId);
    List<Station> findAll();
    Station save(Station station);
}