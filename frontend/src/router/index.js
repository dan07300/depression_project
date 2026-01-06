import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'el-icon-s-home' }
    }]
  },
  {
    path: '/hospital',
    component: Layout,
    redirect: '/hospital/list',
    name: 'Hospital',
    meta: { title: '医院管理', icon: 'el-icon-office-building' },
    children: [
      {
        path: 'list',
        name: 'HospitalList',
        component: () => import('@/views/hospital/index'),
        meta: { title: '医院管理', icon: 'el-icon-office-building' }
      }
    ]
  },
  {
    path: '/hospital-admin',
    component: Layout,
    redirect: '/hospital-admin/list',
    name: 'HospitalAdmin',
    meta: { title: '医院管理员', icon: 'el-icon-user' },
    children: [
      {
        path: 'list',
        name: 'HospitalAdminList',
        component: () => import('@/views/hospital-admin/index'),
        meta: { title: '医院管理员管理', icon: 'el-icon-user' }
      }
    ]
  },
  {
    path: '/doctor',
    component: Layout,
    redirect: '/doctor/list',
    name: 'Doctor',
    meta: { title: '所有医生', icon: 'el-icon-user-solid' },
    children: [
      {
        path: 'list',
        name: 'DoctorList',
        component: () => import('@/views/doctor/index'),
        meta: { title: '所有医生', icon: 'el-icon-user-solid' }
      }
    ]
  },
  {
    path: '/patient',
    component: Layout,
    redirect: '/patient/list',
    name: 'Patient',
    meta: { title: '所有病人', icon: 'el-icon-user' },
    children: [
      {
        path: 'list',
        name: 'PatientList',
        component: () => import('@/views/patient/index'),
        meta: { title: '所有病人', icon: 'el-icon-user' }
      }
    ]
  },
  {
    path: '/warning',
    component: Layout,
    redirect: '/warning/list',
    name: 'Warning',
    meta: { title: '预警管理', icon: 'el-icon-warning' },
    children: [
      {
        path: 'list',
        name: 'WarningList',
        component: () => import('@/views/warning/index'),
        meta: { title: '预警管理', icon: 'el-icon-warning' }
      }
    ]
  },
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/tree',
    name: 'Permission',
    meta: { title: '权限管理', icon: 'el-icon-lock' },
    children: [
      {
        path: 'tree',
        name: 'PermissionTree',
        component: () => import('@/views/permission/index'),
        meta: { title: '权限管理', icon: 'el-icon-lock' }
      }
    ]
  },
  {
    path: '/scale',
    component: Layout,
    redirect: '/scale/question',
    name: 'Scale',
    meta: { title: '量表管理', icon: 'el-icon-document' },
    children: [
      {
        path: 'question',
        name: 'ScaleQuestion',
        component: () => import('@/views/scale/index'),
        meta: { title: '量表管理', icon: 'el-icon-document' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/list',
    name: 'User',
    meta: { title: '所有用户', icon: 'el-icon-user' },
    children: [
      {
        path: 'list',
        name: 'UserList',
        component: () => import('@/views/user/index'),
        meta: { title: '所有用户管理', icon: 'el-icon-user' }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'Profile',
        component: () => import('@/views/profile/index'),
        meta: { title: '个人信息', icon: 'el-icon-setting' }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
