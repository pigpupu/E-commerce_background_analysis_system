import { createApp, VueElement } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import "echarts";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'default-passive-events'



const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

//app.prototype.global=global;
//app.use(globalVariable)
app.use(router)
app.use(ElementPlus)
//app.prototype.GLOBAL=globalVariable;
app.mount('#app')
