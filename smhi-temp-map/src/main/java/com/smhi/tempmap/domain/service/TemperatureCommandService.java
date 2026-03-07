package com.smhi.tempmap.domain.service;

import com.smhi.tempmap.client.SmhiClient.SmhiStationData;
import com.smhi.tempmap.domain.model.Station;
import com.smhi.tempmap.ports.ReadingPort;
import com.smhi.tempmap.ports.StationPort;
import com.smhi.tempmap.ports.WeatherDataPort;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TemperatureCommandService {
    
    private static final Logger LOG = Logger.getLogger(TemperatureCommandService.class);
    
    private final StationPort stationPort;
    private final ReadingPort readingPort;
    private final WeatherDataPort weatherDataPort;
    
    @Inject
    public TemperatureCommandService(
            StationPort stationPort, 
            ReadingPort readingPort,
            WeatherDataPort weatherDataPort) {
        this.stationPort = stationPort;
        this.readingPort = readingPort;
        this.weatherDataPort = weatherDataPort;
    }
    
    @Scheduled(every = "1h")
    @Transactional
    public void fetchAndStoreTemperature() {
        LOG.info("Starting scheduled temperature fetch from SMHI");
        
        try {
            List<SmhiStationData> data = weatherDataPort.fetchTemperatureData();
            LOG.infof("Fetched %d temperature readings from SMHI", data.size());
            
            for (SmhiStationData smhiData : data) {
                try {
                    Station station = stationPort.findByStationId(smhiData.stationId())
                        .orElseGet(() -> createStation(smhiData));
                    
                    com.smhi.tempmap.domain.model.Reading reading = 
                        new com.smhi.tempmap.domain.model.Reading(
                            null,
                            station,
                            smhiData.temperature(),
                            parseTimestamp(smhiData.dateTime())
                        );
                    readingPort.save(reading);
                } catch (Exception e) {
                    LOG.errorf("Error storing reading for station %s: %s", 
                        smhiData.stationId(), e.getMessage());
                }
            }
            
            LOG.info("Temperature fetch completed successfully");
        } catch (Exception e) {
            LOG.error("Failed to fetch temperature data", e);
        }
    }
    
    private Station createStation(SmhiStationData data) {
        Station station = new Station(
            null,
            data.stationId(),
            data.name() != null ? data.name() : "Unknown",
            data.latitude() != null ? data.latitude() : 0.0,
            data.longitude() != null ? data.longitude() : 0.0
        );
        return stationPort.save(station);
    }
    
    private Instant parseTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return Instant.now();
        }
        try {
            return Instant.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            LOG.warnf("Failed to parse timestamp: %s, using current time", dateTimeStr);
            return Instant.now();
        }
    }
}