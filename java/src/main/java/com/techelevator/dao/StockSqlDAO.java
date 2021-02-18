package com.techelevator.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.techelevator.model.Stock;

@Component
public class StockSqlDAO implements StockDAO{
	
	private JdbcTemplate jdbcTemplate;

	public StockSqlDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	

	@Override
	public void updateStockPrice(Stock stock) {
		String sqlString = "UPDATE stocks SET stock_price = ? WHERE ticker = ?";
		jdbcTemplate.update(sqlString, stock.getStockPrice(), stock.getStockTicker());
	}

}
