<template>
  <div style="min-width:1000px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
    <el-card class="box-card">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item>
          <p style="margin-left: 20px; margin-right: 17px;">商品类别</p><el-select v-model="searchForm.categoryID" clearable
            placeholder="派对物品">
            <el-option v-for="item in catOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <p style="margin-left: 180px;">选填：</p>
          <el-form-item label="" style="margin-left : 10px">
            <el-input v-model="searchForm.inputName" placeholder="商品名称" />
          </el-form-item>
        </el-form-item>
      </el-form>

      <el-row gutter="20" justify="center">
        <el-col :span="12">
          <el-date-picker v-model="timeRange" type="daterange" unlink-panels range-separator="To"
            start-placeholder="Start date" end-placeholder="End date" value-format="YYYY-MM-DD" :shortcuts="shortcuts"
            :size="size" />
        </el-col>
        <el-col :span="8">
          <el-radio-group v-model="searchForm.showSize" label="showSize-choose">
            <el-radio-button :label="1">天</el-radio-button>
            <el-radio-button :label="2">周</el-radio-button>
            <el-radio-button :label="3">月</el-radio-button>
            <el-radio-button :label="4">年</el-radio-button>
          </el-radio-group>
        </el-col>
      </el-row>

      <el-row gutter="200" justify="center" style="top: 30px;height: 100px;">
        <el-col :span="12">
          <el-switch v-model="searchForm.precise" class="mb-2" active-text="精确" inactive-text="模糊" />
        </el-col>

        <el-col :span="8">
          <el-button type="primary" @click="onSubmit">search</el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-row justify="center" style="top: 100px">
      <el-col :span="8" style="margin-right : 110px">
        <single-bar-chart :horizontal="backData.names" :vertical="backData.values"
          :barTitleText="'销售量柱状图'"></single-bar-chart>
      </el-col>
      <el-col :span="8">
        <single-line-chart :horizontal="backData.names" :vertical="backData.values"
          :lineTitleText="'销售量条形图'"></single-line-chart>
      </el-col>
      <el-col :span="8" style="margin-left : 20px ; margin-top: 100px;">
        <basic-pie-chart :classify="backData.names" :piedata="backData.pieMapVoList" :pieTitleText="'销售量饼图'"></basic-pie-chart>
      </el-col>
    </el-row>

    <el-row justify="center" style="top: 200px">
      <el-col :span="24">
        <basic-map-chart :mapdata="backMapData.pieMapVoList" :titleText="'中国区域销售量情况分析图'"></basic-map-chart>
      </el-col>
    </el-row>

    <el-row justify="center" style="top: 300px">
      <div>销售量单日情况统计表</div>
      <el-col>
        <Table :data="tableData" :column="tableTitle" :operation="false" :page="page" :loading="loading"
          @onSizeChange="onSizeChange" @onCurrentChange="onCurrentChange" @setCellColor="setCellColor">
        </Table>
      </el-col>
      <div>该分类下的TOP销售量商品</div>
      <el-col style="margin-bottom: 80px;">
        <Table :data="tableData2" :column="tableTitle2" :operation="false" :page="page2" :loading="loading"
          @onSizeChange="onSizeChange2" @onCurrentChange="onCurrentChange2" @setCellColor="setCellColor">
        </Table>
      </el-col>
      <el-col style="margin-bottom: 80px;">
        <Table :data="tableData20"  :operation="false" :page="page8" :loading="loading"
          @onSizeChange="onSizeChange" @onCurrentChange="onCurrentChange" @setCellColor="setCellColor">
        </Table>
      </el-col>

      <el-col :span="8"><div class="grid-content ep-bg-purple-light" /></el-col>
      <!--<el-col style="margin-bottom: 80px;">
      </el-col>-->
    </el-row>
  </div>
</template>

<script setup>

import { onMounted, reactive, ref, toRefs } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import Table from "../../components/myTable/Table.vue";
import SingleBarChart from "@/components/charts/tryCharts/singleBarChart.vue";
import SingleLineChart from "@/components/charts/tryCharts/singleLineChart.vue";
import BasicPieChart from "@/components/charts/tryCharts/BasicPieChart.vue";
import BasicMapChart from "@/components/charts/tryCharts/BasicMapChart.vue";

import axios from "../../utils/axios";
import {
  queryAllCategory, basicVolumeChart, basicVolumePage,
  Top10MoneyGoodVo, basicVolumeMapChart
} from "../../api/systemApi.js"

