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
        meta: { roles: [1] }, // 仅系统管理员可见
        children: [
            {
                path: 'list',
                name: 'HospitalList',
                component: () => import('@/views/hospital/index'),
                meta: { title: '医院管理', icon: 'el-icon-office-building', roles: [1] }
            }
        ]
    },
    {
        path: '/hospital-admin',
        component: Layout,
        redirect: '/hospital-admin/list',
        meta: { roles: [1] }, // 仅系统管理员可见
        children: [
            {
                path: 'list',
                name: 'HospitalAdminList',
                component: () => import('@/views/hospital-admin/index'),
                meta: { title: '医院管理员', icon: 'el-icon-user', roles: [1] }
            }
        ]
    },
    // 医生管理 - 系统管理员显示"所有医生"，医院管理员显示"医生管理"
    {
        path: '/doctor',
        component: Layout,
        redirect: '/doctor/list',
        meta: { roles: [1, 2] }, // 系统管理员和医院管理员可见
        children: [
            {
                path: 'list',
                name: 'DoctorList',
                component: () => import('@/views/doctor/index'),
                meta: { 
                    title: '医生管理', // 默认标题，会根据角色动态显示
                    icon: 'el-icon-user-solid', 
                    roles: [1, 2],
                    titleMap: {
                        1: '所有医生', // 系统管理员
                        2: '医生管理'  // 医院管理员
                    }
                }
            }
        ]
    },
    // 患者/病人管理 - 不同角色显示不同标题
    {
        path: '/patient',
        component: Layout,
        redirect: '/patient/list',
        meta: { roles: [1, 2, 3] },
        children: [
            {
                path: 'list',
                name: 'PatientList',
                component: () => import('@/views/patient/index'),
                meta: { 
                    title: '患者列表', // 默认标题，会根据角色动态显示
                    icon: 'el-icon-user', 
                    roles: [1, 2, 3],
                    titleMap: {
                        1: '所有病人', // 系统管理员
                        2: '病人管理',  // 医院管理员
                        3: '患者列表'   // 医生
                    }
                }
            }
        ]
    },
    // 评定管理 - 仅医生可见
    {
        path: '/assessment',
        component: Layout,
        redirect: '/assessment/list',
        meta: { roles: [3] }, // 仅医生可见
        children: [
            {
                path: 'list',
                name: 'AssessmentList',
                component: () => import('@/views/assessment/index'),
                meta: { title: '评定管理', icon: 'el-icon-document', roles: [3] }
            }
        ]
    },
    // 预警管理 - 所有角色可见，但标题不同
    {
        path: '/warning',
        component: Layout,
        redirect: '/warning/list',
        meta: { roles: [1, 2, 3] },
        children: [
            {
                path: 'list',
                name: 'WarningList',
                component: () => import('@/views/warning/index'),
                meta: { 
                    title: '预警处理', // 默认标题，会根据角色动态显示
                    icon: 'el-icon-warning', 
                    roles: [1, 2, 3],
                    titleMap: {
                        1: '预警管理', // 系统管理员
                        2: '预警管理',  // 医院管理员
                        3: '预警处理'   // 医生
                    }
                }
            }
        ]
    },
    {
        path: '/permission',
        component: Layout,
        redirect: '/permission/tree',
        meta: { roles: [1] }, // 仅系统管理员可见
        children: [
            {
                path: 'tree',
                name: 'PermissionTree',
                component: () => import('@/views/permission/index'),
                meta: { title: '权限管理', icon: 'el-icon-lock', roles: [1] }
            }
        ]
    },
    {
        path: '/scale',
        component: Layout,
        redirect: '/scale/question',
        meta: { roles: [1] }, // 仅系统管理员可见
        children: [
            {
                path: 'question',
                name: 'ScaleQuestion',
                component: () => import('@/views/scale/index'),
                meta: { title: '量表管理', icon: 'el-icon-document', roles: [1] }
            }
        ]
    },
    {
        path: '/user',
        component: Layout,
        redirect: '/user/list',
        meta: { roles: [1] }, // 仅系统管理员可见
        children: [
            {
                path: 'list',
                name: 'UserList',
                component: () => import('@/views/user/index'),
                meta: { title: '所有用户', icon: 'el-icon-user', roles: [1] }
            }
        ]
    },
    {
        path: '/statistics',
        component: Layout,
        redirect: '/statistics/index',
        meta: { roles: [2] }, // 仅医院管理员可见
        children: [
            {
                path: 'index',
                name: 'Statistics',
                component: () => import('@/views/statistics/index'),
                meta: { title: '统计分析', icon: 'el-icon-data-analysis', roles: [2] }
            }
        ]
    },
    //AI智能助手
    {
        path: '/ai',
        component: Layout,
        redirect: '/ai/index',
        meta: { roles: [1, 2, 3] }, // 系统管理员、医院管理员、医生可见
        children: [
            {
                path: 'index',
                name: 'ai',
                component: () => import('@/views/ai/index'),
                meta: { title: 'AI智能助手', icon: 'el-icon-robot', roles: [1, 2, 3] }
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