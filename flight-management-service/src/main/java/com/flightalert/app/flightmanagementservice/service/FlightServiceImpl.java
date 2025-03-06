package com.flightalert.app.flightmanagementservice.service;

import com.flightalert.app.flightmanagementservice.model.Flight;
import com.flightalert.app.flightmanagementservice.repository.FlightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Mono<Flight> addFlight(Flight flight) {
        return this.findFlightByAirline(flight.getAirline())
                .flatMap(existingFlight -> Mono.error(new RuntimeException("Flight already exists: " + flight.getAirline())))
                .switchIfEmpty(flightRepository.save(flight)).thenReturn(flight);
    }

    @Override
    public Mono<Flight> findFlightByAirline(String airline) {
        return flightRepository.findFlightByAirline(airline);
    }

    @Override
    public Flux<Flight> getFlights() {
        return flightRepository.findAll();
    }
}
