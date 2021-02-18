package com.techelevator.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.techelevator.model.Game;
import com.techelevator.model.Invitation;
import com.techelevator.model.User;

@Component
public class GameSqlDAO implements GameDAO {

	private JdbcTemplate jdbcTemplate;

	public GameSqlDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

	@Override
	public List<Game> findAll() {
		List<Game> games = new ArrayList<>();
		String sql = "SELECT * from games";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while (results.next()) {
			Game game = mapRowToGame(results);
			games.add(game);
		}
		return games;
	}

	@Override
	public List<Game> findGamesLoggedInUser(int id) {
		List<Game> games = new ArrayList<>();

		String sql = "SELECT games.game_id, games.game_name, games.status, games.start_date, games.end_date, games.organizer_id, user_games.invite_status FROM games JOIN user_games ON games.game_id = user_games.game_id WHERE user_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

		while (results.next()) {
			Game game = mapRowToGame(results);
			game.setInvitationStatus(results.getString("invite_status"));
			games.add(game);
		}

		return games;
	}

	@Override
	public Game getGameById(int gameId) {
		String sql = "SELECT * FROM games WHERE game_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, gameId);
		if (results.next()) {
			return mapRowToGame(results);
		} else {
			throw new RuntimeException("gameId " + gameId + " was not found.");
		}
	}

	@Override
	public Game findByGameName(String gameName) {
		Game newGame = new Game();
		for (Game game : this.findAll()) {
			if (game.getGameName().toLowerCase().equals(gameName.toLowerCase())) {
				newGame = game;
			}
		}
		return newGame;
	}

	@Override
	public int findIdByGameName(String gameName) {
		return jdbcTemplate.queryForObject("SELECT game_id FROM games WHERE game_name = ?", int.class, gameName);
	}

	@Override
	public void create(String gameName, String status, LocalDate startDate, LocalDate endDate, int organizerId) {
		String sql = "INSERT INTO games (game_name, status, start_date, end_date, organizer_id) VALUES (?,?,?,?,?)";
		String sql2 = "SELECT game_id FROM games WHERE game_name = ? AND status = ? AND start_date = ? AND end_date = ? AND organizer_id = ?";
		String sql3 = "INSERT INTO user_games (user_id, game_id, invite_status) VALUES (?,?,?)";
		String sql4 = "SELECT user_games_id FROM user_games WHERE user_id = ? AND game_id = ? AND invite_status = ?";
		String sql5 = "INSERT INTO portfolio (user_games_id, cash_balance) VALUES (?,?)";

		jdbcTemplate.update(sql, gameName, status, startDate, endDate, organizerId);
		int gameId = jdbcTemplate.queryForObject(sql2, int.class, gameName, status, startDate, endDate, organizerId);

		jdbcTemplate.update(sql3, organizerId, gameId, "Accepted");
		int userGameId = jdbcTemplate.queryForObject(sql4, int.class, organizerId, gameId, "Accepted");

		BigDecimal startBalance = new BigDecimal(100000).setScale(2, RoundingMode.HALF_UP);
		jdbcTemplate.update(sql5, userGameId, startBalance);

	}

	private Game mapRowToGame(SqlRowSet rs) {
		Game game = new Game();
		game.setGameId(rs.getInt("game_id"));
		game.setGameName(rs.getString("game_name"));
		game.setStatus(rs.getString("status"));
		game.setStartDate(rs.getDate("start_date").toLocalDate());
		game.setEndDate(rs.getDate("end_date").toLocalDate());
		game.setOrganizerId(rs.getInt("organizer_id"));
		return game;
	}

	@Override
	public void addPlayers(int gameId, int userId) {
		String sql = "INSERT INTO user_games (game_id, user_id, invite_status) VALUES (?,?,?)";
		jdbcTemplate.update(sql, gameId, userId, "Pending");

	}

	@Override
	public List<Invitation> getInvitationsByUserId(int userId) {
		List<Invitation> invitationList = new ArrayList<Invitation>();
		String sqlString = "SELECT user_games.user_games_id, user_id, user_games.game_id, invite_status, "
				+ "games.game_name  FROM user_games JOIN games ON games.game_id = user_games.game_id WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlString, userId);

		while (results.next()) {
			Invitation invitation = mapRowToInvitation(results);
			invitationList.add(invitation);

		}

		return invitationList;
	}

	private Invitation mapRowToInvitation(SqlRowSet rs) {
		Invitation invitation = new Invitation();
		invitation.setUserGameId(rs.getInt("user_games_id"));
		invitation.setGameId(rs.getInt("game_id"));
		invitation.setUserId(rs.getInt("user_id"));
		invitation.setStatus(rs.getString("invite_status"));
		invitation.setGameName(rs.getString("game_name"));
		return invitation;
	}

	@Override
	public void updateInvitation(int userGameId, Invitation invitation) {
		String sql = "UPDATE user_games SET invite_status = ? WHERE user_games_id = ?";
		jdbcTemplate.update(sql, invitation.getStatus(), invitation.getUserGameId());
	}

	@Override
	public List<User> getPlayers(int gameId) {
		List<User> userList = new ArrayList<User>();
		String sql = "SELECT users.user_id, username, password_hash, role FROM users JOIN user_games on users.user_id = user_games.user_id WHERE invite_status != 'Declined' AND game_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, gameId);

		while (results.next()) {
			User user = new User();
			user.setId(results.getLong("user_id"));
			user.setUsername(results.getString("username"));
			user.setPassword(results.getString("password_hash"));
			user.setAuthorities(results.getString("role"));
			userList.add(user);
		}
		return userList;
	}

	@Override
	public void updateGame(int gameId, Game game) {
		String sql = "UPDATE games SET status = ? WHERE game_id = ?";
		jdbcTemplate.update(sql, game.getStatus(), gameId);
		
	}

}
