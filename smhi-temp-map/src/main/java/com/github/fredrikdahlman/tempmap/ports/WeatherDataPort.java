package com.github.fredrikdahlman.tempmap.ports;

import com.github.fredrikdahlman.tempmap.client.SmhiClient.SmhiStationData;
import java.util.List;

public interface WeatherDataPort {
    List<SmhiStationData> fetchTemperatureData();
}