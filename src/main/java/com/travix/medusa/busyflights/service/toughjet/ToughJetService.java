package com.travix.medusa.busyflights.service.toughjet;

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
import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;
import com.travix.medusa.busyflights.service.FlightSupplier;
import com.travix.medusa.busyflights.util.FormatterUtil;

/**
 * The Service class for calling the ToughJet flight supplier API and build the
 * response.
 * 
 * @author BSukumar
 */
@Service
public class ToughJetService implements FlightSupplier {

	private static final Logger log = Logger.getLogger(ToughJetService.class);

	@Autowired
	private ToughJetRestClient toughJetRestClient;

	@Bean
	public ToughJetRestClient toughJetRestClient() {
		return new ToughJetRestClient();
	}

	@Value("${busyflight.supplier.fare.roundup}")
	private static int fareRoundup;

	@Value("${busyflight.supplier.toughjet.name}")
	private String supplierName;

	/**
	 * This method calls ToughJet flight suppliers API to get the list of
	 * flights for the user search criteria
	 * 
	 * @param flightsRequest
	 *            user search criteria
	 * @return list of flights
	 */
	@Override
	public List<BusyFlightsResponse> search(BusyFlightsRequest flightsRequest) {
		ToughJetRequest toughJetRequest = toToughJetRequest(flightsRequest);
		log.info("Calling ToughJet flight suppliers API ");
		return Stream.of(toughJetRestClient.get(toughJetRequest)).map(this::toBusyFlightsResponse)
				.collect(Collectors.toList());

	}

	/**
	 * This method is to map BusyFlights Request JSON to ToughJet API Request
	 * JSON
	 * 
	 * @param busyFlightsRequest
	 *            BusyFlights Request JSON
	 * @return ToughJetRequest ToughJet API Request JSON
	 */
	private ToughJetRequest toToughJetRequest(BusyFlightsRequest busyFlightsRequest) {
		ToughJetRequest toughJetRequest = new ToughJetRequest();

		toughJetRequest.setFrom(busyFlightsRequest.getOrigin());
		toughJetRequest.setTo(busyFlightsRequest.getDestination());
		toughJetRequest.setOutboundDate(busyFlightsRequest.getDepartureDate());
		toughJetRequest.setInboundDate(busyFlightsRequest.getReturnDate());
		toughJetRequest.setNumberOfAdults(busyFlightsRequest.getNumberOfPassengers());

		return toughJetRequest;
	}

	/**
	 * This method is to map ToughJet API Response JSON to BusyFlights Response
	 * JSON
	 * 
	 * @param toughJetResponse
	 *            ToughJet API Response JSON
	 * @return BusyFlightsResponse BusyFlights Response JSON
	 */
	private BusyFlightsResponse toBusyFlightsResponse(ToughJetResponse toughJetResponse) {
		BusyFlightsResponse busyFlightsResponse = new BusyFlightsResponse();

		busyFlightsResponse.setAirline(toughJetResponse.getCarrier());
		busyFlightsResponse.setSupplier(supplierName);
		busyFlightsResponse.setFare(FormatterUtil.round(calculateFinalPrice(toughJetResponse), fareRoundup));
		busyFlightsResponse.setDepartureAirportCode(toughJetResponse.getDepartureAirportName());
		busyFlightsResponse.setDestinationAirportCode(toughJetResponse.getArrivalAirportName());
		busyFlightsResponse.setDepartureDate(FormatterUtil.instantToIsoDate(toughJetResponse.getOutboundDateTime()));
		busyFlightsResponse.setArrivalDate(FormatterUtil.instantToIsoDate(toughJetResponse.getInboundDateTime()));

		return busyFlightsResponse;
	}

	/**
	 * This method is to calculate FinalPrice for ToughJet by applying discount
	 * on the base price and including the tax needs to charged along with the
	 * price
	 * 
	 * @param toughJetResponse
	 *            ToughJet API Response JSON
	 * @return FinalPrice
	 */
	private static double calculateFinalPrice(ToughJetResponse toughJetResponse) {
		double discount = toughJetResponse.getBasePrice() * (toughJetResponse.getDiscount() / 100);
		double finalPrice = toughJetResponse.getBasePrice() - discount + toughJetResponse.getTax();
		return finalPrice;
	}

}
