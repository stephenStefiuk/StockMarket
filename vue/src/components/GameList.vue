<template>
  <div >
    
    <h2 class="page-title">My Games</h2>
    <br>
    <div v-if="gameList.length === 0">
    <h3>You have no games</h3>
    <h4>Check your invitations or create a new game!</h4>
      </div>
    <div class="mainDiv" v-if="gameList.length !== 0">
    
    <div id="active-games">
      <h4>Active Games</h4>
    <game-card
      class="game-card"
      
      
      v-for="game in activeGames"
      v-bind:key="game.id"
      v-bind:game="game"
    >
    </game-card>
    </div>
    
    <div id="inactive-games">
      
      <h4>Completed Games</h4>
    <game-card
      class="game-card"
      
      
      v-for="game in completedGames"
      v-bind:key="game.id"
      v-bind:game="game"
    >
      <!-- <router-link v-bind:to="{name: 'gameDetails', params: {id: game.id}}">See Details</router-link> -->
    </game-card>
    </div>

      <div id="upcoming-games">
      <h4>Upcoming Games</h4>
    <game-card
      class="game-card"
      
      style="grid-area: upcoming-cards"
      v-for="game in upcomingGames"
      v-bind:key="game.id"
      v-bind:game="game"
    >
      <!-- <router-link v-bind:to="{name: 'gameDetails', params: {id: game.id}}">See Details</router-link> -->
    </game-card>
    </div>

    </div>
  
  </div>
</template>

<script>
import GameCard from "./GameCard.vue";
import GameService from "../services/GameService.js";

export default {
  components: {
    GameCard,
  },
  data() {
    return {
      gameList: [],
      playerList: [],
    };
  },
  created() {
    GameService.getMyGames(this.$store.state.user.id).then((response) => {
      this.gameList = response.data;
    });
    // GameService.getPlayers(this.id).then((response) => {
    //   this.playerList = response.data;
    // });
  },
  computed: {
    activeGames() {
      return this.acceptedInvites.filter((game) => {
        return game.status == "Active";
      });
    },
    completedGames() {
      return this.acceptedInvites.filter((game) => {
        return game.status == "Completed";
      });
    },
    upcomingGames() {
      return this.acceptedInvites.filter((game) => {
        return game.status == "Upcoming";
      });
    },
    acceptedInvites() {
      return this.gameList.filter((game) => {
        return game.invitationStatus !== "Declined";
      });
    }
  }
};
</script>

<style scoped>
/* column for each of above */
.mainDiv {
  display: flex;
  flex-direction: row;
  align-content: center;
  justify-content: space-evenly;
  align-items: flex-start;
}
.game-card {
  flex: 1;
  width: auto;
  text-align: center;
  margin: 7px;
  padding: 6px;
  border-radius: 3px;
  color: #DEE7E9;
}

#active-games .game-card {
  flex: 1;
  background-color: #248740;
}
#inactive-games .game-card{
  flex: 1;
  background-color: #545454;
}
#upcoming-games .game-card {
  flex: 1;
  background-color: #532385;
}
#active-games .game-card:hover {
  background-color: gray;
}
#inactive-games .game-card:hover {
  background-color: gray;
}
#upcoming-games .game-card:hover {
  background-color: gray;
}

h3 {
  text-align: center;
}
h4 {
  text-align: center;
}
</style>