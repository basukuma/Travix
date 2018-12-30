package com.travix.medusa.busyflights;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travix.medusa.busyflights.controller.BusyFlightsController;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsResponse;
import com.travix.medusa.busyflights.service.BusyFlightsService;

@RunWith(SpringRunner.class)
@WebMvcTest(BusyFlightsController.class)
public class BusyFlightsControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private BusyFlightsService busyFlightsService;

	@Test
	public void findFlight() throws Exception {

		BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();
		busyFlightsResponse.setAirline("TJ");
		busyFlightsResponse.setSupplier("ToughJet");
		busyFlightsResponse.setFare(23456.8);
		busyFlightsResponse.setDepartureAirportCode("LHR");
		busyFlightsResponse.setDestinationAirportCode("AMS");
		busyFlightsResponse.setDepartureDate("2019-01-02T11:30:30Z[UTC]");
		busyFlightsResponse.setArrivalDate("2019-01-23T11:30:30Z[UTC]");

		BusyFlightsResponse busyFlightsResponse2 = new BusyFlightsResponse();
		busyFlightsResponse2.setAirline("CZ");
		busyFlightsResponse2.setSupplier("CrazyAir");
		busyFlightsResponse2.setFare(20667.78);
		busyFlightsResponse2.setDepartureAirportCode("LHR");
		busyFlightsResponse2.setDestinationAirportCode("AMS");
		busyFlightsResponse2.setDepartureDate("2019-01-02T12:00:00");
		busyFlightsResponse2.setArrivalDate("2019-01-23T12:00:00");

		final BusyFlightsResponse[] flights = new BusyFlightsResponse[] { busyFlightsResponse, busyFlightsResponse2 };

		Mockito.when(busyFlightsService.findFlight(Mockito.any(BusyFlightsRequest.class)))
				.thenReturn(Stream.of(flights).collect(toList()));

		mvc.perform(post("/busyflights/flights/find").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new BusyFlightsRequest())))
				.andExpect(status().isOk()).andDo(resHandler -> {
					String resJson = resHandler.getResponse().getContentAsString();
					BusyFlightsResponse[] res = mapper.readValue(resJson, BusyFlightsResponse[].class);

					assertEquals(flights.length, res.length);
				});
	}

}