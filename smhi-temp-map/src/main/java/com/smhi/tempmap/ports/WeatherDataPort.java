package com.smhi.tempmap.ports;

import com.smhi.tempmap.client.SmhiClient.SmhiStationData;
import java.util.List;

public interface WeatherDataPort {
    List<SmhiStationData> fetchTemperatureData();
}