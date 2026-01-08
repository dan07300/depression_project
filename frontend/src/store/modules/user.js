import { login, getUserById } from '@/api'
import { getToken, setToken, removeToken, getUser, setUser, removeUser } from '@/utils/auth'

const state = {
  token: getToken(),
  name: '', // 用于右上角显示的用户名
  avatar: '',
  roles: [],
  userInfo: getUser() || {} // 兜底为空对象，避免undefined
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name // 给name字段赋值（关键）
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo || {} // 兜底为空对象
    // 同步更新name字段（确保右上角能读到）
    if (userInfo && userInfo.realName) {
      state.name = userInfo.realName
    }
  },
  UPDATE_USER_INFO: (state, userInfo) => {
    state.userInfo = { ...state.userInfo, ...userInfo }
    // 更新userInfo时同步更新name
    if (userInfo.realName) {
      state.name = userInfo.realName
    }
  },
  RESET_USER_INFO: (state) => {
    state.userInfo = {}
    state.name = '' // 清空name
    state.avatar = ''
    state.roles = []
  }
}

const actions = {
  // 登录逻辑
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        if (!data || !data.token) {
          reject('登录失败：未获取到token')
          return
        }
        // 存储token
        commit('SET_TOKEN', data.token)
        // 存储用户信息（确保realName存在）
        commit('SET_USER_INFO', data.userInfo || {})
        // 持久化存储
        setToken(data.token)
        setUser(data.userInfo || {})
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取用户信息逻辑
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      // 校验userInfo.id
      if (!state.userInfo || !state.userInfo.id) {
        reject('用户信息不存在：缺少用户ID')
        return
      }
      getUserById(state.userInfo.id).then(response => {
        const { data } = response
        if (!data) {
          reject('获取用户信息失败：接口返回空')
          return
        }
        // 更新用户信息+同步name
        commit('SET_USER_INFO', data)
        // 持久化最新用户信息
        setUser(data)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 重置token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('RESET_USER_INFO') // 改用重置所有用户信息
      removeToken()
      removeUser()
      resolve()
    })
  },

  // 退出登录
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('RESET_USER_INFO') // 清空所有用户信息
      removeToken()
      removeUser()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}