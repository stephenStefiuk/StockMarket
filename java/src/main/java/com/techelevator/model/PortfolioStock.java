package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PortfolioStock {
	
	private int portfolioStockId;
	private int portfolioId;
	private String stockTicker;
	private int shareQuantity;
	private BigDecimal currentValue;
	
	public PortfolioStock () {
		
	}
	
	public PortfolioStock(int portfolioStockId, int portfolioId, String stockTicker, int shareQuantity) {
		this.portfolioStockId = portfolioStockId;
		this.portfolioId = portfolioId;
		this.stockTicker = stockTicker;
		this.shareQuantity = shareQuantity;
	}
	public int getPortfolioStockId() {
		return portfolioStockId;
	}
	public void setPortfolioStockId(int portfolioStockId) {
		this.portfolioStockId = portfolioStockId;
	}
	public int getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getStockTicker() {
		return stockTicker;
	}
	public void setStockTicker(String stockTicker) {
		this.stockTicker = stockTicker;
	}
	public int getShareQuantity() {
		return shareQuantity;
	}
	public void setShareQuantity(int shareQuantity) {
		this.shareQuantity = shareQuantity;
	}
	public BigDecimal getCurrentValue() {
		return currentValue.setScale(2, RoundingMode.HALF_UP);
	}

	public void setCurrentValue(BigDecimal currentValue) {
		this.currentValue = currentValue.setScale(2, RoundingMode.HALF_UP);
	}
	
	

}
