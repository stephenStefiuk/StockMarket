package com.techelevator.model;

import java.math.BigDecimal;

public class AAResponse {
	
	private String ticker;
	private double price;

	public String getTicker() {
		return ticker;
	}

	public double getPrice() {
		return price;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public void setPrice(String price) {
		this.price = Double.parseDouble(price);
	}
	
}
