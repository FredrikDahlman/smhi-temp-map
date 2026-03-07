package com.github.fredrikdahlman.tempmap.adapters.persistence;

import com.github.fredrikdahlman.tempmap.domain.model.ReadingModel;
import com.github.fredrikdahlman.tempmap.entity.ReadingEntity;
import com.github.fredrikdahlman.tempmap.entity.StationEntity;
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
        List<ReadingEntity> entities = entityManager
            .createQuery(
                "SELECT r FROM ReadingEntity r WHERE r.timestamp = " +
                "(SELECT MAX(r2.timestamp) FROM ReadingEntity r2 WHERE r2.station.id = r.station.id)",
                ReadingEntity.class
            )
            .getResultList();
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public List<ReadingModel> findByStation(Long stationId, int limit) {
        List<ReadingEntity> entities = ReadingEntity.findRecentByStationId(stationId, limit);
        return entities.stream().map(e -> e.toDomain()).toList();
    }
    
    @Override
    public ReadingModel save(ReadingModel reading) {
        // Check if reading already exists for this station and timestamp
        Long stationId = reading.station().id();
        var existing = ReadingEntity.find(
            "station.id = ?1 and timestamp = ?2", 
            stationId, 
            reading.timestamp()
        ).firstResult();
        
        if (existing != null) {
            return null; // Already exists, skip
        }
        
        ReadingEntity entity = new ReadingEntity();
        entity.station = StationEntity.findById(reading.station().id());
        entity.temperature = reading.temperature();
        entity.timestamp = reading.timestamp();
        entity.persist();
        return entity.toDomain();
    }
}
