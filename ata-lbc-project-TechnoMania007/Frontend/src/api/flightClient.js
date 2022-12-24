import BaseClass from "../util/baseClass";
import axios from 'axios'

/**
 * Client to call the FlightService.
 */
export default class FlightClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getFlight', 'createFlight'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the flight for the given flightId.
     * @param flightId Unique identifier for a flight
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The flight
     */
    async getFlight(flightId, errorCallback) {
        try {
            const response = await this.client.get(`/flight/${flightId}`);
            return response.data;
        } catch (error) {
            this.handleError("getFlight", error, errorCallback)
        }
    }

    async getAllFlights(all, errorCallback){
        try{
            const response = await this.client.get(`/flight/all/`);
            return response.data;
        }catch(error) {
            this.handleError("getAllFlights", error, errorCallback)
        }
    }

    async createFlight(name, email, originCity, destinationCity, numOfPassengers, paymentMethod, rate, errorCallback) {
        try {
            const response = await this.client.post(`/flight`, {
                "name" : name,
                "email" : email,
                "originCity" : originCity,
                "destinationCity" : destinationCity,
                "numOfPassengers" : numOfPassengers,
                "paymentMethod" : paymentMethod,
                "rate" : rate,
            });
            return response.data;
        } catch (error) {
            this.handleError("createFlight", error, errorCallback);
        }
    }

    async deleteFlight(flightId, errorCallback){
        try{
            const response = await this.client.delete(`/flight/${flightId}`)
        }catch(error) {
            this.handleError("deleteFlight", error, errorCallback)
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }

}