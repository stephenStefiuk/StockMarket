package com.techelevator.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.techelevator.model.Game;
import com.techelevator.model.Invitation;
import com.techelevator.model.User;

public interface GameDAO {
	
	List<Game> findAll();
	
	Game getGameById(int gameId);

    Game findByGameName(String gameName);

    int findIdByGameName(String gameName);

    void create(String gameName, String status, LocalDate startDate, LocalDate endDate, int organizerId);
    
    void addPlayers(int gameId, int userId);
    
    List<Invitation> getInvitationsByUserId(int userId);

	List<Game> findGamesLoggedInUser(int id);

	void updateInvitation(int userGameId, Invitation invitation);

	List<User> getPlayers(int gameId);
	
	void updateGame(int gameId, Game game);
}
