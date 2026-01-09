<template>
  <div class="login-container">
    <!-- 装饰元素（P1风格） -->
    <div class="decorate-circle"></div>
    <div class="decorate-line"></div>

    <!-- 登录框外层容器（居中） -->
    <div class="login-box-wrapper">
      <!-- 顶部图标+标题（P1风格） -->
      <div class="login-header">
        <div class="logo-icon">
          <div class="circle-out">
            <div class="circle-in">
              <i class="el-icon-lightbulb"></i>
            </div>
          </div>
        </div>
        <h2 class="login-title">欢迎回来</h2>
        <p class="login-subtitle">请选择登录方式</p>
      </div>


      <!-- 登录方式切换（优化后） -->
      <div class="login-type-tabs">
        <div 
          class="login-tab" 
          :class="{ active: activeType === 'account' }"
          @click="activeType = 'account'"
        >
          <i class="el-icon-user"></i>
          <span>账号登录</span>
        </div>
        <div 
          class="login-tab" 
          :class="{ active: activeType === 'sms' }"
          @click="activeType = 'sms'"
        >
          <i class="el-icon-mobile-phone"></i>
          <span>短信登录</span>
        </div>
      </div>

      <!-- 账号登录表单（保留原有逻辑） -->
      <el-form 
        v-if="activeType === 'account'"
        ref="loginForm" 
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form" 
        auto-complete="on"
        label-position="left"
      >
        <el-form-item prop="username">
          <span class="svg-container">
            <i class="el-icon-user"></i>
          </span>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>

        <el-form-item prop="password">
          <span class="svg-container">
            <i class="el-icon-lock"></i>
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <i :class="passwordType === 'password' ? 'el-icon-view' : 'el-icon-hide'"></i>
          </span>
        </el-form-item>

        <el-form-item class="remember-item">
          <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
        </el-form-item>

        <el-button 
          :loading="loading" 
          type="primary" 
          style="width:100%;" 
          @click.native.prevent="handleLogin"
        >
          登录
        </el-button>
      </el-form>

      <!-- 短信登录表单（仅UI，无功能） -->
      <el-form 
        v-if="activeType === 'sms'"
        ref="smsForm" 
        :model="smsForm" 
        :rules="smsRules" 
        class="login-form" 
        auto-complete="on"
      >
        <el-form-item prop="phone">
          <span class="svg-container">
            <i class="el-icon-mobile-phone"></i>
          </span>
          <el-input
            v-model="smsForm.phone"
            placeholder="手机号"
            name="phone"
            type="text"
          />
        </el-form-item>

        <el-form-item prop="code">
          <span class="svg-container">
            <i class="el-icon-key"></i>
          </span>
          <el-input
            v-model="smsForm.code"
            placeholder="验证码"
            name="code"
            type="text"
          />
          <span class="get-code" @click="getCode">
            获取验证码
          </span>
        </el-form-item>

        <el-button 
          type="primary" 
          style="width:100%;" 
          @click="handleSmsLogin"
        >
          登录
        </el-button>
      </el-form>

      <!-- 测试账号区域（P1风格） -->
      <div class="test-account-area">
        <p class="test-title">测试账号</p>
        <div class="account-grid">
          <div class="account-item" @click="fillAccount('admin', 'admin123')">
            <p class="account-role">管理员</p>
            <p class="account-name">admin</p>
          </div>
          <div class="account-item" @click="fillAccount('hospital_admin1', 'hospital123')">
            <p class="account-role">医院管理</p>
            <p class="account-name">hospital_admin1</p>
          </div>
          <div class="account-item" @click="fillAccount('doctor001', 'doctor123')">
            <p class="account-role">医生</p>
            <p class="account-name">doctor001</p>
          </div>
          <div class="account-item" @click="fillAccount('patient001', 'patient123')">
            <p class="account-role">患者</p>
            <p class="account-name">patient001</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    return {
      activeType: 'account', // 当前登录方式（默认账号登录）
      loginForm: {
        username: 'admin',
        password: 'admin123',
        remember: false
      },
      smsForm: {
        phone: '',
        code: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      smsRules: {
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    // 原有密码显示/隐藏方法
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    // 原有登录方法（未修改）
// 原有登录方法（修改后）
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          // 1. 执行登录
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            // 2. 登录成功后，立即拉取用户信息（新增关键代码）
            this.$store.dispatch('user/getInfo').then(() => {
              // 3. 拉取信息成功后再跳转

              this.$router.push({ path: this.redirect || '/' })
            }).catch(() => {
              // 拉取信息失败的兜底处理
              this.$message.error('获取用户信息失败，请重新登录')
              this.loading = false
            })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 短信登录（仅占位）
    handleSmsLogin() {
      this.$message.info('短信登录功能暂未开发')
    },
    // 获取验证码（仅占位）
    getCode() {
      this.$message.info('验证码功能暂未开发')
    },
    // 填充测试账号（P1功能）
    fillAccount(username, password) {
      this.activeType = 'account'
      this.loginForm.username = username
      this.loginForm.password = password
    }
  }
}
</script>

<style lang="scss" scoped>
// P1风格色系
$primary: #409eff;
$light-blue: #e8f3ff;
$light-gray: #f5f7fa;
$text-main: #333;
$text-sub: #999;
$border-color: #e5e7eb;

.login-container {
  width: 100vw;
  height: 100vh;
  background: url('~@/assets/images/login-bg.jpg') no-repeat center center;
  background-size: cover;
  background-attachment: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;

  // P1装饰元素
  .decorate-circle {
    position: absolute;
    top: 15%;
    right: 10%;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: $primary;
    opacity: 0.5;
  }
  .decorate-line {
    position: absolute;
    top: 10%;
    right: 0;
    width: 300px;
    height: 300px;
    border: 2px solid $light-blue;
    border-radius: 50%;
    transform: translate(50%, -50%);
    opacity: 0.5;
  }

  // 登录框容器（P1风格）
  .login-box-wrapper {
    width: 420px;
    max-width: 90%;
    background-color: #fff;
    border-radius: 12px;
    padding: 30px 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
    position: relative;
    z-index: 1;
  }

  // 登录框头部（P1风格）
  .login-header {
    text-align: center;
    margin-bottom: 20px;

    .logo-icon {
      width: 60px;
      height: 60px;
      margin: 0 auto 15px;
      .circle-out {
        width: 100%;
        height: 100%;
        border: 2px solid $light-blue;
        border-radius: 50%;
        display: flex;
        justify-content: center;
        align-items: center;
        .circle-in {
          width: 40px;
          height: 40px;
          border-radius: 50%;
          background: linear-gradient(120deg, $light-blue, $primary);
          display: flex;
          justify-content: center;
          align-items: center;
          color: #fff;
          font-size: 20px;
        }
      }
    }

    .login-title {
      font-size: 22px;
      color: $text-main;
      margin: 0 0 8px;
      font-weight: 600;
    }
    .login-subtitle {
      font-size: 14px;
      color: $text-sub;
      margin: 0;
    }
  }

  // 登录方式切换（优化后）
  .login-type-tabs {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-bottom: 25px;
    width: 100%;

    .login-tab {
      flex: 1;
      max-width: 160px;
      padding: 10px 0;
      border-radius: 8px;
      text-align: center;
      font-size: 14px;
      color: $text-sub;
      background-color: $light-gray;
      cursor: pointer;
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 6px;

      // 未选中态
      &:not(.active):hover {
        background-color: $light-blue;
        color: $primary;
      }

      // 选中态（突出显示）
      &.active {
        background-color: $primary;
        color: #fff;
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
      }

    // 图标样式
    i {
      font-size: 16px;
    }
  }
}

  // 表单样式（保留原有逻辑，优化UI）
  .login-form {
    .el-form-item {
      margin-bottom: 15px;
      position: relative;

      .svg-container {
        position: absolute;
        left: 10px;
        top: 50%;
        transform: translateY(-50%);
        color: $text-sub;
        width: 20px;
        display: inline-block;
      }

      .el-input {
        padding-left: 35px;
        input {
          border-radius: 8px;
          border-color: $border-color;
        }
      }

      .show-pwd, .get-code {
        position: absolute;
        right: 10px;
        top: 50%;
        transform: translateY(-50%);
        font-size: 14px;
        color: $text-sub;
        cursor: pointer;
        user-select: none;
      }
      .get-code {
        color: $primary;
      }
    }

    .remember-item {
      .el-checkbox {
        font-size: 14px;
        color: $text-sub;
      }
    }

    .el-button {
      border-radius: 8px;
      padding: 10px 0;
      background: linear-gradient(120deg, #409eff, #66b1ff);
      border: none;
    }
  }

  // 测试账号区域（P1风格）
  .test-account-area {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid $border-color;

    .test-title {
      font-size: 14px;
      color: $text-sub;
      text-align: center;
      margin-bottom: 15px;
    }

    .account-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 10px;
    }

    .account-item {
      padding: 10px;
      background-color: $light-gray;
      border-radius: 8px;
      text-align: center;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: $light-blue;
      }

      .account-role {
        font-size: 13px;
        color: $text-main;
        margin: 0 0 5px;
      }
      .account-name {
        font-size: 12px;
        color: $text-sub;
        margin: 0;
      }
    }
  }
}
</style>