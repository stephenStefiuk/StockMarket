<template>
  <div>
    <router-link
      class="gameLink"
      v-bind:to="{ name: 'gameDetails', params: { id: game.gameId } }"
    >
      <div
        class="gameCard"
        v-bind:class="{ inactive: game.status == 'Inactive' }"
        v-bind:key="game.id"
      >
        <strong>{{ game.gameName }}</strong>
        <br />
        Start Date: {{ game.startDate }}
        <!--pending or in future-->
        <br />
        End Date: {{ game.endDate }}
        <!--all but past--->
        <br />
        Organizer: {{ gameOrganizer.username }}
        <!--username: only pending v-if="currentInvite.inviteStatus == 'Pending'" --->
        <br />
        <button
          v-on:click.prevent="goToInvitations"
          v-if="findGameInvite.status == 'Pending'"
        >
          Respond
        </button>
      </div>
    </router-link>
  </div>
</template>

<script>
import GameService from "../services/GameService.js";
export default {
  name: "game-card",
  data() {
    return {
      invitationsList: [],
      currentInvite: {},
      userList: [],
    };
  },
  props: {
    game: Object,
  },
  created() {
    GameService.getInvitations(this.$store.state.user.id).then((response) => {
      this.invitationsList = response.data;
    }),
      GameService.getUsers().then((response) => {
        this.userList = response.data;
      });
  },

  computed: {
    findGameInvite() {
      return this.invitationsList.find((invite) => {
        return invite.gameId == this.game.gameId;
      });
    },
    gameOrganizer() {
      return this.userList.find((response) => {
        return response.id == this.game.organizerId;
      });
    },
  },
  methods: {
    goToInvitations() {
      this.$router.push({ name: "invitations" });
    },
  },
};
</script>

<style scoped>
/* background color for active, future, pending, completed */

.gameLink {
  text-decoration: none;
  color: rgba(222, 231, 233, 0.89);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}
</style>