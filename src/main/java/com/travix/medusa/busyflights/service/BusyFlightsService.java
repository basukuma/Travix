package com.travix.medusa.busyflights.service;

import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.exception.APIException;

/**
 * The Service class for calling the flight supplier API and build the response.
 * 
 * @author BSukumar
 */

@Service
public class BusyFlightsService {
	private static final Logger log = Logger.getLogger(BusyFlightsService.class);

	@Autowired
	List<FlightSupplier> suppliers;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	/**
	 * This method call all available flight suppliers API method asynchronously
	 * and build the JSON response which contains a list of flights ordered by
	 * fare.
	 * 
	 * @param flightRequest
	 *            user search criteria
	 * @return list of flights
	 * @throws APIException
	 */
	public List<BusyFlightsResponse> findFlight(BusyFlightsRequest flightRequest) {
		// Call all available flight suppliers API method asynchronously
		ExecutorService executor = Executors.newCachedThreadPool();
		List<CompletableFuture<Stream<BusyFlightsResponse>>> apiresult = suppliers.stream()
				.map(s -> CompletableFuture.supplyAsync(() -> {
					try {
						return s.search(flightRequest).stream();
					} catch (Exception e) {
						log.error(e.getMessage());
						return (Stream<BusyFlightsResponse>) null;
					}
				}, executor)).collect(toList());

		Stream.Builder<BusyFlightsResponse> responseBuilder = Stream.builder();

		apiresult.stream().map(CompletableFuture::join).forEach(stream -> {
			if (stream != null)
				stream.forEach(responseBuilder);
		});

		// Build the JSON response which contains a list of flights ordered by
		// fare
		List<BusyFlightsResponse> response = responseBuilder.build().collect(toList()).stream()
				.sorted(Comparator.comparingDouble(BusyFlightsResponse::getFare)).collect(toList());

		executor.shutdown();

		return response;
	}

}