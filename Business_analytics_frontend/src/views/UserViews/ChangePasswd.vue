<template>
    <div class="rootDiv" style="background-color:#eff4f9">
      <el-card style="width:400px;height:400px;margin:auto;display: flex;">
        <div style="margin:auto">
          <el-form :model="r.loginForm" ref="thisform" size="large" :rules="r.rules" hide-required-asterisk="true">
            <el-form-item>
              <span style="font-size: 20px;margin-left: 88px;font-family: Arial, Helvetica, sans-serif;">修改密码</span>
              <router-link to="/login" style="color:#3A8BFF;margin:auto 0 auto auto">返回登录</router-link>
            </el-form-item>
            <el-form-item label="账号名" prop="account">
              <el-input placeholder="请输入账户" v-model="r.loginForm.account" />
            </el-form-item>
            <div style="display:flex">
              <el-form-item label="原密码" prop="userPassword">
                <el-input placeholder="请输入原密码" type="password" v-model="r.loginForm.userPassword" />
              </el-form-item>
            </div>
            <el-form-item label="新密码" prop="newPassword">
              <el-input placeholder="请输入新密码" type="password" v-model="r.loginForm.newPassword" />
            </el-form-item>
            <el-form-item style="display:flex">
              <el-button type="primary" style="margin:auto;width:100%" @click="findbackpwd">确定</el-button>
            </el-form-item>

          </el-form>
        </div>
      </el-card>
    </div>

  </template>


  <script setup>
  import { reactive, ref } from 'vue';
  import { useRouter } from 'vue-router';
  import axios from '../../utils/axios'

  const router = useRouter();

  var thisform = ref("");

  var r = reactive({
    loginForm: {
      account: "",
      userPassword: "",
      newPassword: "",
    },
    rules: {
      account: [{ required: true, message: "请输入帐号名", trigger: 'blur' },
      { pattern: /^[A-Za-z0-9]{6,20}$/, message: "账户名为6-20位的数字或字母的组合", trigger: 'blur' }],
      userPassword: [{ required: true, message: "请输入原密码", trigger: 'blur' },
      { pattern: /^[A-Za-z0-9]{6,20}$/, message: "密码为6-20位的数字或字母的组合", trigger: 'blur' }],
      newPassword: [{ required: true, message: "请输入新密码", trigger: 'blur' },
      { pattern: /^[A-Za-z0-9]{6,20}$/, message: "密码为6-20位的数字或字母的组合", trigger: 'blur' }]
    }
  })


  function findbackpwd() {
    thisform.value.validate((valid) => {
      if (valid) {
        axios({
                              url:"/alterPassword",
                              method:"post",
                              contentType:"application/json",
                              data:{
                                  account:r.loginForm.account,
                                  userPassword:r.loginForm.userPassword,
                                  newPassword:r.loginForm.newPassword
                              }
                          }).then(function (response) {
                            if(response.data.code == 200) {
                                  router.push({path: "/login"});
                                  alert("您已成功修改密码！")
                              } else {
                                  alert("修改失败!");
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
