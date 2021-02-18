<template>
  <div>
      <tr>
        <th>Name</th>
        <th>Ticker</th>
        <th>Price</th>
      </tr>
      <tr v-for="stock in stocks" v-bind:key="stock.id">
        <td>{{stock.stockName}}</td>
        <td>{{stock.stockTicker}}</td>
        <td>{{stock.stockPrice}}</td>
      </tr>
  </div>
</template>

<script>
import GameService from '../services/GameService'
export default {
    data() {
        return {
            stocks: []
        }
    },
    created() {
        GameService.getStocks().then((response) =>
        {
            this.stocks = response.data;
        }
        )
    }

}
</script>

<style scoped>

</style>