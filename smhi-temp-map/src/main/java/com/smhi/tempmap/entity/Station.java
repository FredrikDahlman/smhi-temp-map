package com.smhi.tempmap.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "stations")
public class Station extends PanacheEntity {

    @Column(name = "station_id", unique = true, nullable = false)
    public String stationId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public Double latitude;

    @Column(nullable = false)
    public Double longitude;

    public static Station findByStationId(String stationId) {
        return find("stationId", stationId).firstResult();
    }

    public static List<Station> listAllStations() {
        return listAll();
    }
}
