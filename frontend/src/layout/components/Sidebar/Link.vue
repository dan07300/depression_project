<template>
  <component :is="linkProps(to).tag" v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script>
import { isExternal } from '@/utils/validate'

export default {
  props: {
    to: {
      type: String,
      required: true
    }
  },
  methods: {
    linkProps(url) {
      if (isExternal(url)) {
        return {
          tag: 'a',
          href: url,
          target: '_blank',
          rel: 'noopener'
        }
      }
      return {
        tag: 'router-link',
        to: url
      }
    }
  }
}
</script>
