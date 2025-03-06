package com.flightalert.app.flightmanagementservice.controllers;

import com.flightalert.app.flightmanagementservice.model.Flight;
import com.flightalert.app.flightmanagementservice.service.FlightServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FlightControllers {

    private final FlightServiceImpl flightService;

    public FlightControllers(FlightServiceImpl flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<Flight>> saveFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight).map( sf -> ResponseEntity.status(HttpStatus.CREATED).body(sf))
                .onErrorResume( e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()));
    }

    @GetMapping("/all-flights")
    public ResponseEntity<Flux<Flight>> getFlights() {
        return new ResponseEntity<>(this.flightService.getFlights(), HttpStatus.OK);
    }

    @GetMapping("/find_flight/{airline}")
    public Mono<ResponseEntity<Flight>> findFlightByAirline(@PathVariable String airline) {
        return flightService.findFlightByAirline(airline)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
