package com.techelevator.dao;


import java.util.List;

import com.techelevator.model.Portfolio;
import com.techelevator.model.PortfolioStock;
import com.techelevator.model.Stats;
import com.techelevator.model.Stock;


public interface PortfolioDAO {
	
	List<Portfolio> findAll();
	
	void create(int userGamesId);

	List<PortfolioStock> findAllPortfolioStock(int portfolioId);
	
	Portfolio getPortfolio(int userGamesId);

	void buyStock(PortfolioStock portfolioStock);
	
	List<Stock> getAllStocks ();
	
	void updateStock(int portfolioStockId, String type, PortfolioStock portfolioStock);
	
	void deleteStock (int portfolioStockId);
	
	List<Stats> getAllStatsForGame (int gameId);
	
	void updateCashBalance (int portfolioId, Portfolio portfolio);
	
	void deletePortfolioStocks (int portfolioId);

	List<Stats> retrieveCashStats(int gameId);
}
