package com.techelevator.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.techelevator.dao.GameDAO;
import com.techelevator.dao.PortfolioDAO;
import com.techelevator.dao.StockDAO;
import com.techelevator.dao.UserDAO;
import com.techelevator.model.AAResponse;
import com.techelevator.model.FinnhubResponse;
import com.techelevator.model.Game;
import com.techelevator.model.Invitation;
import com.techelevator.model.Portfolio;
import com.techelevator.model.PortfolioStock;
import com.techelevator.model.Stats;
import com.techelevator.model.Stock;
import com.techelevator.model.User;
import com.techelevator.services.AlphaAdvantageService;
import com.techelevator.services.FinnhubService;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")

public class ServerController {
	@Autowired
	private GameDAO gameDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PortfolioDAO portfolioDAO;
	
	@Autowired
	private StockDAO stockDAO;

	
	public ServerController(GameDAO gameDAO, UserDAO userDAO, PortfolioDAO portfolioDAO) {
		this.gameDAO = gameDAO;
		this.userDAO = userDAO;
		this.portfolioDAO = portfolioDAO;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path= "/games", method= RequestMethod.POST) 
		public void addGame(@Valid @RequestBody Game game) {
			 gameDAO.create(game.getGameName(), game.getStatus(), game.getStartDate(), game.getEndDate(), game.getOrganizerId());
	}
	
	@RequestMapping(path="/games", method=RequestMethod.GET)
	public List<Game> getAllGames() {
		List<Game> allGames = gameDAO.findAll();
		return allGames;
	}
	
	@RequestMapping(path="/games/{id}", method=RequestMethod.GET)
	public Game getGameById(@PathVariable int id) {
		return gameDAO.getGameById(id);
	}
	
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public List<User> getUsers() {
		List<User> allUsers = userDAO.findAll();
		return allUsers;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path="/games/{id}/players", method=RequestMethod.POST)
	public void addPlayer(@PathVariable int id, @RequestBody Invitation invitation) {
		gameDAO.addPlayers(invitation.getGameId(), invitation.getUserId());
	}
	
	@RequestMapping(path="/user/{id}/games", method=RequestMethod.GET)
	public List<Invitation> getGamesByUserId(@PathVariable int id) {
		return gameDAO.getInvitationsByUserId(id);
	}
	
	@RequestMapping(path="/user/{id}/mygames", method=RequestMethod.GET)
	public List<Game> getGamesInviteStatus(@PathVariable int id) {
		return gameDAO.findGamesLoggedInUser(id);
	}
	
	@RequestMapping(path="/invitation/{id}", method=RequestMethod.PUT)
	public void updateInvitation(@PathVariable int id, @RequestBody Invitation invitation) {
		gameDAO.updateInvitation(id, invitation);
	}
	
	@RequestMapping(path="/games/{id}/players", method=RequestMethod.GET)
	public List<User> getPlayers(@PathVariable int id) {
		return gameDAO.getPlayers(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path="/game/{id}/portfolio", method=RequestMethod.POST)
	public void addPortfolio(@PathVariable int id, @RequestBody Portfolio portfolio) {
		portfolioDAO.create(id);
	}
	
	@RequestMapping(path="/game/{id}/portfolio", method=RequestMethod.GET)
	public Portfolio getPortfolio(@PathVariable int id) {
		return portfolioDAO.getPortfolio(id);
	}
	
	@RequestMapping(path="/portfolio/{id}", method=RequestMethod.GET)
	public List<PortfolioStock> getAllPortfolioStock(@PathVariable int id) {
		return portfolioDAO.findAllPortfolioStock(id);
	}
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path="/portfolio/{id}", method=RequestMethod.POST)
	public void buyStock(@PathVariable int id, @RequestBody PortfolioStock portfolioStock) {
		portfolioDAO.buyStock(portfolioStock);
	}
	
	@RequestMapping(path="/stocks", method=RequestMethod.GET)
	public List<Stock> getAllStocks () {
		return portfolioDAO.getAllStocks();
	}
	
	@RequestMapping(path="/portfolio/stock/{id}/{type}", method=RequestMethod.PUT)
	public void updateStock(@PathVariable int id, @PathVariable String type, @RequestBody PortfolioStock portfolioStock) {
		portfolioDAO.updateStock(id, type, portfolioStock);
	}
	
	@RequestMapping(path="/portfolio/stock/{id}", method=RequestMethod.DELETE)
	public void deleteStock(@PathVariable int id) {
		portfolioDAO.deleteStock(id);
	}
	
	@RequestMapping(path="games/{id}/leaderboard", method=RequestMethod.GET)
	public List<Stats> getLeaderboard (@PathVariable int id) {
		return portfolioDAO.getAllStatsForGame(id);
		
	}
	
	@RequestMapping(path="games/{id}/cash-leaderboard", method=RequestMethod.GET)
	public List<Stats> getCashLeader (@PathVariable int id) {
		return portfolioDAO.retrieveCashStats(id);
	}
	
	@RequestMapping(path="/stocks/{ticker}", method=RequestMethod.PUT)
	public void updateStockData(@PathVariable String ticker, @RequestBody Stock stock) throws JsonMappingException, JsonProcessingException {
		FinnhubResponse data = testFinn(ticker);
		stock.setStockPrice(data.getPrice());
		stockDAO.updateStockPrice(stock);
	}
	
//	@RequestMapping(path="/stocks/{ticker}", method=RequestMethod.PUT)
//	public void updateStockData(@PathVariable String ticker, @RequestBody Stock stock) throws JsonMappingException, JsonProcessingException {
//		AAResponse data = test(ticker);
//		stock.setStockPrice(data.getPrice());
//		stockDAO.updateStockPrice(stock);
//	}
	private FinnhubResponse testFinn(String ticker) throws JsonMappingException, JsonProcessingException {
		FinnhubService fhService = new FinnhubService();
		return fhService.getStockData(ticker);
	}
	private AAResponse test(String ticker) throws JsonMappingException, JsonProcessingException {
		AlphaAdvantageService aaService = new AlphaAdvantageService();
		return aaService.getStockData(ticker);
	}
	
	@RequestMapping(path="/games/{id}", method=RequestMethod.PUT)
	public void updateGameStatus(@PathVariable int id, @RequestBody Game game) {
		gameDAO.updateGame(id, game);
	}
	
	@RequestMapping(path="/portfolio/{id}", method=RequestMethod.PUT)
	public void updatePortfolioCashBalance (@PathVariable int id, @RequestBody Portfolio portfolio) {
		portfolioDAO.updateCashBalance(id, portfolio);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(path="/portfolio/{id}", method=RequestMethod.DELETE) 
		public void deletePortfolioStock(@PathVariable int id) {
			portfolioDAO.deletePortfolioStocks(id);
		}
	
	
	
	
}	
