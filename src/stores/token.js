import { defineStore} from "pinia";
import  {ref} from'vue'
export const useTokenStore=defineStore('token',()=>{
    const token=ref('');
    const setToken=(newToken)=>{
        token.value=newToken
    }
    const removeToken=()=>{
        token.value=''
    }
    return {
  token,
  setToken,
  removeToken,
  // 可选：将 persist 配置移到这里（但推荐作为 defineStore 的第三个参数）
  persist: true 
}
});
