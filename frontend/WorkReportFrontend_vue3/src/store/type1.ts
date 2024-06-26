import {defineStore} from 'pinia'
import {ref} from 'vue'
import {FetchType1, IType} from '@/types/type1'

export const type1Store = defineStore('type1', () => {
    const type1Data = ref<IType[]>([])

    const getType1List = async () => {
        const res = await FetchType1()
        type1Data.value = res.data.type_list
    }

    const FindType1ByDescription = (description: string) => {
        return type1Data.value.find((item) => item.description === description)
    }

    return {
        type1Data,
        getType1List,
        FindType1ByDescription,
    }
})
