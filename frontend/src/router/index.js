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

    // --- 修复重点：下面这些路由，我都删掉了父级的 name 和 meta ---

    {
        path: '/hospital',
        component: Layout,
        redirect: '/hospital/list',
        // 注意：这里删除了 name 和 meta，只保留 children
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
        children: [
            {
                path: 'list',
                name: 'HospitalAdminList',
                component: () => import('@/views/hospital-admin/index'),
                meta: { title: '医院管理员', icon: 'el-icon-user' }
            }
        ]
    },
    {
        path: '/doctor',
        component: Layout,
        redirect: '/doctor/list',
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
        children: [
            {
                path: 'list',
                name: 'UserList',
                component: () => import('@/views/user/index'),
                meta: { title: '所有用户', icon: 'el-icon-user' }
            }
        ]
    },
    //AI智能助手
{
    path: '/ai',
    component: Layout,
    redirect: '/ai/index',
    children: [
      {
        path: 'index',
        name: 'ai',
        component: () => import('@/views/ai/index'), // 对应你的view组件路径
        meta: { title: 'AI智能助手', icon: 'el-icon-s-opportunity' } // 菜单名称+图标
      }
    ]
  },
    // --- 修复结束 ---

    {
        path: '/profile',
        component: Layout,
        redirect: '/profile/index',
        children: [
            {
                path: 'index',
                name: 'Profile',
                component: () => import('@/views/profile/index'),
                meta: { title: '个人信息', icon: 'el-icon-user-solid' }
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