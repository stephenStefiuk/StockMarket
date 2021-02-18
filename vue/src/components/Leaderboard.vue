<template>
  <div class="main" v-if="game.status==='Active'">
    <h2>Leaderboard</h2>
      <table>
        <tr>
          <th>Username </th>
          <th>Cash Balance </th>
          <th>Stock Balance </th>
          <th>Total Balance </th>
          </tr>
      <tr v-for="leader in leaderboard" v-bind:key="leader.username">
        <td>{{leader.username}}</td>
        <td>${{leader.cashBalance}}</td>
        <td>${{leader.totalStockValue}}</td>
        <td>${{leader.totalBalance}}</td>
      </tr>
      </table>
      </div>
</template>

<script>
import GameService from "../services/GameService.js"
export default {
data() {
    return {
    leaderboard: [],
    game: {}
}
},
created() {
    GameService.getLeaderboard(this.$route.params.id).then(
    (response) => {
        this.leaderboard = response.data;
        this.leaderboard.sort((a,b) => (a.totalBalance < b.totalBalance) ? 1 : -1);
    });
    GameService.getGame(this.$route.params.id).then(
      (response) => {
        this.game = response.data;
      }
    )
},
// computed: {
//     sortLeaderboard() {
//         return this.leaderboard.sort((a,b) => (a.totalBalance < b.totalBalance) ? 1 : -1);
//     }
// }
}

</script>

<style scoped>
div {
  text-align: center;
}
h2{
  text-align: center;
}

</style>