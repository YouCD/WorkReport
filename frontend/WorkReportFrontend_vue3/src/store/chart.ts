import { defineStore } from 'pinia'
import { ref } from 'vue'
import { IPid } from '@/types/commont'
import { FetchType1Count, FetchType2Count, ITypeCount } from '@/types/chart'

export const chartStore = defineStore('chart', {
  state: () => ({
    type1CountList: ref<ITypeCount[]>([]),
    type2CountList: ref<ITypeCount[]>([]),
  }),
  getters: {},
  actions: {
    //  获取 Type1Count
    async getType1CountList() {
      const res = await FetchType1Count()
      this.type1CountList = res.data.countType1Data
    },
    //  获取 Type2Count
    async getType2CountList(id: IPid) {
      const res = await FetchType2Count(id)
      this.type2CountList = res.data.countType2Data
    },
  },
})
