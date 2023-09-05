import { http, IResponse } from '@/request'
import { Urls } from '@/request/url'

export interface LoginRequestData {
  username: string
  password: string
}

export interface LoginResponseData {
  token: string
  uid: string
}

//  登入
export const LoginHandler = async (data: LoginRequestData) =>
  await http.Post<IResponse<LoginResponseData>>(Urls.login, data)
