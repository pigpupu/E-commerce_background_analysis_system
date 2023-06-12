<template>

  <div class="rootDiv" style="background-color:#eff4f9">
    <el-card style="width:400px;height:400px;margin:auto;display: flex;">
      <div style="margin:auto">
        <el-form :model="r.loginForm" ref="thisform" size="large" :rules="r.rules" hide-required-asterisk="true">
          <el-form-item>
            <span style="font-size: 20px;margin-left: 144px;font-family: Arial, Helvetica, sans-serif;">忘记密码</span>
            <router-link to="/login" style="color:#3A8BFF;margin:auto 0 auto auto">返回登录</router-link>
          </el-form-item>
          <el-form-item label="帐号名" prop="account">
            <el-input placeholder="请输入账号名" v-model="r.loginForm.account" />
          </el-form-item>
          <div style="display:flex">
            <el-form-item label="验证码" prop="verCode">
              <el-input placeholder="请输入验证码" v-model="r.loginForm.verCode" />
            </el-form-item>
            <el-button type="primary" @click="sendcode">发送验证码</el-button>
          </div>
          <el-form-item label="密码" prop="newPassword">
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
import { reactive, ref, renderSlot } from 'vue';
import { useRouter } from 'vue-router';
import axios from '../../utils/axios'
import { forgetPasswordApi, resetPasswordApi } from '../../api/systemApi.js'
const router = useRouter();

var thisform = ref("");

var r = reactive({
  loginForm: {
    account: "",
    verCode: "",
    newPassword: "",
  },
  rules: {
    //这边的验证拜托啦！！！！
    account: [{ required: true, message: "请输入帐号名", trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{6,20}$/, message: "账户名为6-20位的数字或字母的组合", trigger: 'blur' }],
    verCode: [{ required: false, message: "请输入验证码", trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{6}$/, message: "验证码为6位的数字或字母的组合", trigger: 'blur' }],
    newPassword: [{ required: false, message: "请输入密码", trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]{6,20}$/, message: "密码为6-20位的数字或字母的组合", trigger: 'blur' }]
  }
})

function sendcode() {
  if(r.loginForm.account!=0){
    forgetPasswordApi(r.loginForm).then(function (response) {
    if (response.data.code == 200) {
      //router.push({ path: "/login" });
      alert("验证码已发送")
    } else {
      alert(response.data.message)
    }
  });
  }else{
    alert("账号名不得为空！")
  }

}


function findbackpwd() {
  if(r.loginForm.account!=0&&r.loginForm.verCode!=0&&r.loginForm.newPassword!=0){
    thisform.value.validate((valid) => {
    if (valid) {
      resetPasswordApi(r.loginForm).then(function (response) {
        if (response.data.code == 200) {
          router.push({ path: "/login" });
          alert("您已成功修改密码！")
        } else {
          alert(response.data.message);
        }
      });
    }
  })
  }else{
    alert("请确认账号名、验证码和新密码均填写！")
  }

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
