import request from "@/utils/request.js";

export const userRegisterService=(registerData)=>{
    const params=new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key])
    }
    return request.post('/user/register',params);
}
export const userLoginService=(loginFormData)=>{
    const params=new URLSearchParams()
    for(let key in loginFormData){
        params.append(key,loginFormData[key])
    }
    return request.post('/user/login',params);
}
export const userChangePsdService=(changeData)=>{
    return request.patch('/user/updatePwd',changeData)
}
export const userGetInfo=()=>{
    return request.get('/user/userInfo')
}