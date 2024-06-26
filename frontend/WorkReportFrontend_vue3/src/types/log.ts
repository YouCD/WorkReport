import {BaseUrl, http, IResponse} from '@/request'
import {IPageRequestData, IPid} from '@/types/commont'
import {Urls} from '@/request/url'

export interface IWorkContent {
    id?: number
    date: number
    type1: string
    type1_id: number
    type2: string
    type2_id: number
    content: string
}

export interface IWorkContentRespList {
    work_content_resp_list: IWorkContent[]
    sum: number
}

export interface IAddLogData {
    id?: number
    type1: number
    type2: number
    content: string
    date: number
}

export interface ExportLogRequest {
    dateStart: number
    dateEnd: number
}

export interface IContentData {
    content: string
}

//  添加 日志
export const AddLog = async (data: IAddLogData) =>
    await http.Post<IResponse<any>>(Urls.workLog, data)

//  获取日志
export const FetchLog = async (data: IPageRequestData) =>
    await http.Get<IResponse<IWorkContentRespList>>(Urls.workLog, data)

//  修改日志
export const ModifyLog = async (data: IAddLogData) =>
    await http.Put<IResponse<any>>(Urls.workLog, data)

//  删除
export const DeleteLog = async (data: IPid) =>
    await http.Delete<IResponse<IWorkContentRespList>>(Urls.workLog, data)

//  获取本周日志
export const FetchWeekLog = async () =>
    await http.Get<IResponse<any>>(Urls.workLogFromWeek)

// 导出日志
export const ExportLog = (data: ExportLogRequest) => {
    const xhr = new XMLHttpRequest()
    //GET请求,请求路径url,async(是否异步)
    xhr.open(
        'GET',
        BaseUrl() +
        Urls.downloadWorklog +
        '?dateStart=' +
        data.dateStart +
        '&dateEnd=' +
        data.dateEnd,
        true,
    )
    const jwt = localStorage.getItem('jwt') as string
    //设置请求头参数的方式,如果没有可忽略此行代码
    xhr.setRequestHeader('jwt', jwt)
    //设置响应类型为 blob
    xhr.responseType = 'blob'
    //关键部分
    xhr.onload = function () {
        //如果请求执行成功
        if (this.status == 200) {
            const blob = this.response
            const filename = 'WorkLog.xlsx'
            const a = document.createElement('a')

            // blob.type = "application/octet-stream";
            //创键临时url对象
            const url = URL.createObjectURL(blob)

            a.href = url
            a.download = filename
            a.click()
            //释放之前创建的URL对象
            window.URL.revokeObjectURL(url)
        }
    }
    //发送请求
    xhr.send()
}

//  搜索日志 content=789
export const SearchLog = async (data: IContentData) =>
    await http.Get<IResponse<IWorkContentRespList>>(Urls.workLogFromContent, data)
