package com.smhi.tempmap.service;

import com.smhi.tempmap.client.SmhiClient;
import com.smhi.tempmap.client.SmhiClient.SmhiStationData;
import com.smhi.tempmap.entity.Reading;
import com.smhi.tempmap.entity.Station;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TemperatureService {

    private static final Logger LOG = Logger.getLogger(
        TemperatureService.class
    );
    private static final DateTimeFormatter ISO_FORMATTER =
        DateTimeFormatter.ISO_INSTANT;

    private final SmhiClient smhiClient;

    @Inject
    EntityManager entityManager;

    public TemperatureService(SmhiClient smhiClient) {
        this.smhiClient = smhiClient;
    }

    @Scheduled(every = "1h")
    @Transactional
    public void fetchAndStoreTemperature() {
        LOG.info("Starting scheduled temperature fetch from SMHI");

        try {
            List<SmhiStationData> data = smhiClient.fetchTemperatureData();
            LOG.infof("Fetched %d temperature readings from SMHI", data.size());

            for (SmhiStationData smhiData : data) {
                try {
                    Station station = Station.findByStationId(
                        smhiData.stationId()
                    );
                    if (station == null) {
                        station = new Station();
                        station.stationId = smhiData.stationId();
                        station.name =
                            smhiData.name() != null
                                ? smhiData.name()
                                : "Unknown";
                        station.latitude =
                            smhiData.latitude() != null
                                ? smhiData.latitude()
                                : 0.0;
                        station.longitude =
                            smhiData.longitude() != null
                                ? smhiData.longitude()
                                : 0.0;
                        station.persist();
                    }

                    Reading reading = new Reading();
                    reading.station = station;
                    reading.temperature = smhiData.temperature();
                    reading.timestamp = parseTimestamp(smhiData.dateTime());
                    reading.persist();
                } catch (Exception e) {
                    LOG.errorf(
                        "Error storing reading for station %s: %s",
                        smhiData.stationId(),
                        e.getMessage()
                    );
                }
            }

            LOG.info("Temperature fetch completed successfully");
        } catch (Exception e) {
            LOG.error("Failed to fetch temperature data", e);
        }
    }

    private Instant parseTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return Instant.now();
        }
        try {
            return Instant.parse(dateTimeStr);
        } catch (DateTimeParseException e) {
            LOG.warnf(
                "Failed to parse timestamp: %s, using current time",
                dateTimeStr
            );
            return Instant.now();
        }
    }

    public List<Station> getAllStations() {
        return Station.listAllStations();
    }

    public List<Reading> getCurrentTemperatures() {
        return entityManager
            .createQuery(
                "SELECT r FROM Reading r WHERE r.id IN " +
                "(SELECT MAX(r2.id) FROM Reading r2 GROUP BY r2.station.id)",
                Reading.class
            )
            .getResultList();
    }

    public List<Reading> getTemperatureHistory(Long stationId, int hours) {
        return Reading.findRecentByStationId(stationId, hours);
    }
}
