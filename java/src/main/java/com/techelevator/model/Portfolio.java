package com.techelevator.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Portfolio {

	private int portfolioId;
	private int userGamesId;
	private BigDecimal cashBalance;
	
	
	public Portfolio(int portfolioId, int userGamesId, BigDecimal cashBalance) {
		
		this.portfolioId = portfolioId;
		this.userGamesId = userGamesId;
		this.cashBalance = cashBalance;
	}
	
	public Portfolio() {
		
	}


	public int getPortfolioId() {
		return portfolioId;
	}


	public int getUserGamesId() {
		return userGamesId;
	}


	public BigDecimal getCashBalance() {
		return cashBalance.setScale(2, RoundingMode.HALF_UP);
	}


	public void setPortfolioId(int portfolioId) {
		this.portfolioId = portfolioId;
	}


	public void setUserGamesId(int userGamesId) {
		this.userGamesId = userGamesId;
	}


	public void setCashBalance(BigDecimal cashBalance) {
		this.cashBalance = cashBalance.setScale(2, RoundingMode.HALF_UP);
	}
	
	
	
	
	
}
