<template>
  <div class="page-container">
    <el-card shadow="page-container">
      <template #header>
        <div class="header">
          <span>修改密码</span>
        </div>
      </template>

      <el-form 
        :model="passwordForm" 
        :rules="rules" 
        ref="passwordFormRef" 
        label-width="120px"
        label-position="top"
        class="form-container"
      >
      <el-form-item label="原密码" prop="old_Pwd">
        <el-input 
          v-model="passwordForm.old_Pwd" 
          type="password" 
          placeholder="请输入原密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="新密码" prop="new_Pwd">
        <el-input 
          v-model="passwordForm.new_Pwd" 
          type="password" 
          placeholder="请输入新密码"
          show-password
        />
      </el-form-item>

      <el-form-item label="确认新密码" prop="re_Pwd">
        <el-input 
          v-model="passwordForm.re_Pwd" 
          type="password" 
          placeholder="请再次输入新密码"
          show-password
        />
      </el-form-item>

      <el-form-item>
        <el-button 
          type="primary" 
          @click="submitForm(passwordForm)"
          :loading="loading"
        >
          确认修改
        </el-button>
        <el-button @click="resetForm(passwordForm)">重置</el-button>
      </el-form-item>
    </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { userChangePsdService } from '@/api/user.js'
import {useTokenStore} from '@/stores/token.js'

const router = useRouter()
const loading = ref(false)
const passwordFormRef = ref()
const tokenStore= useTokenStore()
const passwordForm = reactive({
  old_Pwd: '',
  new_Pwd: '',
  re_Pwd: ''
})

// 验证两次密码是否一致
const validatePassword = (rule, value, callback) => {
  if (value !== passwordForm.new_Pwd) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = reactive({
  old_Pwd: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  new_Pwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  re_Pwd: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
})

const submitForm = async(passwordForm) => {
 let result =await userChangePsdService(passwordForm)
 if(result.code===0){
  ElMessage.success(result.msg || '修改成功');
  tokenStore.removeToken()
  router.push('/')

 }else{
  ElMessage.error(result.msg || '修改失败');
 }

}

const resetForm = (formEl) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<style scoped>
@import '@/Style/common.css';
/* 完全匹配图书分类页面的样式 */
.page-container {
  width: 44vw;
  height: 80vh;
  margin: 0;
  padding: 24px;
  box-sizing: border-box;
  overflow: hidden;
}

.el-card {
  width: 100%;
  height: 100%;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

:deep(.el-card__body) {
  padding: 0 !important;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 24px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.form-container {
  flex: 1;
  padding: 24px;
  overflow: auto;
}

/* 表单元素样式 */
.el-form-item {
  margin-bottom: 16px;
}

.el-input {
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .page-container {
    padding: 16px;
  }
  
  .form-container {
    padding: 16px;
  }
  
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    padding: 12px;
  }
}
</style>