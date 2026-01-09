<template>
  <div class="patient-container">
    <div class="header">
      <h2>{{ pageTitle }}</h2>
      <el-button v-if="userInfo && userInfo.roleType !== 3" type="primary" icon="el-icon-plus" @click="handleAdd">
        {{ userInfo && userInfo.roleType === 1 ? '添加病人' : '添加病人' }}
      </el-button>
    </div>

    <!-- 统计卡片 - 仅医生角色显示 -->
    <div v-if="userInfo && userInfo.roleType === 3" class="stats-cards">
      <el-card class="stat-card">
        <div class="stat-label">已登记</div>
        <div class="stat-value">{{ stats.registered || 0 }}</div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-label">进行中</div>
        <div class="stat-value">{{ stats.inProgress || 0 }}</div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-label">随访截止</div>
        <div class="stat-value">{{ stats.followUpDeadline || 0 }}</div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-label">已排除</div>
        <div class="stat-value">{{ stats.excluded || 0 }}</div>
      </el-card>
      <el-card class="stat-card wide">
        <div class="stat-label">已完成随访</div>
        <div class="stat-value">{{ stats.followUpCompleted || 0 }}</div>
      </el-card>
    </div>

    <!-- 筛选按钮 - 仅医生角色显示 -->
    <div v-if="userInfo && userInfo.roleType === 3" class="filter-buttons">
      <el-button 
        :type="queryParams.status === null ? 'primary' : ''" 
        @click="handleStatusFilter(null)"
      >
        全部状态
      </el-button>
      <el-button 
        :type="queryParams.status === 1 ? 'primary' : ''" 
        @click="handleStatusFilter(1)"
      >
        正常
      </el-button>
      <el-button 
        :type="queryParams.status === 0 ? 'primary' : ''" 
        @click="handleStatusFilter(0)"
      >
        已排除
      </el-button>
      <el-button 
        type="primary" 
        icon="el-icon-plus" 
        style="float: right;"
        @click="handleAddToGroup"
      >
        添加入组
      </el-button>
    </div>

    <!-- 搜索筛选栏 - 系统管理员显示 -->
    <div v-if="userInfo && userInfo.roleType === 1" class="filter-bar">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="医院">
          <el-select v-model="queryParams.cohortCode" placeholder="全部医院" clearable style="width: 150px;">
            <el-option
              v-for="item in hospitalList"
              :key="item.cohortCode"
              :label="item.cohortName"
              :value="item.cohortCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关键字">
          <el-input
            v-model="queryParams.keyword"
            placeholder="患者编号/姓名/手机号"
            style="width: 200px;"
            clearable
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部状态" clearable style="width: 150px;">
            <el-option label="全部状态" :value="null" />
            <el-option label="正常" :value="1" />
            <el-option label="已排除" :value="0" />
            <el-option label="已出组" :value="2" />
            <el-option label="失访" :value="3" />
            <el-option label="康复" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 搜索筛选栏 - 医院管理员显示（只有关键字） -->
    <div v-if="userInfo && userInfo.roleType === 2" class="filter-bar">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="关键字">
          <el-input
            v-model="queryParams.keyword"
            placeholder="患者编号/姓名/手机号"
            style="width: 200px;"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 医生角色的表格 -->
    <el-table v-if="userInfo && userInfo.roleType === 3" v-loading="loading" :data="tableData" border>
      <el-table-column prop="patientCode" label="患者编号" width="120" />
      <el-table-column prop="patientName" label="患者姓名" width="120">
        <template slot-scope="scope">
          <el-link type="primary" @click="handlePatientDetail(scope.row)">{{ scope.row.patientName }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="firstScreening" label="首次筛查" width="100">
        <template slot-scope="scope">
          {{ (scope.row.firstScreening === 1 || scope.row.firstScreening === true) ? '是' : (scope.row.firstScreening === 0 || scope.row.firstScreening === false ? '否' : '否') }}
        </template>
      </el-table-column>
      <el-table-column prop="isFollowUp" label="是否随访" width="100">
        <template slot-scope="scope">
          {{ (scope.row.isFollowUp === 1 || scope.row.isFollowUp === true) ? '是' : (scope.row.isFollowUp === 0 || scope.row.isFollowUp === false ? '否' : '否') }}
        </template>
      </el-table-column>
      <el-table-column prop="enrollDate" label="入组时间" width="120">
        <template slot-scope="scope">
          {{ formatDate(scope.row.enrollDate || scope.row.enrollTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="isRelapsed" label="是否复发" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isRelapsed === 1 || scope.row.isRelapsed === true" type="danger" size="small">复发</el-tag>
          <el-tag v-else type="success" size="small">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="relapseDate" label="复发时间" width="120">
        <template slot-scope="scope">
          {{ (scope.row.relapseDate || scope.row.relapseTime) ? formatDate(scope.row.relapseDate || scope.row.relapseTime) : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="400" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="handleRecurrence(scope.row)">复发</el-button>
          <el-button size="mini" type="text" @click="handleFollowUp(scope.row)">随访</el-button>
          <el-button size="mini" type="text" @click="handleExclude(scope.row)">排除</el-button>
          <el-button size="mini" type="text" @click="handleTransfer(scope.row)">迁移</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 系统管理员的表格 -->
    <el-table v-if="userInfo && userInfo.roleType === 1" v-loading="loading" :data="tableData" border>
      <el-table-column prop="patientCode" label="患者编号" />
      <el-table-column prop="patientName" label="姓名">
        <template slot-scope="scope">
          <el-link type="primary" @click="handlePatientDetail(scope.row)">{{ scope.row.patientName }}</el-link>
        </template>
      </el-table-column>
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
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="enrollDate" label="入组日期" width="120">
        <template slot-scope="scope">
          {{ formatDate(scope.row.enrollDate || scope.row.enrollTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handlePatientDetail(scope.row)">详情</el-button>
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="handleTransfer(scope.row)">迁移</el-button>
          <el-button size="mini" type="text" style="color: #f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 医院管理员的表格（不显示所属医院列） -->
    <el-table v-if="userInfo && userInfo.roleType === 2" v-loading="loading" :data="tableData" border>
      <el-table-column prop="patientCode" label="患者编号" />
      <el-table-column prop="patientName" label="姓名">
        <template slot-scope="scope">
          <el-link type="primary" @click="handlePatientDetail(scope.row)">{{ scope.row.patientName }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="80">
        <template slot-scope="scope">
          {{ scope.row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column prop="birthday" label="出生日期" width="120" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="doctorName" label="所属医生" />
      <el-table-column prop="diagnosis" label="诊断" />
      <el-table-column prop="groupName" label="分组" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)" size="small">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="handleTransfer(scope.row)">迁移</el-button>
          <el-button size="mini" type="text" style="color: #f56c6c;" @click="handleDelete(scope.row)">删除</el-button>
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
import { mapGetters } from 'vuex'

export default {
  name: 'Patient',
  computed: {
    ...mapGetters(['userInfo']),
    pageTitle() {
      if (this.userInfo && this.userInfo.roleType === 1) {
        return '所有病人'
      } else if (this.userInfo && this.userInfo.roleType === 2) {
        return '病人管理'
      } else if (this.userInfo && this.userInfo.roleType === 3) {
        return '患者列表'
      }
      return '患者列表'
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      hospitalList: [],
      doctorList: [],
      stats: {
        registered: 0,
        inProgress: 0,
        followUpDeadline: 0,
        excluded: 0,
        followUpCompleted: 0
      },
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
        // 后端以身份证号 idCard 作为患者唯一标识
        idCard: '',
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
        // 这里存放患者身份证号，用于迁移接口路径变量
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
        this.calculateStats()
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    calculateStats() {
      // 计算统计数据
      this.stats = {
        registered: this.total,
        inProgress: this.tableData.filter(p => p.status === 1).length,
        followUpDeadline: 0, // 需要根据业务逻辑计算
        excluded: this.tableData.filter(p => p.status === 0).length,
        followUpCompleted: this.tableData.filter(p => p.isFollowUp === 1 || p.isFollowUp === true).length
      }
    },
    handleStatusFilter(status) {
      this.queryParams.status = status
      this.queryParams.current = 1
      this.fetchData()
    },
    handleAddToGroup() {
      this.handleAdd()
    },
    handlePatientDetail(row) {
      this.handleDetail(row)
    },
    handleRecurrence(row) {
      this.$prompt('请输入复发时间', '复发记录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\d{8}/,
        inputErrorMessage: '请输入8位数字日期，格式：YYYYMMDD'
      }).then(({ value }) => {
        // 调用API更新复发信息
        this.$message.success('复发记录已更新')
        this.fetchData()
      }).catch(() => {})
    },
    handleFollowUp(row) {
      this.$prompt('请输入随访信息', '随访记录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea'
      }).then(({ value }) => {
        // 调用API更新随访信息
        this.$message.success('随访记录已更新')
        this.fetchData()
      }).catch(() => {})
    },
    handleExclude(row) {
      this.$confirm('确定要排除该患者吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await updatePatient(row.idCard, { status: 0 })
          this.$message.success('已排除')
          this.fetchData()
        } catch (error) {
          this.$message.error('操作失败')
        }
      })
    },
    formatDate(date) {
      if (!date) return ''
      const dateStr = date.toString()
      if (dateStr.length === 8) {
        return `${dateStr.substring(0, 4)}/${dateStr.substring(4, 6)}/${dateStr.substring(6, 8)}`
      }
      return date
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
        // 使用身份证号作为唯一标识
        patientId: row.idCard,
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
            // 以 idCard 是否存在判断是新增还是编辑，并在更新时作为路径变量
            if (this.form.idCard) {
              await updatePatient(this.form.idCard, this.form)
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
          // 删除时同样使用身份证号作为唯一标识
          await deletePatient(row.idCard)
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
    margin-bottom: 20px;
    display: flex;                 /* 1. 启用弹性布局 */
    justify-content: space-between; /* 2. 两端对齐：左边是标题，右边是按钮 */
    align-items: center;           /* 3. 垂直居中对齐 */
    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 500;
    }
  }

  .stats-cards {
    display: flex;
    gap: 15px;
    margin-bottom: 20px;
    
    .stat-card {
      flex: 1;
      text-align: center;
      padding: 15px;
      
      &.wide {
        flex: 1.2;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 10px;
      }
      
      .stat-value {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
      }
    }
  }

  .filter-buttons {
    margin-bottom: 20px;
    display: flex;
    gap: 10px;
    align-items: center;
  }

  .filter-bar {
    margin-bottom: 20px;

    .filter-form {
      margin: 0;
      
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }
}
</style>
