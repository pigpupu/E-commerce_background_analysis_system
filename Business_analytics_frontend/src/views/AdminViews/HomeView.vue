<template>
  <div style="min-width:1100px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
  <el-container>
    <el-aside style="width:19%;box-shadow: 10px;background-color:darkseagreen">
      <el-container>
        <el-header>
          <!--<div style="width:100%;height:100%" display="flex">-->
            <RouterLink to="/login">
              <h2 style="color:papayawhip">OKFG管理员</h2>
            </RouterLink>
          <!--</div>-->
        </el-header>
        <el-main>
          <el-menu :default-active="route.path" :router="true" style="background-color:darkseagreen;" active-text-color="indigo" background-color="olive" text-color="beige">
            <el-menu-item index="/admin/userMgr">
              用户管理
            </el-menu-item>
            <el-menu-item index="/admin/goodMgr">
              商品管理
            </el-menu-item>
            <el-menu-item index="/admin/info">
              个人信息
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
