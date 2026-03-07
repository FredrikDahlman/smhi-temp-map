package com.github.fredrikdahlman.tempmap.resource;

import com.github.fredrikdahlman.tempmap.domain.model.Reading;
import com.github.fredrikdahlman.tempmap.domain.model.Station;
import com.github.fredrikdahlman.tempmap.domain.service.TemperatureCommandService;
import com.github.fredrikdahlman.tempmap.domain.service.TemperatureQueryService;
import com.github.fredrikdahlman.tempmap.dto.StationDto;
import com.github.fredrikdahlman.tempmap.dto.TemperatureHistoryDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
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
    TemperatureQueryService queryService;

    @Inject
    TemperatureCommandService commandService;

    @GET
    @Path("/stations")
    public List<StationDto> getAllStations() {
        return queryService.getAllStations().stream()
            .map(StationDto::from)
            .collect(Collectors.toList());
    }

    @GET
    @Path("/temperatures/current")
    public List<StationDto> getCurrentTemperatures() {
        List<Reading> readings = queryService.getCurrentTemperatures();
        return readings.stream()
            .map(r -> StationDto.from(r.station(), r.temperature(), r.timestamp().toString()))
            .collect(Collectors.toList());
    }

    @GET
    @Path("/temperatures/{stationId}/history")
    public TemperatureHistoryDto getTemperatureHistory(
            @PathParam("stationId") Long stationId) {
        Station station = queryService.getAllStations().stream()
            .filter(s -> s.id().equals(stationId))
            .findFirst()
            .orElse(null);
        
        if (station == null) {
            throw new NotFoundException("Station not found: " + stationId);
        }
        
        List<Reading> readings = queryService.getTemperatureHistory(stationId, 24);
        return TemperatureHistoryDto.from(stationId, station.name(), readings);
    }
}