<template>
  <div class="auth-container">
    <div class="auth-form" v-if="isRegister">
      <el-form :model="registerForm" :rules="registerRules" label-width="auto">
        <el-link type="info" :underline="false" @click="isRegister = false;resetRegisterForm()">
          &lt; 返回登录
        </el-link>
        
        <el-form-item>
          <h1>注册</h1>
        </el-form-item>

        <el-form-item label="用户姓名" prop="username">
          <el-input v-model="registerForm.username" />
        </el-form-item>

        <el-form-item label="用户身份证" prop="userId">
          <el-input v-model="registerForm.userId" />
        </el-form-item>

        <el-form-item label="用户密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            show-password 
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password" 
            show-password />
        </el-form-item>

        <el-form-item label="是否为管理员">
          <el-switch v-model="registerForm.isAdmin" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button @click="resetRegisterForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 登录表单 -->
    <div class="auth-form" v-else>
      <el-form :model="loginForm" :rules="loginRules" label-width="auto">
        <el-form-item>
          <h1>登录</h1>
        </el-form-item>

        <el-form-item label="身份证" prop="userId">
          <el-input v-model="loginForm.userId" />
        </el-form-item>

        <el-form-item label="用户密码" prop="password">
          <el-input v-model="loginForm.password" type="password" show-password />
        </el-form-item> 

        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>

        <el-form-item>
          <el-link type="info" :underline="false" @click="isRegister = true;resetLoginForm()">
            没有账号？去注册
          </el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { ElMessage, type FormRules } from 'element-plus'
import {userRegisterService,userLoginService} from '@/api/user.js'
import {useRouter} from 'vue-router'
import {useTokenStore} from '@/stores/token.js'

// 切换注册/登录
const isRegister = ref(false)
const router=useRouter()
const tokenStore=useTokenStore();

// 注册表单
const registerForm = reactive({
  username: '',
  userId: '',
  password: '',
  confirmPassword: '',
  isAdmin: 1,
})
const loginForm = reactive({
  userId: '',
  password: '',
})

// 注册表单验证规则
const registerRules: FormRules = {
  username: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度2-10字符', trigger: 'blur' },
  ],
  userId: [
    { required: true, message: '请输入身份证', trigger: 'blur' },
    { len: 18, message: '身份证必须18位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 10, message: '密码长度5-10字符', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

// 登录表单验证规则
const loginRules: FormRules = {
  userId: [
    { required: true, message: '请输入身份证', trigger: 'blur' },
    { len: 18, message: '身份证必须18位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 10, message: '密码长度5-10字符', trigger: 'blur' },
  ],
}

// 注册提交
const handleRegister = async() => {
  let result=await userRegisterService(registerForm);
  if(result.code===0){
    alert(result.msg ?result.msg :"注册成功")
  }else{
    alert("注册失败")
  }
  isRegister.value = false // 注册后跳转登录
  router.push('/')
 }

// 登录提交
const handleLogin = async() => {
  let result=await userLoginService(loginForm)
   ElMessage.success(result.msg?result.msg:'登陆成功');
   tokenStore.setToken(result.data)
    //跳转
    router.push('/Layout/Layoutdefault')
}

// 重置注册表单
const resetRegisterForm = () => {
  Object.assign(registerForm, {
    username: '',
    userId: '',
    password: '',
    confirmPassword: '',
    isAdmin: false,
  })
}
const resetLoginForm = () => {
  Object.assign(loginForm, {
    userId: '',
    password: '',
  })
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background: url('https://library.gdpu.edu.cn/images/23/01/18/14ig7prwtu/banner1.jpg') no-repeat center/cover;
}

.auth-form {
  width: 400px;
  min-height: 400px;
  padding: 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.auth-form h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-button {
  width: 100%;
  margin-top: 10px;
}

.el-link {
  display: block;
  text-align: left;
  margin-top: 10px;
}
</style>