<template>
  <div id="userRegisterPage">
    <h2 class="register-title">AI 代码生成平台 - 用户注册</h2>
    <div class="desc">不用写一行代码，就可以生成符合要求的应用！</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item
        name="userAccount"
        :rules="[
          { required: true, message: '用户名不能为空！' },
          { validator: validateUserAccount },
        ]"
      >
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

      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '确认密码不能为空！' },
          { min: 8, message: '密码长度不能小于8位' },
          { validator: validateCheckPassword },
        ]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请再次输入密码" />
      </a-form-item>

      <div class="tips">已有账号？ <RouterLink to="/user/login">去登录</RouterLink></div>

      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { register } from '@/api/userController'
import { reactive } from 'vue'
import router from '@/router'
import { message } from 'ant-design-vue'

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const validateUserAccount = async (_rule: any, value: string) => {
  if (!value) {
    return Promise.resolve()
  }
  const regex = /^[a-zA-Z0-9!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?`~]+$/
  if (!regex.test(value)) {
    return Promise.reject('用户名只能包含字母、数字和符号！')
  }
  return Promise.resolve()
}

const validateCheckPassword = async (_rule: any, value: string) => {
  if (value !== formState.userPassword) {
    return Promise.reject('两次输入的密码不一致！')
  }
  return Promise.resolve()
}

const handleSubmit = async (values: any) => {
  const result = await register(values)
  if (result.data.code === 0 && result.data.data) {
    message.success('注册成功！')
    router.push({
      path: '/user/login',
      replace: true,
    })
    return false
  } else {
    message.error('注册失败！' + result.data.message)
  }
}
</script>

<style scoped>
#userRegisterPage {
  max-width: 480px;
  margin: 0 auto;
}

.register-title {
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
