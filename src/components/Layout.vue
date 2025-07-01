<template>
  <el-container class="layout-container">
    <!-- 顶部导航 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo">
          <el-icon><Reading /></el-icon>
          <span>图书管理系统</span>
        </div>
        <div class="user-info">
          <span class="welcome-text">欢迎 {{ username }} {{ identifiy }}</span>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar size="small" src="https://picsum.photos/id/1005/40/40"></el-avatar>
              <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  <span>基本信息</span>
                </el-dropdown-item>
                <el-dropdown-item command="password" >
                  <el-icon><EditPen /></el-icon>
                  <span>重置密码</span>
                </el-dropdown-item>
                <el-dropdown-divider></el-dropdown-divider>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </el-header>
    
    <!-- 中间内容区 -->
    <el-container class="middle-container">
      <!-- 侧边栏折叠按钮 -->
      <div class="sidebar-toggle" @click="toggleSidebar">
        <el-icon v-if="!isCollapsed"><ArrowLeft /></el-icon>
        <el-icon v-else><ArrowRight /></el-icon>
      </div>
      
      <!-- 侧边栏 -->
      <el-aside width="220px" class="sidebar">
        <el-menu 
          default-active="1" 
          class="el-menu-vertical"
          router
          :collapse="isCollapsed"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
          
          <el-sub-menu index="">
            <template #title>
              <el-icon><Reading /></el-icon>
              <span>图书管理</span>
            </template>
            <el-menu-item index="/Layout/Addbook">
              <el-icon><CirclePlus /></el-icon>
              <span>添加图书</span>
            </el-menu-item>
            <el-menu-item index="/Layout/Selectbook">
              <el-icon><Search /></el-icon>
              <span>查询图书</span>
            </el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/Layout/Usercenter">
            <el-icon><User /></el-icon>
            <span>个人中心</span>
          </el-menu-item>
          
          <el-sub-menu index="">
            <template #title>
              <el-icon><Refresh /></el-icon>
              <span>我的还借</span>
            </template>
            <el-menu-item index="/Layout/Borsituation">
              <el-icon><TakeawayBox /></el-icon>
              <span>借书情况</span>
            </el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/Layout/Bookclassification">
            <el-icon><Folder /></el-icon>
            <span>图书分类</span>
          </el-menu-item>
          
          <el-menu-item index="/Layout/Changepassword">
            <el-icon><Key /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view></router-view>
      </el-main>
    </el-container>
    
    <!-- 底部页脚 -->
    <el-footer class="footer">
      <div class="footer-content">
        <span>模拟图书管理系统 @2025 by 许梓浩</span>
      </div>
    </el-footer>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import { 
  Reading, User, Refresh, Search, Key, CirclePlus, Folder, 
  TakeawayBox, Back, CaretBottom, SwitchButton, ArrowLeft, ArrowRight,
  EditPen
} from '@element-plus/icons-vue';
import {userGetInfo} from '@/api/user.js'
import { onMounted } from 'vue';
import { ElMessage,ElMessageBox} from 'element-plus';

import { useRouter } from 'vue-router'
import { useTokenStore} from '@/stores/token.js'
const token=useTokenStore()
const router = useRouter()

const username = ref('访客');
const identifiy = ref('');

onMounted(() => {
  getUserInfo();
});

const getUserInfo = async () => {
  try {
    const result = await userGetInfo();
    if (result.code === 0 && result.data.username) {
      username.value = result.data.username;
      if(result.data.isAdmin === 0){
        identifiy.value = '学生';
      } else {
        identifiy.value = '管理员';
      }
    } else {
      console.log(result);
      ElMessage.error('获取用户信息失败：' + result.msg);
    }
  } catch (error) {
    ElMessage.error('网络请求失败，请重试');
    console.error(error);
  }
};

// 侧边栏折叠状态
const isCollapsed = ref(false);

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      // 1. 显示确认对话框
      await ElMessageBox.confirm(
        '确定要退出登录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ) 
      token.removeToken();

    await router.push('/'); 

    // 4. 提示成功
    ElMessage.success('退出登录成功');
  } catch (error) {
    // 处理用户取消或异常
    if (error === 'cancel') {
      ElMessage.info('已取消退出');
    } else {
      ElMessage.error('退出失败，请重试');
    }
  }
}

  // 其他命令处理（如跳转页面）
  if (command === 'password') {
    router.push('/Layout/Changepassword');
  }
  if (command === 'profile') {
    router.push('/Layout/Usercenter');
  }
};

const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value;
};
const gotochangepassword =()=>{
   router.push('/Layout/Changepassword')
}
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.header {
  background-color: #2c3e50;
  color: white;
  height: 60px;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  color: white;
  gap: 4px;
}

/* 中间内容区样式 */
.middle-container {
  flex: 1;
  display: flex;
  overflow: hidden;
  position: relative;
}

.sidebar-toggle {
  position: absolute;
  left: 220px;
  top: 20px;
  z-index: 100;
  background: #545c64;
  color: white;
  padding: 4px;
  border-radius: 4px;
  cursor: pointer;
  display: none;
  transform: translateX(0);
  transition: transform 0.3s ease;
}

.sidebar {
  background-color: #545c64;
  transition: width 0.3s ease;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  z-index: 50;
}

.el-menu-vertical {
  height: 100vh;
}

/* 主内容区样式 */
.main-content {
  flex: 1;
  padding: 20px;
  background-color: #f0f2f5;
  overflow-y: auto;
  max-width: 1200px;
  margin: 0 auto;
  min-height: calc(100vh - 100px); /* 60px header + 40px footer */
}

/* 页脚样式 */
.footer {
  background-color: #2c3e50;
  color: white;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}

/* 响应式：移动端适配 */
@media (max-width: 768px) {
  .sidebar-toggle {
    display: block;
    left: 20px;
  }

  .sidebar {
    position: fixed;
    z-index: 1000;
    width: 220px !important; /* 强制展开 */
    transform: translateX(-220px); /* 初始隐藏在左侧 */
    transition: transform 0.3s ease;
  }

  .is-collapsed .sidebar {
    transform: translateX(0); /* 展开时显示 */
  }

  .main-content {
    padding: 16px;
  }
}
</style>