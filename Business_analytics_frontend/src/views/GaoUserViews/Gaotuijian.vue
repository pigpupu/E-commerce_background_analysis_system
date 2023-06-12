<template>
  <div style="min-width:1000px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
    <div>&nbsp;</div>
    <div>
    <el-radio v-model="radio1" label="1" border @click="choose1">算法一：基于购买</el-radio>
    <el-radio v-model="radio1" label="2" border @click="choose2">算法二：基于评分</el-radio>
    </div>
    <div>&nbsp;</div>
    <div>&nbsp;</div>
    <el-button type="primary" @click="tuijian">查看推荐情况</el-button>
    <div>&nbsp;</div>
    <div>&nbsp;</div>
    <el-table
      :data="tableData"
      stripe
      style="width: 100%">
      <el-table-column
        prop="customerId"
        label="客户编号"
        width="180">
      </el-table-column>
      <el-table-column
        prop="customerName"
        label="客户姓名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="goodId"
        label="商品编号">
      </el-table-column>
      <el-table-column
        prop="goodName"
        label="商品名">
      </el-table-column>
      <el-table-column
        prop="score"
        label="评分">
      </el-table-column>
    </el-table>
    <el-pagination
  @size-change="handleSizeChange"
  @current-change="handleCurrentChange"
  :current-page="currentPage"
  :page-sizes="[15, 30, 50, 100]"
  :page-size="pageSize"
  layout="sizes, prev, pager, next, jumper"
  :total="total">
</el-pagination>
</div>
  </template>

  <script>
import axios from '../../utils/axios';

import { useRouter } from 'vue-router';
import { values } from 'lodash';

const router = useRouter();

    export default {
      data() {
        return {
          tableData: [{
            customerId: "",
            customerName: "",
            goodId: "",
            goodName: "",
            score: ""
          }],
          radio1: '1',
          currentPage: 1,
          pageSize: 100,
          suanfa: 1,
          total: ''
        }
      },
      methods: {
      handleSizeChange(val) {

        this.pageSize = val;
      },
      handleCurrentChange(val) {

        this.currentPage = val;
        this.tuijian();
      },
      choose1(){
        this.suanfa=1;

      },
      choose2(){
        this.suanfa=2;

      },
      tuijian(){
        var params = {
              pageSize: this.pageSize ,
              nowPage : this.currentPage,
            };
        if(this.suanfa==1){
          axios({
                                      url:"/admin/recommend/planA",
                                      method:"post",
                                      params: params,
                                      headers:{
                                        'Content-Type':'application/x-www-form-urlencoded',
                                      }
                                  }).then( ( response ) => {
                                    if(response.data.code == 200) {

                                          this.tableData=response.data.result.records;
                                          this.total=response.data.result.total;
                                      } else {
                                          this.$message(
                                            {
                                              message : response.data.message,
                                              type : "error",
                                            }
                                          )
                                      }
                                  });
        }else if(this.suanfa==2){
          axios({
                                      url:"/admin/recommend/planB",
                                      method:"post",
                                      params: params,
                                      headers:{
                                        'Content-Type':'application/x-www-form-urlencoded',
                                      }
                                  }).then( ( response ) => {
                                    if(response.data.code == 200) {

                                          this.tableData=response.data.result.records;
                                          this.total=response.data.result.total;
                                      } else {
                                          this.$message(
                                            {
                                              message : response.data.message,
                                              type : "error",
                                            }
                                          )
                                      }
                                  });
        }
      }
    },
    created(){
      this.tuijian();
    }
    }
  </script>
