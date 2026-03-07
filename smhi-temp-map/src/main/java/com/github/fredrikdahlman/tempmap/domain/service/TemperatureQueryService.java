package com.github.fredrikdahlman.tempmap.domain.service;

import com.github.fredrikdahlman.tempmap.domain.model.ReadingModel;
import com.github.fredrikdahlman.tempmap.domain.model.StationModel;
import com.github.fredrikdahlman.tempmap.ports.ReadingPort;
import com.github.fredrikdahlman.tempmap.ports.StationPort;
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
    
    public List<StationModel> getAllStations() {
        return stationPort.findAll();
    }
    
    public List<ReadingModel> getCurrentTemperatures() {
        return readingPort.findLatestByStation();
    }
    
    public List<ReadingModel> getTemperatureHistory(Long stationId, int hours) {
        return readingPort.findByStation(stationId, hours);
    }
}