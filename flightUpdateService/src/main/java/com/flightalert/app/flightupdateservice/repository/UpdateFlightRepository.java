package com.flightalert.app.flightupdateservice.repository;

import com.flightalert.app.flightupdateservice.model.FlightUpdate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateFlightRepository extends ReactiveMongoRepository<FlightUpdate, String> {
}
