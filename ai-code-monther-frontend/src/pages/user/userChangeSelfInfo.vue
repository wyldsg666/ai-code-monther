<template>
  <div id="userChangeSelfInfo">
    <h2 class="profile-title">个人信息</h2>
    <a-form
      :model="formState"
      name="basic"
      autocomplete="off"
      @finish="handleSubmit"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 19 }"
      label-align="right"
    >
      <a-form-item
        label="昵称"
        name="userName"
        :rules="[{ required: true, message: '昵称不能为空！' }]"
      >
        <a-input v-model:value="formState.userName" placeholder="请输入昵称" />
      </a-form-item>

      <a-form-item label="头像" name="userAvatar">
        <a-image
          v-if="formState.userAvatar"
          :src="formState.userAvatar"
          style="width: 80px; height: 80px"
        />
        <a-input v-else v-model:value="formState.userAvatar" placeholder="请输入头像URL" />
      </a-form-item>

      <a-form-item label="个人简介" name="userProfile">
        <a-textarea v-model:value="formState.userProfile" placeholder="请输入个人简介" :rows="4" />
      </a-form-item>

      <a-form-item :wrapper-col="{ span: 19, offset: 5 }">
        <a-button type="primary" html-type="submit" style="width: 100%">保存</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { getLoginUser, updateMyUser } from '@/api/userController'
import { reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const formState = reactive<API.UserUpdateRequest & { userAccount?: string }>({
  id: undefined,
  userName: '',
  userAvatar: '',
  userProfile: '',
  userAccount: '',
  userRole: '',
})

const fetchUserInfo = async () => {
  const result = await getLoginUser()
  if (result.data.code === 0 && result.data.data) {
    const userInfo = result.data.data
    formState.id = userInfo.id || undefined
    formState.userName = userInfo.userName || ''
    formState.userAvatar = userInfo.userAvatar || ''
    formState.userProfile = userInfo.userProfile || ''
    formState.userAccount = userInfo.userAccount || ''
    formState.userRole = userInfo.userRole || ''
  } else {
    message.error('获取用户信息失败！' + result.data.message)
  }
}

const handleSubmit = async (values: any) => {
  if (!formState.id || formState.id === undefined) {
    message.error('请先登录！')
    router.push('/user/login')
    return false
  }
  values.id = formState.id
  values.userRole = formState.userRole
  const result = await updateMyUser(values)
  if (result.data.code === 0 && result.data.data) {
    message.success('修改成功！')
    await fetchUserInfo()
  } else {
    message.error('修改失败！' + result.data.message)
  }
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
#userChangeSelfInfo {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.profile-title {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 32px;
}
</style>
