<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="copyright">
        <a
          href="https://github.com/wyldsg666/ai-code-monther"
          target="_blank"
          rel="noopener noreferrer"
          class="author-link"
        >
          GitHub 项目 AI 零代码生成平台
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// 无需额外的响应式数据
</script>

<style scoped>
.footer {
  position: fixed;
  bottom: 0;
  left: 0;
  width: calc(100vw - 14px);
  z-index: 1000;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  text-align: center;
  padding: 20px;
  border-top: 1px solid rgba(102, 126, 234, 0.1);
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.06);
  border-radius: 12px 12px 0 0;
}

.copyright {
  margin: 0;
  color: #666;
  font-size: 14px;
}
</style>
