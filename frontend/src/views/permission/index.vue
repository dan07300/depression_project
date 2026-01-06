<template>
  <div class="permission-container">
    <div class="header">
      <h2>权限管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">添加权限</el-button>
    </div>

    <div class="content-wrapper">
      <div class="left-panel">
        <div class="panel-header">
          <h3>权限树结构</h3>
          <el-button size="mini" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
        </div>
        <el-tree
          ref="permissionTree"
          :data="permissionTree"
          :props="{ children: 'children', label: 'permissionName' }"
          :default-expand-all="true"
          node-key="id"
          highlight-current
          @node-click="handleNodeClick"
        >
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>
              <i :class="data.icon" style="margin-right: 5px;"></i>
              {{ node.label }}
            </span>
            <span>
              <el-tag v-if="data.permissionType === 1" size="mini" type="primary">菜单</el-tag>
              <el-tag v-else-if="data.permissionType === 2" size="mini" type="success">按钮</el-tag>
              <el-tag v-else size="mini" type="info">数据</el-tag>
            </span>
          </span>
        </el-tree>
      </div>

      <div class="right-panel">
        <div v-if="selectedPermission" class="permission-detail">
          <h3>权限详情</h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="权限编码">{{ selectedPermission.permissionCode }}</el-descriptions-item>
            <el-descriptions-item label="权限名称">{{ selectedPermission.permissionName }}</el-descriptions-item>
            <el-descriptions-item label="权限类型">
              <el-tag v-if="selectedPermission.permissionType === 1" type="primary">菜单</el-tag>
              <el-tag v-else-if="selectedPermission.permissionType === 2" type="success">按钮</el-tag>
              <el-tag v-else type="info">数据</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="路由路径">{{ selectedPermission.path || '-' }}</el-descriptions-item>
            <el-descriptions-item label="图标">{{ selectedPermission.icon || '-' }}</el-descriptions-item>
            <el-descriptions-item label="描述">{{ selectedPermission.description || '-' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="selectedPermission.status === 1 ? 'success' : 'info'">
                {{ selectedPermission.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        <div v-else class="empty-state">
          <i class="el-icon-info"></i>
          <p>请在左侧选择一个权限查看详情</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPermissionTree, getPermissionById } from '@/api'

export default {
  name: 'Permission',
  data() {
    return {
      permissionTree: [],
      selectedPermission: null
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const response = await getPermissionTree()
        this.permissionTree = response.data || []
      } catch (error) {
        this.$message.error('获取权限树失败')
      }
    },
    async handleNodeClick(data) {
      try {
        const response = await getPermissionById(data.id)
        this.selectedPermission = response.data
      } catch (error) {
        this.$message.error('获取权限详情失败')
      }
    },
    handleAdd() {
      this.$message.info('添加权限功能开发中')
    }
  }
}
</script>

<style lang="scss" scoped>
.permission-container {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .content-wrapper {
    display: flex;
    gap: 20px;
    height: calc(100vh - 200px);

    .left-panel {
      flex: 1;
      background: #fff;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 20px;
      overflow-y: auto;

      .panel-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        h3 {
          margin: 0;
        }
      }
    }

    .right-panel {
      flex: 1;
      background: #fff;
      border: 1px solid #e4e7ed;
      border-radius: 4px;
      padding: 20px;

      .permission-detail {
        h3 {
          margin-top: 0;
        }
      }

      .empty-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
        color: #909399;

        i {
          font-size: 64px;
          margin-bottom: 20px;
        }

        p {
          font-size: 16px;
        }
      }
    }
  }
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>
