import request from '@/utils/request.js'
import { useTokenStore } from '../stores/token'
export const borrowerListService=()=>{
    return request.get('/borrow')
}
