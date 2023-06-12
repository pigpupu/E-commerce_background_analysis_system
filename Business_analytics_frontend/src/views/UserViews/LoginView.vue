<template>

  <div class="rootDiv" style="background-color:#eff4f9">
    <el-card style="width:400px;height:400px;margin:auto;display: flex;">
      <div style="margin:auto">
        <el-form :model="r.loginForm" ref="thisform" size="large" :rules="r.rules" hide-required-asterisk="true">
          <el-form-item>
            <span style="font-size: 20px;font-family: Arial, Helvetica, sans-serif; margin-left: 60px">OKFG电商分析</span>

          </el-form-item>
          <el-form-item>
            <!-- <el-avatar @click=change style="margin:auto" src="favicon.ico" /> -->
            <el-avatar style="margin:auto" src="favicon.ico" />
          </el-form-item>
          <el-form-item label="账号" prop="account">
            <el-input placeholder="请输入账号" v-model="r.loginForm.account" />
          </el-form-item>
          <el-form-item label="密码" prop="userPassword">
            <el-input placeholder="请输入密码" type="password" v-model="r.loginForm.userPassword" />
          </el-form-item>
          <el-form-item style="display:flex">
            <el-button type="primary" style="margin:auto;width:100%" @click="login">登录</el-button>
          </el-form-item>
          <el-form-item>
            <div style="margin:auto auto auto 0"><router-link to="/register" style="color:#3A8BFF">注册</router-link></div>
            <div style="margin:auto 0 auto auto"><router-link to="/forgetpwd" style="color:#3A8BFF">找回密码</router-link>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>

</template>

<script setup>
import { reactive, ref, resolveComponent } from 'vue';
import { useRouter } from 'vue-router';
import axios from '../../utils/axios'
import globalVariable from './global_variable';
import { Key } from '@element-plus/icons-vue';
import { values } from 'lodash';
import { loginApi, getOwnInfoApi } from '../../api/systemApi.js'

const router = useRouter();
var thisform = ref("");

var r = reactive({
  loginForm: {
    account: "",
    userPassword: ""
  },
  rules: {
    account: [{ required: true, message: "请输入帐号名", trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{6,20}$/, message: "账户名为6-20位的数字或字母的组合", trigger: 'blur' }],
    //{ pattern: /(?=.*[0-9])[A-Za-z0-9 _]{6,10}$/, message: "6-10位，字母+数字", trigger: 'blur' }],
    //{ pattern: /^(1[3-9]{1}[0-9]{9})|(([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+)$/, message: "请正确输入手机号或邮箱号", trigger: 'blur' }]
    userPassword: [{ required: true, message: "请输入密码", trigger: 'blur' },
    //{ pattern: /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9 _]{6,10}$/, message: "6-10位含大小写字母和数字", trigger: 'blur' }],
    //{ pattern: /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9 _]{6,10}$/, message: "6-10位含大小写字母和数字", trigger: 'blur' }]
    { pattern: /^[A-Za-z0-9]{6,20}$/, message: "密码为6-20位的数字或字母的组合", trigger: 'blur' }],
  }
})

function login() {
  //localStorage.setItem(globalVariable.a, JSON.stringify(r.loginForm.account));
  thisform.value.validate((valid) => {
    if (valid) {
      loginApi(r.loginForm).then(function (response) {
        if (response.data.code == 200) {
          localStorage.setItem("token", response.data.result);
          getOwnInfoApi(r.loginForm).then((response) => {
            if (response.data.code == 200) {
              if (response.data.result.roleName == "普通用户") {
                localStorage.setItem("nowAccount", r.loginForm.account);
                router.push({ path: "/saleInfo" });
              }
              else if (response.data.result.roleName == "高级用户") {
                localStorage.setItem("nowAccount", r.loginForm.account);
                router.push({ path: "/gao/gsale" });
              }
              else if (response.data.result.roleName == "管理员") {
                localStorage.setItem("nowAccount", r.loginForm.account);
                router.push({ path: "/admin" });
              }
            } else {
              this.$message(
                {
                  message: response.data.message,
                  type: "error",
                }
              )
            }
          });
        } else {
          localStorage.setItem("token", "invalid");
          alert(response.data.message);
        }
      });
    }
  })
}


</script>

<style scoped>
.rootDiv {
  height: 100%;
  display: flex;

}

.el-card {
  --el-card-border-radius: 16px;
}

:deep().el-card__body {
  display: flex;
  width: 100%;
  height: 100%;
}
</style>
