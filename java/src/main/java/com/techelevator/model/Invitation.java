package com.techelevator.model;

public class Invitation {
	
	private int userGameId;
	
	private int gameId;
	
	private int userId;
	
	private String status;
	
	private String gameName;
	

	public Invitation(int userGameId, int gameId, int userId, String status, String gameName) {
		this.userGameId = userGameId;
		this.gameId = gameId;
		this.userId = userId;
		this.status = status;
		this.gameName = gameName;
	}
	
	public Invitation() {
		
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUserGameId() {
		return userGameId;
	}

	public void setUserGameId(int userGameId) {
		this.userGameId = userGameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	

}
