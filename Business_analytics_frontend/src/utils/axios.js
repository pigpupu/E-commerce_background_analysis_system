import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

axios.defaults.baseURL = 'http://47.115.203.21:8087'
//axios.defaults.baseURL = 'http://47.115.231.183:8087'
//axios.defaults.baseURL = 'http://localhost:8087'
axios.defaults.headers.post['Content-Type'] = 'application/json'

NProgress.inc(0.3)
NProgress.configure({ easing: 'ease', speed: 600, showSpinner: false })

axios.interceptors.request.use((config) => {// 在发送请求之前做些什么
    if (['/login', '/register'
    ,'/forgetPassword','/resetPassword'].indexOf(config.url) === -1) {
        const token = localStorage.getItem('token') // 判断是否存在token，如果存在的话，则每个http header都加上token
        NProgress.start()
        if (token && token!="invalid") {
            config.headers.token = token
        }
        NProgress.done()
    }
    return config
}, (error) => {
    return Promise.reject(error);
})

export default axios
