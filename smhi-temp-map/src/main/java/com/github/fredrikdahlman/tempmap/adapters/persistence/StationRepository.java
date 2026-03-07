package com.github.fredrikdahlman.tempmap.adapters.persistence;

import com.github.fredrikdahlman.tempmap.domain.model.StationModel;
import com.github.fredrikdahlman.tempmap.entity.StationEntity;
import com.github.fredrikdahlman.tempmap.ports.StationPort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StationRepository implements StationPort {
    
    private static final StationEntity JPA_STATION = null;
    
    @Override
    public Optional<StationModel> findByStationId(String stationId) {
        StationEntity entity = 
            StationEntity.findByStationId(stationId);
        return Optional.ofNullable(StationEntity.toDomain(entity));
    }
    
    @Override
    public List<StationModel> findAll() {
        List<StationEntity> entities = 
            StationEntity.listAll();
        return entities.stream()
            .map(e -> e.toDomain())
            .toList();
    }
    
    @Override
    public StationModel save(StationModel station) {
        StationEntity entity = 
            StationEntity.fromDomain(station);
        entity.persist();
        return entity.toDomain();
    }
}