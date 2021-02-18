<template>
  <div>
    <h2 class="page-title">Invitations</h2>
    
    <h3 v-if="!hasInvites">There are no pending invitations</h3>
    <div v-if="hasInvites">
      <table class="pending-invitations">
        <thead class="table-head">
          <th>Game Name</th>
          <th>Invitation Status</th>
        </thead>
        <tr
          v-for="invitation in pendingInvitationsList"
          v-bind:key="invitation.id"
          class="table-row"
        >
          <td>{{ invitation.gameName }}</td>
          <td>{{ invitation.status }}</td>

          <td>
            <button v-on:click.prevent="userAccept(invitation)">Accept</button>
            <button v-on:click.prevent="userDecline(invitation)">
              Decline
            </button>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import GameService from "../services/GameService.js";
export default {
  data() {
    return {
      invitationsList: [],
      gamesList: [],
      hasInvites: false
    };
  },
  created() {
    GameService.getInvitations(this.$store.state.user.id).then((response) => {
      this.invitationsList = response.data;
      console.log(this.pendingInvitationsList)
      if(this.pendingInvitationsList.length!==0) {
        this.hasInvites = true;
      }
    }),
      GameService.getAll().then((response) => {
        this.gamesList = response.data;
      });
  },
  computed: {
    pendingInvitationsList() {
      return this.invitationsList.filter((invitation) => {
        return invitation.status == "Pending";
      });
    },
    gameInfo(id) {
      return this.gamesList.find((game) => {
        return game.gameId == id;
      });
    },
  },

  methods: {
    //     getGames() {
    //         this.invitationsList.forEach(
    //         (invitation) => {
    //         GameService.getGame(invitation.gameId).then((response) => {
    //       this.gameList = response.data;
    //   })
    // })
    //     }

    userAccept(invitation) {
      const updatedInvitation = {
        userGameId: invitation.userGameId,
        userId: invitation.userId,
        gameId: invitation.gameId,
        status: "Accepted",
      };
      const newPortfolio = {
        userGameId: invitation.userGameId,
      };
      GameService.updateInviteStatus(
        invitation.userGameId,
        updatedInvitation
      ).then((response) => {
        if (response.status === 200) {
          GameService.createPortfolio(invitation.userGameId, newPortfolio).then(
            (response) => {
              if (response.status === 201) {
                this.$router.go();
              }
            }
          );
        }
      });
    },
    userDecline(invitation) {
      const updatedInvitation = {
        userGameId: invitation.userGameId,
        userId: invitation.userId,
        gameId: invitation.gameId,
        status: "Declined",
      };
      GameService.updateInviteStatus(
        invitation.userGameId,
        updatedInvitation
      ).then((response) => {
        if (response.status === 200) {
          this.$router.go();
        }
      });
    },
  },
};
</script>

<style scoped>
div {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  text-align: center;
}
table {
  align-self: center;
  text-align: center;
  border-spacing: 20px;
  border-collapse: separate;
  padding: 10px;
}

.table-head {
  flex: 1;
}

.table-row {
  flex: 1;
}
button {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
  text-decoration: none;
  color: rgb(222, 231, 233);
  background-color: rgb(119, 142, 168);
  border-radius: 4px;
  padding: 4px;
  width: 60px;
  margin: 3px;
}

button:hover {
  color: #248740;
  background-color: rgb(231, 241, 247);
}
</style>