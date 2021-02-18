<template>
  <div class="main-div">
    
      <h2 class="page-title">Portfolio</h2>
      <div class="cash-balance">
      <h4>Cash Balance</h4>
      <p>${{portfolio.cashBalance}}</p>
      <h4>Stock Balance</h4>
      <p>${{totalStockValue}}</p>
      <h4>Total Balance</h4>
      <p>${{totalPortfolioValue}}</p>
      <span class="buy-stock-button">
      <button v-on:click="buyStock()" >Buy Stocks</button>
      <form v-if="showBuyForm===true" v-on:submit.prevent="purchaseStock">
        <!-- <stocks/> -->
        <select id='stocksOptions' v-model="portfolioStock.stockTicker">
          <!-- <option></option> -->
          <option v-for='stock in unpurchasedStocks' v-bind:key='stock.id' v-bind:value='stock.stockTicker'>{{stock.stockName}}  ${{stock.stockPrice}}</option>
        </select>
        <!-- <input type="text" placeholder="Ticker" v-model="portfolioStock.stockTicker"/> -->
        <input type="number" placeholder="Quantity" v-model="portfolioStock.shareQuantity"/>
        <button>Submit</button>
        <button v-on:click="showBuyForm=false">Cancel</button>

      </form>
      </span>
      </div>
      <div class="stock-value-table">
      <table ><br />
      <tr class="table-head">
        <th>Stock Symbol</th>
        <th>Shares</th>
        <th>Value</th>
      </tr>
      <tr v-for="stock in myStocks" v-bind:key="stock.id" class="table-content">
      
        
        <td>{{stock.stockTicker}}</td>
        <td>{{stock.shareQuantity}}</td>
        <td>${{stock.currentValue}}</td>
        
        <button value="buy" v-on:click="showForm(stock, 'buy')">Buy Stocks</button>
        <button v-show="stock.shareQuantity > 0" value="sell" v-on:click="showForm(stock, 'sell')">Sell Stock</button>
      </tr>
      </table>
      
      <form v-if="showUpdateForm===true" v-on:submit.prevent="updateStock(type)">
      <input type="number" placeholder="Quantity" v-model="quantityToUpdate" />
      <button>Submit</button>
      <button v-on:click="showForm=false">Cancel</button>
      </form>
            <p v-if="showUpdateForm===true">Click submit to {{type}} {{quantityToUpdate}} shares of {{lineOfStockData.stockTicker}}</p>
            
      
  </div>
  
  </div>
</template>

<script>
import GameService from '../services/GameService.js'
// import Stocks from './Stocks.vue'
export default {
  components: {
    // Stocks
  },
  computed: {
    unpurchasedStocks() { 
          return this.stocks.filter(stock => !this.myStockTickers.includes(stock.stockTicker));
    },
    
    myStockTickers() {
      const tickersList = []
      this.myStocks.forEach(
        (stock) =>
        {tickersList.push(stock.stockTicker)}
      )
      return tickersList
    },
    filteredList() {
      const realList = [];
      this.stocks.forEach(
        (stock) =>
        {
          this.myStockTickers.forEach(
            (ticker) => {
              if(stock.stockTicker !== ticker) {
                realList.push(stock);
              }
            }
          )
        }
        )
        return realList;
    }
    
  },
  data() {
    return {
      portfolio: {},
      myStocks: [],
      totalStockValue: 0,
      totalPortfolioValue: 0,
      lineOfStockData: {},
      quantityToUpdate: "",
      showBuyForm: false,
      showUpdateForm: false,
      type: "",
      portfolioStock: {
        stockTicker : "",
        shareQuantity : "",
        portfolioId : "",
      },
      stocks: [],
    }
  },
  created() {
    GameService.getPortfolio(this.$route.params.id).then(response => {
      this.portfolio = response.data;
      GameService.getPortfolioStock(this.portfolio.portfolioId).then(
      (response) => {
        this.myStocks = response.data;
        for(let i=0; i < this.myStocks.length; i++) {
          this.totalStockValue += this.myStocks[i].currentValue;
        }
        this.totalStockValue = this.totalStockValue;
        this.totalPortfolioValue = this.totalStockValue + this.portfolio.cashBalance;
      })
    }),
        GameService.getStocks().then((response) =>
        {
            this.stocks = response.data;
        }
        )
  },
  methods: {
    stockValue(ticker) {
    let stock = this.stocks.find(stock => {
      return stock.stockTicker == ticker
    })
    return stock.stockPrice;
    },
    buyStock() {
      this.showBuyForm = true;
    },
    purchaseStock() {
      let currentStockPrice = this.stockValue(this.portfolioStock.stockTicker);
      let cost = (currentStockPrice * this.portfolioStock.shareQuantity);
      this.portfolioStock.portfolioId = this.portfolio.portfolioId;
      if (this.portfolio.cashBalance >= cost) {
      GameService.buyStock(this.portfolioStock.portfolioId, this.portfolioStock).then(
        response => {
          if (response.status === 201) {
        this.portfolioStock = {
        stockTicker : "",
        shareQuantity : "",
        portfolioId : "",
      }
            this.$router.go();
          } 
        }
      )} else {
        window.alert("You do not have enough funds to complete this transaction");
      }
    },
    updateStock(type) {
      const updatedStock = {
        stockTicker : this.lineOfStockData.stockTicker,
        shareQuantity : this.quantityToUpdate,
        portfolioId : this.lineOfStockData.portfolioId,
        portfolioStockId: this.lineOfStockData.portfolioStockId
      }; if (type == "buy") {
        let currentStockPrice = this.stockValue(updatedStock.stockTicker);
        let cost = (currentStockPrice * updatedStock.shareQuantity);
        if (this.portfolio.cashBalance < cost) {
      window.alert("You do not have enough funds to complete this transaction");}
      }
      if (type == "sell") {
        if ((this.lineOfStockData.shareQuantity - this.quantityToUpdate) < 0) {
          window.alert("You do not have enough shares to complete this transaction")
        }
      }
      GameService.updateExistingStockPosition(updatedStock.portfolioStockId, type, updatedStock).then(
        (response) => {
          if (response.status == 200) {
            this.$router.go();
          }
        }
      )
      
    
    },
    showForm(stock, value) {
      this.showUpdateForm = true;
      this.lineOfStockData = stock;
      this.type = value;
    }
  }
}
</script>

<style scoped>
.stock-value-table {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  text-align: center;
}

.table-head {
  flex: 1;
}

.table-content {
  flex: 1;
}

.cash-balance {
  text-align: center;
}

.buy-stock-button {

}
</style>