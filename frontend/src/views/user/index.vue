<template>
  <div class="user-container">
    <div class="header">
      <h2>所有用户管理</h2>
    </div>

    <div class="filter-bar">
      <el-select v-model="queryParams.roleType" placeholder="全部角色" clearable style="width: 150px; margin-right: 10px;">
        <el-option label="系统管理员" :value="1" />
        <el-option label="医院管理员" :value="2" />
        <el-option label="医生" :value="3" />
      </el-select>
      <el-select v-model="queryParams.cohortCode" placeholder="全部医院" clearable style="width: 150px; margin-right: 10px;">
        <el-option
          v-for="item in hospitalList"
          :key="item.cohortCode"
          :label="item.cohortName"
          :value="item.cohortCode"
        />
      </el-select>
      <el-select v-model="queryParams.status" placeholder="正常用户" clearable style="width: 150px; margin-right: 10px;">
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-input
        v-model="queryParams.keyword"
        placeholder="用户名/姓名"
        style="width: 200px; margin-right: 10px;"
        clearable
      />
      <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="真实姓名" />
      <el-table-column prop="roleType" label="角色类型" width="150">
        <template slot-scope="scope">
          <el-tag :type="getRoleType(scope.row.roleType)">
            {{ getRoleTypeName(scope.row.roleType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="cohortName" label="所属医院" />
      <el-table-column prop="department" label="科室" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="warning" @click="handleChangeRole(scope.row)">变更身份</el-button>
          <el-button size="mini" type="primary" @click="handlePermission(scope.row)">权限</el-button>
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="fetchData"
    />
  </div>
</template>

<script>
import { getUsers, getAllHospitals } from '@/api'

export default {
  name: 'User',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      hospitalList: [],
      queryParams: {
        current: 1,
        size: 10,
        roleType: null,
        cohortCode: '',
        status: 1,
        keyword: ''
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchHospitals()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const response = await getUsers(this.queryParams)
        this.tableData = response.data.records || []
        this.total = response.data.total || 0
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    async fetchHospitals() {
      try {
        const response = await getAllHospitals()
        this.hospitalList = response.data || []
      } catch (error) {
        console.error('获取医院列表失败', error)
      }
    },
    getRoleType(roleType) {
      const map = {
        1: 'danger',
        2: 'warning',
        3: 'primary'
      }
      return map[roleType] || 'info'
    },
    getRoleTypeName(roleType) {
      const map = {
        1: '系统管理员',
        2: '医院管理员',
        3: '医生'
      }
      return map[roleType] || '未知'
    },
    handleQuery() {
      this.queryParams.current = 1
      this.fetchData()
    },
    handleReset() {
      this.queryParams = {
        current: 1,
        size: 10,
        roleType: null,
        cohortCode: '',
        status: 1,
        keyword: ''
      }
      this.fetchData()
    },
    handleEdit(row) {
      this.$message.info('编辑功能开发中')
    },
    handleChangeRole(row) {
      this.$message.info('变更身份功能开发中')
    },
    handlePermission(row) {
      this.$message.info('权限管理功能开发中')
    }
  }
}
</script>

<style lang="scss" scoped>
.user-container {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .filter-bar {
    margin-bottom: 20px;
  }
}
</style>
