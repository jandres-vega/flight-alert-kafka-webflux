package com.flightalert.app.flightmanagementservice.repository;

import com.flightalert.app.flightmanagementservice.model.Flight;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FlightRepository extends R2dbcRepository<Flight, Integer> {

    Mono<Flight> findFlightByAirline(String airline);
}
