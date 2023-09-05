//  获取 Type1Count

import { http, IResponse } from '@/request'
import { Urls } from '@/request/url'

//  检查更新
export const UpdateCheck = async () =>
  await http.Get<IResponse<any>>(Urls.updateCheck)
