package com.kenzie.appserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.FlightCreateRequest;
import com.kenzie.appserver.service.FlightService;
import com.kenzie.appserver.service.model.FlightInfo;
import net.andreinc.mockneat.MockNeat;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class FlightControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    FlightService flightService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getFlightById_Exists() throws Exception {
        String flightId = randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String email = mockNeat.strings().valStr();
        String originCity = mockNeat.strings().valStr();
        String destinationCity = mockNeat.strings().valStr();
        Integer numOfPassengers = mockNeat.ints().val();
        String paymentMethod = mockNeat.strings().valStr();
        Integer rate = numOfPassengers * 1300;

        FlightInfo flightInfo = new FlightInfo(flightId, name, email, originCity,
                destinationCity, numOfPassengers, paymentMethod, rate);
        FlightInfo persistedFlight = flightService.createFlight(flightInfo);
        this.mvc.perform(get("/flight/{flightId}", persistedFlight.getFlightId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("flightId")
                        .value(is(flightId)))
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("email")
                        .value(is(email)))
                .andExpect(jsonPath("originCity")
                        .value(is(originCity)))
                .andExpect(jsonPath("destinationCity")
                        .value(is(destinationCity)))
                .andExpect(jsonPath("numOfPassengers")
                        .value(is(numOfPassengers)))
                .andExpect(jsonPath("paymentMethod")
                        .value(is(paymentMethod)))
                .andExpect((jsonPath("rate")
                        .value(is(rate))))
                .andExpect(status().isOk());
    }

    @Test
    public void createFlight_CreateSuccessful() throws Exception {
        String name = mockNeat.strings().valStr();
        String email = mockNeat.strings().valStr();
        String originCity = mockNeat.strings().valStr();
        String destinationCity = mockNeat.strings().valStr();
        Integer numOfPassengers = mockNeat.ints().val();
        String paymentMethod = mockNeat.strings().valStr();
        Integer rate = numOfPassengers * 1300;

        FlightCreateRequest flightCreateRequest = new FlightCreateRequest();
        flightCreateRequest.setName(name);
        flightCreateRequest.setEmail(email);
        flightCreateRequest.setOriginCity(originCity);
        flightCreateRequest.setDestinationCity(destinationCity);
        flightCreateRequest.setNumOfPassengers(numOfPassengers);
        flightCreateRequest.setPaymentMethod(paymentMethod);
        flightCreateRequest.setRate(rate);

        mapper.registerModule(new JavaTimeModule());

        this.mvc.perform(post("/flight")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(flightCreateRequest)))
                .andExpect(jsonPath("flightId")
                        .exists())
                .andExpect(jsonPath("name")
                        .value(is(name)))
                .andExpect(jsonPath("email")
                        .value(is(email)))
                .andExpect(jsonPath("originCity")
                        .value(is(originCity)))
                .andExpect(jsonPath("destinationCity")
                        .value(is(destinationCity)))
                .andExpect(jsonPath("numOfPassengers")
                        .value(is(numOfPassengers)))
                .andExpect(jsonPath("paymentMethod")
                        .value(is(paymentMethod)))
                .andExpect((jsonPath("rate")
                        .value(is(rate))))
                .andExpect(status().isCreated());

    }

    @Test
    public void deleteFlight_deleteSuccessful() throws Exception {
        String flightId = randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String email = mockNeat.strings().valStr();
        String originCity = mockNeat.strings().valStr();
        String destinationCity = mockNeat.strings().valStr();
        Integer numOfPassengers = mockNeat.ints().val();
        String paymentMethod = mockNeat.strings().valStr();
        Integer rate = numOfPassengers * 1300;

        FlightInfo flightInfo = new FlightInfo(flightId, name, email, originCity,
                destinationCity, numOfPassengers, paymentMethod, rate);
        FlightInfo persistedFlight = flightService.createFlight(flightInfo);
        this.mvc.perform(delete("/flight/{flightId}", persistedFlight.getFlightId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(flightInfo)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getAllFlights_GetSuccessful() throws Exception {
        String flightId = randomUUID().toString();
        String name = mockNeat.strings().valStr();
        String email = mockNeat.strings().valStr();
        String originCity = mockNeat.strings().valStr();
        String destinationCity = mockNeat.strings().valStr();
        Integer numOfPassengers = mockNeat.ints().val();
        String paymentMethod = mockNeat.strings().valStr();
        Integer rate = numOfPassengers * 1300;

        FlightInfo flightInfo = new FlightInfo(flightId, name, email, originCity,
                destinationCity, numOfPassengers, paymentMethod, rate);
        List<FlightInfo> persistedFlight = flightService.getAllFlights();
        this.mvc.perform(get("/flight/all/", persistedFlight.get(0).getFlightId(), persistedFlight.get(0).getName(),
                        persistedFlight.get(0).getEmail(), persistedFlight.get(0).getOriginCity(),
                        persistedFlight.get(0).getDestinationCity(), persistedFlight.get(0).getNumOfPassengers(),
                        persistedFlight.get(0).getPaymentMethod(), persistedFlight.get(0).getRate())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(flightInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(persistedFlight.size())));
    }
}
