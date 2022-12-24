package com.kenzie.appserver.repositories;


import com.kenzie.appserver.repositories.model.FlightRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface FlightRepository extends CrudRepository<FlightRecord, String> {
}
