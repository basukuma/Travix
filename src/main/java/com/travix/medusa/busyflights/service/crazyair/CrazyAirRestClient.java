package com.travix.medusa.busyflights.service.crazyair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.travix.medusa.busyflights.domain.crazyair.CrazyAirRequest;
import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;

/**
 * Rest client for the crazyAir Flight Supplier
 * 
 * @author BSukumar
 */

public class CrazyAirRestClient {

	@Value("${busyflight.supplier.crazyair.url}")
	private String crazyAirUrl;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * This method call crazyAir flight suppliers API method to get the list of
	 * flights for the user search criteria
	 * 
	 * @param crazyAirRequest
	 *            user search criteria
	 * @return list of flights
	 */
	public CrazyAirResponse[] get(CrazyAirRequest crazyAirRequest) {
		return restTemplate.getForObject(crazyAirUrl, CrazyAirResponse[].class, crazyAirRequest);
	}
}
