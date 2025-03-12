package com.flightalert.app.flightupdateservice.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightalert.app.flightupdateservice.model.FlightUpdate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KafkaServiceProvider {

    private static final String FLIGHT_CREATION_TOPIC = "flight-updates";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaServiceProvider(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<FlightUpdate> publishEventUpdateFlight(FlightUpdate flightUpdate) {
        return Mono.fromCallable(() -> {
            try {
                String flightJson = objectMapper.writeValueAsString(flightUpdate);
                String key = flightUpdate.getAirportCode();
                kafkaTemplate.send(FLIGHT_CREATION_TOPIC, key, flightJson);
                return flightUpdate;
            } catch (Exception e) {
                throw new RuntimeException("Error publishing flight creation event", e);
            }
        });
    }


}
