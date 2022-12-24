package com.kenzie.appserver.service.model;

public class FlightInfo {
    private final String flightId;
    private final String name;
    private final String email;
    private final String originCity;
    private final String destinationCity;
    private final Integer numOfPassengers;
    private final String paymentMethod;
    private final Integer rate;

    public FlightInfo(String flightId, String name, String email, String originCity,
                      String destinationCity, Integer numOfPassengers, String paymentMethod, Integer rate){
        this.flightId = flightId;
        this.name = name;
        this.email = email;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.numOfPassengers = numOfPassengers;
        this.paymentMethod = paymentMethod;
        this.rate = getNumOfPassengers() * 1300;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFlightId() {
        return this.flightId;
    }

    public String getOriginCity() {
        return this.originCity;
    }

    public String getDestinationCity() {
        return this.destinationCity;
    }

    public Integer getNumOfPassengers() {
        return this.numOfPassengers;
    }

    public String getPaymentMethod(){
        return this.paymentMethod;
    }

    public Integer getRate() {
        return this.rate;
    }

}
