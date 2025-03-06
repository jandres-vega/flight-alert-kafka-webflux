package com.flightalert.app.flightmanagementservice.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flightalert.app.flightmanagementservice.model.Flight;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class FlightEventProducer {

    private static final String FLIGHT_CREATION_TOPIC = "flight-creation";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public FlightEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        // Configurar módulo para manejo de fechas (Java 8)
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Mono<Flight> publishFlightCreationEvent(Flight flight) {
        return Mono.fromCallable(() ->{
            try {
                String flightJson = objectMapper.writeValueAsString(flight);
                String key = flight.getAirline();
                kafkaTemplate.send(FLIGHT_CREATION_TOPIC, key, flightJson);
                return flight;
            } catch (Exception e) {
                throw new RuntimeException("Error publishing flight creation event", e);
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
