<template>

  <div id="app">
    <head>
      <title>Stock Market Game </title>
    </head>
    
    <div id="nav">

      <router-link class="nav-links" v-bind:to="{ name: 'home' }"
        >Home</router-link
      >&nbsp;|&nbsp;
      <router-link class="nav-links" v-bind:to="{ name: 'newGame' }"
        >Create Game</router-link
      >&nbsp;|&nbsp;
      <router-link class="nav-links" v-bind:to="{ name: 'games' }"
        >My Games</router-link
      >&nbsp;|&nbsp;
      <router-link class="nav-links" v-bind:to="{ name: 'invitations' }"
        >Invitations</router-link
      >
      <span class="right-links">
      <span>Logged in as: {{ CurrentUser.username }}</span>
      &nbsp;|&nbsp;

      <router-link
        class="nav-links logout"
        v-bind:to="{ name: 'logout' }"
        v-if="$store.state.token != ''"
        >Logout</router-link
      >
      </span>
    </div>
          <img src="./assets/img1.jpg" />

    <router-view />
    
    <div class="footer">
      <p>
        $router Game by AAMS
      </p>
    </div>
  </div>
  <!-- purple: 532385-->
</template>

<script>
export default {
  data() {
    return {
      CurrentUser: this.$store.state.user,
    };
  },
};
</script>

<style>


.page-title {
  text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    color: rgb(222, 231, 233);
}

.main {
  display: flex;
  flex-direction: column;
  
}
body {
  color: rgb(222, 231, 233);
  background-color: rgb(36, 87, 134);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}

#nav {
  position: relative;
}

.nav-links {
  color: rgb(222, 231, 233);
  text-decoration: none;
}

.nav-links:hover {
  color: rgb(119, 142, 168);
}

.right-links {
  position: absolute;
  right: 0;
  color: rgb(222, 231, 233);
  text-decoration: none;
}
.logout:hover {
  color: rgb(119, 142, 168); 
 }

.footer {
  position: fixed;
  bottom: 0;
  font-size: 13px; 
  background-color: rgb(119, 142, 168);
  width: 100%;
  padding-left: 7px;
}

img {
  width: 100%;
  height: 50px;
object-fit: cover;
padding-top: 30px;
}

button {
  margin-top: 10px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}
</style>
