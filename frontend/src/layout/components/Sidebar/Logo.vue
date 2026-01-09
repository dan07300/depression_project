<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <!-- 折叠状态：仅显示图标，和侧边栏其他图标位置对齐 -->
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <i class="el-icon-lightbulb sidebar-icon"></i>
      </router-link>
      <!-- 展开状态：图标居左（和其他菜单图标对齐）+ 文字 -->
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <i class="el-icon-lightbulb sidebar-icon"></i>
        <span class="sidebar-title">心理健康平台</span>
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b3f56;
  text-align: left; /* 改为左对齐，匹配侧边栏菜单 */
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: flex-start; /* 内容左对齐 */
    padding: 0 20px; /* 和侧边栏其他菜单的内边距保持一致 */

    & .sidebar-icon {
      color: #fff; /* 匹配侧边栏其他图标颜色 */
      font-size: 18px; /* 和侧边栏其他图标大小一致 */
      margin-right: 10px; /* 图标和文字的间距，匹配菜单样式 */
      flex-shrink: 0; /* 防止图标被压缩 */
    }

    & .sidebar-title {
      color: #fff;
      font-weight: 600;
      font-size: 16px;
      white-space: nowrap;
    }
  }

  &.collapse {
    .sidebar-logo-link {
      justify-content: center; /* 折叠时图标居中 */
      padding: 0; /* 折叠时取消内边距 */
      
      .sidebar-icon {
        margin-right: 0; /* 折叠时取消图标右边距 */
      }
    }
  }
}
</style>