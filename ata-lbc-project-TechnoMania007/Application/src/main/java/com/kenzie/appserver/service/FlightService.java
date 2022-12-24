package com.kenzie.appserver.service;

import com.kenzie.appserver.FlightNotFoundException;
import com.kenzie.appserver.repositories.FlightRepository;
import com.kenzie.appserver.repositories.model.FlightRecord;
import com.kenzie.appserver.service.model.FlightInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.fromString;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightInfo getFlight(String flightId) {
        if (StringUtils.isEmpty(flightId) || isInvalidUuid(flightId)) {
            throw new FlightNotFoundException("Flight doesn't exist in our database!");
        }
            FlightInfo flightInfo = flightRepository
                    .findById(flightId)
                    .map(flight -> new FlightInfo(flight.getFlightId(),
                            flight.getName(),
                            flight.getEmail(),
                            flight.getOriginCity(),
                            flight.getDestinationCity(),
                            flight.getNumOfPassengers(),
                            flight.getPaymentMethod(),
                            flight.getRate()))
                    .orElse(null);
            return flightInfo;
    }

    public List<FlightInfo> getAllFlights() {
        List<FlightInfo> flightInfoList = new ArrayList<>();
        flightRepository
                .findAll()
                .forEach(flight -> flightInfoList.add(new FlightInfo(flight.getFlightId(), flight.getName(), flight.getEmail(), flight.getOriginCity(),
                        flight.getDestinationCity(), flight.getNumOfPassengers(), flight.getPaymentMethod(), flight.getRate())));
        return flightInfoList;
    }

    public FlightInfo createFlight(FlightInfo flightInfo) {
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFlightId(flightInfo.getFlightId());
        flightRecord.setName(flightInfo.getName());
        flightRecord.setEmail(flightInfo.getEmail());
        flightRecord.setOriginCity(flightInfo.getOriginCity());
        flightRecord.setDestinationCity(flightInfo.getDestinationCity());
        flightRecord.setNumOfPassengers(flightInfo.getNumOfPassengers());
        flightRecord.setPaymentMethod(flightInfo.getPaymentMethod());
        flightRecord.setRate(flightInfo.getRate());
        flightRepository.save(flightRecord);

        return flightInfo;
    }

    public void deleteFlight(String flightId) {
        if (StringUtils.isEmpty(flightId) || isInvalidUuid(flightId)) {
            throw new FlightNotFoundException("Flight doesn't exist in our database!");
        }

        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFlightId(flightId);
        flightRepository.delete(flightRecord);
    }

    private boolean isInvalidUuid(String uuid) {
        try {
            fromString(uuid);
        } catch (IllegalArgumentException exception) {
            return true;
        }
        return false;
    }

}
