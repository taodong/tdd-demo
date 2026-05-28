<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <h2 class="mb-4">Login</h2>

        <div v-if="errors.length" class="alert alert-danger">
          <div v-for="err in errors" :key="err">{{ err }}</div>
        </div>

        <form @submit.prevent="submit">
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input v-model="form.email" type="text" class="form-control" @input="clearErrors" />
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input v-model="form.password" type="password" class="form-control" @input="clearErrors" />
          </div>
          <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../services/api'

const router = useRouter()

const form = reactive({ email: '', password: '' })
const errors = ref<string[]>([])

function clearErrors() {
  errors.value = []
}

async function submit() {
  const missing: string[] = []
  if (!form.email.trim()) missing.push('Email')
  if (!form.password) missing.push('Password')
  if (missing.length) {
    errors.value = [`${missing.join(', ')} ${missing.length === 1 ? 'is' : 'are'} required`]
    return
  }

  try {
    const user = await authApi.login(form.email.trim(), form.password)
    localStorage.setItem('currentUser', JSON.stringify(user))
    router.push('/home')
  } catch (e) {
    errors.value = [(e as Error).message]
  }
}
</script>
