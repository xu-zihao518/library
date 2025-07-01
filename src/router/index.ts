import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/components/Login.vue'
import Addbook from '@/views/Addbook.vue'
import Layout from '@/components/Layout.vue'
import Bookclassification from '@/views/Bookclassification.vue'
import Borsituation from '@/views/Borsituation.vue'
import Changepassword from '@/views/Changepassword.vue'
import Selectbook from '@/views/Selectbook.vue'
import Usercenter from '@/views/Usercenter.vue'
import Layoutdefault from '@/views/Layoutdefault.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Login,
    },
    {
      path: '/Layout',
      name: 'Layout',
      component: Layout,
      redirect:'/Layout/Layoutdefault',
      children:[
        {path:'Layoutdefault',component:Layoutdefault},
        {path:'Addbook',component:Addbook},
        {path:'Bookclassification',component:Bookclassification},
        {path:'Borsituation',component:Borsituation},
        {path:'Changepassword',component:Changepassword},
        {path:'Selectbook',component:Selectbook},
        {path:'Usercenter',component:Usercenter}

      ]
    },
    
  ],
})

export default router
