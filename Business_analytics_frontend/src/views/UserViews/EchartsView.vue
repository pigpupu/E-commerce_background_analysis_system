<template>
  <el-card class="box-card">
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="搜索">
        <el-input v-model="formInline.thing" placeholder="thing" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">search</el-button>
      </el-form-item>

    </el-form>

    <el-row gutter="20" justify="center">
      <el-col :span=12>
        <el-date-picker
            v-model="value1"
            type="daterange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
        />
      </el-col>
      <el-col :span="8">
        <el-radio-group v-model="time" label="time-choose">
          <el-radio-button label="近一月">近一月</el-radio-button>
          <el-radio-button label="近一周">近一周</el-radio-button>
          <el-radio-button label="今日">今日</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>
  </el-card>

  <el-row justify="center" style="top: 50px">
    <el-col :span=8><pie-charts></pie-charts></el-col>
    <el-col :span=8><bar-chart></bar-chart></el-col>
  </el-row>

  <el-row justify="center">
    <el-col :span=8><line-chart></line-chart></el-col>
    <el-col :span=8><bar-charts></bar-charts></el-col>
  </el-row>


</template>

<script setup>
import PieCharts from "@/components/charts/PieCharts.vue";
import BarChart from "@/components/charts/BarChart.vue";
import LineChart from "@/components/charts/LineChart.vue";
import BarCharts from "@/components/charts/BarCharts.vue";

import { ref } from 'vue'

const value1 = ref('')
const time = ref('近一月')

let formInline = ref({
  thing: ''
})

let  onSubmit = () => {
  console.log('submit!')
}
</script>

<style scoped>
.demo-form-inline{
  text-align: center;
}

.box-card{
  margin-left: 20%;
  margin-right: 20%;
}
</style>
