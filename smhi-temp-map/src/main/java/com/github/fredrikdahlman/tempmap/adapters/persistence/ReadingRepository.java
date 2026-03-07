package com.github.fredrikdahlman.tempmap.adapters.persistence;

import com.github.fredrikdahlman.tempmap.domain.model.Reading;
import com.github.fredrikdahlman.tempmap.ports.ReadingPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class ReadingRepository implements ReadingPort {
    
    @Inject
    EntityManager entityManager;
    
    @Override
    public List<Reading> findLatestByStation() {
        List<com.github.fredrikdahlman.tempmap.entity.Reading> entities = entityManager
            .createQuery(
                "SELECT r FROM com.github.fredrikdahlman.tempmap.entity.Reading r WHERE r.timestamp IN " +
                "(SELECT MAX(r2.timestamp) FROM com.github.fredrikdahlman.tempmap.entity.Reading r2 GROUP BY r2.station.id)",
                com.github.fredrikdahlman.tempmap.entity.Reading.class
            )
            .getResultList();
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public List<Reading> findByStation(Long stationId, int limit) {
        List<com.github.fredrikdahlman.tempmap.entity.Reading> entities = 
            com.github.fredrikdahlman.tempmap.entity.Reading.findRecentByStationId(stationId, limit);
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public Reading save(Reading reading) {
        // Check if reading already exists for this station and timestamp
        Long stationId = reading.station().id();
        var existing = com.github.fredrikdahlman.tempmap.entity.Reading.find(
            "station.id = ?1 and timestamp = ?2", 
            stationId, 
            reading.timestamp()
        ).firstResult();
        
        if (existing != null) {
            return null; // Already exists, skip
        }
        
        com.github.fredrikdahlman.tempmap.entity.Reading entity = new com.github.fredrikdahlman.tempmap.entity.Reading();
        entity.station = com.github.fredrikdahlman.tempmap.entity.Station.findById(reading.station().id());
        entity.temperature = reading.temperature();
        entity.timestamp = reading.timestamp();
        entity.persist();
        return entity.toDomain();
    }
}