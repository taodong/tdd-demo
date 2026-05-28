<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <h2 class="mb-4">Register Account</h2>

        <div v-if="errors.length" class="alert alert-danger">
          <div v-for="err in errors" :key="err">{{ err }}</div>
        </div>

        <form @submit.prevent="submit">
          <div class="mb-3">
            <label class="form-label">Name</label>
            <input v-model="form.name" type="text" class="form-control" @input="clearErrors" />
          </div>
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input v-model="form.email" type="text" class="form-control" @input="clearErrors" />
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input v-model="form.password" type="password" class="form-control" @input="clearErrors" />
          </div>
          <div class="mb-3">
            <label class="form-label">Confirm Password</label>
            <input v-model="form.confirmPassword" type="password" class="form-control" @input="clearErrors" />
          </div>
          <button type="submit" class="btn btn-primary w-100">Register</button>
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

const form = reactive({ name: '', email: '', password: '', confirmPassword: '' })
const errors = ref<string[]>([])

function clearErrors() {
  errors.value = []
}

async function submit() {
  const errs: string[] = []

  const missing: string[] = []
  if (!form.name.trim()) missing.push('Name')
  if (!form.email.trim()) missing.push('Email')
  if (!form.password) missing.push('Password')
  if (!form.confirmPassword) missing.push('Confirm Password')
  if (missing.length) {
    errs.push(`${missing.join(', ')} ${missing.length === 1 ? 'is' : 'are'} required`)
  }

  if (form.password && form.password.length < 8) {
    errs.push('Password requires at least 8 characters')
  }

  if (form.password && form.confirmPassword && form.password !== form.confirmPassword) {
    errs.push("Password fields don't match")
  }

  if (errs.length) {
    errors.value = errs
    return
  }

  try {
    const user = await authApi.register(form.name.trim(), form.email.trim(), form.password)
    localStorage.setItem('currentUser', JSON.stringify(user))
    router.push('/home')
  } catch (e) {
    errors.value = [(e as Error).message]
  }
}
</script>
