package com.github.fredrikdahlman.tempmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "stations")
public class StationEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "station_id", unique = true, nullable = false)
    public String stationId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public Double latitude;

    @Column(nullable = false)
    public Double longitude;

    public com.github.fredrikdahlman.tempmap.domain.model.StationModel toDomain() {
        return new com.github.fredrikdahlman.tempmap.domain.model.StationModel(
            id, stationId, name, latitude, longitude
        );
    }

    public static com.github.fredrikdahlman.tempmap.domain.model.StationModel toDomain(StationEntity entity) {
        return entity != null ? entity.toDomain() : null;
    }

    public static StationEntity fromDomain(com.github.fredrikdahlman.tempmap.domain.model.StationModel domain) {
        if (domain == null) return null;
        StationEntity entity = new StationEntity();
        entity.id = domain.id();
        entity.stationId = domain.stationId();
        entity.name = domain.name();
        entity.latitude = domain.latitude();
        entity.longitude = domain.longitude();
        return entity;
    }

    public static StationEntity findByStationId(String stationId) {
        return find("stationId", stationId).firstResult();
    }

    public static List<StationEntity> listAllStations() {
        return listAll();
    }
}
