package com.github.fredrikdahlman.tempmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "readings")
public class ReadingEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    public StationEntity station;

    @Column(nullable = false)
    public Double temperature;

    @Column(nullable = false)
    public Instant timestamp;

    public com.github.fredrikdahlman.tempmap.domain.model.ReadingModel toDomain() {
        return new com.github.fredrikdahlman.tempmap.domain.model.ReadingModel(
            id, 
            station != null ? station.toDomain() : null, 
            temperature, 
            timestamp
        );
    }

    public static com.github.fredrikdahlman.tempmap.domain.model.ReadingModel toDomain(ReadingEntity entity) {
        return entity != null ? entity.toDomain() : null;
    }

    public static List<com.github.fredrikdahlman.tempmap.domain.model.ReadingModel> toDomainList(List<ReadingEntity> entities) {
        return entities.stream().map(e -> e.toDomain()).toList();
    }

    public static List<ReadingEntity> findByStationId(Long stationId) {
        return list("station.id = ?1", stationId);
    }

    public static List<ReadingEntity> findRecentByStationId(Long stationId, int limit) {
        return find("station.id = ?1 ORDER BY timestamp DESC", stationId).page(0, limit).list();
    }
}
