package com.github.fredrikdahlman.tempmap.ports;

import com.github.fredrikdahlman.tempmap.domain.model.Station;
import java.util.List;
import java.util.Optional;

public interface StationPort {
    Optional<Station> findByStationId(String stationId);
    List<Station> findAll();
    Station save(Station station);
}