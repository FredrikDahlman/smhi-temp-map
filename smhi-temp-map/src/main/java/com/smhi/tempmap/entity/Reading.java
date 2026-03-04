package com.smhi.tempmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "readings")
public class Reading extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    public Station station;

    @Column(nullable = false)
    public Double temperature;

    @Column(nullable = false)
    public Instant timestamp;

    public static List<Reading> findByStationId(Long stationId) {
        return list("station.id = ?1", stationId);
    }

    public static List<Reading> findRecentByStationId(Long stationId, int limit) {
        return find("station.id = ?1 ORDER BY timestamp DESC", stationId).page(0, limit).list();
    }
}
