<template>
  <div class="diagnosis-page">
    <!-- 最简化的测试模板，先确保组件能挂载 -->
    <el-card>
      <div slot="header">症状诊断</div>
      <div>
        <el-input v-model="newSymptom" placeholder="输入症状"></el-input>
        <el-button @click="addSymptom">添加</el-button>
        <div>已添加症状：{{ symptomList.join('、') }}</div>
      </div>
    </el-card>
  </div>
</template>

<script>
// 确保导出格式正确，无语法错误
export default {
  name: 'AiDiagnosis', // 组件名和路由中name一致
  data() {
    return {
      newSymptom: '',
      symptomList: []
    }
  },
  methods: {
    addSymptom() {
      if (this.newSymptom) {
        this.symptomList.push(this.newSymptom)
        this.newSymptom = ''
      }
    }
  }
}
</script>

<style scoped>
.diagnosis-page {
  padding: 20px;
}
</style>