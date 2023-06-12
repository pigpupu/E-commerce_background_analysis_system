<template>
  <el-card>
    <template #header>
      <span>客户分析</span>
    </template>
    <template style="display:flex;height:80px;width:100%">

      <div style="width:20%">分析对象：
        <el-select placeholder="对象id" v-model="idTable.userids.id" filterable clearable style="width:55%"
          @change="changeValue">
          <el-option v-for="item in idTable.userids" :key="item.id" :label="item.id" :value="item.id" />
        </el-select>
        <!-- {{ r.labelform.id }} -->
      </div>
      <div style="width:80%;display: block;">
        <div style="width:100%;height:50%;display: flex">
          <!-- 对象姓名：<el-input label="对象姓名" v-model="r.labelform.name" style="width:20%" readonly></el-input>
          性别：<el-input label="性别" v-model="r.labelform.gender" style="width:20%" readonly></el-input>
          对象类型：<el-input label="对象类型" v-model="r.labelform.type" style="width:20%" readonly></el-input> -->
          <div style="width:20%">对象姓名：{{ r.labelform.name }}</div>
          <div style="width:20%">性别：{{ r.labelform.gender }}</div>
          <div style="width:20%">对象类型： {{ r.labelform.type }}</div>
        </div>
        <div style="width:100%;height:50%;display: flex;">
          <div style="width:20%">婚姻状态：{{ r.labelform.marriage }}</div>
          <div style="width:20%">学历：{{ r.labelform.degeree }}</div>
          <div style="width:20%">流失预警：{{ r.labelform.losspossble }}</div>
        </div>
      </div>

    </template>
  </el-card>
  <el-card>
    <el-row gutter="10" justify="center">
      <!-- <el-col :span=12>
        <el-date-picker v-model="value1" type="daterange" range-separator="To" start-placeholder="Start date"
          end-placeholder="End date" @change="timechange" value-format="YYYY-MM-DD" />
      </el-col> -->
      <div style="margin:auto 0 auto auto">时间跨度:</div>
      <el-col :span="8">
        <el-radio-group v-model="time" label="time-choose">
          <el-radio-button label="年">年</el-radio-button>
          <el-radio-button label="月">月</el-radio-button>
          <el-radio-button label="周">周</el-radio-button>
        </el-radio-group>
      </el-col>
      <div style="margin:auto 0 auto auto">具体时间:</div>
      <el-col :span="8">
        <div v-if="time === '年'">
          <el-date-picker v-model="value1" type="year" placeholder="Pick a year" @change="timechange"
            value-format="YYYY-MM-DD" />
        </div>
        <div v-if="time === '月'">
          <el-date-picker v-model="value1" type="month" placeholder="Pick a month" @change="timechange"
            value-format="YYYY-MM-DD" />
        </div>
        <div v-if="time === '周'">
          <el-date-picker v-model="value1" type="week" format="YYYY [Week] ww" placeholder="Pick a week"
            @change="timechange" value-format="YYYY-MM-DD" />
        </div>
      </el-col>

    </el-row>
    <el-row justify="center">
      <el-col :span=100>
        <div style="height: 250px;width: 800px;">
          <order-chart :xaxispay="payaxis.xaxis" :yaxispay="payaxis.yaxis" />
        </div>
      </el-col>
    </el-row>

    <!--
    <el-row style="top: 50px">
      <div style="height: 400px;width: 350px;">
        <FadChart />
      </div>
      <div style="height: 400px;width: 350px;">
        <FadChangeChart />
      </div>
      <div style="height: 400px;width: 400px;">
        <CompareChart />
      </div>
    </el-row>
    -->
  </el-card>
</template>

<script setup>
import OrderChart from "@/components/charts/OrderChart.vue"
import { onMounted, ref, reactive, onBeforeMount, onUpdated, toRefs } from 'vue'
import PieCharts from "@/components/charts/PieCharts.vue";
import BarChart from "@/components/charts/BarChart.vue";
import LineChart from "@/components/charts/LineChart.vue";
import BarCharts from "@/components/charts/BarCharts.vue";
import PossibleChart from "@/components/charts/PossibleChart.vue"

import CompareChart from "@/components/charts/CompareChart.vue"
import FadChart from "@/components/charts/FadChart.vue"
import FadChangeChart from "@/components/charts/FadChangeChart.vue"
import axios from 'axios';

const value1 = ref('') //具体日期（YY-MM-DD）
const time = ref('年') //时间跨度
const idnow = ref('') //当前id
const normalstartdate = ref('2017-01-01') //默认时间

