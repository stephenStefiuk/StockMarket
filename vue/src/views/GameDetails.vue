<template>
  <div>
    <game-details />
    <leaderboard />
  </div>
</template>

<script>
import GameDetails from "../components/GameDetails.vue";
import Leaderboard from "../components/Leaderboard.vue";
export default {
  components: 
  { GameDetails, Leaderboard },
};
</script>

<style scoped>
</style>