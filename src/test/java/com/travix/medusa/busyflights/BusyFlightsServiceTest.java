package com.travix.medusa.busyflights;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertArrayEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.BusyFlightsService;
import com.travix.medusa.busyflights.service.crazyair.CrazyAirService;
import com.travix.medusa.busyflights.service.toughjet.ToughJetService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusyFlightsServiceTest {

	@MockBean
	private CrazyAirService crazyAirService;

	@MockBean
	private ToughJetService toughJetService;

	@Autowired
	private BusyFlightsService busyFlightService;

	@Test
	public void testSearch() throws Exception {

		BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
		busyFlightsResponse.setAirline("CZ");
		busyFlightsResponse.setSupplier("CrazyAir");
		busyFlightsResponse.setFare(23456.8);
		busyFlightsResponse.setDepartureAirportCode("LHR");
		busyFlightsResponse.setDestinationAirportCode("AMS");
		busyFlightsResponse.setDepartureDate("2019-01-02T12:00:00");
		busyFlightsResponse.setArrivalDate("2019-01-23T12:00:00");

		BusyFlightsResponse busyFlightsResponse2 = new BusyFlightsResponse();
		busyFlightsResponse2.setAirline("CZ");
		busyFlightsResponse2.setSupplier("CrazyAir");
		busyFlightsResponse2.setFare(20667.78);
		busyFlightsResponse2.setDepartureAirportCode("LHR");
		busyFlightsResponse2.setDestinationAirportCode("AMS");
		busyFlightsResponse2.setDepartureDate("2019-01-02T12:00:00");
		busyFlightsResponse2.setArrivalDate("2019-01-23T12:00:00");

		final BusyFlightsResponse[] crazyAirResponse = new BusyFlightsResponse[] { busyFlightsResponse,
				busyFlightsResponse2 };

		BusyFlightsResponse busyFlightsResponse3 = new BusyFlightsResponse();
		busyFlightsResponse3.setAirline("TJ");
		busyFlightsResponse3.setSupplier("ToughJet");
		busyFlightsResponse3.setFare(21376.86);
		busyFlightsResponse3.setDepartureAirportCode("LHR");
		busyFlightsResponse3.setDestinationAirportCode("AMS");
		busyFlightsResponse3.setDepartureDate("2019-01-02T11:30:30Z[UTC]");
		busyFlightsResponse3.setArrivalDate("2019-01-23T11:30:30Z[UTC]");

		BusyFlightsResponse busyFlightsResponse4 = new BusyFlightsResponse();
		busyFlightsResponse4.setAirline("TJ");
		busyFlightsResponse4.setSupplier("ToughJet");
		busyFlightsResponse4.setFare(27867.78);
		busyFlightsResponse4.setDepartureAirportCode("LHR");
		busyFlightsResponse4.setDestinationAirportCode("AMS");
		busyFlightsResponse4.setDepartureDate("2019-01-02T11:30:30Z[UTC]");
		busyFlightsResponse4.setArrivalDate("2019-01-23T11:30:30Z[UTC]");

		final BusyFlightsResponse[] toughJetResponse = new BusyFlightsResponse[] { busyFlightsResponse3,
				busyFlightsResponse4 };

		Mockito.when(crazyAirService.search(Mockito.any(BusyFlightsRequest.class)))
				.thenReturn(Stream.of(crazyAirResponse).collect(toList()));
		Mockito.when(toughJetService.search(Mockito.any(BusyFlightsRequest.class)))
				.thenReturn(Stream.of(toughJetResponse).collect(toList()));

		List<BusyFlightsResponse> flights = busyFlightService.findFlight(new BusyFlightsRequest());

		double[] fare = flights.stream().map(BusyFlightsResponse::getFare).mapToDouble(x -> x).toArray();
		assertArrayEquals(fare, new double[] { 20667.78, 21376.86, 23456.8, 27867.78 }, 1E-9);
	}
}