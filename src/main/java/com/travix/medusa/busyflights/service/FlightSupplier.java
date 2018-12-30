package com.travix.medusa.busyflights.service;

import java.util.List;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;

/**
 * Represents common interface for the Flight Supplier
 * 
 * @author BSukumar
 */
public interface FlightSupplier {

	public List<BusyFlightsResponse> search(BusyFlightsRequest flightsRequest);

}
