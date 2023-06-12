<!--
<template>
  <div style="min-width:1350px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
<div>

      <el-carousel :interval="4000" type="card" height="350px">
      <el-carousel-item v-for="item in imagebox" :key="item.id">
      <img :src="item.idView" class="image">
      </el-carousel-item>
      </el-carousel>
  <el-col :span="8">
    <el-card shadow="hover" class="box-card">
      <h2 class="ziti">客户情况分析功能，包括客户的下单周期、单品变化、客诉情况、流失预警等。”有效的客户细分是深度分析客户需求、应对客户需求变化的重要手段。通过合理、系统的客户分析，企业可以知道不同的客户有着什么样的需求，分析客户消费特征与商务效益的关系，使运营策略得到最优的规划；更为重要的是可以发现潜在客户，从而进一步扩大商业规模，使企业得到快速的发展。</h2>
    <el-button type="primary" @click="changerole" style="margin-top:140px;margin-left:670px;">升级为高级用户</el-button>
    <el-button type="primary" @click="checkTrade" style="margin-top:140px;margin-left:44px;">查询用户等级</el-button>
    </el-card>
  </el-col>

</div>
</div>
</template>




<script>

import axios from '../../utils/axios';

import { useRouter } from 'vue-router';


const router = useRouter();

 export default{
   data(){
     return {
           drawer: false,
           direction: 'btt',

       imagebox:[
       {id:0,idView:('https://tse4-mm.cn.bing.net/th/id/OIP-C.0PnqttVw6tEN4CmURj0AhwHaEK?w=297&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:1,idView:('https://tse1-mm.cn.bing.net/th/id/OIP-C.cPoiMhWaorhz8RXyOpxxJAHaEL?w=330&h=186&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:2,idView:('https://tse1-mm.cn.bing.net/th/id/OIP-C.YT9ZQo5C21Y53Sd4D17bIgHaD2?w=302&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:3,idView:('https://tse3-mm.cn.bing.net/th/id/OIP-C.hpozAzPz60d4oiZLH5H3_wHaEP?w=315&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:4,idView:('https://tse4-mm.cn.bing.net/th/id/OIP-C.-dvQR8u90O_lKCKcpDRe8QHaDt?w=305&h=175&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:5,idView:('https://tse1-mm.cn.bing.net/th/id/OIP-C.xpQ0EQwr7igc6VzYsqIFgAHaCF?w=350&h=98&c=7&r=0&o=5&dpr=1.5&pid=1.7')},

       ],
       // 浏览器宽度
       screenWidth :0,
       out_trade_no:""
     }
   },
    methods:{
         handleClose(done) {
       this.$confirm('确认关闭？')
         .then(_ => {
           done();
         })
         .catch(_ => {});
     },
            //下面是自适应框体大小的设置
             setSize:function () {
               // 通过浏览器宽度(图片宽度)实现计算高度
               this.bannerHeight = 400 / 1920 * this.screenWidth;
             },
             changerole(){
                axios({
                                    url:"/getVipTrade",
                                    method:"post",
                                    headers:{
                                        'Content-Type':'application/x-www-form-urlencoded',
                                        //'Content-Type':'application/json',
                                      },
                                    data:{
                                        account:localStorage.getItem("nowAccount")
                                    }
                                }).then(function (response) {
                                  if(response.data.code == 200) {
                                    //window.location.href = "https://excashier.alipaydev.com/standard/auth.htm?payOrderId=f95859a1e8f24d59a4f79468849e903b.00";

                                    window.location.href = response.data.result.jumpUrl ;
                                    this.out_trade_no=response.data.out_trade_no;
                                    //alert(response.data.result.jumpUrl)
                                    } else {
                                        alert("网络较差，请再次尝试！");
                                    }
                                });

             },

             checkTrade(){
              console.log("查询ing")
              axios({
                url:"/queryTradeResult",
                                    method:"post",
                                    headers:{
                                        'Content-Type':'application/x-www-form-urlencoded',
                                        //'Content-Type':'application/json',
                                      },
                                    data:{
                                        account:localStorage.getItem("nowAccount"),
                                        out_trade_no:this.out_trade_no
                                    }
                                }).then(function (response) {
                                  //router.push({path: "/clientanaly"});
                                  console.log("查询等级")
                                  console.log(this.out_trade_no)
                                  if(response.data.code == 200) {
                                        alert("您已成功升级为高级用户!");
                                        //window.location.reload();
                                        window.location.href = "/clientanaly";
                                    } else {
                                        alert("开通会员失败！");
                                    }

              })
             }
           },
         mounted() {
             // 首次加载时,需要调用一次
             this.screenWidth =  window.innerWidth;
             this.setSize();
             // 窗口大小发生改变时,调用一次
             window.onresize = () =>{
             this.screenWidth =  window.innerWidth;
             this.setSize();
           }
         }
 }
</script>

<style>
  .el-carousel__item h3 {
     color: #475669;
     font-size: 14px;
     opacity: 0.75;
     line-height: 200px;
     margin: 0;
   }

   .el-carousel__item:nth-child(2n) {
     background-color: #99a9bf;
   }

   .el-carousel__item:nth-child(2n+1) {
     background-color: #d3dce6;
   }
   img{
    /*设置图片宽度和浏览器宽度一致*/
     width:100%;
     height:inherit;
   }
   .location{
     margin-right: 8%;
     margin-bottom: 10%;
   }
   .box-card {
    width: 1000px;
    height: 400px;
  }
   .centertry {
    text-align: center;
   }
   .ziti{
    font-family:"PingFang SC";
    color:#475669;
   }
</style>


-->

<template>
  <div style="min-width:1350px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
<div>

      <el-carousel :interval="4000" type="card" height="350px">
      <el-carousel-item v-for="item in imagebox" :key="item.id">
      <img :src="item.idView" class="image">
      </el-carousel-item>
      </el-carousel>
  <el-col :span="8">
    <el-card shadow="hover" class="box-card">
      <h2 class="ziti">推荐系统是利用电子商务网站向客户提供商品信息和建议，帮助用户决定应该购买什么产品，模拟销售人员帮助客户完成购买过程。个性化推荐是根据用户的兴趣特点和购买行为，向用户推荐用户感兴趣的信息和商品。</h2>
    <el-button type="primary" @click="changerole" style="margin-top:140px;margin-left:670px;">免费升级为高级用户，重新登陆后生效！</el-button>

    </el-card>
  </el-col>

</div>
</div>
</template>




<script>

import axios from '../../utils/axios';

import { useRouter } from 'vue-router';


const router = useRouter();

 export default{
   data(){
     return {
           drawer: false,
           direction: 'btt',

       imagebox:[
       {id:0,idView:('https://tse4-mm.cn.bing.net/th/id/OIP-C.0PnqttVw6tEN4CmURj0AhwHaEK?w=297&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:1,idView:('https://tse1-mm.cn.bing.net/th/id/OIP-C.cPoiMhWaorhz8RXyOpxxJAHaEL?w=330&h=186&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:2,idView:('https://tse1-mm.cn.bing.net/th/id/OIP-C.YT9ZQo5C21Y53Sd4D17bIgHaD2?w=302&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:3,idView:('https://tse3-mm.cn.bing.net/th/id/OIP-C.hpozAzPz60d4oiZLH5H3_wHaEP?w=315&h=180&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:4,idView:('https://tse4-mm.cn.bing.net/th/id/OIP-C.-dvQR8u90O_lKCKcpDRe8QHaDt?w=305&h=175&c=7&r=0&o=5&dpr=1.5&pid=1.7')},
       {id:5,idView:('https://tse1-mm.cn.bing.net/th/id/OIP-C.xpQ0EQwr7igc6VzYsqIFgAHaCF?w=350&h=98&c=7&r=0&o=5&dpr=1.5&pid=1.7')},

       ],
       // 浏览器宽度
       screenWidth :0,
       out_trade_no:""
     }
   },
    methods:{
         handleClose(done) {
       this.$confirm('确认关闭？')
         .then(_ => {
           done();
         })
         .catch(_ => {});
     },
            //下面是自适应框体大小的设置
             setSize:function () {
               // 通过浏览器宽度(图片宽度)实现计算高度
               this.bannerHeight = 400 / 1920 * this.screenWidth;
             },
             changerole(){
                axios({
                                    url:"/updateCenter",
                                    method:"post",
                                    headers:{
                                        'Content-Type':'application/x-www-form-urlencoded',
                                        //'Content-Type':'application/json',
                                      },
                                    data:{
                                        account:localStorage.getItem("nowAccount"),
                                        roleId:1
                                    }
                                }).then(function (response) {
                                  if(response.data.code == 200) {
                                    //window.location.href = "https://excashier.alipaydev.com/standard/auth.htm?payOrderId=f95859a1e8f24d59a4f79468849e903b.00";

                                    //window.location.href = response.data.result.jumpUrl ;

                                    //window.location.href = "/login";
                                    alert("重新登录即权益生效！");
                                    
                                    
                                    } else {
                                        alert("账户有误！");
                                    }
                                });
             },
           },
         mounted() {
             // 首次加载时,需要调用一次
             this.screenWidth =  window.innerWidth;
             this.setSize();
             // 窗口大小发生改变时,调用一次
             window.onresize = () =>{
             this.screenWidth =  window.innerWidth;
             this.setSize();
           }
         }
 }
</script>

<style>
  .el-carousel__item h3 {
     color: #475669;
     font-size: 14px;
     opacity: 0.75;
     line-height: 200px;
     margin: 0;
   }

   .el-carousel__item:nth-child(2n) {
     background-color: #99a9bf;
   }

   .el-carousel__item:nth-child(2n+1) {
     background-color: #d3dce6;
   }
   img{
    /*设置图片宽度和浏览器宽度一致*/
     width:100%;
     height:inherit;
   }
   .location{
     margin-right: 8%;
     margin-bottom: 10%;
   }
   .box-card {
    width: 1000px;
    height: 400px;
  }
   .centertry {
    text-align: center;
   }
   .ziti{
    font-family:"PingFang SC";
    color:#475669;
   }
</style>
