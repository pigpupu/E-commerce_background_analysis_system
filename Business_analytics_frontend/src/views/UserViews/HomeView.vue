<template>
  <div style="min-width:1350px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
  <el-container>
    <el-aside style="width:17%;box-shadow: 10px;background-color:darkseagreen">
      <el-container>
        <el-header>
          <!--<div style="width:100%;height:100%" display="flex">-->
            <RouterLink to="/login">
              <h2 style="color:papayawhip">OKFG分析系统</h2>
            </RouterLink>
          <!--</div>-->
        </el-header>
        <el-main>
          <el-menu :default-active="route.path" :router="true" style="background-color:darkseagreen;" active-text-color="indigo" background-color="olive" text-color="beige">

            <el-menu-item index="/saleInfo">
              销售量
            </el-menu-item>
            <el-menu-item index="/salemoneyInfo">
              销售额
            </el-menu-item>
            <el-menu-item index="/stockInfo">
              库存量
            </el-menu-item>
            <el-menu-item index="/info">
              用户信息
            </el-menu-item>
            <el-menu-item index="/pukehu">
              商品推荐
            </el-menu-item>
          </el-menu>
        </el-main>
      </el-container>
    </el-aside>
    <el-main>
      <RouterView />
    </el-main>
  </el-container>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';

const route = useRoute()

</script>

<style scoped>
a:-webkit-any-link {
  text-decoration: none;
}

.el-menu-item {
  font-size: 18px;
}
</style>
