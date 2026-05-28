<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <span class="navbar-brand">Auth App</span>
      <div class="navbar-nav ms-auto">
        <router-link v-if="route.path === '/login'" to="/register" class="nav-link">Register</router-link>
        <router-link v-if="route.path === '/register'" to="/login" class="nav-link">Login</router-link>
        <a v-if="route.path === '/home'" href="#" class="nav-link" @click.prevent="logout">Logout</a>
      </div>
    </div>
  </nav>
  <router-view />
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

function logout() {
  localStorage.removeItem('currentUser')
  router.push('/login')
}
</script>
