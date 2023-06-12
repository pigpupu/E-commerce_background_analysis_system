 /* jshint esversion:11 */
 //import { createRouter, createWebHistory } from 'vue-router';
 import { createRouter, createWebHashHistory } from 'vue-router';
 import HomeView from '../views/UserViews/HomeView.vue';

 import NProgress from 'nprogress'
 import 'nprogress/nprogress.css'




 const router = createRouter({
   //history: createWebHistory(import.meta.env.BASE_URL),
   history: createWebHashHistory(),
   routes: [
     {
       path: '/login',
       name: 'login',
       meta: {
         noAuth: true,
       },
       component: () => import ('../views/UserViews/LoginView.vue'),
     },
     {
       path: '/register',
       name: 'register',
       meta: {
         noAuth: true,
       },
       component: () => import ('../views/UserViews/RegistView.vue'),
     },
     {
      path: '/changepasswd',
      name: 'changepasswd',
      component: () => import ('../views/UserViews/ChangePasswd.vue'),
    },
     {
       path: '/forgetpwd',
       name: 'forgetpassword',
       meta: {
         noAuth: true,
       },
       component: () => import ('../views/UserViews/ForgetPwdView.vue'),
     },

     {
       path: '/',
       name: 'home',
       component: HomeView,
       redirect:"/login",
       children:[
         {
           path: '/echarts',
           name: 'echarts',
           component: () => import ('../views/UserViews/EchartsView.vue'),
         },
         {
           path: '/info',
           name: 'information',
           component: () => import ('../views/UserViews/InfoView.vue'),
         },
         {
           path: '/pukehu',
           name: 'pukehu',
           component: () => import ('../views/UserViews/PukehuView.vue'),
         },
         {
           path: '/saleInfo',
           name: 'saleInfo',
           component: () => import ('../views/UserViews/SaleInfo.vue'),
         },
         {
           path: '/saleMoneyInfo',
           name: 'saleMoneyInfo',
           component: () => import ('../views/UserViews/SaleMoneyInfo.vue'),
         },
         {
           path: '/stockInfo',
           name: 'stockInfo',
           component: () => import ('../views/UserViews/StockInfo.vue'),
         },
         {
           path: '/puyuce',
           name: 'puyuce',
           component: () => import ('../views/UserViews/PuyuceView.vue'),
         },
         {
           path: '/lookfor',
           name: 'lookfor',
           component: () => import ('../views/UserViews/LookforView.vue'),
         }
       ]
     },
     {
       path: '/gao',
       name: 'GaoUserViews',
       component: () => import ('../views/GaoUserViews/HomeView.vue'),
       redirect:"/gao/glookfor",
       children: [
         {
           path: '/gao/clientanaly',
           name: 'useranalyse',
           component: () => import ('../views/GaoUserViews/ClientAnalyView.vue'),
         },
         {
           path: '/gao/ginfo',
           name: 'ginfo',
           component: () => import ('../views/GaoUserViews/InfoView.vue'),
         },
         {
           path: '/gao/glookfor',
           name: 'glookfor',
           component: () => import ('../views/GaoUserViews/LookforView.vue'),
         },
         {
           path: '/gao/gsale',
           name: 'gsale',
           component: () => import ('../views/GaoUserViews/SaleInfo.vue'),
         },
         {
           path: '/gao/gsalemoney',
           name: 'gsalemoney',
           component: () => import ('../views/GaoUserViews/saleMoneyInfo.vue'),
         },
         {
           path: '/gao/gstock',
           name: 'gstock',
           component: () => import ('../views/GaoUserViews/StockInfo.vue'),
         },
         {
           path: '/gao/Gaoyuce',
           name: 'Gaoyuce',
           component: () => import ('../views/GaoUserViews/Gaoyuce.vue'),
         },
         {
           path: '/gao/Gaotuijian',
           name: 'Gaotuijian',
           component: () => import ('../views/GaoUserViews/Gaotuijian.vue'),
         },

       ]
     },
     {
       path: '/admin',
       name: 'AdminViews',
       component: () => import ('../views/AdminViews/HomeView.vue'),
       redirect:"/admin/userMgr",
       children: [
         {
           path: '/admin/userMgr',
           name: 'userMgr',
           component: () => import ('../views/AdminViews/UserMgr.vue'),
         },
         {
           path: '/admin/goodMgr',
           component: () => import('../views/AdminViews/GoodMgr.vue')
         },
         {
           path: '/admin/info',
           component: () => import('../views/AdminViews/InfoView.vue'),
         }
       ]
     }
   ]
 });

 NProgress.inc(0.3)
 NProgress.configure({ easing: 'ease', speed: 600, showSpinner: false })

 // 新增路由守卫
 router.beforeEach(async (to, from, next) => {
     // 启动进度条
     NProgress.start()
     console.log("progress start")
     // 是否登陆
     if(!to.meta.noAuth){
      if(localStorage.getItem("token")=="invalid" ||localStorage.getItem("token")==null){
         next("/login")
       }else{
         next()
       }
     }else{
       next()
     }
 })
 router.afterEach(() => {
   // 关闭进度条
   console.log("over")
   NProgress.done()
 })

 export default router;
