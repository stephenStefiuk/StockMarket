package com.techelevator.model;

import java.math.BigDecimal;

public class Stock {
	
	private int stockId;
	private String stockTicker;
	private String stockName;
	private double stockPrice;
	
	public Stock() {
		
	}
	
	public Stock(int stockId, String stockTicker, String stockName, double stockPrice) {
		this.stockId = stockId;
		this.stockTicker = stockTicker;
		this.stockName = stockName;
		this.stockPrice = stockPrice;
	}
	
	public int getStockId() {
		return stockId;
	}
	public void setStockId(int stockId) {
		this.stockId = stockId;
	}
	public String getStockTicker() {
		return stockTicker;
	}
	public void setStockTicker(String stockTicker) {
		this.stockTicker = stockTicker;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public double getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}
	
	

}
