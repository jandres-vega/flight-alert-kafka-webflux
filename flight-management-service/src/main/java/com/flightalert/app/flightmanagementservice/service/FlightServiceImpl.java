package com.flightalert.app.flightmanagementservice.service;

import com.flightalert.app.flightmanagementservice.model.Flight;
import com.flightalert.app.flightmanagementservice.provider.FlightEventProducer;
import com.flightalert.app.flightmanagementservice.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final FlightEventProducer flightEventProducer;
    private final Logger log = LoggerFactory.getLogger(FlightServiceImpl.class);

    public FlightServiceImpl(FlightRepository flightRepository, FlightEventProducer flightEventProducer) {
        this.flightRepository = flightRepository;
        this.flightEventProducer = flightEventProducer;
    }

    @Override
    public Mono<Flight> addFlight(Flight flight) {
        return this.findFlightByAirline(flight.getAirline())
                .flatMap(existingFlight -> Mono.error(new RuntimeException("Flight already exists: " + flight.getAirline())))
                .switchIfEmpty(flightRepository.save(flight)).flatMap(saveFlight -> {
                    log.info("Flight saved with ID: {}", flight.getFlightId());
                    return flightEventProducer.publishFlightCreationEvent(flight);
                });
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
