<template>
  <div class="statistics-container">
    <div class="header">
      <h2>统计分析</h2>
    </div>

    <div class="filter-bar">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="margin-right: 10px;"
      />
      <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <div class="kpi-cards">
      <el-card class="kpi-card blue">
        <div class="kpi-icon">
          <i class="el-icon-user"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">病人总数</div>
          <div class="kpi-value">{{ statisticsData.patientCount || 0 }}</div>
        </div>
      </el-card>
      <el-card class="kpi-card green">
        <div class="kpi-icon">
          <i class="el-icon-user-solid"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">医生总数</div>
          <div class="kpi-value">{{ statisticsData.doctorCount || 0 }}</div>
        </div>
      </el-card>
      <el-card class="kpi-card orange">
        <div class="kpi-icon">
          <i class="el-icon-warning"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">预警数量</div>
          <div class="kpi-value">{{ statisticsData.warningCount || 0 }}</div>
        </div>
      </el-card>
      <el-card class="kpi-card red">
        <div class="kpi-icon">
          <i class="el-icon-close-notification"></i>
        </div>
        <div class="kpi-content">
          <div class="kpi-label">复发人数</div>
          <div class="kpi-value">{{ statisticsData.relapseCount || 0 }}</div>
        </div>
      </el-card>
    </div>

    <div class="charts-container">
      <el-card class="chart-card">
        <div slot="header" class="chart-header">
          <i class="el-icon-pie-chart"></i>
          <span>病人状态分布</span>
        </div>
        <div ref="statusChart" style="width: 100%; height: 300px;"></div>
      </el-card>
      <el-card class="chart-card">
        <div slot="header" class="chart-header">
          <i class="el-icon-data-line"></i>
          <span>月度预警趋势</span>
        </div>
        <div ref="trendChart" style="width: 100%; height: 300px;"></div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Statistics',
  data() {
    return {
      dateRange: [],
      statisticsData: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      // TODO: 调用后端接口获取统计数据
      this.statisticsData = {
        patientCount: 0,
        doctorCount: 0,
        warningCount: 0,
        relapseCount: 0
      }
      this.$nextTick(() => {
        this.initStatusChart()
        this.initTrendChart()
      })
    },
    initStatusChart() {
      const chart = echarts.init(this.$refs.statusChart)
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
            name: '病人状态',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 0, name: '正常' },
              { value: 0, name: '已排除' },
              { value: 0, name: '已出组' },
              { value: 0, name: '失访' },
              { value: 0, name: '康复' }
            ]
          }
        ]
      }
      chart.setOption(option)
    },
    initTrendChart() {
      const chart = echarts.init(this.$refs.trendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['预警数量']
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
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '预警数量',
            type: 'line',
            data: []
          }
        ]
      }
      chart.setOption(option)
    },
    handleQuery() {
      this.fetchData()
    },
    handleReset() {
      this.dateRange = []
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics-container {
  padding: 20px;

  .header {
    margin-bottom: 20px;
  }

  .filter-bar {
    margin-bottom: 20px;
  }

  .kpi-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
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
