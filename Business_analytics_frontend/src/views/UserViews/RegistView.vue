<template>

  <div class="rootDiv" style="background-color:#eff4f9">
    <el-card style="width:400px;height:400px;margin:auto;display: flex;">
      <div style="margin:auto">
        <el-form :model="r.loginForm" ref="thisform" size="large" :rules="r.rules" hide-required-asterisk="true">
          <el-form-item>
            <span style="font-size: 20px;margin-left: 88px;font-family: Arial, Helvetica, sans-serif;">用户注册</span>
          </el-form-item>
          <el-form-item label="账号" prop="account">
            <el-input placeholder="请输入账号" v-model="r.loginForm.account" />
          </el-form-item>
          <el-form-item label="密码" prop="userPassword">
            <el-input placeholder="请输入密码" type="password" v-model="r.loginForm.userPassword" />
          </el-form-item>
          <el-form-item style="display:flex">
            <el-button type="primary" style="margin:auto;width:100%" @click="register">注册</el-button>
          </el-form-item>
          <el-form-item>
            已有账号？ 请点击<router-link to="/login" style="color:#3A8BFF">登录</router-link>
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
import { registerApi } from '../../api/systemApi.js'
const router = useRouter();

var thisform = ref("");

var r = reactive({
  loginForm: {
    account: "",
    userPassword: "",
  },
  rules: {
    //用户名的通配符输入其他的不是手机号或邮箱号时也能通过，有问题
    account: [{ required: true, message: "请输入帐号名", trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{6,20}$/, message: "账户名为6-20位的数字或字母的组合", trigger: 'blur' }],
    //{ pattern: /^(1[3-9]{1}[0-9]{9})|(([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+)$/, message: "请正确输入手机号或邮箱号", trigger: 'blur' }]
    userPassword: [{ required: true, message: "请输入密码", trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{6,20}$/, message: "密码为6-20位的数字或字母的组合", trigger: 'blur' }],
    //{ pattern: /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9 _]{6,10}$/, message: "6-10位含大小写字母和数字", trigger: 'blur' }]
  }
})

function register() {
  thisform.value.validate((valid) => {
    if (valid) {
      registerApi(r.loginForm).then(function (response) {
                          if(response.data.code == 200) {
                                router.push({path: "/login"});
                            } else {
                                alert(response.data.result);
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
