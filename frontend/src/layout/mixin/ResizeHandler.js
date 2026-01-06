import { debounce } from '@/utils'

export default {
  data() {
    return {
      $_resizeHandler: null
    }
  },
  mounted() {
    this.$_resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.$_resizeHandler)
  },
  beforeDestroy() {
    if (!this.$_resizeHandler) return
    window.removeEventListener('resize', this.$_resizeHandler)
  },
  activated() {
    if (this.$_resizeHandler) {
      window.addEventListener('resize', this.$_resizeHandler)
    }
  },
  deactivated() {
    if (this.$_resizeHandler) {
      window.removeEventListener('resize', this.$_resizeHandler)
    }
  }
}
