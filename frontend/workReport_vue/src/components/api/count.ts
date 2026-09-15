import request from "../utils/request"

// 通过type1获得总数量
export function getType1Count() {
    return request({
        url: '/w/type1Count',
        method: 'GET',
    })
}

export function getType2Count(type1: any) {
    return request({
        url: '/w/type2Count',
        params: { id: type1 },
        method: 'GET',
    })
}
