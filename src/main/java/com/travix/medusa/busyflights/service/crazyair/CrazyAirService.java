package com.travix.medusa.busyflights.service.crazyair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;
import com.travix.medusa.busyflights.service.FlightSupplier;
import com.travix.medusa.busyflights.util.FormatterUtil;

/**
 * The Service class for calling the CrazyAir flight supplier API and build the
 * response.
 * 
 * @author BSukumar
 */
@Service
public class CrazyAirService implements FlightSupplier {

	private static final Logger log = Logger.getLogger(CrazyAirService.class);

	@Autowired
	private CrazyAirRestClient crazyAirRestClient;

	@Bean
	public CrazyAirRestClient crazyAirRestClient() {
		return new CrazyAirRestClient();
	}

	@Value("${busyflight.supplier.fare.roundup}")
	private static int fareRoundup;

	@Value("${busyflight.supplier.crazyair.name}")
	private String supplierName;

	/**
	 * This method calls crazyAir flight suppliers API to get the list of
	 * flights for the user search criteria
	 * 
	 * @param flightsRequest
	 *            user search criteria
	 * @return list of flights
	 */
	@Override
	public List<BusyFlightsResponse> search(BusyFlightsRequest flightsRequest) {
		CrazyAirRequest crazyAirRequest = toCrazyAirRequest(flightsRequest);
		log.info("Calling CrazyAir flight suppliers API ");
		return Stream.of(crazyAirRestClient.get(crazyAirRequest)).map(this::toBusyFlightsResponse)
				.collect(Collectors.toList());

	}

	/**
	 * This method is to map BusyFlights Request JSON to CrazyAir API Request
	 * JSON
	 * 
	 * @param busyFlightsRequest
	 *            BusyFlights Request JSON
	 * @return CrazyAirRequest CrazyAir API Request JSON
	 */
	private CrazyAirRequest toCrazyAirRequest(BusyFlightsRequest busyFlightsRequest) {
		CrazyAirRequest crazyAirRequest = new CrazyAirRequest();
		crazyAirRequest.setOrigin(busyFlightsRequest.getOrigin());
		crazyAirRequest.setDestination(busyFlightsRequest.getDestination());
		crazyAirRequest.setDepartureDate(busyFlightsRequest.getDepartureDate());
		crazyAirRequest.setReturnDate(busyFlightsRequest.getReturnDate());
		crazyAirRequest.setPassengerCount(busyFlightsRequest.getNumberOfPassengers());
		return crazyAirRequest;
	}

	/**
	 * This method is to map CrazyAir API Response JSON to BusyFlights Response
	 * JSON
	 * 
	 * @param crazyAirResponse
	 *            CrazyAir API Response JSON
	 * @return BusyFlightsResponse BusyFlights Response JSON
	 */
	private BusyFlightsResponse toBusyFlightsResponse(CrazyAirResponse crazyAirResponse) {
		BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
		busyFlightsResponse.setAirline(crazyAirResponse.getAirline());
		busyFlightsResponse.setSupplier(supplierName);
		busyFlightsResponse.setFare(FormatterUtil.round(crazyAirResponse.getPrice(), fareRoundup));
		busyFlightsResponse.setDepartureAirportCode(crazyAirResponse.getDepartureAirportCode());
		busyFlightsResponse.setDestinationAirportCode(crazyAirResponse.getDestinationAirportCode());
		busyFlightsResponse.setDepartureDate(FormatterUtil.localToIsoDateTime(crazyAirResponse.getDepartureDate()));
		busyFlightsResponse.setArrivalDate(FormatterUtil.localToIsoDateTime(crazyAirResponse.getArrivalDate()));
		return busyFlightsResponse;
	}

}
