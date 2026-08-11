<template>
  <a-layout-footer class="footer">
    <div class="footer-content">
      <p class="copyright">
        <a
          href="https://github.com/Zonest798/ai-code-monther"
          target="_blank"
          rel="noopener noreferrer"
          class="author-link"
        >
          @Copyright 2026 - Zonest798 AI Code Monther. All rights reserved.
        </a>
      </p>
      <p class="beian">
        <a
          href="https://beian.miit.gov.cn"
          target="_blank"
          rel="noopener noreferrer"
          class="beian-link"
        >
          皖ICP备2026020889-1号
        </a>
        <a
          href="https://beian.mps.gov.cn/#/query/webSearch?code=34012302001395"
          rel="noreferrer"
          target="_blank"
          class="beian-link"
        >
          <img :src="beiantubiao" alt="备案图标" class="beian-img" />
          皖公网安备34012302001395号
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
import beiantubiao from "../assets/beiantubiao.png";
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

.copyright,
.beian {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.beian-link {
  color: #666;
  text-decoration: none;
}

.beian-link:hover {
  color: #667eea;
}

.beian-img {
  width: 18px;
  height: auto;
  vertical-align: middle;
  margin-right: 6px;
}
</style>
