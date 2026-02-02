<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.png" alt="Logo" />
            <h1 class="site-title">AI 代码生成平台</h1>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                <span>{{ loginUserStore.loginUser.userName ?? '新用户' }}</span>
                <DownOutlined />
              </a-space>

              <template #overlay>
                <a-menu>
                  <a-sub-menu key="group" title="个人中心">
                    <a-menu-item key="profile" @click="goProfile">
                      <UserOutlined />
                      个人信息
                    </a-menu-item>
                    <a-menu-item key="changePassword" @click="goChangePassword">
                      <KeyOutlined />
                      修改密码
                    </a-menu-item>
                  </a-sub-menu>
                  <a-menu-item key="logout" @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import type { MenuProps } from 'ant-design-vue'
import { message } from 'ant-design-vue'
// 引入登录用户信息存储
import { useLoginUserStore } from '@/stores/loginUser'
// 引入图标
import { LogoutOutlined, UserOutlined, DownOutlined, KeyOutlined } from '@ant-design/icons-vue'
import { userLogout } from '@/api/userController'

const loginUserStore = useLoginUserStore()

const router = useRouter()
// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

// 菜单配置项
const originItems = [
  {
    key: '/',
    label: '首页',
    title: '首页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  // {
  //   key: 'others',
  //   label: h('a', { href: 'https://www.codefather.cn', target: '_blank' }, '编程导航'),
  //   title: '编程导航',
  // },
]

const filterItems = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    if (menuKey.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        // 非管理员角色，不展示用户管理菜单
        return false
      }
    }
    // 管理员角色，展示用户管理菜单
    return true
  })
}

const menuItems = computed<MenuProps['items']>(() => filterItems(originItems))

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}

// 处理退出登录
const doLogout = async () => {
  const result = await userLogout()
  if (result.data.code === 0) {
    // 清空登录用户信息
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败' + result.data.message)
  }
}

// 处理个人信息点击
const goProfile = () => {
  router.push('/user/changeSelfInfo')
}

// 处理修改密码点击
const goChangePassword = () => {
  router.push('/user/changePassword')
}
</script>

<style scoped>
.header {
  background: #fff;
  padding: 0 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  height: 48px;
  width: 48px;
}

.site-title {
  margin: 0;
  font-size: 19px;
  color: #1890ff;
  font-weight: 700;
}

.ant-menu-horizontal {
  border-bottom: none !important;
}
</style>
