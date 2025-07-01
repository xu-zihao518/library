<template>
  <div class="user-info-container">
    <el-card class="user-card">
      <!-- 头像区域 -->
      <div class="avatar-section">
        <el-avatar 
          size="large" 
          class="user-avatar"
        >
          <!-- 当没有头像时显示首字母 -->
          <template #default>
            {{ userInfo.username.slice(0, 1) }}
          </template>
        </el-avatar>
        <!-- 身份标签 -->
        <el-tag 
          :type="userInfo.isAdmin === 1 ? 'success' : 'info'"
          class="identity-tag"
        >
          {{ userInfo.isAdmin === 1 ? '管理员' : '学生' }}
        </el-tag>
      </div>

      <!-- 详细信息区域 -->
      <div class="info-section">
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="用户名">
              <span class="info-value">{{ userInfo.username }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户ID">
              <span class="info-value">{{ userInfo.userId }}</span>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 扩展信息（可折叠） -->
        <el-collapse v-model="isCollapsed" class="expand-info">
          <el-collapse-item title="更多信息">
            <el-form>
              <el-form-item label="身份标识">
                <span class="info-value">
                  {{ userInfo.isAdmin }} 
                  <small>(0=学生，1=管理员)</small>
                </span>
              </el-form-item>
            </el-form>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElAvatar, ElTag, ElRow, ElCol, ElForm, ElFormItem, ElCollapse, ElCollapseItem } from 'element-plus';
import {userGetInfo} from '@/api/user.js'
import { onMounted } from 'vue';
onMounted(()=>{
  getUserInfo();
})
// 模拟用户信息（实际可通过 props 或接口获取）
const userInfo = ref({
    username: '',
    userId: '',
    isAdmin: 0,
});

// 折叠状态
const isCollapsed = ref(true);

const getUserInfo = async () => {
  try {
    const result = await userGetInfo();
    if (result.code === 0) {
      userInfo.value.username = result.data.username;
      userInfo.value.userId=result.data.userId;
      userInfo.value.isAdmin=result.data.isAdmin;
    } else {
      console.log(result);
      ElMessage.error('获取用户信息失败：' + result.msg);
    }
  } catch (error) {
    ElMessage.error('网络请求失败，请重试');
    console.error(error);
  }
};

</script>

<style scoped>
/* 主容器样式 - 与图书管理组件保持一致 */
.user-info-container {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

/* 卡片样式 - 统一阴影和圆角 */
.user-card {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 12px;
  padding: 24px;
  transition: all 0.3s ease;
}

/* 头像区域 - 与图书管理组件的标题区域对齐 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

/* 头像样式 - 保持与操作按钮相同的蓝色系 */
.user-avatar {
  width: 120px;
  height: 120px;
  font-size: 48px;
  background-color: #409eff;
  color: white;
  border: 4px solid #fff;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
}

/* 身份标签 - 与库存状态标签风格一致 */
.identity-tag {
  font-size: 1em;
  padding: 6px 16px;
  font-weight: 500;
}

/* 信息区域 - 与图书表单相同的布局结构 */
.info-section {
  width: 100%;
}

/* 表单项 - 与图书表单相同的间距 */
.el-form-item {
  margin-bottom: 16px;
}

/* 标签样式 - 与图书表单标签一致 */
.el-form-item :deep(.el-form-item__label) {
  font-size: 0.95em;
  color: #666;
  font-weight: 500;
}

/* 信息值 - 与图书信息相同的字体风格 */
.info-value {
  color: #333;
  font-weight: 500;
  font-size: 1em;
}

/* 扩展信息区域 - 与图书分类区域相似 */
.expand-info {
  margin-top: 24px;
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
}

/* 响应式设计 - 与图书管理组件相同的断点 */
@media (max-width: 768px) {
  .user-info-container {
    padding: 16px;
  }
  
  .user-card {
    padding: 16px;
  }
}
</style>