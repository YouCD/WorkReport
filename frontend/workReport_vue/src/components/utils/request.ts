import axios, { AxiosInstance } from 'axios'  //引入axios
import { message } from 'ant-design-vue';  //引入 Ant for vue 的全局消息提醒
import router from '../../router/index'  //引入vue 路由

// let apiUrl = "/api";
// if (process.env.NODE_ENV === "dev") { //开发环境
//   console.log("当前环境：开发环境");
//   apiUrl = "http://127.0.0.1:8012"
// } else if (process.env.NODE_ENV === "prod") {
//   console.log("当前环境：生产环境");
//   apiUrl = "http://172.16.12.10:8012"  //接口地址
// }
// console.log(process.env)

// 创建并配置axios实例
const service: AxiosInstance = axios.create({
    baseURL: import.meta.env.VUE_APP_API_ROOT||"/api",  //请求的url
    method: 'GET',  //默认请求方式
    //添加jwt头部
    headers: { "jwt": localStorage.jwt, },
    timeout: 300000, // 请求超时时间
});

// 配置ant的全局消息提醒
message.config({
    top: `100px`,  //距离顶部的距离
    duration: 2,
    maxCount: 3,  //最大消息数量
});

// 添加请求拦截器
service.interceptors.request.use(
    config => {
        // 发送请求之前
        // 为头部增加JWTtoken
        config.headers['jwt'] = localStorage.getItem('jwt') || "";
        // 为头部增加accId
        // config.headers['accId'] = localStorage.getItem("accid") || ""

        return config
    },
    error => {
        // 请求错误
        message.error("请求错误");
        return error
    }
);

// 添加响应拦截器
service.interceptors.response.use(
    response => {
        // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
        // 否则的话抛出错误
        if (response.status === 200) {
            return Promise.resolve(response);
        } else {
            return Promise.reject(response);
        }
    },
    // 服务器状态码不是2开头的的情况
    // 这里可以跟你们的后台开发人员协商好统一的错误状态码
    // 然后根据返回的状态码进行一些操作，例如登录过期提示，错误提示等等
    // 下面列举几个常见的操作，其他需求可自行扩展
    error => {
        if (error.response.status) {

            switch (error.response.status) {
                // 401: 未登录
                // 未登录则跳转登录页面，并携带当前页面的路径
                // 在登录成功后返回当前页面，这一步需要在登录页操作。
                case 401:
                    message.warning("Token过期，请重新认证");
                    router.replace({
                        path: "/login",
                        query: {
                            redirect: router.currentRoute.value.fullPath
                        }
                    });
                    break;
                case 402:
                    message.warning("部分功能未开放");
                    break;
                case 404:
                    message.warning("抱歉没有该资源");
                    break;
                // 其他错误，直接抛出错误提示
                default:
                    message.warning("有点迷")
            }
            return Promise.reject(error.response);
        }
    });


export default service
