<template>
  <div style="min-width:1350px;min-height:810px;margin-right:0%;margin-left:0%;overflow:hidden;">
    <div>
      <el-row :gutter="30" style="margin-top:80px; margin-left: 130px">
        <el-col :span="8">
          

          <div class="grid-content bg-purple">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="color: #99a9bf">设置/个人信息</span>
              </div>
              <div class="name-role">
                <span class="sender">Admin - {{ dataForm.nickName }}</span>
              </div>
              <el-divider></el-divider>
              <div class="personal-relation">
                <div class="relation-item">用户信息: <div style="float: right; padding-right:20px;">{{ dataForm.level }}
                </div>
                </div>
              </div>
              <!--
              <div class="personal-relation">
                <div class="relation-item">头像
                  <el-button style="float: right; padding-right:20px;" @click="open">
                    <el-icon color="#409EFC">
                      <Upload />
                    </el-icon>上传头像链接</el-button>
                </div>
              </div>
              -->
              <div class="personal-relation">
                <div class="relation-item">用户昵称:
                  <el-input style="float: right; padding-right:20px; width: 100px"
                            v-model="dataForm.nickName"></el-input>
                </div>
              </div>
              <div class="personal-relation">
                <div class="relation-item">生日:
                  <div style="float: right; padding-right:20px;">
                    <el-date-picker v-model="dataForm.birthday" value-format="YYYY-MM-DD" type="date"
                                    placeholder="Pick a day" style="width: 60%;float: right;" />
                  </div>
                </div>
              </div>
              <div class="personal-relation">
                <div class="relation-item">性别:
                  <el-radio-group v-model="dataForm.sex" style="float: right;padding-right:20px;">
                    <el-radio label="男" size="large">男</el-radio>
                    <el-radio label="女" size="large">女</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <el-card class="box-card">
              <div slot="header" class="clearfix" style="margin-bottom: 20px">
                <span>个人信息</span>
              </div>
              <div justify="center">
                <el-form label-width="80px" :model="dataForm" label-position="right"
                         style="padding-right: 50px" ref="form" :rules="rules">
                  <el-form-item label="用户昵称" prop="nickName">
                    <el-input auto-complete="off" v-model="dataForm.nickName"></el-input>
                  </el-form-item>
                  <el-form-item label="电话" prop="phone">
                    <el-input maxlength="18" v-model="dataForm.phone" ></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱" prop="email">
                    <el-input maxlength="18" v-model="dataForm.uemail" ></el-input>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>
          </div>
          <div class="grid-content bg-purple" style="margin-top: 30px">
            <el-card class="box-card">
              <div slot="header" class="clearfix" style="margin-bottom: 20px">
                <span>个人信息</span>
              </div>
              <div justify="center">

                <el-form label-width="80px" :model="dataForm" label-position="right"
                         style="padding-right: 50px" ref="form" :rules="rules">
                  <el-form-item label="昵称" prop="nickName">
                    <el-input auto-complete="off" v-model="dataForm.nickName"></el-input>
                  </el-form-item>
                  <el-form-item label="电话" prop="phone" >
                    <el-input maxlength="18" v-model="dataForm.phone" ></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱" prop="uemail">
                    <el-input maxlength="18" v-model="dataForm.uemail" ></el-input>
                  </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button type="primary" @click="onSubmit">提交</el-button>
                  
                  <el-button type="primary" @click="changepasswd" style="margin-left:100px">修改密码</el-button>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import axios from '../../utils/axios';

import { useRouter } from 'vue-router';
import { reactive, ref } from 'vue';

import globalVariable from '../UserViews/global_variable';
import {ElMessage} from "element-plus";

const router = useRouter();
import { loginApi, getOwnInfoApi } from '../../api/systemApi.js'

//var storage = window.localStorage;
//var json = storage.getItem('r.loginForm.account');


