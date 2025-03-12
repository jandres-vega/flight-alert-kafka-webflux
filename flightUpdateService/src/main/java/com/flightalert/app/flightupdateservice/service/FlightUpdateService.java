package com.flightalert.app.flightupdateservice.service;

import com.flightalert.app.flightupdateservice.model.FlightUpdate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FlightUpdateService {

    Mono<FlightUpdate> updateFlight(FlightUpdate flightUpdate);
    Flux<FlightUpdate> getFlightUpdates();
    Mono<FlightUpdate> deleteFlightUpdate(String idUpdate);
    Mono<FlightUpdate> getFlightUpdateById(String idUpdate);
}
