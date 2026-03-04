package com.smhi.tempmap.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SmhiClient {

    private static final Logger LOG = Logger.getLogger(SmhiClient.class);
    private static final String SMHI_API_URL = "https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station-set/all/period/latest-hour/data.json";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public List<SmhiStationData> fetchTemperatureData() {
        List<SmhiStationData> results = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SMHI_API_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                LOG.errorf("SMHI API returned status code: %d", response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            LOG.error("Failed to fetch SMHI data", e);
            Thread.currentThread().interrupt();
        }

        return results;
    }

    public List<SmhiStationData> parseResponse(String json) {
        List<SmhiStationData> results = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode station = root.get("station");

            if (station != null && station.isArray()) {
                for (JsonNode stationNode : station) {
                    String stationId = stationNode.has("id") ? stationNode.get("id").asText() : null;
                    String name = stationNode.has("name") ? stationNode.get("name").asText() : null;
                    Double latitude = stationNode.has("latitude") ? stationNode.get("latitude").asDouble() : null;
                    Double longitude = stationNode.has("longitude") ? stationNode.get("longitude").asDouble() : null;

                    JsonNode data = stationNode.get("data");
                    if (data != null && data.isArray()) {
                        for (JsonNode dataNode : data) {
                            Double temperature = null;
                            String dateTime = null;

                            JsonNode value = dataNode.get("value");
                            if (value != null && !value.isNull()) {
                                temperature = value.asDouble();
                            }

                            JsonNode date = dataNode.get("date");
                            if (date != null) {
                                dateTime = date.asText();
                            }

                            if (stationId != null && temperature != null && dateTime != null) {
                                results.add(new SmhiStationData(stationId, name, latitude, longitude, temperature, dateTime));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Failed to parse SMHI response", e);
        }

        return results;
    }

    public record SmhiStationData(String stationId, String name, Double latitude, Double longitude, Double temperature, String dateTime) {}
}
