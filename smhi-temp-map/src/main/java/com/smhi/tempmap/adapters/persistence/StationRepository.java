package com.smhi.tempmap.adapters.persistence;

import com.smhi.tempmap.domain.model.Station;
import com.smhi.tempmap.ports.StationPort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StationRepository implements StationPort {
    
    private static final com.smhi.tempmap.entity.Station JPA_STATION = null;
    
    @Override
    public Optional<Station> findByStationId(String stationId) {
        com.smhi.tempmap.entity.Station entity = 
            com.smhi.tempmap.entity.Station.findByStationId(stationId);
        return Optional.ofNullable(com.smhi.tempmap.entity.Station.toDomain(entity));
    }
    
    @Override
    public List<Station> findAll() {
        List<com.smhi.tempmap.entity.Station> entities = 
            com.smhi.tempmap.entity.Station.listAll();
        return entities.stream()
            .map(e -> e.toDomain())
            .toList();
    }
    
    @Override
    public Station save(Station station) {
        com.smhi.tempmap.entity.Station entity = 
            com.smhi.tempmap.entity.Station.fromDomain(station);
        entity.persist();
        return entity.toDomain();
    }
}