<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="queryParam" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="queryParam.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="queryParam.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
      :row-class-name="rowClassName"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { deleteUser, listUserVoByPage } from '@/api/userController'
import { onMounted, reactive, ref, computed } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

// 展示的数据
const data = ref<API.UserVO[]>([])
const totalRows = ref(0)

// 定义查询参数
const queryParam = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 5,
})

const fetchData = async () => {
  const res = await listUserVoByPage({
    ...queryParam,
  })
  if (res.data.data) {
    data.value = res.data.data.records || []
    totalRows.value = res.data.data.totalRow || 0
  } else {
    message.error('获取用户列表失败' + res.data.message)
  }
}

// 分页参数
const pagination = computed(() => {
  return {
    current: queryParam.pageNum ?? 1,
    pageSize: queryParam.pageSize ?? 10,
    total: +totalRows.value,
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
    pageSizeOptions: ['5', '10', '20', '50', '100'],
    showQuickJumper: true,
  }
})

// 表格分页变化时
const doTableChange = (page: any) => {
  queryParam.pageNum = page.current
  queryParam.pageSize = page.pageSize
  fetchData()
}

const doSearch = () => {
  queryParam.pageNum = 1
  fetchData()
}

const doDelete = async (id: string) => {
  if (!id) {
    return message.error('请指定要删除的用户')
  }

  const res = await deleteUser({
    id,
  })

  if (res.data.code === 0) {
    message.success('删除成功')
    fetchData()
  } else {
    message.error('删除失败' + res.data.message)
  }
}

const rowClassName = (_record: API.UserVO, index: number) => {
  return index % 2 === 1 ? 'table-striped' : null
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
[data-doc-theme='light'] .ant-table-striped :deep(.table-striped) td {
  background-color: #fafafa;
}
[data-doc-theme='dark'] .ant-table-striped :deep(.table-striped) td {
  background-color: rgb(29, 29, 29);
}
</style>
