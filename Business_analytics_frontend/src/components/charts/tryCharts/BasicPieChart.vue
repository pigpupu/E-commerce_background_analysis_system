<template>
    <div class="box">
        <v-chart class="chart" :option="option" :autoresize="true" />
    </div>
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
import { defineComponent, reactive, toRefs } from 'vue';
import { GridComponent } from 'echarts/components';
import { defineProps, defineEmits } from 'vue';


use([
    CanvasRenderer,
    PieChart,
    TitleComponent,
    TooltipComponent,
    LegendComponent,
    GridComponent
]);

const props = defineProps({ classify: { type: Array }, piedata: { type: Array },pieTitleText:{type:Array} })
const { classify, piedata,pieTitleText } = toRefs(props);

const option = reactive({
    title: {
        text: pieTitleText,
        left: 'center',
    },
    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)',
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: classify,
    },
    series: [
        {
            name: '饼图情况分析',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: piedata,
            emphasis: {
                itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
            },
        },
    ],
});

</script>

<style scoped>
.box {
    height: 400px;
    width: 400px;
}
</style>
