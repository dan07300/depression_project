# 心理健康平台 - 前端项目

## 📋 项目简介

青少年心理健康促进管理平台的前端部分，基于 Vue 2 + Element UI 构建的现代化管理后台。

## 🛠 技术栈

- **Vue**: 2.6.10
- **Element UI**: 2.13.2
- **Vue Router**: 3.0.6
- **Vuex**: 3.1.0
- **Axios**: 0.18.1
- **ECharts**: 5.6.0
- **Vue CLI**: 4.4.4

## 📁 项目结构

```
src/
├── api/                  # API接口封装
│   └── index.js         # 所有API接口定义
├── assets/              # 静态资源
├── components/          # 公共组件
│   ├── Breadcrumb/      # 面包屑导航
│   ├── Hamburger/       # 菜单折叠按钮
│   ├── Pagination/      # 分页组件
│   └── SvgIcon/         # SVG图标组件
├── icons/               # 图标资源
├── layout/              # 布局组件
│   ├── components/      # 布局子组件
│   │   ├── AppMain.vue  # 主内容区
│   │   ├── Navbar.vue   # 顶部导航栏
│   │   └── Sidebar/      # 侧边栏
│   └── index.vue        # 布局主组件
├── router/              # 路由配置
│   └── index.js         # 路由定义
├── store/               # Vuex状态管理
│   ├── modules/         # 状态模块
│   │   ├── app.js       # 应用状态
│   │   ├── user.js      # 用户状态
│   │   └── settings.js  # 设置状态
│   ├── getters.js       # Getters
│   └── index.js         # Store入口
├── styles/              # 样式文件
│   ├── index.scss       # 全局样式
│   ├── sidebar.scss     # 侧边栏样式
│   └── variables.scss   # 变量定义
├── utils/               # 工具函数
│   ├── auth.js          # 认证相关
│   ├── request.js       # Axios封装
│   ├── validate.js      # 验证函数
│   └── index.js         # 其他工具
├── views/               # 页面组件
│   ├── login/           # 登录页
│   ├── dashboard/       # 首页
│   ├── hospital/        # 医院管理
│   ├── hospital-admin/  # 医院管理员管理
│   ├── doctor/          # 医生管理
│   ├── patient/         # 患者管理
│   ├── warning/         # 预警管理
│   ├── permission/      # 权限管理
│   ├── scale/           # 量表管理
│   ├── user/            # 用户管理
│   └── profile/         # 个人信息
├── App.vue              # 根组件
├── main.js              # 入口文件
├── permission.js        # 权限控制
└── settings.js          # 配置文件
```

## 🚀 快速开始

### 环境要求

- Node.js 12+
- npm 6+ 或 yarn 1.22+

### 安装依赖

```bash
npm install
# 或
yarn install
```

### 开发

```bash
npm run dev
# 或
yarn dev
```

访问 http://localhost:9528

### 构建

```bash
# 生产环境构建
npm run build:prod

# 测试环境构建
npm run build:stage
```

构建产物在 `dist/` 目录下。

### 代码检查

```bash
npm run lint
```

## 🔧 配置说明

### 环境变量

- `.env.development` - 开发环境配置
- `.env.production` - 生产环境配置

### 代理配置

在 `vue.config.js` 中配置后端API代理：

```javascript
devServer: {
  proxy: {
    '/api': {
      target: 'http://localhost:8082',
      changeOrigin: true
    }
  }
}
```

### API接口

所有API接口定义在 `src/api/index.js` 中，包括：

- 认证相关：登录、修改密码
- 首页统计：获取统计数据
- 医院管理：CRUD操作
- 用户管理：用户增删改查、权限分配
- 患者管理：患者管理、迁移
- 预警管理：预警查看、处理
- 权限管理：权限树查询
- 量表管理：题目管理

## 📱 页面说明

### 1. 登录页 (`/login`)
- 用户名密码登录
- 支持记住登录状态

### 2. 首页 (`/dashboard`)
- 系统概览统计
- 患者风险等级分布（饼图）
- 近半年趋势分析（折线图）

### 3. 医院管理 (`/hospital/list`)
- 医院信息管理
- 支持搜索和筛选

### 4. 医院管理员管理 (`/hospital-admin/list`)
- 管理员账号管理
- 权限分配功能

### 5. 医生管理 (`/doctor/list`)
- 医生信息管理
- 支持按医院筛选

### 6. 患者管理 (`/patient/list`)
- 患者信息管理
- 患者迁移功能
- 多条件查询

### 7. 预警管理 (`/warning/list`)
- 预警记录查看
- 预警处理功能

### 8. 权限管理 (`/permission/tree`)
- 权限树结构展示
- 权限详情查看

### 9. 量表管理 (`/scale/question`)
- 量表题目管理
- 题目选项配置
- 左右分栏布局

### 10. 用户管理 (`/user/list`)
- 所有用户统一管理
- 支持多条件筛选

### 11. 个人信息 (`/profile/index`)
- 个人信息查看和编辑
- 密码修改

## 🎨 组件使用

### 分页组件

```vue
<pagination
  :total="total"
  :page.sync="queryParams.current"
  :limit.sync="queryParams.size"
  @pagination="fetchData"
/>
```

### 表格组件

```vue
<el-table :data="tableData" border>
  <el-table-column prop="name" label="姓名" />
  <el-table-column label="操作">
    <template slot-scope="scope">
      <el-button @click="handleEdit(scope.row)">编辑</el-button>
    </template>
  </el-table-column>
</el-table>
```

### 表单组件

```vue
<el-form ref="form" :model="form" :rules="rules">
  <el-form-item label="用户名" prop="username">
    <el-input v-model="form.username" />
  </el-form-item>
</el-form>
```

## 🔐 权限控制

系统使用路由守卫进行权限控制，在 `src/permission.js` 中实现：

- 登录状态检查
- Token验证
- 路由权限控制

## 📦 构建部署

### 开发环境

```bash
npm run dev
```

### 生产环境

```bash
npm run build:prod
```

将 `dist/` 目录部署到 Nginx 或其他 Web 服务器。

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/frontend/dist;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8082;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 🐛 常见问题

### 1. 依赖安装失败
- 使用 `npm install --registry=https://registry.npmmirror.com`
- 或使用 `yarn install`

### 2. 页面空白
- 检查浏览器控制台错误
- 确认后端服务已启动
- 检查API接口地址配置

### 3. 路由跳转404
- 检查路由配置是否正确
- 确认 Nginx 配置了 `try_files`

## 📝 开发规范

1. **命名规范**：
   - 组件名使用 PascalCase
   - 文件名使用 kebab-case
   - 变量名使用 camelCase

2. **代码风格**：
   - 使用 ESLint 进行代码检查
   - 遵循 Vue 官方风格指南

3. **提交规范**：
   - feat: 新功能
   - fix: 修复bug
   - docs: 文档更新
   - style: 代码格式
   - refactor: 重构

## 📚 相关文档

- [Vue 2 文档](https://cn.vuejs.org/)
- [Element UI 文档](https://element.eleme.cn/)
- [Vue Router 文档](https://router.vuejs.org/zh/)
- [Vuex 文档](https://vuex.vuejs.org/zh/)
- [ECharts 文档](https://echarts.apache.org/zh/index.html)

---

**最后更新**: 2025年1月
