const state = {
  title: '心理健康平台'
}

const mutations = {
  SET_TITLE: (state, title) => {
    state.title = title
  }
}

const actions = {
  changeSetting({ commit }, data) {
    commit('SET_TITLE', data.title)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
