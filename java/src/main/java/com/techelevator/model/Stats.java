package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Stats {
	
	private BigDecimal totalStockValue;
	private BigDecimal cashBalance;
	private String username;
	private BigDecimal totalBalance;
	private int portfolioId;
	
	public Stats() {};
	public Stats (BigDecimal totalStockValue, BigDecimal cashBalance, String username, BigDecimal totalBalance, int portfolioId) {
		this.totalStockValue = totalStockValue;
		this.cashBalance = cashBalance;
		this.username = username;
		this.totalBalance = totalBalance;
		this.portfolioId = portfolioId;
	}
	public BigDecimal getTotalBalance() {
		return totalBalance.setScale(2, RoundingMode.HALF_UP);
	}
	public void setTotalBalance(BigDecimal totalBalance) {
		this.totalBalance = totalBalance.setScale(2, RoundingMode.HALF_UP);
	}
	public BigDecimal getTotalStockValue() {
		return totalStockValue.setScale(2, RoundingMode.HALF_UP);
	}
	public void setTotalStockValue(BigDecimal totalStockValue) {
		this.totalStockValue = totalStockValue.setScale(2, RoundingMode.HALF_UP);
	}
	public BigDecimal getCashBalance() {
		return cashBalance.setScale(2, RoundingMode.HALF_UP);
	}
	public void setCashBalance(BigDecimal cashBalance) {
		this.cashBalance = cashBalance.setScale(2, RoundingMode.HALF_UP);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}
	
	
	

}
