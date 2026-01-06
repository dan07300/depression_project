<template>
  <div class="warning-container">
    <div class="header">
      <h2>预警管理</h2>
    </div>

    <div class="filter-bar">
      <el-select v-model="queryParams.warningLevel" placeholder="请选择" clearable style="width: 150px; margin-right: 10px;">
        <el-option label="低风险" :value="1" />
        <el-option label="中风险" :value="2" />
        <el-option label="高风险" :value="3" />
      </el-select>
      <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="patientCode" label="患者编号" />
      <el-table-column prop="patientName" label="患者姓名" />
      <el-table-column prop="warningLevel" label="预警等级" width="120">
        <template slot-scope="scope">
          <el-tag :type="getLevelType(scope.row.warningLevel)">
            {{ scope.row.warningLevelName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="riskScore" label="风险评分" width="120" />
      <el-table-column prop="warningReason" label="预警原因" show-overflow-tooltip />
      <el-table-column prop="suggestion" label="建议" show-overflow-tooltip />
      <el-table-column prop="processStatus" label="处理状态" width="120">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.processStatus)">
            {{ scope.row.processStatusName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.processStatus !== 2"
            size="mini"
            type="primary"
            @click="handleProcess(scope.row)"
          >
            处理
          </el-button>
          <span v-else>已处理</span>
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

    <el-dialog title="处理预警" :visible.sync="processDialogVisible" width="600px">
      <el-form ref="processForm" :model="processForm" label-width="120px">
        <el-form-item label="预警等级">
          <el-tag :type="getLevelType(processForm.warningLevel)">
            {{ processForm.warningLevelName }}
          </el-tag>
        </el-form-item>
        <el-form-item label="风险评分">
          {{ processForm.riskScore }}
        </el-form-item>
        <el-form-item label="预警原因">
          {{ processForm.warningReason }}
        </el-form-item>
        <el-form-item label="建议">
          {{ processForm.suggestion }}
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input v-model="processForm.processRemark" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProcessSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getWarnings, processWarning } from '@/api'

export default {
  name: 'Warning',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        current: 1,
        size: 10,
        warningLevel: null,
        processStatus: null
      },
      processDialogVisible: false,
      processForm: {
        id: null,
        warningLevel: null,
        warningLevelName: '',
        riskScore: null,
        warningReason: '',
        suggestion: '',
        processRemark: ''
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
        const response = await getWarnings(this.queryParams)
        this.tableData = response.data.records || []
        this.total = response.data.total || 0
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    getLevelType(level) {
      const map = {
        1: 'success',
        2: 'warning',
        3: 'danger'
      }
      return map[level] || 'info'
    },
    getStatusType(status) {
      const map = {
        0: 'info',
        1: 'warning',
        2: 'success'
      }
      return map[status] || 'info'
    },
    handleQuery() {
      this.queryParams.current = 1
      this.fetchData()
    },
    handleReset() {
      this.queryParams = {
        current: 1,
        size: 10,
        warningLevel: null,
        processStatus: null
      }
      this.fetchData()
    },
    handleProcess(row) {
      this.processForm = {
        id: row.id,
        warningLevel: row.warningLevel,
        warningLevelName: row.warningLevelName,
        riskScore: row.riskScore,
        warningReason: row.warningReason,
        suggestion: row.suggestion,
        processRemark: ''
      }
      this.processDialogVisible = true
    },
    async handleProcessSubmit() {
      try {
        const userInfo = this.$store.getters.userInfo
        await processWarning(
          this.processForm.id,
          userInfo.id,
          this.processForm.processRemark
        )
        this.$message.success('处理成功')
        this.processDialogVisible = false
        this.fetchData()
      } catch (error) {
        this.$message.error('处理失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.warning-container {
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
