<template>
  <div>
    <div class="Box1">
      <div id="Type1Count" style="width: 100%; background: white;height:360px;padding: 20px" v-motion :initial="{  opacity: 0,  x: -50,}" :enter="{  opacity: 1,  x: 0,}" ></div>
    </div>
    <div class="Box2">
      <div id="Type1CountPie" style="width: 100%; background: white;height:360px;padding: 20px;" v-motion :initial="{  opacity: 0,  x: 50,}" :enter="{  opacity: 1,  x: 0,}"></div>
    </div>
  </div>
  <div style="height: 360px;background: white;width: 100%;margin-top: 10px;overflow: hidden">
    <div id="Type2Count" style="width: 100%; background: white;height:360px;padding: 20px;" v-motion :initial="{  opacity: 0,  y: 1000,}" :enter="{  opacity: 1,  y: 0,}"></div>
  </div>
</template>

<script setup lang="ts">
import {chartStore} from "@/store/chart";
import {storeToRefs} from "pinia";
import {onMounted, ref, watch} from "vue";
import {Column, Pie} from '@antv/g2plot';
import {type1Store} from "@/store/type1";
import {IType} from "@/types/type1";


const {getType1CountList, getType2CountList} = chartStore()
const {type1CountList, type2CountList} = storeToRefs(chartStore())


const {type1Data} = storeToRefs(type1Store())
const {getType1List, FindType1ByDescription} = type1Store()


onMounted(async () => {
  await getType1CountList()
  type1ChartHandler()
  Type1CountPieHandler()
  await getType1List()
  await getType2CountList({id: type1Data.value[0].id})
  type2CountEcharts()
})


const type1ChartHandler = () => {
  const plot = new Column('Type1Count', {
    data: type1CountList.value,
    xField: 'type1',
    yField: 'count',
    seriesField: 'type1',
    legend: {
      position: 'right',
    },
    columnStyle: {
      radius: [50, 50, 0, 0],
    },
  });
  plot.render();
  plot.on('element:mouseenter', (data: any) => {
    const type1 = FindType1ByDescription(data.data.data.type1) as IType
    if (type1){
      getType2CountList({id: type1.id})
    }
  });
}


const type2Chart = ref<Column>();
const type2CountEcharts = () => {
  const t = new Column('Type2Count', {
    data: type2CountList.value,
    xField: 'type2',
    yField: 'count',
    seriesField: 'type2',
    legend: {
      position: 'right',
    },
    columnStyle: {
      radius: [20, 20, 0, 0],
    },
  });
  t.render()

  type2Chart.value = t
}

watch(
    () => type2CountList.value,
    (newValue, oldValue) => {
      type2Chart.value?.changeData(type2CountList.value)
    }, {immediate: true}
)


const Type1CountPieHandler = () => {
  const piePlot = new Pie('Type1CountPie', {
    appendPadding: 10,
    data: type1CountList.value,
    angleField: 'count',
    colorField: 'type1',
    radius: 1,
    innerRadius: 0.6,
    label: {
      type: 'inner',
      offset: '-50%',
      content: ({percent}) => `${(percent * 100).toFixed(0)}%`,
      style: {
        textAlign: 'center',
        fontSize: 14,
      },
    },
    interactions: [{type: 'element-active'}],
    statistic: {
      title: false,
      content: {
        style: {
          whiteSpace: 'pre-wrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
        },
        content: '工作占比',
      },
    },
  });

  piePlot.render();
}

</script>

<style scoped>

.Box1 {
  height: 360px;
  width: calc(50% - 5px);
  margin-bottom: 10px;
  float: left
}

.Box2 {
  height: 360px;
  width: calc(50% - 5px);
  margin-bottom: 10px;
  float: right
}

</style>