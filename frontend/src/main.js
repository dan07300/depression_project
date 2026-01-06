import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

// --- 【新增 1】引入分页组件 ---
import Pagination from '@/components/Pagination'

Vue.use(ElementUI)

// --- 【新增 2】全局注册分页组件 ---
Vue.component('Pagination', Pagination)

Vue.config.productionTip = false

new Vue({
    el: '#app',
    router,
    store,
    render: h => h(App)
})