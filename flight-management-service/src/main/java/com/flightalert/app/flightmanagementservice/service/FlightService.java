package com.flightalert.app.flightmanagementservice.service;

import com.flightalert.app.flightmanagementservice.model.Flight;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightService {

    Mono<Flight> addFlight(Flight flight);
    Mono<Flight> findFlightByAirline(String airline);
    Flux<Flight> getFlights();
}
