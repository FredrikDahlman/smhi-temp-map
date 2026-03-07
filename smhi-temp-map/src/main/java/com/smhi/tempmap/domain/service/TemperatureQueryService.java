package com.smhi.tempmap.domain.service;

import com.smhi.tempmap.domain.model.Reading;
import com.smhi.tempmap.domain.model.Station;
import com.smhi.tempmap.ports.ReadingPort;
import com.smhi.tempmap.ports.StationPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TemperatureQueryService {
    
    private final StationPort stationPort;
    private final ReadingPort readingPort;
    
    @Inject
    public TemperatureQueryService(StationPort stationPort, ReadingPort readingPort) {
        this.stationPort = stationPort;
        this.readingPort = readingPort;
    }
    
    public List<Station> getAllStations() {
        return stationPort.findAll();
    }
    
    public List<Reading> getCurrentTemperatures() {
        return readingPort.findLatestByStation();
    }
    
    public List<Reading> getTemperatureHistory(Long stationId, int hours) {
        return readingPort.findByStation(stationId, hours);
    }
}