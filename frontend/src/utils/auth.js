import Cookies from 'js-cookie'

const TokenKey = 'depression-token'
const UserKey = 'depression-user'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getUser() {
  const user = Cookies.get(UserKey)
  return user ? JSON.parse(user) : null
}

export function setUser(user) {
  return Cookies.set(UserKey, JSON.stringify(user))
}

export function removeUser() {
  return Cookies.remove(UserKey)
}
