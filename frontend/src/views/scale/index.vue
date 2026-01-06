<template>
  <div class="scale-container">
    <div class="content-wrapper">
      <div class="left-panel">
        <div class="panel-header">
          <h3>量表列表</h3>
          <div>
            <el-button size="mini" icon="el-icon-refresh" @click="fetchScaleList">刷新</el-button>
            <el-button size="mini" type="primary" icon="el-icon-plus" @click="handleAddScale">添加</el-button>
          </div>
        </div>
        <el-input
          v-model="scaleKeyword"
          placeholder="搜索量表"
          prefix-icon="el-icon-search"
          style="margin-bottom: 10px;"
        />
        <el-select v-model="scaleCategory" placeholder="全部分类" style="width: 100%; margin-bottom: 10px;">
          <el-option label="全部分类" value="" />
          <el-option label="基础评定" value="基础评定" />
          <el-option label="医生评定" value="医生评定" />
          <el-option label="患者自评" value="患者自评" />
        </el-select>
        <el-checkbox v-model="showDisabled">显示已禁用</el-checkbox>
        <el-scrollbar style="height: calc(100vh - 250px); margin-top: 10px;">
          <div
            v-for="item in scaleList"
            :key="item.assessmentType"
            class="scale-item"
            :class="{ active: selectedScale === item.assessmentType }"
            @click="selectScale(item)"
          >
            <i class="el-icon-document"></i>
            <span>{{ item.assessmentName }}</span>
            <el-tag size="mini" style="margin-left: auto;">{{ item.category }}</el-tag>
          </div>
        </el-scrollbar>
      </div>

      <div class="right-panel">
        <div class="panel-header">
          <h3>{{ selectedScaleName || '请选择量表' }} - 题目列表</h3>
          <div>
            <el-button size="mini" type="primary" icon="el-icon-plus" @click="handleAddQuestion">添加题目</el-button>
            <el-button size="mini" type="warning" icon="el-icon-upload2">批量导入</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete">批量删除</el-button>
            <el-button size="mini" icon="el-icon-sort">按题号排序</el-button>
          </div>
        </div>
        <el-input
          v-model="questionKeyword"
          placeholder="搜索题目内容"
          prefix-icon="el-icon-search"
          style="margin-bottom: 10px;"
        >
          <el-button slot="append" icon="el-icon-search">搜索</el-button>
        </el-input>
        <el-table v-loading="questionLoading" :data="questionList" border>
          <el-table-column type="selection" width="55" />
          <el-table-column prop="questionNo" label="题号" width="80" />
          <el-table-column prop="content" label="题目内容" show-overflow-tooltip />
          <el-table-column prop="questionType" label="题目类型" width="120">
            <template slot-scope="scope">
              {{ getQuestionTypeName(scope.row.questionType) }}
            </template>
          </el-table-column>
          <el-table-column prop="optionCount" label="选项数" width="100" />
          <el-table-column prop="required" label="必答" width="80">
            <template slot-scope="scope">
              <el-tag :type="scope.row.required === 1 ? 'success' : 'info'" size="mini">
                {{ scope.row.required === 1 ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'info'" size="mini">
                {{ scope.row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleEditQuestion(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="handleDeleteQuestion(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog :title="questionDialogTitle" :visible.sync="questionDialogVisible" width="800px">
      <el-form ref="questionForm" :model="questionForm" :rules="questionRules" label-width="120px">
        <el-form-item label="评定类型" prop="assessmentType">
          <el-input v-model="questionForm.assessmentType" :disabled="!!questionForm.id" />
        </el-form-item>
        <el-form-item label="题目编号" prop="questionNo">
          <el-input-number v-model="questionForm.questionNo" :min="1" />
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <el-input v-model="questionForm.content" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="题目类型" prop="questionType">
          <el-select v-model="questionForm.questionType" style="width: 100%;">
            <el-option label="单选" :value="1" />
            <el-option label="多选" :value="2" />
            <el-option label="填空" :value="3" />
            <el-option label="量表评分" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否必答" prop="required">
          <el-radio-group v-model="questionForm.required">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选项列表">
          <el-button size="mini" type="primary" @click="handleAddOption">添加选项</el-button>
          <el-table :data="questionForm.options" border style="margin-top: 10px;">
            <el-table-column prop="optionLabel" label="选项标签" width="120">
              <template slot-scope="scope">
                <el-input v-model="scope.row.optionLabel" size="mini" />
              </template>
            </el-table-column>
            <el-table-column prop="optionContent" label="选项内容">
              <template slot-scope="scope">
                <el-input v-model="scope.row.optionContent" size="mini" />
              </template>
            </el-table-column>
            <el-table-column prop="score" label="分值" width="100">
              <template slot-scope="scope">
                <el-input-number v-model="scope.row.score" :min="0" size="mini" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template slot-scope="scope">
                <el-button size="mini" type="danger" @click="handleRemoveOption(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="questionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleQuestionSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getScaleQuestions, createScaleQuestion, updateScaleQuestion, deleteScaleQuestion } from '@/api'

export default {
  name: 'Scale',
  data() {
    return {
      scaleList: [
        { assessmentType: 'phq9', assessmentName: '抑郁症筛查量表(PHQ-9)', category: '患者自评' },
        { assessmentType: 'gad7', assessmentName: '广泛性焦虑障碍量表(GAD-7)', category: '患者自评' },
        { assessmentType: 'hamd', assessmentName: '汉密尔顿抑郁量表', category: '医生评定' }
      ],
      selectedScale: null,
      selectedScaleName: '',
      scaleKeyword: '',
      scaleCategory: '',
      showDisabled: false,
      questionList: [],
      questionLoading: false,
      questionKeyword: '',
      questionDialogVisible: false,
      questionDialogTitle: '添加题目',
      questionForm: {
        assessmentType: '',
        questionNo: 1,
        content: '',
        questionType: 1,
        required: 1,
        options: []
      },
      questionRules: {
        assessmentType: [{ required: true, message: '请输入评定类型', trigger: 'blur' }],
        questionNo: [{ required: true, message: '请输入题目编号', trigger: 'blur' }],
        content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
        questionType: [{ required: true, message: '请选择题目类型', trigger: 'change' }]
      }
    }
  },
  methods: {
    selectScale(item) {
      this.selectedScale = item.assessmentType
      this.selectedScaleName = item.assessmentName
      this.fetchQuestions()
    },
    async fetchQuestions() {
      if (!this.selectedScale) return
      this.questionLoading = true
      try {
        const response = await getScaleQuestions({
          assessmentType: this.selectedScale,
          current: 1,
          size: 100
        })
        this.questionList = response.data.records || []
      } catch (error) {
        this.$message.error('获取题目列表失败')
      } finally {
        this.questionLoading = false
      }
    },
    fetchScaleList() {
      this.$message.info('刷新量表列表')
    },
    handleAddScale() {
      this.$message.info('添加量表功能开发中')
    },
    handleAddQuestion() {
      if (!this.selectedScale) {
        this.$message.warning('请先选择量表')
        return
      }
      this.questionDialogTitle = '添加题目'
      this.questionForm = {
        assessmentType: this.selectedScale,
        questionNo: 1,
        content: '',
        questionType: 1,
        required: 1,
        options: []
      }
      this.questionDialogVisible = true
    },
    handleEditQuestion(row) {
      this.questionDialogTitle = '编辑题目'
      this.questionForm = {
        id: row.id,
        assessmentType: row.assessmentType,
        questionNo: row.questionNo,
        content: row.content,
        questionType: row.questionType,
        required: row.required,
        options: row.options ? [...row.options] : []
      }
      this.questionDialogVisible = true
    },
    handleDeleteQuestion(row) {
      this.$confirm('确定要删除该题目吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await deleteScaleQuestion(row.id)
          this.$message.success('删除成功')
          this.fetchQuestions()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    handleAddOption() {
      this.questionForm.options.push({
        optionLabel: '',
        optionContent: '',
        score: 0
      })
    },
    handleRemoveOption(index) {
      this.questionForm.options.splice(index, 1)
    },
    async handleQuestionSubmit() {
      this.$refs.questionForm.validate(async(valid) => {
        if (valid) {
          try {
            if (this.questionForm.id) {
              await updateScaleQuestion(this.questionForm.id, this.questionForm)
            } else {
              await createScaleQuestion(this.questionForm)
            }
            this.$message.success('操作成功')
            this.questionDialogVisible = false
            this.fetchQuestions()
          } catch (error) {
            this.$message.error('操作失败')
          }
        }
      })
    },
    getQuestionTypeName(type) {
      const map = {
        1: '单选',
        2: '多选',
        3: '填空',
        4: '量表评分'
      }
      return map[type] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.scale-container {
  padding: 20px;

  .content-wrapper {
    display: flex;
    gap: 20px;
    height: calc(100vh - 100px);

    .left-panel {
      width: 300px;
      background: #fff;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 20px;

      .panel-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        h3 {
          margin: 0;
        }
      }

      .scale-item {
        display: flex;
        align-items: center;
        padding: 10px;
        margin-bottom: 5px;
        cursor: pointer;
        border-radius: 4px;
        transition: background-color 0.3s;

        &:hover {
          background-color: #f5f7fa;
        }

        &.active {
          background-color: #ecf5ff;
          color: #409eff;
        }

        i {
          margin-right: 8px;
        }
      }
    }

    .right-panel {
      flex: 1;
      background: #fff;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 20px;

      .panel-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        h3 {
          margin: 0;
        }
      }
    }
  }
}
</style>
