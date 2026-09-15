import request from "../utils/request"

// 添加工作类型
export function addWorkType(params: any) {
    return request({
        url: '/w/workType',
        data: params,
        method: 'POST',
    })
}

export function getWorkType(params: any) {
    return request({
        url: '/w/workType',
        params: params,
        method: 'GET',
    })
}

export function editWorkType(params: any) {
    return request({
        url: '/w/workType',
        data: params,
        method: 'PUT',
    })
}
