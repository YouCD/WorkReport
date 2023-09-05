import { IPid } from '@/types/commont'
import { http, IResponse } from '@/request'
import { Urls } from '@/request/url'

export interface ITypeCount {
  count: number
  type1: string
}

export interface ICountType1Data {
  countType1Data: ITypeCount[]
}

export interface ICountType2Data {
  countType2Data: ITypeCount[]
}

//  获取 Type1Count
export const FetchType1Count = async () =>
  await http.Get<IResponse<ICountType1Data>>(Urls.type1Count)

//  获取 Type2Count
export const FetchType2Count = async (data: IPid) =>
  await http.Get<IResponse<ICountType2Data>>(Urls.type2Count, data)
