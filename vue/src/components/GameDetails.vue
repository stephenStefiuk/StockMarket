<template>
  <div class="main">
    <section class="game-display">
      <h2 class="page-title">Details for: {{ game.gameName }}</h2>
      <div v-if="game.status !== 'Completed'">
        <div class="game-details">
          <table>
            <tr>
              <th>Game Status</th>
              <th>Start Date</th>
              <th>End Date</th>
              <th>Game Organizer</th>
            </tr>
            <tr>
              <td>{{ game.status }}</td>
              <td>{{ game.startDate }}</td>
              <td>{{ game.endDate }}</td>
              <td>{{ gameOrganizer.username }}</td>
            </tr>
          </table>
        </div>
        <br />
        <h3>Invited Players</h3>
        <span v-for="player in invitedPlayers" v-bind:key="player.id">
          <img src="../assets/icon.jpg" /> {{ player.username }}
        </span>
      </div>
      <div v-if="game.status === 'Completed'">
        <h2>Final scores</h2>

<table class="final-results">
  <tr>
    <th>Rank</th>
  <th>Player Name</th>
  <th>Final Balance</th>
  </tr>
        <tr v-for="(leader, index) in cashLeaders" v-bind:key="leader.id">
          <td>
            {{ index + 1 }}
          </td>
          <td>
            {{ leader.username }}
          </td>
          <td>${{ leader.cashBalance }}</td>
        </tr>
</table>
      </div>
      <button
        v-on:click="invitePlayers()"
        v-if="
          currentDate < startDateObj &&
          game.organizerId === this.$store.state.user.id
        "
      >
        Invite Players
      </button>
    </section>
    <form v-if="invitePlayersBool" v-on:submit.prevent="addPlayersToGame">
      <tr v-for="user in filteredList" v-bind:key="user.id">
        <td>
          <input
            type="checkbox"
            v-bind:id="user.id"
            v-bind:value="user.id"
            v-bind:checked="playerList.includes(user.id)"
            v-on:change="addToPlayerList($event)"
          />
        </td>
        <td>{{ user.username }}</td>
      </tr>

      <button>Add Players</button>
      <button v-on:click="invitePlayersBool = false">Cancel</button>
    </form>

    <router-link
      class="portfolio-link"
      v-if="game.status === 'Active'"
      v-bind:to="{
        name: 'portfolio',
        params: { id: currentInvitations.userGameId },
      }"
      >View Portfolio</router-link
    >
  </div>
</template>

<script>
import GameService from "../services/GameService.js";

export default {
  data() {
    return {
      game: {},
      currentDate: new Date(),
      startDateObj: {},
      invitePlayersBool: false,
      userList: [],
      playerList: [],
      invitedPlayers: [],
      invitationList: [],
      cashLeaders: [],
      userGamesList: [],
    };
  },
  computed: {
    filteredList() {
      const realList = [];
      this.userList.forEach((user) => {
        this.uninvitedPlayerList.forEach((uip) => {
          if (user.username === uip) {
            realList.push(user);
          }
        });
      });
      return realList;
    },
    uninvitedPlayerList() {
      return this.usernameList.filter(
        (username) => !this.invitePlayersUsernames.includes(username)
      );
    },
    usernameList() {
      const theList = [];
      this.userList.forEach((user) => theList.push(user.username));
      return theList;
    },
    invitePlayersUsernames() {
      const aList = [];
      this.invitedPlayers.forEach((player) => aList.push(player.username));
      return aList;
    },
    currentInvitations() {
      return this.invitationList.find((response) => {
        return response.gameId == this.$route.params.id;
      });
    },
    gameOrganizer() {
      return this.userList.find((response) => {
        return response.id == this.game.organizerId;
      });
    },
  },

  created() {
    GameService.getGame(this.$route.params.id).then((response) => {
      this.game = response.data;
      this.startDateObj = new Date(this.game.startDate);
    }),
      GameService.getUsers().then((response) => {
        this.userList = response.data;
      }),
      GameService.getPlayers(this.$route.params.id).then((response) => {
        this.invitedPlayers = response.data;
      }),
      GameService.getInvitations(this.$store.state.user.id).then((response) => {
        this.invitationList = response.data;
      }),
      GameService.getCashLeader(this.$route.params.id).then((response) => {
        this.cashLeaders = response.data;
        this.cashLeaders.sort((a, b) =>
          a.cashBalance < b.cashBalance ? 1 : -1
        );
      });
  },
  methods: {
    invitePlayers() {
      this.invitePlayersBool = true;
    },
    addToPlayerList(event) {
      if (event.target.checked) {
        this.playerList.push(parseInt(event.target.id));
      } else {
        this.playerList = this.playerList.filter((player) => {
          return player !== parseInt(event.target.id);
        });
      }
    },
    addPlayersToGame() {
      this.playerList.forEach((playerId) => {
        let player = {
          gameId: this.$route.params.id,
          userId: playerId,
        };
        GameService.addPlayer(this.$route.params.id, player).then(
          (response) => {
            if (response.status === 201) {
              console.log("Created");
            }
          }
        );
      });
      this.playerList = [];
      this.$router.go();
    },
  },
};
</script>

<style scoped>
@import url("https://css.gg/user.css");

div {
  display: flex;
  flex-direction: column;
  text-align: center;
  justify-items: space-evenly;
}
div > table {
  align-items: space-evenly;
}

.portfolio-link {
  align-self: center;
  text-decoration: none;
  color: black;
  background-color: rgb(175, 191, 199);
  border-radius: 4px;
  padding: 10px;
  width: max-content;
  margin-top: 20px;
}

.portfolio-link:hover {
  background-color: rgb(119, 142, 168);
}

form {
  align-self: center;
  margin: 10px;
  text-align: left;
}
img {
  padding: 0px;
  height: 15px;
  width: 15px;
  color: rgb(175, 191, 199);
}
.final-results {
  align-self: center;
  text-align: center;
  background-color:#532385;
  border-spacing: 15px;
  border-collapse: separate;
  padding: 10px;
  
}

</style>