package com.techelevator.model;

public class FinnhubResponse {

	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = Double.parseDouble(price);
	}
	
}