const state = reactive({
  payaxis: {
    xaxis: [],
    yaxis: [],
  },
  allaxis: { //全品类
    xaxis: [],
    yaxis: [],
  },
  cutaxis: { //时间段内
    xaxis: [],
    yaxis: [],
  }
});
const { payaxis } = toRefs(state);

var r = reactive({
  labelform: {
    id: "",
    name: "",
    gender: "",
    type: "",
    marriage: "",
    degeree: "",
    losspossble: ""
  }
})

var idTable = reactive({
  userids: [
    // {
    //   id: '10',
    //   name: 'test10',
    //   gender: '男',
    //   type: '零售',
    //   marriage: '未婚',
    //   degeree: 'high',
    //   losspossble: 'danger',
    // },
    // {
    //   id: '20',
    //   name: 'test20',
    //   gender: '女',
    //   type: '批发',
    //   marriage: '已婚',
    //   degeree: 'high20',
    //   losspossble: 'danger20',
    // },
    // {
    //   id: '30',
    //   name: 'test30',
    //   gender: '男',
    //   type: '零售',
    //   marriage: 'yes3',
    //   degeree: 'high3',
    //   losspossble: 'danger30',
    // },
  ],
})

var outids = reactive({
  userids: [
    {
      id: '10',
      name: 'test10',
      gender: '男',
      type: '零售',
      marriage: '未婚',
      degeree: 'high',
      losspossble: 'danger',
    },
    {
      id: '20',
      name: 'test20',
      gender: '女',
      type: '批发',
      marriage: '已婚',
      degeree: 'high20',
      losspossble: 'danger20',
    },
    {
      id: '30',
      name: 'test30',
      gender: '男',
      type: '零售',
      marriage: 'yes3',
      degeree: 'high3',
      losspossble: 'danger30',
    },
  ],
})


const ids = ['test1', 'e', 'we', 'df']
const idall = [] //存储所有的用户id


let onSubmit= () => {
  state.payaxis.xaxis = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']; //赋值
  state.payaxis.yaxis = [120, 132, 101, 134, 90, 230, 210];
}



//先获取所有用户id
onMounted(() => {
  idTable.userids = outids.userids; //此处outids.userids将向后端获取

  for (var i = 0; i < outids.userids.length; i++) { //获取所有的id
    idall[i] = outids.userids[i].id;
  }
  // console.log(idall) //测试将所有id存入idall

  // axios.post('/getid') //获取id数据，存入idTable.userids[]，
  //   .then((res) => {
  //     if (res.data.code == 200) {
  //       idTable.userids = res.data.object;
  //     }
  //   })

  // axios.get("/getclientid")
  //   .then((Response) => {
  //     if (Response.data.code == 200) {
  //       idTable.userids=Response.data.sdf; //从后端获取用户的所有相关数据
  //     }
  //   })

 onSubmit();
})

//监测下拉框变化，发送id并拉取用户信息和下单数据
function changeValue(now) //此处now可获取option的值
{
  idnow.value = now; //将选择的id存储下来

  // //获取该id对应用户信息
  // axios.get('/getdata', { params: { id: idnow.value } })
  //   .then((res) => {
  //     r.labelform = res.data.object;
  //   })

  // //获取该id的2017的下单周期
  // axios.get('/getbuynumber', { params: { id: idnow.value, timrspan: time.value, startdate: normalstartdate.value } })
  //   .then((res) => {

  //   })

  // //获取该id的全品类数据
  // axios.get('/getbuyall', { params: { id: idnow.value } })
  // .then((res) => {

  // })

  for (var i = 0; i < idTable.userids.length; i++) {
    if (idTable.userids[i].id == now) {
      r.labelform = idTable.userids[i];
      break;
    }
  }
}

function timechange(val) { //time changed then send a get request, update data of table

  // axios.get('/xxxx', { params: { id: idnow.value, timespan: time.value, startdate: value1.value } }) //对象id，时间跨度，开始时间
  //   .then((res) => {

  //   })

  // payaxis.xaxis =['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']; //赋值
  // payaxis.yaxis=[120, 132, 101, 134, 90, 230, 210];
  // console.log(payaxis.xaxis)
  // console.log(payaxis.yaxis)

  // axios.post('/clientdatachange', { id: localStorage.getItem("userid"),starttime: })
  //   .then((res) => {
  //     echart.data = []
  //     tableData.value = res.data.object; //object depends on back
  //     for (let i = 0; i < tableData.value.length; i++) {
  //       echart.legend.push(tableData.value[i].name);
  //       echart.data.push(tableData.value[i]); //add data into the table
  //     }
  //     initChart() //call init chart
  //   })
}

</script>
