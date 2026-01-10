<template>
  <div class="assessment-container">
    <div class="header">
      <h2>评定管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增评定</el-button>
    </div>

    <!-- 搜索筛选栏 -->
    <div class="filter-bar">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="病人姓名">
          <el-input 
            v-model="queryParams.patientName" 
            placeholder="请输入病人姓名" 
            style="width: 200px;"
            clearable
          />
        </el-form-item>
        <el-form-item label="评定类型">
          <el-select 
            v-model="queryParams.assessmentType" 
            placeholder="请选择" 
            style="width: 200px;"
            clearable
          >
            <el-option label="PHQ-9" value="phq9" />
            <el-option label="GAD-7" value="gad7" />
            <el-option label="HAMD" value="hamd" />
          </el-select>
        </el-form-item>
        <el-form-item label="评定日期">
          <el-date-picker
            v-model="queryParams.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px;"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="tableData" border>
      <el-table-column prop="patientName" label="病人姓名" width="120" />
      <el-table-column prop="assessmentType" label="评定类型" width="150">
        <template slot-scope="scope">
          {{ getAssessmentTypeName(scope.row.assessmentType) }}
        </template>
      </el-table-column>
      <el-table-column prop="score" label="评分" width="100" />
      <el-table-column prop="level" label="程度" width="120">
        <template slot-scope="scope">
          <el-tag :type="getLevelType(scope.row.level)" size="small">
            {{ scope.row.level }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="assessmentDate" label="评定日期" width="150" />
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 空状态 -->
    <div v-if="!loading && tableData.length === 0" class="empty-state">
      <i class="el-icon-loading"></i>
      <p>暂无数据</p>
    </div>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="fetchData"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="病人姓名" prop="patientName">
          <el-select 
            v-model="form.patientId" 
            placeholder="请选择病人" 
            style="width: 100%;"
            filterable
          >
            <el-option
              v-for="item in patientList"
              :key="item.idCard"
              :label="item.patientName"
              :value="item.idCard"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="评定类型" prop="assessmentType">
          <el-select v-model="form.assessmentType" placeholder="请选择评定类型" style="width: 100%;">
            <el-option label="PHQ-9" value="phq9" />
            <el-option label="GAD-7" value="gad7" />
            <el-option label="HAMD" value="hamd" />
          </el-select>
        </el-form-item>
        <el-form-item label="评分" prop="score">
          <el-input-number v-model="form.score" :min="0" :max="100" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="程度" prop="level">
          <el-select v-model="form.level" placeholder="请选择程度" style="width: 100%;">
            <el-option label="正常" value="正常" />
            <el-option label="轻度" value="轻度" />
            <el-option label="中度" value="中度" />
            <el-option label="重度" value="重度" />
          </el-select>
        </el-form-item>
        <el-form-item label="评定日期" prop="assessmentDate">
          <el-date-picker
            v-model="form.assessmentDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%;"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
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
import { getPatients } from '@/api'

export default {
  name: 'Assessment',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      patientList: [],
      queryParams: {
        current: 1,
        size: 10,
        patientName: '',
        assessmentType: '',
        dateRange: []
      },
      dialogVisible: false,
      dialogTitle: '新增评定',
      form: {
        id: null,
        patientId: '',
        assessmentType: '',
        score: 0,
        level: '',
        assessmentDate: '',
        remark: ''
      },
      rules: {
        patientId: [{ required: true, message: '请选择病人', trigger: 'change' }],
        assessmentType: [{ required: true, message: '请选择评定类型', trigger: 'change' }],
        score: [{ required: true, message: '请输入评分', trigger: 'blur' }],
        level: [{ required: true, message: '请选择程度', trigger: 'change' }],
        assessmentDate: [{ required: true, message: '请选择评定日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchData()
    this.fetchPatients()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        // TODO: 调用后端API获取评定数据
        // const response = await getAssessments(this.queryParams)
        // this.tableData = response.data.records || []
        // this.total = response.data.total || 0
        
        // 模拟数据
        this.tableData = []
        this.total = 0
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    async fetchPatients() {
      try {
        const response = await getPatients({ current: 1, size: 1000 })
        this.patientList = response.data.records || []
      } catch (error) {
        console.error('获取病人列表失败', error)
      }
    },
    getAssessmentTypeName(type) {
      const map = {
        'phq9': 'PHQ-9',
        'gad7': 'GAD-7',
        'hamd': 'HAMD'
      }
      return map[type] || type
    },
    getLevelType(level) {
      const map = {
        '正常': 'success',
        '轻度': 'info',
        '中度': 'warning',
        '重度': 'danger'
      }
      return map[level] || 'info'
    },
    handleQuery() {
      this.queryParams.current = 1
      this.fetchData()
    },
    handleReset() {
      this.queryParams = {
        current: 1,
        size: 10,
        patientName: '',
        assessmentType: '',
        dateRange: []
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增评定'
      this.form = {
        id: null,
        patientId: '',
        assessmentType: '',
        score: 0,
        level: '',
        assessmentDate: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑评定'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该评定记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // TODO: 调用删除API
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.form.validate(async(valid) => {
        if (valid) {
          try {
            // TODO: 调用保存API
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error('保存失败')
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.assessment-container {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 500;
    }
  }

  .filter-bar {
    margin-bottom: 20px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;

    .filter-form {
      margin: 0;
    }
  }

  .empty-state {
    text-align: center;
    padding: 60px 0;
    color: #909399;

    i {
      font-size: 48px;
      margin-bottom: 15px;
      display: block;
    }

    p {
      margin: 0;
      font-size: 14px;
    }
  }
}
</style>
