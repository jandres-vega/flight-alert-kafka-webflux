package com.flightalert.app.flightupdateservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "flight_updates")
public class FlightUpdate {

    @Id
    private String idUpdate;
    private String idFlight;
    private String airportCode;
    private UpdateType updateType;
    private String message;
    private LocalDateTime timestamp;
}