export default {
  data() {
    return {
      dataForm: {
        nickName: "",
        level: "",
        birthday: "",
        sex: "",
        weChat: "ZZS_123",
        phone: "",
        uemail: ""
      },

      uimage: '',
      rules: {
        phone:[{ pattern: /^(1[3-9]{1}[0-9]{9})$/,
          required: true, message: '请输入正确的手机号', trigger: 'blur'  }],
        uemail: [{
          required: false,
          pattern: /^([a-zA-Z\d][\w-]{2,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/, message: "请正确输入邮箱号", trigger: 'blur'
        }],
      }
    }
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.pushdata()
        } else {
          ElMessage({
            showClose: true,
            message: '输入有误，请重新输入',
            type: 'warning',
          })
        }
      });

    },
    showdata() {
      //alert(typeof(localStorage.getItem(globalVariable.a)));
      //console.log(localStorage.getItem(globalVariable.a));
      //alert(localStorage.getItem('b'))
      //alert(localStorage.getItem('zheng'));
      //alert(JSON.parse(localStorage.getItem('a')));
      //console.log(JSON.stringify(r.loginForm.account)) ;
      var params = {
        //account: "18912977777",
        //account: JSON.parse(localStorage.getItem(globalVariable.a)),
        account:localStorage.getItem("nowAccount"),
        userName: this.nickName,
        roleName: this.level,
        gender: this.sex,
        email: this.uemail,
        mobile: this.phone,
        birthday: this.birthday,
        image: this.uimage
      };
      axios({
        url: "/getOwnInfo",
        method: "post",
        params: params,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        }
      }).then((response) => {
        if (response.data.code == 200) {

          this.dataForm.nickName = response.data.result.userName;
          this.dataForm.level = response.data.result.roleName;
          this.dataForm.sex = response.data.result.gender;
          this.dataForm.uemail = response.data.result.email;
          this.dataForm.phone = response.data.result.mobile;
          this.dataForm.birthday = response.data.result.birthday;
          this.uimage = response.data.result.image;
        } else {
          this.$message(
              {
                message: response.data.message,
                type: "error",
              }
          )
        }
      });
    },
    pushdata() {
      if (this.dataForm.sex == "男") this.dataForm.sex = 0;
      else this.dataForm.sex = 1;
      axios({
        url: "/updateCenter",
        method: "post",
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          //'Content-Type':'application/json',
        },
        data: {
          //account:"18912977777",
          //account: JSON.parse(localStorage.getItem(globalVariable.a)),
          account:localStorage.getItem("nowAccount"),
          userName: this.dataForm.nickName,
          roleName: this.dataForm.level,
          gender: this.dataForm.sex,
          email: this.dataForm.uemail,
          mobile: this.dataForm.phone,
          birthdayStr: this.dataForm.birthday
        }
      }).then(function (response) {
        if (response.data.code == 200) {
          ElMessage({
            showClose: true,
            message: '提交成功',
            type: 'success',
          })
          window.location.reload();
        } else {
          alert("修改失败！");
        }
      });

    },
    changepasswd() {
      //router.push({ path: '/changepasswd' });
      this.$router.push({path:'/changepasswd'});
    },
    open() {
      this.$prompt('请输入头像的图片链接', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /^(?:(http|https|ftp):\/\/)?((?:[\w-]+\.)+[a-z0-9]+)((?:\/[^/?#]*)+)?(\?[^#]+)?(#.+)?$/i,
        inputErrorMessage: '链接格式不正确'
      }).then(({ value }) => {
        this.$message({
          type: 'success',
          message: '成功上传头像',
        });


        axios({
          url: "/updateImageForZzs",
          method: "post",
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            //'Content-Type':'application/json',
          },
          data: {
            //account:"18912977777",
            //account: JSON.parse(localStorage.getItem(globalVariable.a)),
            account:localStorage.getItem("nowAccount"),
            Url: value,
          }
        }).then(function (response) {
          if (response.data.code == 200) {
            //alert("您已成功修改个人信息！");
            window.setTimeout(function () {
              window.location.reload();
            }, 800)
            //alert(value);
            //alert(response.data.result.image)
          } else {
            alert("修改失败！");
          }
        });

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        });
      });
    }
  },
  mounted() {
    this.showdata();
  },
  upload() {
    if (fileList._rawValue.length > 0) {
      for (var i = 0; i < fileList._rawValue.length; i++) {
        filedata.append("files", fileList._rawValue[i].raw)
      }

    }
  }

}

</script>

<style lang="scss" scoped>
//卡片样式
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 100%;
}

//文本样式区
.name-role {
  font-size: 16px;
  padding: 5px;
  text-align: center;
}

.sender {
  text-align: center;
}

.registe-info {
  text-align: center;
  padding-top: 10px;
}

.personal-relation {
  font-size: 16px;
  padding: 0px 5px 15px;
  margin-right: 1px;
  width: 100%
}

.relation-item {
  padding: 12px;
}

.dialog-footer {
  padding-top: 10px;
  padding-left: 10%;
}

//布局样式区
.el-row {
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #99a9bf;
}

.bg-purple {
  background: #d3dce6;
}

.bg-purple-light {
  background: #e5e9f2;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
</style>
