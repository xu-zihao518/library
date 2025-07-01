import request from '@/utils/request.js'
import { useTokenStore } from '../stores/token'
export const bookListService=()=>{
    return request.get('/book')
}
export const bookAddService=(bookModule)=>{
    return request.post('/book',bookModule)
}
// export const categoryUpdateService=(catagoryData)=>{
//     return request.put('category',catagoryData)
// }
export const bookDeleteService =(bookData)=>{
    return request.delete('/book',{data:bookData})
}