<template>
  <div style="min-width:1000px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
  <div>&nbsp;</div>
  <div>商品类别</div>
  <div>&nbsp;</div>
  <div>&nbsp;</div>
  <el-select v-model="value" placeholder="请选择您想要预测的类型">
    <el-option
      v-for="item in options"
      :key="item.value"
      :label="item.label"
      :value="item.value"
      @click="dayin">
    </el-option>
  </el-select>
  <div>&nbsp;</div>
  <div>&nbsp;</div>
  <div>请您选择预测周期</div>
  <div>&nbsp;</div>
  <div>&nbsp;</div>
  <div>
    <el-radio-group v-model="radio1" @click="draw">
      <el-radio-button label="年"></el-radio-button>
      <el-radio-button label="月"></el-radio-button>
      <el-radio-button label="周"></el-radio-button>
      <el-radio-button label="日"></el-radio-button>
    </el-radio-group>
  </div>
  <div>&nbsp;</div>
  <div>&nbsp;</div>
  <div>下一个周期的销量预测为&nbsp;&nbsp;&nbsp;{{this.manunum}}</div>
  <div id="test_app">
      <!--echarts的容器-->
	<div id="main" style="min-width:420px;min-height:810px;background:#fff"></div>
  </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
  export default {
    name: '',
    data() {
      return {
        options: [{
          value: 1,
          label: '乐器'
        }, {
          value: 2,
          label: '电器'
        }, {
          value: 3,
          label: '服饰'
        }, {
          value: 4,
          label: '食物'
        }, {
          value: 5,
          label: '车具'
        }],
        value: '',
        radio1: '年',
        firnum: 123,
        secnum: 208,
        thinum: 398,
        manunum: '',
        charts: '',
				opinionData: ['','','','']
      }
    },
    methods: {
      dayin(){
      },
      draw(){
        if(this.firnum<this.secnum&&this.secnum<this.thinum){
          this.manunum=this.secnum+(this.thinum-this.firnum)/2;
        }else if(this.firnum>this.secnum&&this.secnum>this.thinum){
          this.manunum=this.secnum-(this.firnum-this.thinum)/2;
        }else{
          this.manunum=(this.firnum+this.secnum+this.thinum)/3;
        }

        this.opinionData[0]=this.firnum;
        this.opinionData[1]=this.secnum;
        this.opinionData[2]=this.thinum;
        this.opinionData[3]=this.manunum;

        this.drawLine('main');
      },
      drawLine(id) {
				this.charts = echarts.init(document.getElementById(id))
				this.charts.setOption({
                    title:{
                        left:'3%',
                        top:'5%',
                        text:"商品销量预测走向",//标题文本，支持使用 \n 换行。
                    },
					tooltip: {
						trigger: 'axis'
					},
					legend: {
                        align:'right',//文字在前图标在后
                        left:'3%',
                        top:'15%',
						data: ['近三个周期以及未来一个周期的销量']
					},
					grid: {
                        top:'30%',
						left: '5%',
						right: '5%',
						bottom: '5%',
						containLabel: true
					},

					toolbox: {
						feature: {
							saveAsImage: {} //保存为图片
						}
					},
					xAxis: {
						type: 'category',
                        boundaryGap:true,
                        axisTick:{
                            alignWithLabel:true //保证刻度线和标签对齐
                        },
                        data: ["周期一","周期二","周期三","未来一个周期"] //x坐标的名称

					},
					yAxis: {
						type: 'value',
						boundaryGap: true,
                        splitNumber:4, //纵坐标数
                        interval:250 //强制设置坐标轴分割间隔
					},

					series: [{
						name: '近一周',
						type: 'line', //折线图line;柱形图bar;饼图pie
						stack: '总量',
                        areaStyle: {
                            //显示区域颜色---渐变效果
                            color:{
                                type: 'linear',
                                x: 0,
                                y: 0,
                                x2: 0,
                                y2: 1,
                                colorStops: [{
                                    offset: 0, color: 'rgb(255,200,213)' // 0% 处的颜色
                                }, {
                                    offset: 1, color: '#ffffff' // 100% 处的颜色
                                }],
                                global: false // 缺省为 false
                            }
                        },
                        itemStyle: {
							color: 'rgb(255,96,64)', //改变折线点的颜色
							lineStyle: {
								color: 'rgb(255,96,64)' //改变折线颜色
							}

                        },
						data: this.opinionData
					}]
				})
			},
    },
  }
</script>
<style scoped>
	* {
		margin: 0;
		padding: 0;
		list-style: none;
	}
</style>
