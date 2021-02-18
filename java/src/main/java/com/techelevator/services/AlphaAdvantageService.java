package com.techelevator.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.model.AAResponse;

public class AlphaAdvantageService {

	public AAResponse getStockData(String ticker) throws JsonMappingException, JsonProcessingException {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(null, null);
		
		ResponseEntity<String> responseEntity = restTemplate.exchange("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + ticker +"&apikey=AP3PE356G2G2X4RS"
				, HttpMethod.GET, requestEntity, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
		
		AAResponse responseObjectAA= new AAResponse();
		
		responseObjectAA.setPrice(jsonNode.path("Global Quote").path("05. price").asText());
		responseObjectAA.setTicker(jsonNode.path("Global Quote").path("01. symbol").asText());
		
		return responseObjectAA;
	}
	
	
}
