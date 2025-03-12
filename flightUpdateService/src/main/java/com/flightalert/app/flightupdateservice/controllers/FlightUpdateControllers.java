package com.flightalert.app.flightupdateservice.controllers;

import com.flightalert.app.flightupdateservice.model.FlightUpdate;
import com.flightalert.app.flightupdateservice.service.FlightUpdateImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
public class FlightUpdateControllers {

    private final FlightUpdateImpl flightUpdateService;

    public FlightUpdateControllers(FlightUpdateImpl flightUpdate) {
        this.flightUpdateService = flightUpdate;
    }

    @PostMapping("/{flightId}/updates")
    public Mono<ResponseEntity<FlightUpdate>> createFlightUpdate(@PathVariable String flightId, @RequestBody FlightUpdate flightUpdate) {
        flightUpdate.setIdFlight(flightId);
        if (flightUpdate.getTimestamp() == null) {
            flightUpdate.setTimestamp(LocalDateTime.now());
        }
        return flightUpdateService.updateFlight(flightUpdate)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
}
