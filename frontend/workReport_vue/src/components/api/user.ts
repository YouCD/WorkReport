import request from "../utils/request"

// 用户登入
export function Login(params: any) {
    return request({
        url: '/login',
        data: params,
        method: 'POST',
    })
}
