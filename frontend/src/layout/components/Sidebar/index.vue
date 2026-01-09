<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters([
      'sidebar',
      'userInfo'
    ]),
    routes() {
      const allRoutes = this.$router.options.routes
      const roleType = this.userInfo?.roleType
      
      // 根据角色类型过滤路由
      return this.filterRoutesByRole(allRoutes, roleType)
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return true
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  methods: {
    filterRoutesByRole(routes, roleType) {
      if (!roleType) {
        return routes
      }
      
      return routes.filter(route => {
        // 登录页、404页、个人信息页始终显示
        if (route.path === '/login' || route.path === '/404' || route.path === '/profile') {
          return true
        }
        
        // 首页始终显示
        if (route.path === '/' || (route.children && route.children.some(child => child.path === 'dashboard'))) {
          return true
        }
        
        // 检查子路由
        if (route.children && route.children.length > 0) {
          const filteredChildren = route.children.filter(child => {
            // 检查子路由的meta中是否有roles配置
            if (child.meta && child.meta.roles) {
              return child.meta.roles.includes(roleType)
            }
            // 如果没有roles配置，默认显示（向后兼容）
            return true
          })
          
          if (filteredChildren.length > 0) {
            // 创建新的路由对象，避免修改原对象
            const newRoute = { ...route }
            newRoute.children = filteredChildren
            return true
          }
          return false
        }
        
        // 检查路由本身的meta中是否有roles配置
        if (route.meta && route.meta.roles) {
          return route.meta.roles.includes(roleType)
        }
        
        // 如果没有roles配置，系统管理员显示所有，其他角色不显示
        return roleType === 1
      }).map(route => {
        // 如果路由有子路由且被过滤过，需要返回新的路由对象
        if (route.children && route.children.length > 0) {
          const filteredChildren = route.children.filter(child => {
            if (child.meta && child.meta.roles) {
              return child.meta.roles.includes(roleType)
            }
            return true
          }).map(child => {
            // 根据角色类型动态设置标题
            if (child.meta && child.meta.titleMap && child.meta.titleMap[roleType]) {
              return {
                ...child,
                meta: {
                  ...child.meta,
                  title: child.meta.titleMap[roleType]
                }
              }
            }
            return child
          })
          if (filteredChildren.length > 0) {
            return { ...route, children: filteredChildren }
          }
        }
        return route
      })
    }
  }
}
</script>
