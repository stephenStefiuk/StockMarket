import Vue from 'vue'
import Router from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Logout from '../views/Logout.vue'
import Register from '../views/Register.vue'
import store from '../store/index'
import NewGame from '../views/NewGame.vue'
import ListOfGames from '../views/ListOfGames.vue'
import GameDetails from '../views/GameDetails.vue'
import Invitations from '../views/Invitations.vue'
import MyPortfolio from '../views/MyPortfolio.vue'
import UpdateStocks from '../components/UpdateStocks.vue'

Vue.use(Router)

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false
      }
    },
    {
      path: "/newGame",
      name: "newGame",
      component: NewGame,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/games",
      name: "games",
      component: ListOfGames,
      meta: {
        requiresAuth: true
      }
    },
      {
        path: "/game/:id",
        name: "gameDetails",
        component: GameDetails,
        meta: {
          requiresAuth: true
        }
    },
    {
      path: "/invitations",
      name: "invitations",
      component: Invitations,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: "/game/:id/portfolio",
      name: "portfolio",
      component: MyPortfolio,
      meta: {
        requiresAuth: true
      }
    },
    {
    path: "/updateStocks",
    name: "update",
    component: UpdateStocks,
    meta: {
      requiresAuth: true
    }
    }
  ]
})

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;
