<template>
  <v-chart class="chart" :option="option" />
</template>

<script setup>
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
} from 'echarts/components';
import VChart, { THEME_KEY } from 'vue-echarts';
import { ref, defineComponent, toRefs, reactive, onMounted } from 'vue';

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
]);

const props = defineProps({ xaxispay: {type:Array}, yaxispay: {type:Array} })
const { xaxispay, yaxispay } = toRefs(props);

//父子组件间传值未能实现

const option = reactive({
  title: {
    text: '下单周期'
  },
  tooltip: {
    trigger: 'axis'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: [
    {
      type: 'category',
      boundaryGap: false,
      data: xaxispay
    }
  ],
  yAxis: [{
    type: 'value'
  }],
  series: [
    {
      // name: 'Email',
      type: 'line',
      stack: 'Total',
      data: yaxispay
    }
  ]
});

</script>

<style scoped>
/* .box {
height: 200px;
width: 200px;
} */
</style>
