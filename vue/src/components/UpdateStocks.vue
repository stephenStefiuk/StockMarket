<template>
  <div>
      <p v-for='stock in listOfStocks' v-bind:key='stock.id'>{{stock.stockTicker}}: {{stock.stockPrice}}</p><br>
    <button v-on:click="updateStockPrices">UPDATE STOCK</button>
    <p>
      <button v-on:click="updateGame">UPDATE GAME STATUS</button>
    </p>
    
  </div>
</template>

<script>
import GameService from '../services/GameService.js';
export default {
    data() {
        return {
            listOfStocks: [],
            listOfGames: [],
        }
    },
    
    created() {
    GameService.getStocks().then((response) =>
        {
            this.listOfStocks = response.data;
        }
        ),
    GameService.getAll().then(
      (response) => {
        this.listOfGames = response.data;
      }
    )
  },
  methods: {
    updateStockPrices() {
      this.listOfStocks.forEach(stock => {
         GameService.updateStockData(stock.stockTicker, stock).then(response => {
          if (response.status == 200) {
            this.$router.go();
          }
        });
      })},
    updateGame() {
      let currentDate = new Date();
      this.listOfGames.forEach(
        (game) => {
          let startDate = new Date(game.startDate);
          let endDate = new Date(game.endDate);
          if (startDate <= currentDate && endDate > currentDate) {
            (game.status = "Active");
            GameService.updateGameStatus(game.gameId, game).then(
              (response) => {
                if (response.status == 200) {
                  console.log('ok game status changed');
                }
              }
            )
          } else if (game.status=="Active" && (endDate <= currentDate)) {
            (game.status = "Completed")
            GameService.updateGameStatus(game.gameId, game).then(
              GameService.getLeaderboard(game.gameId).then(
                (response) => {
                response.data.forEach(
                  (player) => {
                    player.cashBalance = player.totalBalance
                    GameService.updateCashBalance(player.portfolioId, player).then(
                      GameService.deletePortfolioStock(player.portfolioId).then(
                      (response) => {if (response.status == 200) {
                        console.log("GAME COMPLETED")
                      }}
                    )
                  )
                  }
              )
                }
              )
            )
          }

        }
      )
    }
  }

}
</script>

<style scoped>

div {
  margin-bottom: 100px;
}

</style>