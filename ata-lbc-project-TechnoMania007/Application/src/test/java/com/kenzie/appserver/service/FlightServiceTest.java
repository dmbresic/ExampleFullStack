package com.kenzie.appserver.service;

import com.kenzie.appserver.FlightNotFoundException;
import com.kenzie.appserver.controller.model.FlightResponse;
import com.kenzie.appserver.repositories.FlightRepository;
import com.kenzie.appserver.repositories.model.FlightRecord;
import com.kenzie.appserver.service.model.FlightInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlightServiceTest {
    public static final String flightId = randomUUID().toString();

    public static final String name = "name";
    public static final String email = "email@gmail.com";
    public static final Integer numOfPassengers = 4;
    public static final String paymentMethod = "Credit Card";
    public static final String originCity = "New York";
    public static final String destinationCity = "Miami";
    public static final Integer rate = numOfPassengers * 1300;
    private FlightRepository flightRepository;
    private FlightService flightService;

    @BeforeEach
    void setup() {
        flightRepository = mock(FlightRepository.class);
        flightService = new FlightService(flightRepository);
    }

    @Test
    void getFlight_validInput_returnsValidFLights() {
        // GIVEN
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setName(name);
        flightRecord.setEmail(email);
        flightRecord.setFlightId(flightId);
        flightRecord.setOriginCity(originCity);
        flightRecord.setDestinationCity(destinationCity);
        flightRecord.setNumOfPassengers(numOfPassengers);
        flightRecord.setPaymentMethod(paymentMethod);
        flightRecord.setRate(rate);

        // WHEN
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flightRecord));
        FlightInfo flightInfo = flightService.getFlight(flightId);

        // THEN
        assertNotNull(flightInfo, "The flight is returned properly");
        assertEquals(flightRecord.getName(), flightInfo.getName(), "Both names match");
        assertEquals(flightRecord.getEmail(), flightInfo.getEmail(), "Both emails match");
        assertEquals(flightRecord.getFlightId(), flightInfo.getFlightId(), "Both id match");
        assertEquals(flightRecord.getOriginCity(), flightInfo.getOriginCity(), "Both cities match");
        assertEquals(flightRecord.getDestinationCity(), flightInfo.getDestinationCity(), "Both cities match");
        assertEquals(flightRecord.getNumOfPassengers(), flightInfo.getNumOfPassengers(), "Both number of passengers match");
        assertEquals(flightRecord.getPaymentMethod(), flightInfo.getPaymentMethod(), "Both payments match");
        assertEquals(flightRecord.getRate(), flightInfo.getRate(), "Both rates match");
    }

    @Test
    void getFlightById_emptyId() {
        // GIVEN
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // WHEN
        FlightInfo flight = flightService.getFlight(flightId);

        // THEN
        assertNull(flight, "The example is null when not found");
    }

    @Test
    void getFlightById_notValidFlightIdType_throwsException() throws FlightNotFoundException {
        // GIVEN
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFlightId("abc");
        flightRecord.setName(name);
        flightRecord.setEmail(email);
        flightRecord.setOriginCity(originCity);
        flightRecord.setDestinationCity(destinationCity);
        flightRecord.setNumOfPassengers(numOfPassengers);
        flightRecord.setPaymentMethod(paymentMethod);
        flightRecord.setRate(rate);

        when(flightRepository.findById(flightRecord.getFlightId())).thenReturn(Optional.of(flightRecord));

        // WHEN & THEN
        assertThrows(FlightNotFoundException.class,
                () -> flightService.getFlight(flightRecord.getFlightId()));
    }

    @Test
    void getFlightById_emptyFlightId_throwsException() throws FlightNotFoundException {
        // GIVEN
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFlightId("");
        flightRecord.setName(name);
        flightRecord.setEmail(email);
        flightRecord.setOriginCity(originCity);
        flightRecord.setDestinationCity(destinationCity);
        flightRecord.setNumOfPassengers(numOfPassengers);
        flightRecord.setPaymentMethod(paymentMethod);
        flightRecord.setRate(rate);

        when(flightRepository.findById(flightRecord.getFlightId())).thenReturn(Optional.of(flightRecord));

        // WHEN & THEN
        assertThrows(FlightNotFoundException.class,
                () -> flightService.getFlight(flightRecord.getFlightId()));
    }

    @Test
    void getAllFlights() {
        // GIVEN
        List<FlightRecord> flightRecordList = new ArrayList<>();
        FlightRecord flightRecord = new FlightRecord();
        flightRecord.setFlightId(flightId);
        flightRecord.setName(name);
        flightRecord.setEmail(email);
        flightRecord.setOriginCity(originCity);
        flightRecord.setDestinationCity(destinationCity);
        flightRecord.setNumOfPassengers(numOfPassengers);
        flightRecord.setPaymentMethod(paymentMethod);
        flightRecord.setRate(rate);
        flightRecordList.add(flightRecord);

        // WHEN
        when(flightRepository.findAll()).thenReturn(flightRecordList);
        List<FlightInfo> flightInfoList = flightService.getAllFlights();

        // THEN
        assertEquals(flightInfoList.size(), 1);
        assertEquals(flightInfoList.get(0).getFlightId(), flightId);
        assertEquals(flightInfoList.get(0).getName(), name);
        assertEquals(flightInfoList.get(0).getEmail(), email);
        assertEquals(flightInfoList.get(0).getOriginCity(), originCity);
        assertEquals(flightInfoList.get(0).getDestinationCity(), destinationCity);
        assertEquals(flightInfoList.get(0).getNumOfPassengers(), numOfPassengers);
        assertEquals(flightInfoList.get(0).getPaymentMethod(), paymentMethod);
        assertEquals(flightInfoList.get(0).getRate(), rate);
    }

    @Test
    void createFlight_validInput_CreatesValidFlight(){
        FlightInfo flightInfo = new FlightInfo(name, email, flightId, originCity, destinationCity, numOfPassengers, paymentMethod, rate);
        ArgumentCaptor<FlightRecord> flightRecordCaptor = ArgumentCaptor.forClass(FlightRecord.class);

        // WHEN
        FlightInfo returnedFlight = flightService.createFlight(flightInfo);

        // THEN
        Assertions.assertNotNull(returnedFlight);
        verify(flightRepository).save(flightRecordCaptor.capture());
        FlightRecord flightRecord = flightRecordCaptor.getValue();

        assertNotNull(flightRecord, "The flightRecord record is returned");
        assertEquals(flightRecord.getName(), flightInfo.getName(), "The flight name matches");
        assertEquals(flightRecord.getEmail(), flightInfo.getEmail(), "The flight email matches");
        assertEquals(flightRecord.getFlightId(), flightInfo.getFlightId(), "The flight id matches");
        assertEquals(flightRecord.getOriginCity(), flightInfo.getOriginCity(), "The flight origin zipcode matches");
        assertEquals(flightRecord.getDestinationCity(), flightInfo.getDestinationCity(), "The flight destination zipcode matches");
        assertEquals(flightRecord.getNumOfPassengers(), flightInfo.getNumOfPassengers(), "The number of passengers match");
        assertEquals(flightRecord.getPaymentMethod(), flightInfo.getPaymentMethod(), "The payment method matches");
        assertEquals(flightRecord.getRate(), flightInfo.getRate(), "Both rates match");
    }

    @Test
    void deleteFlight() {
        // GIVEN
        FlightInfo flightInfo = new FlightInfo(flightId, name, email, originCity, destinationCity, numOfPassengers, paymentMethod, rate);
        ArgumentCaptor<FlightRecord> flightRecordCaptor = ArgumentCaptor.forClass(FlightRecord.class);

        // WHEN
        flightService.deleteFlight(flightId);

        // THEN
        verify(flightRepository).delete(flightRecordCaptor.capture());

        FlightRecord flightRecord = flightRecordCaptor.getValue();

        assertNotNull(flightRecord, "The flight record is returned");
        assertNotNull(flightInfo, "The flight info exists");
        assertEquals(flightRecord.getFlightId(), flightInfo.getFlightId(), "The flightId matches");
    }

    @Test
    void deleteFlight_invalidFlightIdType_throwsException() throws FlightNotFoundException {
        // GIVEN
        FlightInfo flightInfo = new FlightInfo(flightId, name, email, originCity, destinationCity, numOfPassengers, paymentMethod, rate);
        String flightId = "abc";

        // THEN
        assertNotNull(flightInfo, "The flight info exists");
        assertNotEquals(flightId, flightInfo.getFlightId(), "The flightId's don't match");
        assertThrows(FlightNotFoundException.class,
                () -> flightService.deleteFlight(flightId));
    }

    @Test
    void deleteFlight_emptyFlightId_throwsException() throws FlightNotFoundException {
        // GIVEN
        FlightInfo flightInfo = new FlightInfo(flightId, name, email, originCity, destinationCity, numOfPassengers, paymentMethod, rate);
        String flightId = "";

        // THEN
        assertNotNull(flightInfo, "The flight info exists");
        assertNotEquals(flightId, flightInfo.getFlightId(), "The flightId's don't match");
        assertThrows(FlightNotFoundException.class,
                () -> flightService.deleteFlight(flightId));
    }
}
