export interface UserResponse {
  name: string
  email: string
}

interface ApiError {
  error: string
}

async function post<T>(path: string, body: unknown): Promise<T> {
  const res = await fetch(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  const data = await res.json()
  if (!res.ok) {
    throw new Error((data as ApiError).error)
  }
  return data as T
}

export const authApi = {
  register(name: string, email: string, password: string) {
    return post<UserResponse>('/api/auth/register', { name, email, password })
  },
  login(email: string, password: string) {
    return post<UserResponse>('/api/auth/login', { email, password })
  },
}
