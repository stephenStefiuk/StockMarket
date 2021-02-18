import axios from 'axios';

const http = axios.create({
  baseURL: "http://localhost:8080"
});

export default {
    create(newGame) {
        return http.post("/games", newGame);
    },
    getAll() {
        return http.get("/games");
    },
    getPlayers(id) {
        return http.get(`/games/${id}/players`);
    },
    getGame(id) {
        return http.get(`/games/${id}`);
    },
    getUsers() {
        return http.get('/users');
    },
    addPlayer(id, player) {
        return http.post(`/games/${id}/players`, player);
    },
    getInvitations(id) {
        return http.get(`/user/${id}/games`);
    },
    getMyGames(id) {
        return http.get(`/user/${id}/mygames`);
    },
    updateInviteStatus(id, invitation) {
        return http.put(`/invitation/${id}`, invitation);
    },
    createPortfolio(id, portfolio) {
        return http.post(`/game/${id}/portfolio`, portfolio);
    },
    getPortfolio(id) {
        return http.get(`/game/${id}/portfolio`);
    },
    getPortfolioStock(id) {
        return http.get(`/portfolio/${id}`);
    },
    buyStock(id, portfolioStock) {
        return http.post(`/portfolio/${id}`, portfolioStock);
    },
    getStocks() {
        return http.get('/stocks');
    },
    updateExistingStockPosition(id, type, portfolioStock) {
        return http.put(`/portfolio/stock/${id}/${type}`, portfolioStock);
    },
    updateStockData(ticker, stock) {
        return http.put(`/stocks/${ticker}`, stock);
    },
    getLeaderboard(id) {
        return http.get(`games/${id}/leaderboard`)
    },
    updateGameStatus(id, game) {
        return http.put(`/games/${id}`, game);
    },
    updateCashBalance(id, portfolio) {
        return http.put(`/portfolio/${id}`, portfolio);
    },
    deletePortfolioStock(id) {
        return http.delete(`/portfolio/${id}`);
    },
    getCashLeader(id) {
        return http.get(`games/${id}/cash-leaderboard`)
    }
    

}