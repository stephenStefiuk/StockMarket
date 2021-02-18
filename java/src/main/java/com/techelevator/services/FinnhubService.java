package com.techelevator.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.FinnhubResponse;

public class FinnhubService {
	
public FinnhubResponse getStockData(String ticker) throws JsonMappingException, JsonProcessingException {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(null, null);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("https://finnhub.io/api/v1/quote?symbol=" + ticker + "&token=c0hdauf48v6phn6spb40"
				, HttpMethod.GET, requestEntity, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
		
		FinnhubResponse responseObjectAA= new FinnhubResponse();
		
		responseObjectAA.setPrice(jsonNode.path("c").asText());
		
		return responseObjectAA;
	}
	
}
