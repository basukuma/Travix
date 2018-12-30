package com.travix.medusa.busyflights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.exception.APIException;
import com.travix.medusa.busyflights.service.BusyFlightsService;

/**
 * The Controller class for receiving the input from the user and call the
 * corresponding service.
 * 
 * @author BSukumar
 */

@RestController
@RequestMapping("/busyflights/flights")
public class BusyFlightsController {

	@Autowired
	public BusyFlightsService busyFlightsService;

	@PostMapping(path = "/find", consumes = "application/json", produces = "application/json")
	public ResponseEntity<List<BusyFlightsResponse>> findFlight(@RequestBody BusyFlightsRequest flightRequest) {
		List<BusyFlightsResponse> flightsFound = busyFlightsService.findFlight(flightRequest);
		if (flightsFound == null || flightsFound.isEmpty()) {
			throw new APIException("No Flights Found");
		}
		return ResponseEntity.ok(flightsFound);
	}
}
