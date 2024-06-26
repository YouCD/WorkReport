import {defineStore} from 'pinia'
import {ref} from 'vue'
import {IType} from '@/types/type1'
import {FetchType2} from '@/types/type2'
import {IPid} from '@/types/commont'

export const type2Store = defineStore('type2', () => {
    const type2Data = ref<IType[]>([])
    const getType2List = async (data: IPid) => {
        const res = await FetchType2(data)
        type2Data.value = res.data.type_list
    }
    return {
        type2Data,
        getType2List,
    }
})
