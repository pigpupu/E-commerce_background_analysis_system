<template>

  <div style="margin-left: 50px; margin-top: 25px ;min-height:690px;">
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-row>
        <el-form-item label="用户姓名">
          <el-input v-model="searchForm.userName" placeholder="名称" />
        </el-form-item>
        <el-form-item label="账号名">
          <el-input v-model="searchForm.userId" placeholder="账户" @keyup.enter="search" />
        </el-form-item>
        <el-form-item label="权限">
          <el-select v-model="searchForm.roleId" @change="fetchData">
            <el-option label="全选" value="3" />
            <el-option label="普通用户" value="2" />
            <el-option label="高级用户" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :icon="Search" type="primary" @click="search">搜索</el-button>
          <el-button @click="refresh()"><el-icon>
              <Refresh />
            </el-icon>重置</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <div>
      <el-button type="primary" @click="dialogVisible2 = true">
        <el-icon>
          <Plus />
        </el-icon>
        添加
      </el-button>
      <el-button type="danger" @click="download()"><el-icon>
          <Download />
        </el-icon>
        导出
      </el-button>
    </div>
    <div class="currentTable">
      <el-table :data="tableData" stripe style="width: 1200px" border>
        <el-table-column type="index" :index="indexMethod" label="序号" width="60" align="center" />
        <el-table-column label="用户姓名" property="userName" width="100" align="center">
        </el-table-column>
        <el-table-column label="账户名" prop="account" width="120" align="center">
        </el-table-column>
        <el-table-column label="性别" property="gender" width="60" align="center">
        </el-table-column>
        <el-table-column label="手机号" property="mobile" align="center">
        </el-table-column>
        <el-table-column label="邮箱地址" property="email" align="center">
        </el-table-column>
        <el-table-column label="上次登录时间" property="lastLoginTime" align="center">
        </el-table-column>
        <el-table-column label="用户类别" prop="roleName" width="100" align="center">
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button size="small" type="warning" @click="openDialog(scope.row)">修改
            </el-button>
            <el-popconfirm title="确认删除吗?" @confirm="deleteRow(scope.row)">
              <template #reference>
                <el-button size="small" type="danger">删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div class="page" style="float: right;margin-right: 10%;margin-top: 20px">
        <el-pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :page-sizes="[1, 10, 20, 30, 40]"
          :small="small" :disabled="disabled" :background="background" layout="total, sizes, prev, pager, next, jumper"
          :total="total" @change="pageChange" @current-change="pageChange" @size-change="pageChange" />
      </div>
    </div>
    <el-dialog v-model="dialogVisible" title="信息修改">
      <el-form :model="form" :rules="baseRules" ref="validForm1">
        <el-form-item label="用户姓名" :label-width="formLabelWidth" prop="userName">
          <el-input v-model="form.userName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="账号名" :label-width="formLabelWidth">
          <el-input v-model="form.account" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <!--          <el-input v-model="form.gender" autocomplete="off" />-->
          <el-radio-group v-model="form.gender">
            <el-radio label="男" size="large">男</el-radio>
            <el-radio label="女" size="large">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth" prop="mobile">
          <el-input v-model="form.mobile" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱地址" :label-width="formLabelWidth" prop="email">
          <el-input v-model="form.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="用户类别" :label-width="formLabelWidth">
          <el-radio-group v-model="form.roleName">
            <el-radio label="普通用户" size="large">普通用户</el-radio>
            <el-radio label="高级用户" size="large">高级用户</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="editAck()">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogVisible2" title="添加用户">
      <el-form :model="addForm" :rules="baseRules" ref="validForm2">
        <el-form-item label="用户姓名" :label-width="formLabelWidth" prop="userName">
          <el-input v-model="addForm.userName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="账号名" :label-width="formLabelWidth" prop="account">
          <el-input v-model="addForm.account" autocomplete="off" />
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth">
          <el-radio-group v-model="addForm.gender">
            <el-radio label="0" size="large">男</el-radio>
            <el-radio label="1" size="large">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" :label-width="formLabelWidth" prop="mobile">
          <el-input v-model="addForm.mobile" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱地址" :label-width="formLabelWidth">
          <el-input v-model="addForm.email" autocomplete="off" />
        </el-form-item>
        <el-form-item label="用户类别" :label-width="formLabelWidth">
          <el-radio-group v-model="addForm.roleId">
            <el-radio label="2" size="large">普通用户</el-radio>
            <el-radio label="1" size="large">高级用户</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible2 = false">取消</el-button>
          <el-button type="primary" @click="addAck()">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>


</template>

<script setup>
import { onMounted, reactive } from 'vue'
import {
  Search
} from '@element-plus/icons-vue'
import { ElMessage, ElTable, thumbProps } from 'element-plus'
import { ref } from 'vue'
import axios from "../../utils/axios";
import qs from "qs";

const result = ref(false)
// const check = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const small = ref(true)
const background = ref(false)
const disabled = ref(false)
const dialogVisible = ref(false)
const dialogVisible2 = ref(false)
const formLabelWidth = '140px'
const total = ref()
const searchForm = reactive({
  userName: '',
  userId: '',
  roleId: '3'
})
const tableData = ref()
const form = ref()
const addForm = ref({
  userName: "",
  account: "",
  gender: "",
  mobile: "",
  email: "",
  roleId: "",
  time: ""
})
const flag = ref(0)

