import axios from 'axios';
import { ElMessage } from 'element-plus'; // 导入ElMessage
import router from '@/router'; 
const baseURL='/api';
const instance=axios.create({baseURL})
import {useTokenStore} from '@/stores/token.js'
instance.interceptors.request.use(
    (config)=>{
        const tokenStore=useTokenStore();
        if(tokenStore.token){
            config.headers.Authorization=tokenStore.token
        }
        return config;
    },
    (err)=>{
        Promise.reject(err)
    }
)
instance.interceptors.response.use(
    result=>{
        if(result.data.code==0){
            return result.data;
        }
    ElMessage.error(result.data.msg || '服务异常');
    return Promise.reject(result.data);
    },
    err=>{
        if(err.response.status===401){
            ElMessage.error('请先登录');
            routerKey.push('/')
        }else{
            ElMessage.error('服务异常')
        }
        
        return Promise.reject(err)
    }
)
export default instance