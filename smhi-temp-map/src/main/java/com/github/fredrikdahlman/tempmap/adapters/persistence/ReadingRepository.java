package com.github.fredrikdahlman.tempmap.adapters.persistence;

import com.github.fredrikdahlman.tempmap.domain.model.ReadingModel;
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
    public List<ReadingModel> findLatestByStation() {
        List<com.github.fredrikdahlman.tempmap.entity.ReadingEntity> entities = entityManager
            .createQuery(
                "SELECT r FROM com.github.fredrikdahlman.tempmap.entity.ReadingEntity r WHERE r.timestamp IN " +
                "(SELECT MAX(r2.timestamp) FROM com.github.fredrikdahlman.tempmap.entity.ReadingEntity r2 GROUP BY r2.station.id)",
                com.github.fredrikdahlman.tempmap.entity.ReadingEntity.class
            )
            .getResultList();
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public List<ReadingModel> findByStation(Long stationId, int limit) {
        List<com.github.fredrikdahlman.tempmap.entity.ReadingEntity> entities = 
            com.github.fredrikdahlman.tempmap.entity.ReadingEntity.findRecentByStationId(stationId, limit);
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public ReadingModel save(ReadingModel reading) {
        // Check if reading already exists for this station and timestamp
        Long stationId = reading.station().id();
        var existing = com.github.fredrikdahlman.tempmap.entity.ReadingEntity.find(
            "station.id = ?1 and timestamp = ?2", 
            stationId, 
            reading.timestamp()
        ).firstResult();
        
        if (existing != null) {
            return null; // Already exists, skip
        }
        
        com.github.fredrikdahlman.tempmap.entity.ReadingEntity entity = new com.github.fredrikdahlman.tempmap.entity.ReadingEntity();
        entity.station = com.github.fredrikdahlman.tempmap.entity.StationEntity.findById(reading.station().id());
        entity.temperature = reading.temperature();
        entity.timestamp = reading.timestamp();
        entity.persist();
        return entity.toDomain();
    }
}