<template>
  <div class="ai-assistant-container">
    <!-- 顶部标题栏 -->
    <div class="header-bar">
      <div class="logo-title">
        <i class="el-icon-chat-round icon-chat"></i>
        <div>
          <h2>AI医学助手</h2>
          <p>基于大语言模型的抑郁症诊疗问答系统</p>
        </div>
      </div>
      <el-button type="success" size="mini" icon="el-icon-check">AI服务就绪</el-button>
    </div>

    <!-- 导航标签页（仅切换activeTab，不跳转路由） -->
    <el-tabs v-model="activeTab" class="nav-tabs">
      <!-- 智能问答标签页 -->
      <el-tab-pane label="智能问答" name="qa">
        <template slot="label">
          <i class="el-icon-message"></i>
          <span>智能问答</span>
        </template>
        <!-- 智能问答内容 -->
        <div class="tab-content" v-show="activeTab === 'qa'">
          <div class="content-wrapper">
            <div class="chat-area">
              <div class="empty-chat">
                <i class="el-icon-chat-round icon-empty"></i>
                <h3>开始与AI医学助手对话</h3>
                <p>输入您的问题,获取专业的抑郁症相关知识解答</p>
              </div>
            </div>
            <div class="right-panel">
              <el-card class="quick-question-card">
                <div slot="header" class="card-header">
                  <i class="el-icon-search"></i>
                  <span>快捷问题</span>
                </div>
                <el-button 
                  type="text" 
                  size="mini" 
                  class="quick-btn"
                  v-for="(item, idx) in quickQuestions" 
                  :key="idx"
                  @click="handleQuickQuestion(item)"
                >
                  {{ item }}
                </el-button>
              </el-card>

              <el-card class="function-desc-card">
                <div slot="header" class="card-header">
                  <i class="el-icon-info"></i>
                  <span>功能说明</span>
                </div>
                <el-descriptions :column="1" size="small">
                  <el-descriptions-item>
                    <i class="el-icon-check icon-check"></i> 基于DeepSeek大模型
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <i class="el-icon-check icon-check"></i> 专业抑郁症知识库
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </div>
          </div>
        </div>
      </el-tab-pane>
      
      <!-- 症状诊断标签页（嵌入到当前页面，非独立路由） -->
      <el-tab-pane label="症状诊断" name="diagnosis">
        <template slot="label">
          <i class="el-icon-stethoscope"></i>
          <span>症状诊断</span>
        </template>
        <!-- 症状诊断内容 -->
        <div class="tab-content" v-show="activeTab === 'diagnosis'">
          <div class="diagnosis-page">
            <!-- 内容区域 -->
            <div class="content-container">
              <!-- 左侧：症状信息输入 -->
              <el-card class="left-card">
                <div slot="header" class="card-header">
                  <i class="el-icon-edit"></i>
                  <span>输入症状信息</span>
                </div>

                <!-- 症状列表区域 -->
                <div class="symptom-section">
                  <h4>症状列表</h4>
                  <div class="symptom-input-group">
                    <el-input
                      v-model="newSymptom"
                      placeholder="输入症状，按回车添加"
                      @keyup.enter="handleAddSymptom"
                      clearable
                    />
                    <el-button type="primary" @click="handleAddSymptom">添加</el-button>
                  </div>
                  
                  <!-- 已添加的症状标签 -->
                  <div class="symptom-tags" v-if="symptomList.length > 0">
                    <el-tag
                      v-for="(item, idx) in symptomList"
                      :key="idx"
                      closable
                      @close="handleRemoveSymptom(idx)"
                    >
                      {{ item }}
                    </el-tag>
                  </div>
                  <p class="tips" v-else>请添加至少2个症状</p>
                </div>

                <!-- 患者信息区域 -->
                <div class="patient-info-section">
                  <h4>患者信息（可选）</h4>
                  <el-input
                    type="textarea"
                    v-model="patientInfo"
                    placeholder="如：45岁女性，病程3个月，既往无精神病史..."
                    rows="3"
                  />
                </div>

                <!-- 开始诊断按钮 -->
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  @click="handleDiagnosis"
                  :disabled="symptomList.length < 2"
                  style="margin-top: 15px;"
                >
                  开始诊断分析
                </el-button>
              </el-card>

              <!-- 右侧：诊断分析结果 -->
              <el-card class="right-card">
                <div slot="header" class="card-header">
                  <i class="el-icon-menu"></i>
                  <span>诊断分析结果</span>
                </div>

                <div class="result-empty" v-if="!diagnosisResult">
                  <i class="el-icon-menu"></i>
                  <p>输入症状后点击“开始诊断分析”</p>
                </div>
                <div class="result-content" v-else>
                  {{ diagnosisResult }}
                </div>
              </el-card>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 治疗建议标签页（占位） -->
      <el-tab-pane label="治疗建议" name="treatment">
        <template slot="label">
          <i class="el-icon-medical"></i>
          <span>治疗建议</span>
        </template>
        <div class="tab-content" v-show="activeTab === 'treatment'">
          <div class="empty-tab">
            <i class="el-icon-medical"></i>
            <p>治疗建议功能正在开发中...</p>
          </div>
        </div>
      </el-tab-pane>

      <!-- 复发评估标签页（占位） -->
      <el-tab-pane label="复发评估" name="recurrence">
        <template slot="label">
          <i class="el-icon-refresh"></i>
          <span>复发评估</span>
        </template>
        <div class="tab-content" v-show="activeTab === 'recurrence'">
          <div class="empty-tab">
            <i class="el-icon-refresh"></i>
            <p>复发评估功能正在开发中...</p>
          </div>
        </div>
      </el-tab-pane>

      <!-- 专家问诊标签页（占位） -->
      <el-tab-pane label="专家问诊" name="consult">
        <template slot="label">
          <i class="el-icon-user"></i>
          <span>专家问诊</span>
        </template>
        <div class="tab-content" v-show="activeTab === 'consult'">
          <div class="empty-tab">
            <i class="el-icon-user"></i>
            <p>专家问诊功能正在开发中...</p>
          </div>
        </div>
      </el-tab-pane>

      <!-- 知识图谱标签页（占位） -->
      <el-tab-pane label="知识图谱" name="knowledge">
        <template slot="label">
          <i class="el-icon-s-data"></i>
          <span>知识图谱</span>
        </template>
        <div class="tab-content" v-show="activeTab === 'knowledge'">
          <div class="empty-tab">
            <i class="el-icon-s-data"></i>
            <p>知识图谱功能正在开发中...</p>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'AiAssistant',
  data() {
    return {
      // 标签页激活状态
      activeTab: 'qa',
      // 智能问答相关数据
      quickQuestions: [
        '抑郁症的典型症状有哪些?',
        '如何区分抑郁情绪和抑郁症?',
        '抗抑郁药物的常见副作用?',
        '认知行为疗法的原理是什么?',
        'PHQ-9评分标准是什么?',
        '抑郁症的治疗疗程一般多久?'
      ],
      // 症状诊断相关数据（核心：整合到当前页面，非独立路由）
      newSymptom: '', // 待添加的症状
      symptomList: [], // 已添加的症状列表
      patientInfo: '', // 患者信息
      diagnosisResult: '' // 诊断分析结果
    }
  },
  methods: {
    // ========== 智能问答相关方法 ==========
    handleQuickQuestion(question) {
      console.log('选择快捷问题:', question)
      this.$message.info(`已选择快捷问题：${question}`)
      // 后续可对接AI接口发送问题
    },

    // ========== 症状诊断相关方法 ==========
    // 添加症状（回车/点击按钮）
    handleAddSymptom() {
      if (!this.newSymptom.trim()) {
        this.$message.warning('请输入症状内容')
        return
      }
      if (this.symptomList.includes(this.newSymptom.trim())) {
        this.$message.warning('该症状已添加')
        return
      }
      this.symptomList.push(this.newSymptom.trim())
      this.newSymptom = ''
    },
    // 移除症状
    handleRemoveSymptom(idx) {
      this.symptomList.splice(idx, 1)
    },
    // 开始诊断分析（调用AI接口）
    async handleDiagnosis() {
      try {
        const loading = this.$loading({
          lock: true,
          text: '诊断分析中，请稍候...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        // 模拟AI接口请求（实际项目替换为真实接口）
        await new Promise(resolve => setTimeout(resolve, 1500))
        this.diagnosisResult = `
          基于症状：${this.symptomList.join('、')}
          ${this.patientInfo ? '\n患者信息：' + this.patientInfo : ''}
          \n诊断分析结果：
          1. 症状符合中度抑郁发作表现，核心症状为情绪低落+兴趣减退；
          2. 建议完善PHQ-9、SDS量表评估；
          3. 治疗建议：心理治疗（认知行为疗法）联合舍曲林（50mg/日），2周后复诊。
        `
        loading.close()
        this.$message.success('诊断分析完成')
      } catch (err) {
        this.$message.error('诊断分析失败，请重试')
        console.error(err)
      }
    }
  }
}
</script>

<style scoped lang="scss">
.ai-assistant-container {
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  padding: 10px;

  // 顶部标题栏样式
  .header-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: linear-gradient(120deg, #667eea, #764ba2);
    color: #fff;
    padding: 15px 20px;
    border-radius: 4px;
    margin-bottom: 20px;

    .logo-title {
      display: flex;
      align-items: center;
      gap: 10px;

      .icon-chat {
        font-size: 24px;
      }

      h2 {
        margin: 0;
        font-size: 18px;
      }

      p {
        margin: 5px 0 0;
        font-size: 12px;
        opacity: 0.8;
      }
    }
  }

  // 标签页样式
  .nav-tabs {
    --el-tabs-header-text-color: #333;
    --el-tabs-active-text-color: #667eea;
    --el-tabs-border-color: #e5e7eb;
  }

  // 标签页内容通用样式
  .tab-content {
    width: 100%;
    padding: 10px 0;
  }

  // ========== 智能问答内容样式 ==========
  .content-wrapper {
    display: flex;
    gap: 20px;
    margin-top: 20px;

    .chat-area {
      flex: 3;
      background: #fff;
      border-radius: 4px;
      padding: 40px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      min-height: 500px;

      .empty-chat {
        text-align: center;

        .icon-empty {
          font-size: 40px;
          color: #c0c6cc;
          margin-bottom: 20px;
        }

        h3 {
          margin: 0 0 10px;
          font-size: 16px;
          color: #333;
        }

        p {
          color: #999;
          font-size: 12px;
        }
      }
    }

    .right-panel {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 20px;

      .card-header {
        display: flex;
        align-items: center;
        gap: 5px;
        font-weight: 500;
      }

      .quick-question-card {
        .quick-btn {
          display: block;
          text-align: left;
          margin-bottom: 10px;
          color: #666;
          border: 1px solid #e5e7eb;
          border-radius: 4px;
          padding: 8px 10px;
          width: 100%;
          transition: all 0.2s;

          &:hover {
            background: #f5f7fa;
            border-color: #667eea;
            color: #667eea;
          }
        }
      }

      .function-desc-card {
        .icon-check {
          color: #42b983;
          margin-right: 5px;
        }
      }
    }
  }

  // ========== 症状诊断内容样式 ==========
  .diagnosis-page {
    padding: 20px;
    background: #f5f7fa;
    min-height: calc(100vh - 80px);

    .content-container {
      display: flex;
      gap: 20px;

      .left-card, .right-card {
        flex: 1;
        background: #fff;
        border-radius: 4px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      }

      .card-header {
        display: flex;
        align-items: center;
        gap: 5px;
        font-weight: 500;
      }

      // 左侧卡片样式
      .left-card {
        .symptom-section {
          margin: 20px 0;

          h4 {
            margin: 0 0 10px;
            font-size: 14px;
            color: #333;
          }

          .symptom-input-group {
            display: flex;
            gap: 10px;
            margin: 10px 0;
          }

          .symptom-tags {
            margin: 10px 0;
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
          }

          .tips {
            color: #999;
            font-size: 12px;
            margin-top: 10px;
          }
        }

        .patient-info-section {
          margin: 20px 0;

          h4 {
            margin: 0 0 10px;
            font-size: 14px;
            color: #333;
          }
        }
      }

      // 右侧卡片样式
      .right-card {
        .result-empty {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          min-height: 300px;
          color: #999;

          i {
            font-size: 40px;
            margin-bottom: 10px;
          }
        }

        .result-content {
          padding: 20px;
          line-height: 1.6;
          white-space: pre-wrap; // 保留换行符
        }
      }
    }
  }

  // ========== 其他标签页占位样式 ==========
  .empty-tab {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 500px;
    color: #999;

    i {
      font-size: 40px;
      margin-bottom: 10px;
    }
  }
}
</style>