<template>
  <div class="hospital-admin-container">
    <div class="header">
      <h2>医院管理员管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">添加管理员</el-button>
    </div>

    <div class="filter-bar">
      <el-select v-model="queryParams.cohortCode" placeholder="请选择医院" clearable style="width: 200px; margin-right: 10px;">
        <el-option
          v-for="item in hospitalList"
          :key="item.cohortCode"
          :label="item.cohortName"
          :value="item.cohortCode"
        />
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
      <el-table-column prop="gender" label="性别" width="80">
        <template slot-scope="scope">
          {{ scope.row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="cohortName" label="所属医院" />
      <el-table-column prop="department" label="科室" />
      <el-table-column prop="title" label="职称" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="warning" @click="handlePermission(scope.row)">权限</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属医院" prop="cohortCode">
          <el-select v-model="form.cohortCode" placeholder="请选择医院" style="width: 100%;">
            <el-option
              v-for="item in hospitalList"
              :key="item.cohortCode"
              :label="item.cohortName"
              :value="item.cohortCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="科室" prop="department">
          <el-input v-model="form.department" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="分配权限" :visible.sync="permissionDialogVisible" width="600px">
      <el-tree
        ref="permissionTree"
        :data="permissionTree"
        :props="{ children: 'children', label: 'permissionName' }"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedPermissions"
      />
      <div slot="footer">
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePermissionSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUsers, createUser, updateUser, deleteUser, assignPermissions, getAllHospitals } from '@/api'
import { getPermissionTree } from '@/api'

export default {
  name: 'HospitalAdmin',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      hospitalList: [],
      queryParams: {
        current: 1,
        size: 10,
        cohortCode: '',
        roleType: 2,
        keyword: ''
      },
      dialogVisible: false,
      permissionDialogVisible: false,
      dialogTitle: '添加管理员',
      form: {
        username: '',
        realName: '',
        gender: 1,
        cohortCode: '',
        department: '',
        title: '',
        phone: '',
        email: '',
        roleType: 2,
        status: 1
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        cohortCode: [{ required: true, message: '请选择所属医院', trigger: 'change' }]
      },
      permissionTree: [],
      checkedPermissions: [],
      currentUserId: null
    }
  },
  created() {
    this.fetchData()
    this.fetchHospitals()
    this.fetchPermissionTree()
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
    async fetchPermissionTree() {
      try {
        const response = await getPermissionTree()
        this.permissionTree = response.data || []
      } catch (error) {
        console.error('获取权限树失败', error)
      }
    },
    handleQuery() {
      this.queryParams.current = 1
      this.fetchData()
    },
    handleReset() {
      this.queryParams = {
        current: 1,
        size: 10,
        cohortCode: '',
        roleType: 2,
        keyword: ''
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '添加管理员'
      this.form = {
        username: '',
        realName: '',
        gender: 1,
        cohortCode: '',
        department: '',
        title: '',
        phone: '',
        email: '',
        roleType: 2,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑管理员'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handlePermission(row) {
      this.currentUserId = row.id
      this.checkedPermissions = row.permissions ? JSON.parse(row.permissions) : []
      this.permissionDialogVisible = true
    },
    async handlePermissionSubmit() {
      const checkedKeys = this.$refs.permissionTree.getCheckedKeys()
      const halfCheckedKeys = this.$refs.permissionTree.getHalfCheckedKeys()
      const allKeys = [...checkedKeys, ...halfCheckedKeys]
      
      try {
        await assignPermissions({
          userId: this.currentUserId,
          permissionIds: allKeys
        })
        this.$message.success('权限分配成功')
        this.permissionDialogVisible = false
        this.fetchData()
      } catch (error) {
        this.$message.error('权限分配失败')
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async(valid) => {
        if (valid) {
          try {
            if (this.form.id) {
              await updateUser(this.form.id, this.form)
            } else {
              await createUser(this.form)
            }
            this.$message.success('操作成功')
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error('操作失败')
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该管理员吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await deleteUser(row.id)
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.hospital-admin-container {
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
