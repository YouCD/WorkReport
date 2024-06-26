import {createApp} from 'vue'
import {MotionPlugin} from '@vueuse/motion'


import router from './router'

//  antd
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import {createPinia} from 'pinia'
//  日期组件 汉化
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import App from './App.vue'

dayjs.locale('zh-cn');


const app = createApp(App);
app.use(Antd)
    .use(router)
    .use(MotionPlugin)
    .use(createPinia())
    .mount('#app');
// // 必须使用 nextTick，不然会有加载顺序问题，导致绑定失败
// nextTick(() => {
//     // 配置全局对象
//     app.config.globalProperties.icons = Icons
//     // Antd 注入全部图标（这样注入之后，就可以全局直接使用 icon 组件，不需要每个页面去引入了）
//     for (const key in Icons) {
//         app.component(key, Icons[key as keyof typeof Icons])
//     }
// })