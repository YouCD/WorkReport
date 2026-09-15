import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';

dayjs.extend(customParseFormat);

// import echarts from 'echarts'
//
// Vue.prototype.$echarts = echarts

const app = createApp(App)

app.use(router)
app.use(Antd)

app.mount('#app')
