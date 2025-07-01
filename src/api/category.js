import request from '@/utils/request.js'
import { useTokenStore } from '../stores/token'
export const bookCategoryListService=()=>{
    return request.get('/category')
}
export const categoryAddService=(categoryModule)=>{
    return request.post('/category',categoryModule)
}
export const categoryUpdateService=(catagoryData)=>{
    return request.put('category',catagoryData)
}
export const categoryDeleteService=(categoryData)=>{
    return request.delete('category',{data:categoryData})
}