<template>
  <div class="charts-wrap">
    <div class="chart-card card-half">
      <VChart :option="type1Option" autoresize @mouseover="onType1Hover" />
    </div>
    <div class="chart-card card-half">
      <VChart :option="pieOption" autoresize />
    </div>
    <div class="chart-card card-full">
      <VChart :option="type2Option" autoresize />
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getType1Count, getType2Count } from "@/components/api/count";
import { getWorkType1 } from "@/components/api/worklog";
import VChart from 'vue-echarts';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { BarChart, PieChart } from 'echarts/charts';
import { GridComponent, TooltipComponent, LegendComponent, GraphicComponent } from 'echarts/components';

use([CanvasRenderer, BarChart, PieChart, GridComponent, TooltipComponent, LegendComponent, GraphicComponent]);

const palette = ['#5B8FF9', '#5AD8A6', '#F6BD16', '#E8684A', '#6DC8EC', '#9270CA', '#FF9D4D', '#269A99', '#FF99C3', '#1E9493'];

const type1_List = ref([] as any[]);
const countType1Data = ref([] as any[]);
const countType2Data = ref([] as any[]);
const type1Option = ref({} as any);
const pieOption = ref({} as any);
const type2Option = ref({} as any);
const defaultType1 = ref(1);
const lastType1Id = ref<number | string | null>(null);

function buildBarOption(data: any[], field: string) {
  const names = data.map(d => d[field]);
  const values = data.map(d => d.count);
  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      right: 5,
      top: 'middle',
    },
    grid: { left: '3%', right: '18%', bottom: '3%', top: '12%', containLabel: true },
    xAxis: {
      type: 'category',
      data: names,
      axisLabel: { interval: 0, rotate: 30 },
    },
    yAxis: { type: 'value' },
    series: names.map((name, i) => ({
      name,
      type: 'bar',
      barWidth: '45%',
      data: names.map((n, j) => (j === i ? { value: values[j], itemStyle: { color: palette[i % palette.length], borderRadius: [12, 12, 0, 0] } } : null)),
    })),
  };
}
function buildPieOption(data: any[]) {
  return {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      right: 5,
      top: 'middle',
    },
    series: [{
      type: 'pie',
      radius: ['40%', '62%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 1 },
      label: { show: true, position: 'inner', formatter: (p: any) => `${p.percent.toFixed(0)}%`, fontSize: 14, fontWeight: 'bold', color: '#fff' },
      data: data.map((d, i) => ({ name: d.type1, value: d.count, itemStyle: { color: palette[i % palette.length] } })),
    }],
    graphic: [{
      type: 'text',
      left: '34%',
      top: 'middle',
      style: { text: '工作占比', fill: '#666', fontSize: 16, fontWeight: 'bold', textAlign: 'center' },
    }],
  };
}
function loadType1Data() {
  getType1Count().then(res => {
    if (res.data.flag) {
      countType1Data.value = res.data.data.countType1Data
      type1Option.value = buildBarOption(countType1Data.value, 'type1')
      pieOption.value = buildPieOption(countType1Data.value)
    }
  })
}
function Type2CountEcharts() {
  getType2Count(defaultType1.value).then(res => {
    if (res.data.flag) {
      countType2Data.value = res.data.data.countType2Data
      type2Option.value = buildBarOption(countType2Data.value, 'type2')
    }
  })
}
function WorkLogType1Change(value: any) {
  getType2Count(value).then(res => {
    if (res.data.flag) {
      countType2Data.value = res.data.data.countType2Data
      type2Option.value = buildBarOption(countType2Data.value, 'type2')
    }
  })
}
function onType1Hover(params: any) {
  const type1Value = params.name;
  type1_List.value.forEach(item => {
    if (item.description === type1Value && item.id !== lastType1Id.value) {
      lastType1Id.value = item.id;
      WorkLogType1Change(item.id)
    }
  })
}
function getWorkType1Handler() {
  getWorkType1().then(res => {
    if (res.data.flag) {
      type1_List.value = res.data.data.type_list
    }
  })
}

onMounted(() => {
  getWorkType1Handler()
  loadType1Data();
  Type2CountEcharts();
});
</script>
<style scoped>
.charts-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  width: 100%;
}
.chart-card {
  box-sizing: border-box;
  height: 320px;
  padding: 12px;
  background: #fafafa;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
}
.card-half {
  flex: 1 1 0;
  min-width: 0;
}
.card-full {
  flex: 0 0 100%;
}
@media (max-width: 768px) {
  .chart-card {
    flex: 0 0 100%;
    height: 400px;
  }
}
</style>