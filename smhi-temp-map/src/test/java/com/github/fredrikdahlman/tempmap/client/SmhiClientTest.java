package com.github.fredrikdahlman.tempmap.client;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SmhiClientTest {

    private final SmhiClient client = new SmhiClient();

    @Test
    void testParseResponseWithValidData() {
        String json = """
            {
                "station": [
                    {
                        "id": "123456",
                        "name": "Test Station",
                        "latitude": 60.0,
                        "longitude": 15.0,
                        "data": [
                            {
                                "value": 5.5,
                                "date": "2024-01-15T12:00:00Z"
                            }
                        ]
                    }
                ]
            }
            """;

        List<SmhiClient.SmhiStationData> result = client.parseResponse(json);

        assertEquals(1, result.size());
        SmhiClient.SmhiStationData data = result.get(0);
        assertEquals("123456", data.stationId());
        assertEquals("Test Station", data.name());
        assertEquals(60.0, data.latitude());
        assertEquals(15.0, data.longitude());
        assertEquals(5.5, data.temperature());
    }

    @Test
    void testParseResponseWithMultipleStations() {
        String json = """
            {
                "station": [
                    {
                        "id": "111",
                        "name": "Station A",
                        "latitude": 60.0,
                        "longitude": 15.0,
                        "data": [{"value": 10.0, "date": "2024-01-15T12:00:00Z"}]
                    },
                    {
                        "id": "222",
                        "name": "Station B",
                        "latitude": 61.0,
                        "longitude": 16.0,
                        "data": [{"value": 8.5, "date": "2024-01-15T12:00:00Z"}]
                    }
                ]
            }
            """;

        List<SmhiClient.SmhiStationData> result = client.parseResponse(json);

        assertEquals(2, result.size());
    }

    @Test
    void testParseResponseWithInvalidJson() {
        String json = "not valid json";

        List<SmhiClient.SmhiStationData> result = client.parseResponse(json);

        assertTrue(result.isEmpty());
    }
}
