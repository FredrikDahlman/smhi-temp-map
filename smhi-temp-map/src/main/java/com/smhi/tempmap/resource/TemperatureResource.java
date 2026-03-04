package com.smhi.tempmap.resource;

import com.smhi.tempmap.dto.StationDto;
import com.smhi.tempmap.dto.TemperatureHistoryDto;
import com.smhi.tempmap.entity.Reading;
import com.smhi.tempmap.entity.Station;
import com.smhi.tempmap.service.TemperatureService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class TemperatureResource {

    @Inject
    TemperatureService temperatureService;

    @GET
    @Path("/stations")
    public List<StationDto> getAllStations() {
        return temperatureService.getAllStations().stream()
            .map(StationDto::from)
            .collect(Collectors.toList());
    }

    @GET
    @Path("/temperatures/current")
    public List<StationDto> getCurrentTemperatures() {
        List<Reading> readings = temperatureService.getCurrentTemperatures();
        return readings.stream()
            .map(r -> StationDto.from(r.station, r.temperature, r.timestamp.toString()))
            .collect(Collectors.toList());
    }

    @GET
    @Path("/temperatures/{stationId}/history")
    public TemperatureHistoryDto getTemperatureHistory(
            @PathParam("stationId") Long stationId) {
        List<Reading> readings = temperatureService.getTemperatureHistory(stationId, 24);
        Station station = Station.findById(stationId);
        String stationName = station != null ? station.name : "Unknown";
        return TemperatureHistoryDto.from(stationId, stationName, readings);
    }
}