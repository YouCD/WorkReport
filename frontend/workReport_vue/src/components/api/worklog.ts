import request from "../utils/request"

// 获取所有用户
export function getWorkType1(params?: any) {
    return request({
        url: '/w/workType1',
        params: params,
        method: 'GET',
    })
}

export function getWorkType2(params: any) {
    return request({
        url: '/w/workType2',
        params: params,
        method: 'GET',
    })
}

export function addWorkLog(params: any) {
    return request({
        url: '/w/workLog',
        data: params,
        method: 'POST',
    })
}

export function getWorkLog(params: any) {
    return request({
        url: '/w/workLog',
        params: params,
        method: 'GET',
    })
}

export function delWorkLog(params: any) {
    return request({
        url: '/w/workLog',
        params: params,
        method: 'DELETE',
    })
}

export function modifyWorkLog(params: any) {
    return request({
        url: '/w/workLog',
        data: params,
        method: 'PUT',
    })
}

export function getWorkLogFromType(params: any) {
    return request({
        url: '/w/workLogFromType',
        params: params,
        method: 'GET',
    })
}

export function getWorkLogFromWeek() {
    return request({
        url: '/w/workLogFromWeek',
        method: 'GET',
    })
}

export function getWorkLogFromMonth() {
    return request({
        url: '/w/workLogFromMonth',
        method: 'GET',
    })
}

export function getWorkLogFromContent(params: any) {
    return request({
        url: '/w/workLogFromContent',
        params: params,
        method: 'GET',
    })
}

export function getWorkLogFromDate(params: any) {
    return request({
        url: '/w/workLogFromDate',
        params: params,
        method: 'GET',
    })
}


export function UpdateCheck() {
    return request({
        url: '/w/updateCheck',
        method: 'GET',
    })
}

export function addContent(params: any) {
    return request({
        url: '/ai/addContent',
        data: params,
        method: 'POST',
    })
}

export function getAiWorkLogFromWeek() {
    return request({
        url: '/ai/workLogFromWeek',
        method: 'GET',
    })
}

export function sendEmail(params: any) {
    return request({
        url: '/ai/sendEmail',
        data: params,
        method: 'POST',
    })
}
