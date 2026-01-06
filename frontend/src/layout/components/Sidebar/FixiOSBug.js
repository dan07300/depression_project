export default {
  mounted() {
    this.fixBugIniOS()
  },
  methods: {
    fixBugIniOS() {
      const ua = navigator.userAgent
      const isIOS = /iPad|iPhone|iPod/.test(ua)
      if (isIOS) {
        const subMenu = this.$refs.subMenu
        if (subMenu) {
          subMenu.$on('mouseenter', function() {
            this.$el.querySelector('.el-menu').style.display = 'none'
            setTimeout(() => {
              this.$el.querySelector('.el-menu').style.display = 'block'
            }, 300)
          })
        }
      }
    }
  }
}
