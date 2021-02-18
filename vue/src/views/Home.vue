<template>
  <div class="home">
  
    <h1>$router</h1>
    
    <p>Hello {{CurrentUser.username}}, Welcome Home</p>
    <router-link class="homeLinks" v-bind:to="{name : 'newGame'}">Create Game</router-link>
    <router-link class="homeLinks" v-bind:to="{name: 'games'}">View Games</router-link>
    <router-link class="homeLinks" v-bind:to="{name: 'invitations'}">View Invitations</router-link>
    <router-link v-show="role.name == 'ROLE_ADMIN'" class="homeLinks admin" v-bind:to="{name: 'update'}">ADMIN</router-link>

  </div>
</template>

<script>
export default {
  name: "home",
  data() {
    return {
      CurrentUser: this.$store.state.user,
      CurrentUserAuthorities: this.$store.state.user.authorities
    }
  },
  computed: {
    role() {
      return this.CurrentUserAuthorities.find(object =>
      {
       return object.name;
      })
    }
  }
};
</script>

<style scoped>
  .home {
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    color: rgb(222, 231, 233);
  }
  
  .homeLinks {
    text-decoration: none;
    color: black;
    background-color: rgb(175, 191, 199);
    border-radius: 4px;
    padding: 2rem;
    margin: 2rem;
    width: 125px;
  }
  .admin {
    margin-bottom: 100px;
  }

  .homeLinks:hover {
    background-color: rgb(119, 142, 168);
  }


h1 {
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;
background-image: url("../assets/img3.jpg");
font-size: 100px;
margin: 20px;
} 

</style>
