<template>
  <div class="patient-container">
    <div class="header">
      <h2>所有病人</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">添加病人</el-button>
    </div>

    <div class="filter-bar">
      <el-select v-model="queryParams.cohortCode" placeholder="全部医院" clearable style="width: 150px; margin-right: 10px;">
        <el-option
          v-for="item in hospitalList"
          :key="item.cohortCode"
          :label="item.cohortName"
          :value="item.cohortCode"
        />
      </el-select>
      <el-input
        v-model="queryParams.keyword"
        placeholder="患者编号/姓名/手机号"
        style="width: 200px; margin-right: 10px;"
        clearable
      />
      <el-select v-model="queryParams.status" placeholder="全部状态" clearable style="width: 150px; margin-right: 10px;">
        <el-option label="正常" :value="1" />
        <el-option label="已排除" :value="0" />
        <el-option label="已出组" :value="2" />
        <el-option label="失访" :value="3" />
        <el-option label="康复" :value="4" />
      </el-select>
      <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="patientCode" label="患者编号" />
      <el-table-column prop="patientName" label="姓名" />
      <el-table-column prop="gender" label="性别" width="80">
        <template slot-scope="scope">
          {{ scope.row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="birthday" label="出生日期" width="120" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="cohortName" label="所属医院" />
      <el-table-column prop="doctorName" label="所属医生" />
      <el-table-column prop="diagnosis" label="诊断" />
      <el-table-column prop="groupName" label="分组" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="enrollDate" label="入组日期" width="120" />
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button size="mini" type="info" @click="handleDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="warning" @click="handleTransfer(scope.row)">迁移</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者编号" prop="patientCode">
              <el-input v-model="form.patientCode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input v-model="form.patientName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker v-model="form.birthday" type="date" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="诊断" prop="diagnosis">
              <el-input v-model="form.diagnosis" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分组" prop="groupName">
              <el-input v-model="form.groupName" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="患者迁移" :visible.sync="transferDialogVisible" width="500px">
      <el-form ref="transferForm" :model="transferForm" label-width="120px">
        <el-form-item label="目标医院" prop="targetCohortCode">
          <el-select v-model="transferForm.targetCohortCode" placeholder="请选择目标医院" style="width: 100%;" @change="handleHospitalChange">
            <el-option
              v-for="item in hospitalList"
              :key="item.cohortCode"
              :label="item.cohortName"
              :value="item.cohortCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="目标医生" prop="targetDoctorId">
          <el-select v-model="transferForm.targetDoctorId" placeholder="请选择目标医生" style="width: 100%;" clearable>
            <el-option
              v-for="item in doctorList"
              :key="item.id"
              :label="item.realName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="transferDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTransferSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getPatients, createPatient, updatePatient, deletePatient, transferPatient, getAllHospitals, listUsers } from '@/api'

export default {
  name: 'Patient',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      hospitalList: [],
      doctorList: [],
      queryParams: {
        current: 1,
        size: 10,
        cohortCode: '',
        keyword: '',
        status: null
      },
      dialogVisible: false,
      transferDialogVisible: false,
      dialogTitle: '添加病人',
      form: {
        patientCode: '',
        patientName: '',
        gender: 1,
        birthday: '',
        phone: '',
        cohortCode: '',
        diagnosis: '',
        groupName: ''
      },
      transferForm: {
        patientId: null,
        targetCohortCode: '',
        targetDoctorId: null
      },
      rules: {
        patientName: [{ required: true, message: '请输入患者姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        cohortCode: [{ required: true, message: '请选择所属医院', trigger: 'change' }]
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
        const response = await getPatients(this.queryParams)
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
    async fetchDoctors(cohortCode) {
      if (!cohortCode) {
        this.doctorList = []
        return
      }
      try {
        const response = await listUsers(cohortCode, 3)
        this.doctorList = response.data || []
      } catch (error) {
        console.error('获取医生列表失败', error)
        this.doctorList = []
      }
    },
    handleHospitalChange(cohortCode) {
      this.transferForm.targetDoctorId = null
      this.fetchDoctors(cohortCode)
    },
    getStatusType(status) {
      const map = {
        0: 'info',
        1: 'success',
        2: 'warning',
        3: 'danger',
        4: 'success'
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        0: '已排除',
        1: '正常',
        2: '已出组',
        3: '失访',
        4: '康复'
      }
      return map[status] || '未知'
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
        keyword: '',
        status: null
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '添加病人'
      this.form = {
        patientCode: '',
        patientName: '',
        gender: 1,
        birthday: '',
        phone: '',
        cohortCode: '',
        diagnosis: '',
        groupName: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑病人'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDetail(row) {
      this.$message.info('详情功能开发中')
    },
    handleTransfer(row) {
      this.transferForm = {
        patientId: row.id,
        targetCohortCode: '',
        targetDoctorId: null
      }
      this.transferDialogVisible = true
      // 如果选择了医院，加载该医院的医生列表
      if (row.cohortCode) {
        this.fetchDoctors(row.cohortCode)
      }
    },
    async handleTransferSubmit() {
      if (!this.transferForm.targetCohortCode) {
        this.$message.warning('请选择目标医院')
        return
      }
      try {
        const transferData = {
          targetCohortCode: this.transferForm.targetCohortCode,
          targetDoctorId: this.transferForm.targetDoctorId
        }
        await transferPatient(this.transferForm.patientId, transferData)
        this.$message.success('迁移成功')
        this.transferDialogVisible = false
        this.fetchData()
      } catch (error) {
        this.$message.error('迁移失败')
      }
    },
    handleSubmit() {
      this.$refs.form.validate(async(valid) => {
        if (valid) {
          try {
            if (this.form.id) {
              await updatePatient(this.form.id, this.form)
            } else {
              await createPatient(this.form)
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
      this.$confirm('确定要删除该患者吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await deletePatient(row.id)
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
.patient-container {
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
