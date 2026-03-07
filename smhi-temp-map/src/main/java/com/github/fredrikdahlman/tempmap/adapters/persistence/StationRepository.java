package com.github.fredrikdahlman.tempmap.adapters.persistence;

import com.github.fredrikdahlman.tempmap.domain.model.Station;
import com.github.fredrikdahlman.tempmap.ports.StationPort;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StationRepository implements StationPort {
    
    private static final com.github.fredrikdahlman.tempmap.entity.Station JPA_STATION = null;
    
    @Override
    public Optional<Station> findByStationId(String stationId) {
        com.github.fredrikdahlman.tempmap.entity.Station entity = 
            com.github.fredrikdahlman.tempmap.entity.Station.findByStationId(stationId);
        return Optional.ofNullable(com.github.fredrikdahlman.tempmap.entity.Station.toDomain(entity));
    }
    
    @Override
    public List<Station> findAll() {
        List<com.github.fredrikdahlman.tempmap.entity.Station> entities = 
            com.github.fredrikdahlman.tempmap.entity.Station.listAll();
        return entities.stream()
            .map(e -> e.toDomain())
            .toList();
    }
    
    @Override
    public Station save(Station station) {
        com.github.fredrikdahlman.tempmap.entity.Station entity = 
            com.github.fredrikdahlman.tempmap.entity.Station.fromDomain(station);
        entity.persist();
        return entity.toDomain();
    }
}