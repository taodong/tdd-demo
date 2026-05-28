<template>
  <div class="container mt-5">
    <h2>Welcome {{ currentUser?.name }}</h2>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import type { UserResponse } from '../services/api'

const router = useRouter()
const currentUser = ref<UserResponse | null>(null)

onMounted(() => {
  const stored = localStorage.getItem('currentUser')
  if (!stored) {
    router.push('/login')
    return
  }
  currentUser.value = JSON.parse(stored) as UserResponse
})
</script>
