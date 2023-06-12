<template>
  <v-chart class="chart" id="fad" />
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
import { ref, defineComponent, reactive, onMounted } from 'vue';
import axios from 'axios';
import * as echarts from "echarts/core";

use([
  CanvasRenderer,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
]);

// const tableData = ref([  //five category would save here 
//   {
//     category1: "",
//     category2: "",
//     category3: "",
//     category4: "",
//     category5: "",
//   },
// ])

const echart = reactive({
  data: [0],
  legend: [0],
})

//use for local test
const tableData = ref([
  { value: 335, name: '电脑' },
  { value: 310, name: '鞋包' },
  { value: 234, name: '酒水' },
  { value: 135, name: '图书' },
  { value: 1548, name: '母婴' },
])

function getfad() {
  // axios.post('/getfad', { id: localStorage.getItem("userid") })
  //   .then((res) => {
  //     echart.data = []
  //     tableData.value = res.data.tabledata; //object depends on back
  //     for (let i = 0; i < tableData.value.length; i++) {
  //       echart.legend.push(tableData.value[i].name);
  //       echart.data.push({ value: tableData.value[i].value, name: tableData.value[i].name }); //add data into the table
  //     }
  //     initChart() //call init chart
  //   })

  //local test
  echart.data = []
  //tableData.value = res.data.object; //object depends on back
  for (let i = 0; i < tableData.value.length; i++) {
    echart.legend.push(tableData.value[i].name);
    echart.data.push({ value: tableData.value[i].value, name: tableData.value[i].name }); //add data into the table
  }
  initChart();
}

function initChart() {
  var myChart = echarts.init(document.getElementById('fad'));
  const option = {
    title: {
      text: '总品类统计',
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b} : {c} ({d}%)',
      // formatter: '{a} <br/>{b} : {c} ({d}%)',
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      data: echart.legend,
      // data: ['电脑', '鞋包', '酒水', '图书', '母婴'],
    },
    series: [
      {
        name: '总品类统计',
        type: 'pie',
        radius: '55%',
        center: ['50%', '60%'],
        data: echart.data,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  };
  myChart.setOption(option);
}

onMounted(() => {
  getfad();
})



</script>

<!-- <style scoped>
.box{
height: 400px;
width: 400px;
}
</style> -->
