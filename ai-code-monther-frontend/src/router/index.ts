import { createRouter, createWebHistory } from 'vue-router'
import HomePage from '../pages/HomePage.vue'
import ACCESS_ENUM from '@/access/accessEnum'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/noAuth',
      name: 'noAuth',
      component: () => import('@/pages/noAuth.vue'),
      meta: {
        access: ACCESS_ENUM.USER,
      },
    },
    {
      path: '/user/login',
      name: 'userLogin',
      component: () => import('@/pages/user/userLoginPage.vue'),
    },
    {
      path: '/user/register',
      name: 'userRegister',
      component: () => import('@/pages/user/userRegisterPage.vue'),
    },
    {
      path: '/user/changeSelfInfo',
      name: 'userChangeSelfInfo',
      component: () => import('@/pages/user/userChangeSelfInfo.vue'),
    },
    {
      path: '/user/changePassword',
      name: 'userChangePassword',
      component: () => import('@/pages/user/userChangePasswordPage.vue'),
    },
    {
      path: '/admin/userManage',
      name: 'adminUserManage',
      component: () => import('@/pages/admin/userManagePage.vue'),
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
  ],
})

export default router
