<template>
  <component :is="type" v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: String,
      required: false, // 改成 false，允许为空
      default: '/'     // 加上默认值
    }
  },
  computed: {
    isExternal() {
      return isExternal(this.to)
    },
    type() {
      if (this.isExternal) {
        return 'a'
      }
      return 'router-link'
    }
  },
  methods: {
    // 这里就是你找不到的 methods，我们把它补上
    linkProps(to) {
      if (this.isExternal) {
        return {
          href: to,
          target: '_blank',
          rel: 'noopener'
        }
      }
      return {
        // 如果 to 是空的，就给它一个斜杠 '/'，这样就不会报错了
        to: to || '/'
      }
    }
  }
}
</script>