const baseRules = ref({
  userName: [{ required: false, message: "请输入用户姓名", trigger: 'blur' }],
  account: [{ required: true, message: "请输入账号名", trigger: 'blur' },
  { pattern: /^[A-Za-z0-9]{6,20}$/, message: "账户名为6-20位的数字或字母的组合", trigger: 'blur' }],
  mobile: [{
    required: false,
    pattern: /^(1[3-9]{1}[0-9]{9})$/, message: "请正确输入手机号", trigger: 'blur'
  }],
  email: [{
    required: false,
    pattern: /^(([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+)$/, message: "请正确输入邮箱号", trigger: 'blur'
  }],
})

const validForm1 = ref("")
const validForm2 = ref("")
onMounted(() =>
  fetchData()
)

onMounted(() =>
  fetchData()
)

function indexMethod(index) {
  return index + 1 + (currentPage.value - 1) * pageSize.value;
}
function refresh() {
  searchForm.userName = '',
    searchForm.userId = '',
    searchForm.roleId = '3',
    flag.value = 0
  fetchData()
}
function download() {
  let data = axios.get('/admin/user/download', {
    responseType: 'blob'
  }).then(function (res) {
    const url = window.URL.createObjectURL(new Blob([res.data]))
    const link = document.createElement('a')
    link.style.display = 'none'
    link.href = url
    link.setAttribute('download', 'test.xlsx')
    document.body.appendChild(link)
    link.click();
    document.body.removeChild(link)

  })
}
function numToString(str) {
  if (str === "男") {
    str = 0;
  }
  else if (str === "女") {
    str = 1;
  }
  else if (str === "普通用户") {
    str = 2;
  }
  else if (str === "高级用户") {
    str = 1;
  }
  else {
    str = 3
  }
  return str;
}
function fetchData() {
  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/user/queryAll',
    data: qs.stringify({
      nowPage: currentPage.value,
      pageSize: pageSize.value,
      type: searchForm.roleId
    })
  }).then(function (response) {
    if (response.data.code == 200) {
      tableData.value = response.data.result.records,
        total.value = response.data.result.total
    } else {
      alert(response.data.message)
    }
  });
}
function search() {
  if (flag.value == 0) {
    currentPage.value = 1
  }
  flag.value = 1
  if (searchForm.userId !== '') {
    searchId()
  }
  else if (searchForm.userName !== '') {
    searchName()
  }
}
function searchId() {
  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/user/queryByAccount',
    data: qs.stringify({
      account: searchForm.userId.valueOf()
    })
  }).then(function (response) {
    if (response.data.code == 200) {
      tableData.value = response.data.result
      total.value=1
    } else {
      alert(response.data.message)
    }
  });
}
function searchName() {
  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/user/queryByName',
    data: qs.stringify({
      nowPage: currentPage.value,
      pageSize: pageSize.value,
      type: searchForm.roleId,
      userName: searchForm.userName
    })
  }).then(function (response) {
    if (response.data.code == 200) {
      tableData.value = response.data.result.records
      total.value = response.data.result.total
    } else {
      alert(response.data.message)
    }
  })
}
function openDialog(row) {
  dialogVisible.value = true
  form.value = Object.assign({}, row);
  //this.dialogVisible = true;
  //this.form = Object.assign({}, row);
}

function editAck() {
  //let that = this
  validForm1.value.validate((valid) => {
    if (valid) {
      axios({
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        method: 'post',
        url: '/admin/user/update',
        data: qs.stringify({
          userName: form.value.userName,
          account: form.value.account,
          gender: numToString(form.value.gender),
          email: form.value.email,
          mobile: form.value.mobile,
          roleId: numToString(form.value.roleName)
        })
      }).then(function (response) {
        if (response.data.result === true) {
          ElMessage({
            showClose: true,
            message: '恭喜你修改成功',
            type: 'success',
          })
          fetchData()
          dialogVisible.value = false
        }
        else {
          alert("未完成修改")
        }
      }).catch(function (error) {

      });
    }
  })
}

function addAck() {
  //const that = this;
  validForm2.value.validate((valid) => {
    if (valid) {
      refresh()
      axios({
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        method: 'post',
        url: '/admin/user/create',
        data: qs.stringify({
          account: addForm.value.account,
          userName: addForm.value.userName,
          gender: addForm.value.gender,
          mobile: addForm.value.mobile,
          email: addForm.value.email,
          roleId: addForm.value.roleId
        })
      }).then(function (response) {
        if (response.data.result == true) {
          ElMessage({
            showClose: true,
            message: '恭喜你添加成功',
            type: 'success',
          })
          fetchData()
          dialogVisible2.value = false
        }
        else {
          alert("账号名重复")
        }
      }).catch(function (error) {

      });
    }
  })

}
function deleteRow(row) {

  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/user/delete',
    data: qs.stringify({
      accounts: row.account,
    })
  }).then(function (response) {
    if (response.data.result == true) {

      ElMessage({
        showClose: true,
        message: '删除成功',
        type: 'success',
      })
      fetchData()
    }
    else {
      alert("删除失败")
    }
  }).catch(function (error) {

  });
}


function pageChange() {
  if (flag.value == 0) {
    fetchData()
  }
  else if (flag.value == 1) {
    search()
  }
}
</script>

<style scoped>
.currentTable {
  margin-top: 50px;
}

.el-icon {
  margin-right: 5px;
}
</style>
