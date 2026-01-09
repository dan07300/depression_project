<template>
	<!-- ...existing code... -->
	<div class="header-logo">
		<!-- 使用 public 下的 logo-new.png（也可改为 /logo.png 覆盖原文件） -->
		<img class="logo" src="E:/study/AAA-yanjiusheng/depression_project/frontend/public/logo-new.png" alt="应用 Logo" />
	</div>
	<!-- ...existing code... -->
</template>

<script>
export default {
	name: 'AppHeader',
	// ...existing code...
}
</script>

<style scoped>
/* ...existing code... */
.header-logo .logo {
	height: 36px; /* 如需更大或更小可调整此值 */
	display: block;
}
</style>