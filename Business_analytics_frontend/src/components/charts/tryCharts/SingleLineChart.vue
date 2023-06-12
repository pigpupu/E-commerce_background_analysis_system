<template>
    <div class="box">
        <v-chart class="chart" :option="option" :autoresize="true" />
    </div>
</template>

<script setup>
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { LineChart } from 'echarts/charts';
import {
    TitleComponent,
    TooltipComponent,
    LegendComponent,
} from 'echarts/components';
import VChart, { THEME_KEY } from 'vue-echarts';
import { defineComponent, reactive, toRefs } from 'vue';
import { GridComponent } from 'echarts/components';
import { defineProps, defineEmits } from 'vue';


use([
    CanvasRenderer,
    LineChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent
]);

const props = defineProps({ horizontal: { type: Array }, vertical: { type: Array },lineTitleText:{type: String}})
const { horizontal,vertical,lineTitleText} = toRefs(props);

const option = reactive({
    legend: {},
    title: {
        text: lineTitleText,
        left: 'left',
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
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
            name: '',
            nameTextStyle: {// 名称样式
                fontSize: 12,
                color: '#333333',
                fontWeight: 'bold'
            },
            data: horizontal,
            axisTick: {
                alignWithLabel: true
            }
        }
    ],
    yAxis: [
        {
            type: 'value'
        }
    ],
    series: [
        {
            name: 'indicator',
            nameTextStyle: {
                fontSize: 12,
                color: '#333333',
                fontWeight: 'bold'
            },
            type: 'line',
            data: vertical
        }
    ]
});

</script>

<style scoped>
.box {
    height: 400px;
    width: 400px;
}
</style>
