<template>
  <div class="dashboard-container">
    <div class="welcome-banner">
      <h1>欢迎使用青少年心理健康促进管理平台</h1>
    </div>

    <div class="system-description">
      <i class="el-icon-info"></i>
      <span>本系统旨在通过多维度数据分析,为青少年提供心理健康评估与预警服务。</span>
    </div>

    <div class="feature-cards">
      <el-card class="feature-card">
        <div class="feature-icon">
          <i class="el-icon-data-line"></i>
        </div>
        <h3>多维评估</h3>
        <p>整合临床量表、生活规律、语音特征等多模态数据</p>
      </el-card>
      <el-card class="feature-card">
        <div class="feature-icon">
          <i class="el-icon-cpu"></i>
        </div>
        <h3>智能预测</h3>
        <p>基于机器学习算法,精准评估心理健康风险等级</p>
      </el-card>
      <el-card class="feature-card">
        <div class="feature-icon">
          <i class="el-icon-bell"></i>
        </div>
        <h3>实时预警</h3>
        <p>及时发现高危倾向,辅助医生进行干预决策</p>
      </el-card>
    </div>

    <div class="kpi-cards">
      <el-card class="kpi-card blue">
        <div class="kpi-icon">
          <i class="el-icon-office-building"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">医院数量</div>
          <div class="kpi-value">{{ dashboardData.hospitalCount || 0 }}</div>
        </div>
      </el-card>
      <el-card class="kpi-card green">
        <div class="kpi-icon">
          <i class="el-icon-user"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">用户总数</div>
          <div class="kpi-value">{{ dashboardData.userCount || 0 }}</div>
        </div>
      </el-card>
      <el-card class="kpi-card orange">
        <div class="kpi-icon">
          <i class="el-icon-warning"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">预警总数</div>
          <div class="kpi-value">{{ dashboardData.warningCount || 0 }}</div>
        </div>
      </el-card>
      <el-card class="kpi-card red">
        <div class="kpi-icon">
          <i class="el-icon-bell"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">高风险预警</div>
          <div class="kpi-value">{{ dashboardData.highRiskWarningCount || 0 }}</div>
        </div>
      </el-card>
    </div>

    <div class="charts-container">
      <el-card class="chart-card">
        <div slot="header" class="chart-header">
          <i class="el-icon-time"></i>
          <span>患者风险等级分布</span>
        </div>
        <div ref="riskChart" style="width: 100%; height: 300px;"></div>
      </el-card>
      <el-card class="chart-card">
        <div slot="header" class="chart-header">
          <i class="el-icon-time"></i>
          <span>近半年趋势分析</span>
        </div>
        <div ref="trendChart" style="width: 100%; height: 300px;"></div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getDashboardData } from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      dashboardData: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const response = await getDashboardData()
        this.dashboardData = response.data || {}
        this.$nextTick(() => {
          this.initRiskChart()
          this.initTrendChart()
        })
      } catch (error) {
        console.error('获取数据失败', error)
      }
    },
    initRiskChart() {
      const chart = echarts.init(this.$refs.riskChart)
      const distribution = this.dashboardData.riskLevelDistribution || {}
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '风险等级',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['55%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: distribution['高风险'] || 0, name: '高风险', itemStyle: { color: '#f56c6c' }},
              { value: distribution['中风险'] || 0, name: '中风险', itemStyle: { color: '#e6a23c' }},
              { value: distribution['低风险'] || 0, name: '低风险', itemStyle: { color: '#67c23a' }},
              { value: distribution['缓解期'] || 0, name: '缓解期', itemStyle: { color: '#409eff' }}
            ]
          }
        ]
      }
      chart.setOption(option)
    },
    initTrendChart() {
      const chart = echarts.init(this.$refs.trendChart)
      const trendData = this.dashboardData.trendData || []
      const months = trendData.map(item => item.month)
      const enrollmentData = trendData.map(item => item.enrollmentCount || 0)
      const warningData = trendData.map(item => item.highRiskWarningCount || 0)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['入组人数', '高风险预警']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: months
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '入组人数',
            type: 'line',
            data: enrollmentData,
            itemStyle: { color: '#409eff' }
          },
          {
            name: '高风险预警',
            type: 'line',
            data: warningData,
            itemStyle: { color: '#f56c6c' }
          }
        ]
      }
      chart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .welcome-banner {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 30px;
    border-radius: 8px;
    margin-bottom: 20px;
    text-align: center;

    h1 {
      margin: 0;
      font-size: 28px;
    }
  }

  .system-description {
    background: #f0f9ff;
    padding: 15px 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    display: flex;
    align-items: center;
    gap: 10px;

    i {
      color: #409eff;
      font-size: 20px;
    }
  }

  .feature-cards {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    margin-bottom: 20px;

    .feature-card {
      text-align: center;

      .feature-icon {
        font-size: 48px;
        color: #409eff;
        margin-bottom: 15px;
      }

      h3 {
        margin: 10px 0;
        color: #303133;
      }

      p {
        color: #606266;
        font-size: 14px;
      }
    }
  }

  .kpi-cards {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    margin-bottom: 20px;

    .kpi-card {
      display: flex;
      align-items: center;
      padding: 20px;

      &.blue {
        border-left: 4px solid #409eff;
      }
      &.green {
        border-left: 4px solid #67c23a;
      }
      &.orange {
        border-left: 4px solid #e6a23c;
      }
      &.red {
        border-left: 4px solid #f56c6c;
      }

      .kpi-icon {
        font-size: 40px;
        margin-right: 15px;
      }

      .kpi-content {
        flex: 1;

        .kpi-label {
          font-size: 14px;
          color: #909399;
          margin-bottom: 5px;
        }

        .kpi-value {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
        }
      }
    }
  }

  .charts-container {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;

    .chart-card {
      .chart-header {
        display: flex;
        align-items: center;
        gap: 8px;
        font-weight: bold;
      }
    }
  }
}
</style>
