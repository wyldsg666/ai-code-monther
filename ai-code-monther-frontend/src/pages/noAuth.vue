<template>
  <a-result status="403" title="403" sub-title="抱歉，您没有权限访问此页面，请联系系统管理员!">
    <template #extra>
      <a-button type="primary" @click="goHome">回到首页</a-button>
    </template>
  </a-result>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>
