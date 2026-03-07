package com.smhi.tempmap.adapters.persistence;

import com.smhi.tempmap.domain.model.Reading;
import com.smhi.tempmap.ports.ReadingPort;
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
        List<com.smhi.tempmap.entity.Reading> entities = entityManager
            .createQuery(
                "SELECT r FROM com.smhi.tempmap.entity.Reading r WHERE r.timestamp IN " +
                "(SELECT MAX(r2.timestamp) FROM com.smhi.tempmap.entity.Reading r2 GROUP BY r2.station.id)",
                com.smhi.tempmap.entity.Reading.class
            )
            .getResultList();
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public List<Reading> findByStation(Long stationId, int limit) {
        List<com.smhi.tempmap.entity.Reading> entities = 
            com.smhi.tempmap.entity.Reading.findRecentByStationId(stationId, limit);
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public Reading save(Reading reading) {
        // Check if reading already exists for this station and timestamp
        Long stationId = reading.station().id();
        var existing = com.smhi.tempmap.entity.Reading.find(
            "station.id = ?1 and timestamp = ?2", 
            stationId, 
            reading.timestamp()
        ).firstResult();
        
        if (existing != null) {
            return null; // Already exists, skip
        }
        
        com.smhi.tempmap.entity.Reading entity = new com.smhi.tempmap.entity.Reading();
        entity.station = com.smhi.tempmap.entity.Station.findById(reading.station().id());
        entity.temperature = reading.temperature();
        entity.timestamp = reading.timestamp();
        entity.persist();
        return entity.toDomain();
    }
}