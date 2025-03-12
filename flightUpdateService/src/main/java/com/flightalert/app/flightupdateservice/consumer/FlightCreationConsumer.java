package com.flightalert.app.flightupdateservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightalert.app.flightupdateservice.model.FlightCreationEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class FlightCreationConsumer {

    private final ObjectMapper objectMapper;
    private final Map<String, FlightCreationEvent> flightCache = new ConcurrentHashMap<>();

    public FlightCreationConsumer(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "flight-creation", groupId = "flight-update-consumers")
    public void consumeFlightCreationEvent(String message) {
        try {
            FlightCreationEvent event = objectMapper.readValue(message, FlightCreationEvent.class);
            flightCache.put(event.getFlightId().toString(), event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing flight creation event", e);
        }
    }

    public boolean isValidFlight(String flightId) {
        return flightCache.containsKey(flightId);
    }
}
