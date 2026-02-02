<template>
  <div id="userLoginPage">
    <h2 class="login-title">AI 代码生成平台 - 用户登录</h2>
    <div class="desc">不用写一行代码，就可以生成符合要求的应用！</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '用户名不能为空！' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入用户名" />
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '密码不能为空！' },
          { min: 8, message: '密码长度不能小于8位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>

      <div class="tips">没有账号？ <RouterLink to="/user/register">去注册</RouterLink></div>

      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { userLogin } from '@/api/userController'
import { reactive } from 'vue'
import router from '@/router'
// 引入登录用户信息存储
import { useLoginUserStore } from '@/stores/loginUser'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

/**
 * 登录提交处理函数
 * @param values 登录表单数据
 */
const loginUserStore = useLoginUserStore()
const handleSubmit = async (values: any) => {
  const result = await userLogin(values)
  if (result.data.code === 0 && result.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功！')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败！' + result.data.message)
  }
}
</script>

<style scoped>
#userLoginPage {
  max-width: 480px;
  margin: 0 auto;
}

.login-title {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}

.tips {
  text-align: right;
  font-size: 14px;
  color: #666;
  margin-bottom: 16px;
}
</style>
