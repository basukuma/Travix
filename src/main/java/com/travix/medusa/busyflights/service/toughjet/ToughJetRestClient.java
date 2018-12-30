package com.travix.medusa.busyflights.service.toughjet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.travix.medusa.busyflights.domain.toughjet.ToughJetRequest;
import com.travix.medusa.busyflights.domain.toughjet.ToughJetResponse;

/**
 * Rest client for the ToughJet Flight Supplier
 * 
 * @author BSukumar
 */
@Component
public class ToughJetRestClient {

	@Value("${busyflight.supplier.toughjet.url}")
	private String toughJetUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * This method call ToughJet flight suppliers API method to get the list of
	 * flights for the user search criteria
	 * 
	 * @param toughJetRequest
	 *            user search criteria
	 * @return list of flights
	 */
	public ToughJetResponse[] get(ToughJetRequest toughJetRequest) {
		return restTemplate.getForObject(toughJetUrl, ToughJetResponse[].class, toughJetRequest);
	}
}
