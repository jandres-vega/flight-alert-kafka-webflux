package com.flightalert.app.flightupdateservice.service;

import com.flightalert.app.flightupdateservice.consumer.FlightCreationConsumer;
import com.flightalert.app.flightupdateservice.model.FlightUpdate;
import com.flightalert.app.flightupdateservice.provider.KafkaServiceProvider;
import com.flightalert.app.flightupdateservice.repository.UpdateFlightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightUpdateImpl implements FlightUpdateService{

    private final UpdateFlightRepository updateFlightRepository;
    private final FlightCreationConsumer flightCreationConsumer;
    private final KafkaServiceProvider kafkaServiceProvider;

    public FlightUpdateImpl(UpdateFlightRepository updateFlightRepository, FlightCreationConsumer flightCreationConsumer, KafkaServiceProvider kafkaServiceProvider) {
        this.updateFlightRepository = updateFlightRepository;
        this.flightCreationConsumer = flightCreationConsumer;
        this.kafkaServiceProvider = kafkaServiceProvider;
    }

    @Override
    public Mono<FlightUpdate> updateFlight(FlightUpdate flightUpdate) {

        if (!flightCreationConsumer.isValidFlight(flightUpdate.getIdFlight())){
            return Mono.error(new IllegalArgumentException("Flight does not exist: " + flightUpdate.getIdFlight()));
        }
        return this.getFlightUpdateById(flightUpdate.getIdUpdate())
                .switchIfEmpty(
                        updateFlightRepository.save(flightUpdate)
                                .doOnSuccess(kafkaServiceProvider::publishEventUpdateFlight)
                );
    }

    @Override
    public Flux<FlightUpdate> getFlightUpdates() {
        return updateFlightRepository.findAll();
    }

    @Override
    public Mono<FlightUpdate> deleteFlightUpdate(String idUpdate) {
        return this.getFlightUpdateById(idUpdate)
                .flatMap(flightUpdate -> updateFlightRepository
                        .deleteById(idUpdate).thenReturn(flightUpdate));
    }

    @Override
    public Mono<FlightUpdate> getFlightUpdateById(String idUpdate) {
        return updateFlightRepository
                .findById(idUpdate)
                .switchIfEmpty(Mono.error(new RuntimeException("No flight update found")));
    }
}