const state = reactive({

  searchForm: {
    startTimeStr: null,
    endTimeStr: null,
    categoryID: null,
    showSize: 4,
    inputName: "",
    precise: 0,
  },
  page: {
    nowPage: 1,
    pageSize: 10,
    total: 10
  },
  page2:{
    nowPage: 1,
    pageSize: 10,
    total: 10
  },
  timeRange: [],
  catOptions: [
    {
      value: "1",
      label: "选择种类",
    }
  ],
  tableData: [],
  tableData2: [],
  tableTitle: [
    { prop: '', label: '', fixed: 'left' },
    { prop: 'addTimeDay', label: '时间' },
    { prop: 'periodVolumeTotal', label: '销售量' },
  ],
  tableTitle2: [
    { prop: '', label: '', fixed: 'left' },
    { prop: 'good_id', label: '商品id' },
    { prop: 'good_name', label: '商品名称' },
    { prop: 'price', label: '商品价格' },
    { prop: 'periodVolumeTotal', label: '销售量' },
  ],
  backData: {
    names: [],
    values: [],
    pieMapVoList: []
  },
  backMapData: {
    names: [],
    values: [],
    pieMapVoList: []
  }
});

const {searchForm, page,page2,timeRange, catOptions, tableData, tableData2, tableTitle, tableTitle2, backData, backMapData } = toRefs(state);

const shortcuts = [
  {
    text: "Last 5 Year",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 365 * 5);
      return [start, end];
    },
  },
  {
    text: "Last 7 Year",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 365 *7);
      return [start, end];
    },
  },
  {
    text: "Last 10 Year",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 365 * 10);
      return [start, end];
    },
  },
];

onMounted(() => {
  queryAllCategory(state.searchForm).then((response) => {
    state.catOptions = response.data;
  });

});


const setCellColor = (e, callback) => {
  /**
   * e.row：表格那一行的数据
   * e.column：表格单元格那一列的信息
   */
  if (e.row.delFlag === 0 && e.column.property === 'delFlagName') {
    callback({ color: '#67C23A', fontWeight: '700' });
  } else if (e.row.delFlag === 1 && e.column.property === 'delFlagName') {
    callback({ color: '#f00', fontWeight: '700' });
  } else {
    callback({});
  }
}

const onSizeChange = (e) => {
  state.page.pageSize = e;
  onSubmit_pageChange();
}
const onCurrentChange = (e) => {
  state.page.nowPage = e;
  onSubmit_pageChange();
}
const onSizeChange2 = (e) => {
  state.page2.pageSize = e;
  onSubmit_pageChange();
}
const onCurrentChange2 = (e) => {
  state.page2.nowPage = e;
  onSubmit_pageChange();
}

let onSubmit = () => {
  searchForm.value.startTimeStr = timeRange.value[0];
  searchForm.value.endTimeStr = timeRange.value[1];
  searchForm.value.precise = searchForm.value.precise === true ? 1 : 0;
  if (searchForm.value.categoryID == null) alert("提示：默认查询的商品种类为派对用品")
  if (searchForm.value.startTimeStr == null) alert("默认查询最近5年的数据")
  basicVolumeChart(state.searchForm).then((response) => {
    if (response.data.code == 200) {
      state.backData = response.data.result;
    } else {
      alert(response.data.message);
    }
  });

  basicVolumePage(Object.assign(state.page, state.searchForm)).then((response) => {
    if (response.data.code == 200) {
      state.tableData = response.data.result.records;
      state.page.nowPage = response.data.result.current;
      state.page.pageSize = response.data.result.size;
      state.page.total = response.data.result.total;
    }
  });

  Top10MoneyGoodVo(state.searchForm).then((response) => {
    if (response.data.code == 200) {
      state.tableData2 = response.data.result;
    }
  });

  basicVolumeMapChart(state.searchForm).then((response) => {
    if (response.data.code == 200) {
      state.backMapData = response.data.result;
    }
  });

};

let onSubmit_pageChange = () => {
  searchForm.value.startTimeStr = timeRange.value[0];
  searchForm.value.endTimeStr = timeRange.value[1];
  searchForm.value.precise = searchForm.value.precise === true ? 1 : 0;

  basicVolumePage(Object.assign(state.page, state.searchForm)).then((response) => {
    state.tableData = response.data.result.records;
    state.page.nowPage = response.data.result.current;
    state.page.pageSize = response.data.result.size;
    state.page.total = response.data.result.total;
  });

  Top10MoneyGoodVo(state.searchForm).then((response) => {
    state.tableData2 = response.data.result;
  });

};
</script>

<style scoped>
.demo-form-inline {
  text-align: center;
}

.box-card {
  margin-left: 5%;
  margin-right: 5%;
}
</style>
