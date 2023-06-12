<template>
    <div class="box">
              <div class="searchBox">
                <input type="text" v-model="keyword" placeholder="请输入你想查询的商品名" @change="onchange" class="searchInput" style="padding-left: 10px;">
                <!--<el-autocomplete
                v-model="keyword" :fetch-suggestions="querySearch"
                placeholder="请输入你想查询的商品名"
                @change="onchange" @select="choose"
                class="searchInput" style="padding-left: 10px;"
                ></el-autocomplete>-->
                <input type="button"  @click="getvalue" value="搜索" class="searchButton">
                <el-card class="box-card" v-model="suggest" v-show="isshow">
                  <div v-for="o in num" :key="o" class="text item">
                    {{ suggest[o] }}
                  </div>
                </el-card>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <br>
                <a href="http://127.0.0.1:8081/list">示例</a>
              </div>
            </div>
            <!--<el-col :span="12">
    <div class="sub-title">输入后匹配输入建议</div>
    <el-autocomplete
      class="inline-input"
      v-model="state2"
      :fetch-suggestions="querySearch"
      placeholder="请输入内容"
      :trigger-on-focus="false"
      @select="handleSelect"
    ></el-autocomplete>
  </el-col>-->

</template>
<script>

import axios from '../../utils/axios';

import { useRouter } from 'vue-router';

const router = useRouter();

 export default{
    data(){
        return {
            keyword: '',
            suggest: '',
            num: '',
            isshow: false
        }
    },
    methods: {
        getvalue(){
            var x= this.keyword//获取输入框元素
            if(x==0) alert("内容不得为空");
        },
        onchange(e){
        const { value } = e.target;
        console.log("检测到变化"+value);
        console.log(value);
        axios({
                                      url:"/input/goodName/tips",
                                      method:"get",
                                      params:{
                                        inputName:value,
                                    }
                                  }).then( ( response ) => {
                                    if(response.status==200&&value!=0) {
                                          //console.log(response);
                                          console.log(response.data);
                                          this.num = response.data.length -1 ;
                                          this.suggest=response.data;
                                          this.isshow=true;
                                          //alert(JSON.stringify(response));
                                      } else {
                                          alert("请输入你想查询的商品名！");
                                          this.isshow=false;
                                      }
                                  });
      },
      /*querySearch(queryString,cb) {
        // 调用 callback 返回建议列表的数据
        queryString = this.keyword;
        var result = queryString;
        cb(result);
      },

      choose(item) {
        console.log(item);
      }*/
    },
 }
</script>
<style scoped>
.box{
    margin: 0 auto;
    padding-top: 80px;
    height: 50px;
    width: 100%;
  }
  .searchBox{
    margin: 0 auto;
    width: 60%;
    margin-top: 10%;
    position: relative;
  }
  .searchInput{
    display: inline-block;
    width: 85%;
    height: 38px;
    border: 1px solid #cccccc;
    float: left;
    box-sizing: border-box;
    -moz-box-sizing:border-box; /* Firefox */
    -webkit-box-sizing:border-box; /* Safari */
    border-bottom-left-radius: 5px;
    border-top-left-radius: 5px;
  }
  .searchButton{
    display: inline-block;
    width: 15%;
    height: 38px;
    line-height: 40px;
    float: left;
    background-color: #00a0e9;
    font-size: 16px;
    cursor: pointer;
    border-bottom-right-radius: 5px;
    border-top-right-radius: 5px;
    border: none;
    color: #fff;
  }

</style>
