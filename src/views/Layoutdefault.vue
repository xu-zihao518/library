<template>
  <div class="welcome-page">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="logo">
        <el-icon><Notebook /></el-icon>
        <span>欢迎来到图书管理系统</span>
      </div>
    </header>

    <!-- 主要内容区 -->
    <main class="main-content">
      <div class="welcome-section">
        <h1>欢迎来到图书管理系统</h1>
        <p class="subtitle">轻松管理您的图书资源</p>
        
        <div class="feature-cards">
          <div class="feature-card">
            <el-icon><Search /></el-icon>
            <h3>图书检索</h3>
            <p>快速查找您需要的图书</p>
          </div>
          
          <div class="feature-card">
            <el-icon><Reading /></el-icon>
            <h3>借阅管理</h3>
            <p>便捷的借阅归还系统</p>
          </div>
          
          <div class="feature-card">
            <el-icon><User /></el-icon>
            <h3>用户中心</h3>
            <p>个人借阅记录管理</p>
          </div>
        </div>
        
        <el-button type="primary" size="large" @click="goToBooks">
          开始使用
        </el-button>
      </div>
    </main>


  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { 
  Notebook, 
  Search, 
  Reading, 
  User 
} from '@element-plus/icons-vue'

const router = useRouter()

const goToBooks = () => {
  router.push('/Layout/Addbook')
}
</script>

<style scoped>
.welcome-page {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: 'Helvetica Neue', Arial, sans-serif;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #2c3e50;
  color: white;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 1.5rem;
  font-weight: bold;
}

.logo .el-icon {
  margin-right: 0.5rem;
  font-size: 1.8rem;
}

.main-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  background-color: #f5f7fa;
}

.welcome-section {
  text-align: center;
  max-width: 800px;
}

.welcome-section h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: #2c3e50;
}

.subtitle {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin-bottom: 3rem;
}

.feature-cards {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 2rem;
  margin-bottom: 3rem;
}

.feature-card {
  flex: 1;
  min-width: 200px;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card .el-icon {
  font-size: 2.5rem;
  color: #3498db;
  margin-bottom: 1rem;
}

.feature-card h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

.feature-card p {
  color: #7f8c8d;
  font-size: 0.9rem;
}

.footer {
  text-align: center;
  padding: 1rem;
  background-color: #2c3e50;
  color: white;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header {
    flex-direction: column;
    padding: 1rem;
  }
  
  .logo {
    margin-bottom: 1rem;
  }
  
  .welcome-section h1 {
    font-size: 2rem;
  }
  
  .feature-cards {
    flex-direction: column;
    align-items: center;
  }
  
  .feature-card {
    width: 80%;
  }
}
</style>