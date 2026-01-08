<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <router-link class="sidebar-logo-link" to="/">
      <span class="bulb-icon">💡</span>
      <span class="sidebar-title">心理健康平台</span>
    </router-link>
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
.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  background: #2b3f56;
  text-align: center;
  overflow: visible; /* 取消溢出隐藏，避免图标被裁 */

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 10px;

    /* 灯泡图标：强制显示 + 样式强化 */
    & .bulb-icon {
      font-size: 18px !important; 
      color: #ffd04b !important; /* 黄色，确保醒目 */
      margin-right: 8px !important;
      visibility: visible !important;
    }

    & .sidebar-title {
      color: #fff;
      font-weight: 600;
      font-size: 16px;
    }
  }

  /* 折叠态仅调整宽度，不隐藏内容 */
  &.collapse {
    .sidebar-logo-link {
      width: 100%;
    }
  }
}
</style>