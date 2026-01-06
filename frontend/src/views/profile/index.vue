<template>
  <div class="profile-container">
    <div class="header-section">
      <div class="avatar-section">
        <el-avatar :size="80" :icon="'el-icon-user-solid'" style="background-color: #409eff;"></el-avatar>
      </div>
      <div class="info-section">
        <h2>{{ userInfo.realName || '系统管理员' }}</h2>
        <p>{{ getRoleTypeName(userInfo.roleType) }}</p>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-check" @click="handleSave">保存修改</el-button>
      </div>
    </div>

    <div class="content-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <div slot="header" class="card-header">
              <i class="el-icon-user"></i>
              <span>基本信息</span>
            </div>
            <el-form ref="form" :model="form" label-width="100px">
              <el-form-item label="用户名">
                <el-input v-model="form.username" :disabled="true">
                  <i slot="prefix" class="el-icon-user"></i>
                </el-input>
              </el-form-item>
              <el-form-item label="真实姓名" required>
                <el-input v-model="form.realName">
                  <i slot="prefix" class="el-icon-user"></i>
                </el-input>
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="form.phone">
                  <i slot="prefix" class="el-icon-phone"></i>
                </el-input>
                <el-button size="mini" style="margin-left: 10px;">修改</el-button>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="form.email">
                  <i slot="prefix" class="el-icon-message"></i>
                </el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="form.gender">
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="0">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card>
            <div slot="header" class="card-header">
              <i class="el-icon-setting"></i>
              <span>账号信息</span>
            </div>
            <el-form :model="form" label-width="100px">
              <el-form-item label="角色类型">
                <el-tag :type="getRoleTypeTag(userInfo.roleType)">
                  {{ getRoleTypeName(userInfo.roleType) }}
                </el-tag>
              </el-form-item>
              <el-form-item label="注册时间">
                <span>{{ userInfo.createTime || '2025/12/10' }}</span>
              </el-form-item>
              <el-form-item label="账号状态">
                <el-tag type="success">
                  <i class="el-icon-success"></i>
                  正常
                </el-tag>
              </el-form-item>
              <el-form-item label="安全设置">
                <div class="security-setting">
                  <p>定期更换密码可以保护账号安全</p>
                  <el-button size="mini">修改</el-button>
                </div>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getUserById, updateProfile } from '@/api'

export default {
  name: 'Profile',
  data() {
    return {
      userInfo: {},
      form: {
        username: '',
        realName: '',
        phone: '',
        email: '',
        gender: 1
      }
    }
  },
  created() {
    this.fetchUserInfo()
  },
  methods: {
    async fetchUserInfo() {
      const userInfo = this.$store.getters.userInfo
      if (userInfo && userInfo.id) {
        try {
          const response = await getUserById(userInfo.id)
          this.userInfo = response.data || {}
          this.form = {
            username: this.userInfo.username || '',
            realName: this.userInfo.realName || '',
            phone: this.userInfo.phone || '',
            email: this.userInfo.email || '',
            gender: this.userInfo.gender !== undefined ? this.userInfo.gender : 1
          }
        } catch (error) {
          this.$message.error('获取用户信息失败')
        }
      }
    },
    getRoleTypeName(roleType) {
      const map = {
        1: '系统管理员',
        2: '医院管理员',
        3: '医生'
      }
      return map[roleType] || '未知'
    },
    getRoleTypeTag(roleType) {
      const map = {
        1: 'danger',
        2: 'warning',
        3: 'primary'
      }
      return map[roleType] || 'info'
    },
    async handleSave() {
      try {
        await updateProfile(this.userInfo.id, this.form)
        this.$message.success('保存成功')
        this.fetchUserInfo()
      } catch (error) {
        this.$message.error('保存失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;

  .header-section {
    display: flex;
    align-items: center;
    padding: 30px;
    background: #fff;
    border-radius: 8px;
    margin-bottom: 20px;

    .avatar-section {
      margin-right: 20px;
    }

    .info-section {
      flex: 1;

      h2 {
        margin: 0 0 10px 0;
        font-size: 24px;
      }

      p {
        margin: 0;
        color: #909399;
      }
    }
  }

  .content-section {
    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: bold;

      i {
        font-size: 18px;
      }
    }

    .security-setting {
      display: flex;
      justify-content: space-between;
      align-items: center;

      p {
        margin: 0;
        color: #606266;
      }
    }
  }
}
</style>
