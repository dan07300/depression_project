<template>
  <div class="hospital-container">
    <div class="header">
      <h2>医院管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">添加医院</el-button>
    </div>

    <div class="filter-bar">
      <el-input
        v-model="queryParams.keyword"
        placeholder="医院名称/编码"
        style="width: 200px; margin-right: 10px;"
        clearable
      />
      <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="cohortName" label="医院名称" />
      <el-table-column prop="cohortCode" label="医院编码" />
      <el-table-column prop="address" label="地址" />
      <el-table-column prop="contactPerson" label="联系人" />
      <el-table-column prop="contactPhone" label="联系电话" />
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
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
        <el-form-item label="医院编码" prop="cohortCode">
          <el-input v-model="form.cohortCode" :disabled="!!form.cohortCode" />
        </el-form-item>
        <el-form-item label="医院名称" prop="cohortName">
          <el-input v-model="form.cohortName" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" />
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
  </div>
</template>

<script>
import { getHospitals, createHospital, updateHospital, deleteHospital } from '@/api'

export default {
  name: 'Hospital',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        current: 1,
        size: 10,
        keyword: ''
      },
      dialogVisible: false,
      dialogTitle: '添加医院',
      form: {
        cohortCode: '',
        cohortName: '',
        address: '',
        contactPerson: '',
        contactPhone: '',
        status: 1
      },
      rules: {
        cohortCode: [{ required: true, message: '请输入医院编码', trigger: 'blur' }],
        cohortName: [{ required: true, message: '请输入医院名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const response = await getHospitals(this.queryParams)
        this.tableData = response.data.records || []
        this.total = response.data.total || 0
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
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
        keyword: ''
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '添加医院'
      this.form = {
        cohortCode: '',
        cohortName: '',
        address: '',
        contactPerson: '',
        contactPhone: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑医院'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(async(valid) => {
        if (valid) {
          try {
            if (this.dialogTitle === '编辑医院') {
              await updateHospital(this.form.cohortCode, this.form)
            } else {
              await createHospital(this.form)
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
      this.$confirm('确定要删除该医院吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await deleteHospital(row.cohortCode)
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
.hospital-container {
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
