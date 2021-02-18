package com.techelevator.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.Portfolio;
import com.techelevator.model.PortfolioStock;
import com.techelevator.model.Stats;
import com.techelevator.model.Stock;

@Component
public class PortfolioSqlDAO implements PortfolioDAO {

	private JdbcTemplate jdbcTemplate;

	public PortfolioSqlDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<Portfolio> findAll() {

		List<Portfolio> portfolios = new ArrayList<>();
		String sql = "SELECT * from portfolio";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Portfolio portfolio = mapRowToPortfolio(results);
			portfolios.add(portfolio);
		}
		return portfolios;
	}

	@Override
	public void create(int userGamesId) {
		BigDecimal startBalance = new BigDecimal(100000).setScale(2, RoundingMode.HALF_UP);
		String sqlString = "INSERT INTO portfolio (user_games_id, cash_balance) VALUES (?,?)";
		jdbcTemplate.update(sqlString, userGamesId, startBalance);

	}

	private Portfolio mapRowToPortfolio(SqlRowSet rs) {
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioId(rs.getInt("portfolio_id"));
		portfolio.setUserGamesId(rs.getInt("user_games_id"));
		portfolio.setCashBalance(rs.getBigDecimal("cash_balance").setScale(2, RoundingMode.HALF_UP));

		return portfolio;
	}

	@Override
	public List<PortfolioStock> findAllPortfolioStock(int portfolioId) {

		List<PortfolioStock> portfolioStocks = new ArrayList<>();
		String sql = "SELECT portfolio_stock_id, portfolio_stock.portfolio_id, stock_ticker, share_quantity, stocks.stock_price * portfolio_stock.share_quantity "
				+ "AS value FROM portfolio_stock JOIN stocks ON stocks.ticker = portfolio_stock.stock_ticker JOIN portfolio "
				+ "ON portfolio.portfolio_id = portfolio_stock.portfolio_id WHERE portfolio.portfolio_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, portfolioId);
		while (results.next()) {
			PortfolioStock portfolioStock = mapRowToPortfolioStock(results);
			portfolioStocks.add(portfolioStock);
		}
		return portfolioStocks;
	}

	private PortfolioStock mapRowToPortfolioStock(SqlRowSet rs) {
		PortfolioStock portfolioStock = new PortfolioStock();
		portfolioStock.setPortfolioId(rs.getInt("portfolio_id"));
		portfolioStock.setPortfolioStockId(rs.getInt("portfolio_stock_id"));
		portfolioStock.setStockTicker(rs.getString("stock_ticker"));
		portfolioStock.setShareQuantity(rs.getInt("share_quantity"));
		portfolioStock.setCurrentValue(rs.getBigDecimal("value").setScale(2, RoundingMode.HALF_UP));

		return portfolioStock;
	}

	@Override
	public Portfolio getPortfolio(int userGamesId) {
		Portfolio portfolio = new Portfolio();
		String sql = "SELECT * from portfolio WHERE user_games_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userGamesId);

		if (results.next()) {
			portfolio = mapRowToPortfolio(results);
		}

		return portfolio;
	}

	@Override
	public void buyStock(PortfolioStock portfolioStock) {
		String sql2 = "SELECT stock_price FROM stocks WHERE ticker = ?";
		double price = jdbcTemplate.queryForObject(sql2, double.class, portfolioStock.getStockTicker());
		BigDecimal cost = new BigDecimal(price * portfolioStock.getShareQuantity()).setScale(2, RoundingMode.HALF_UP);
		String sql3 = "SELECT cash_balance FROM portfolio WHERE portfolio_id = ?";
		BigDecimal currentBalance = jdbcTemplate.queryForObject(sql3, BigDecimal.class,
				portfolioStock.getPortfolioId()).setScale(2, RoundingMode.HALF_UP);
		BigDecimal updatedBalance = (currentBalance.subtract(cost)).setScale(2, RoundingMode.HALF_UP);
		if (updatedBalance.compareTo(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)) == 0 || updatedBalance.compareTo(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)) == 1) {
			String sql = "INSERT INTO portfolio_stock (portfolio_id, stock_ticker, share_quantity) VALUES (?,?,?)";
			jdbcTemplate.update(sql, portfolioStock.getPortfolioId(), portfolioStock.getStockTicker(),
					portfolioStock.getShareQuantity());
			String sql4 = "UPDATE portfolio SET cash_balance = ? WHERE portfolio_id = ?";
			jdbcTemplate.update(sql4, updatedBalance, portfolioStock.getPortfolioId());

		}
	}

	@Override
	public List<Stock> getAllStocks() {
		List<Stock> stocks = new ArrayList<Stock>();
		String sql = "SELECT * FROM stocks";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

		while (results.next()) {
			Stock stock = new Stock();
			stock = mapRowToStock(results);
			stocks.add(stock);
		}
		return stocks;
	}

	private Stock mapRowToStock(SqlRowSet rs) {
		Stock stock = new Stock();
		stock.setStockId(rs.getInt("stock_id"));
		stock.setStockPrice(rs.getDouble("stock_price"));
		stock.setStockTicker(rs.getString("ticker"));
		stock.setStockName(rs.getString("stock_name"));

		return stock;
	}

	@Override
	public void updateStock(int portfolioStockId, String type, PortfolioStock portfolioStock) {
		if (type.equalsIgnoreCase("buy")) {
			String sql2 = "SELECT stock_price FROM stocks WHERE ticker = ?";
			double price = jdbcTemplate.queryForObject(sql2, double.class, portfolioStock.getStockTicker());
			BigDecimal cost = new BigDecimal(price * portfolioStock.getShareQuantity()).setScale(2, RoundingMode.HALF_UP);
			String sql3 = "SELECT cash_balance FROM portfolio WHERE portfolio_id = ?";
			BigDecimal currentBalance = jdbcTemplate.queryForObject(sql3, BigDecimal.class,
					portfolioStock.getPortfolioId()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal updatedBalance = (currentBalance.subtract(cost)).setScale(2, RoundingMode.HALF_UP);
			if (updatedBalance.compareTo(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)) == 0 || updatedBalance.compareTo(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP)) == 1) {
				String sql5 = "SELECT share_quantity FROM portfolio_stock WHERE portfolio_stock_id = ?";
				int startingShareQuantity = jdbcTemplate.queryForObject(sql5, int.class, portfolioStockId);
				int updatedShareQuantity = portfolioStock.getShareQuantity() + startingShareQuantity;
				String sql = "UPDATE portfolio_stock SET share_quantity = ? WHERE portfolio_stock_id = ?";
				jdbcTemplate.update(sql, updatedShareQuantity, portfolioStockId);
				String sql4 = "UPDATE portfolio SET cash_balance = ? WHERE portfolio_id = ?";
				jdbcTemplate.update(sql4, updatedBalance, portfolioStock.getPortfolioId());
			}
		} else {
			String sql5 = "SELECT share_quantity FROM portfolio_stock WHERE portfolio_stock_id = ?";
			int startingShareQuantity = jdbcTemplate.queryForObject(sql5, int.class, portfolioStockId);
			int updatedShareQuantity = startingShareQuantity - portfolioStock.getShareQuantity();
			if (updatedShareQuantity >= 0) {
				String sql = "UPDATE portfolio_stock SET share_quantity = ? WHERE portfolio_stock_id = ?";
				jdbcTemplate.update(sql, updatedShareQuantity, portfolioStockId);
				String sql2 = "SELECT stock_price FROM stocks WHERE ticker = ?";
				double price = jdbcTemplate.queryForObject(sql2, double.class, portfolioStock.getStockTicker());
				BigDecimal profit = new BigDecimal(price * portfolioStock.getShareQuantity()).setScale(2, RoundingMode.HALF_UP);
				String sql3 = "SELECT cash_balance FROM portfolio WHERE portfolio_id = ?";
				BigDecimal currentBalance = jdbcTemplate.queryForObject(sql3, BigDecimal.class,
						portfolioStock.getPortfolioId()).setScale(2, RoundingMode.HALF_UP);
				BigDecimal updatedBalance = (currentBalance.add(profit)).setScale(2, RoundingMode.HALF_UP);
				String sql4 = "UPDATE portfolio SET cash_balance = ? WHERE portfolio_id = ?";
				jdbcTemplate.update(sql4, updatedBalance, portfolioStock.getPortfolioId());
			}
		}
	}

	@Override
	public void deleteStock(int portfolioStockId) {
		String sql = "DELETE FROM portfolio_stock WHERE portfolio_stock_id = ?";
		jdbcTemplate.update(sql, portfolioStockId);
	}

	@Override
	public List<Stats> getAllStatsForGame(int gameId) {
		List<Stats> allStats = new ArrayList<Stats>();
		String sql = "SELECT portfolio.portfolio_id, users.username, SUM (stocks.stock_price * portfolio_stock.share_quantity) AS total_stock_value, cash_balance FROM portfolio_stock "
				+ "JOIN stocks ON stocks.ticker = portfolio_stock.stock_ticker JOIN portfolio ON portfolio.portfolio_id = portfolio_stock.portfolio_id "
				+ "JOIN user_games ON portfolio.user_games_id = user_games.user_games_id JOIN users ON users.user_id = user_games.user_id WHERE user_games.game_id = ? "
				+ "GROUP BY portfolio.portfolio_id, users.username";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, gameId);
		while (results.next()) {
			Stats stats = new Stats();
			stats.setPortfolioId(results.getInt("portfolio_id"));
			stats.setUsername(results.getString("username"));
			stats.setTotalStockValue(results.getBigDecimal("total_stock_value").setScale(2, RoundingMode.HALF_UP));
			stats.setCashBalance(results.getBigDecimal("cash_balance").setScale(2, RoundingMode.HALF_UP));
			stats.setTotalBalance(stats.getCashBalance().add(stats.getTotalStockValue()).setScale(2, RoundingMode.HALF_UP));
			allStats.add(stats);
		}
		return allStats;
	}

	@Override
	public void updateCashBalance(int portfolioId, Portfolio portfolio) {
		String sql = "UPDATE portfolio SET cash_balance = ? WHERE portfolio_id = ?";
		jdbcTemplate.update(sql, portfolio.getCashBalance(), portfolioId);
	}

	@Override
	public void deletePortfolioStocks(int portfolioId) {
		String sql = "DELETE FROM portfolio_stock WHERE portfolio_id = ?";
		jdbcTemplate.update(sql, portfolioId);
	}

	@Override
	public List<Stats> retrieveCashStats(int gameId) {
		List<Stats> allStats = new ArrayList<Stats>();
		String sql = "SELECT portfolio.portfolio_id, users.username, cash_balance FROM portfolio JOIN user_games ON portfolio.user_games_id = user_games.user_games_id"
				+ " JOIN users ON users.user_id = user_games.user_id WHERE user_games.game_id = ? GROUP BY portfolio.portfolio_id, users.username";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, gameId);
		while (results.next()) {
			Stats stats = new Stats();
			stats.setPortfolioId(results.getInt("portfolio_id"));
			stats.setUsername(results.getString("username"));
			stats.setTotalStockValue(results.getBigDecimal("cash_balance").setScale(2, RoundingMode.HALF_UP));
			stats.setCashBalance(results.getBigDecimal("cash_balance").setScale(2, RoundingMode.HALF_UP));
			stats.setTotalBalance(results.getBigDecimal("cash_balance").setScale(2, RoundingMode.HALF_UP));
			allStats.add(stats);
		}
		return allStats;
	}
}
