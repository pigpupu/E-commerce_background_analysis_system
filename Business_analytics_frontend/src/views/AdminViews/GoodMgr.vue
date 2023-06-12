<template>
  <div style="margin-left: 50px; margin-top: 25px ;min-height:690px;">
    <el-form :inline="true" :model="goodSearch" class="search-line">
      <el-row>
        <el-form-item label="搜索方式">
          <el-select v-model="goodSearch.select" placeholder="请选择搜索项">
            <el-option label="商品编号" value="商品编号" />
            <el-option label="商品名称" value="商品名称" />
          </el-select>
        </el-form-item>
        <el-form-item :label=goodSearch.select label-width="70px">
          <el-input v-model="goodSearch.info" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="search(); set()"><el-icon>
              <Search />
            </el-icon> 搜索</el-button>
          <el-button @click="refresh"><el-icon>
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
      <!--<el-button type="success" @click="openDialog(scope.row);">
        <el-icon>
          <EditPen />
        </el-icon>
        修改
      </el-button>-->


      <el-button type="warning" @click="download()">
        <el-icon size="18">
          <Download />
        </el-icon>
        导出
      </el-button>
    </div>
    <div class="currentTable">
      <el-table :data="tableData" stripe style="width: 1200px" border>
        <el-table-column type="index" label="" width="60" align="center">
          <template #default="scope">
            <el-radio :label="scope.$index" v-model="currentIndex" @change="getCurrentRow(scope.row)"
              style="color: #fff;">
              {{ '' }}</el-radio>
          </template>
        </el-table-column>
        <el-table-column type="index" :index="indexMethod" label="序号" width="60" align="center" />
        <el-table-column label="商品名称" property="goodName" width="120" align="center">
        </el-table-column>
        <el-table-column label="商品编号" prop="id" width="120" align="center">
        </el-table-column>
        <el-table-column label="商品类别" prop="categoryName" width="120" align="center">
        </el-table-column>
        <el-table-column label="商品品牌" prop="brandName" width="120" align="center">
        </el-table-column>
        <el-table-column label="当前价格" property="price" width="120" align="center">
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button size="small" type="warning" @click="openDialog(scope.row)">修改
              <el-icon size="23">
                    <EditPen />
                  </el-icon>
            </el-button>
            <el-popconfirm title="确认删除吗?" @confirm="deleteRow(scope.row)">
              <template #reference>
                <el-button type="danger">
                  <el-icon size="18">
                    <Delete />
                  </el-icon>
                  删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>

      </el-table>
    </div>
    <div class="page" style="float: right;margin-right: 10%;margin-top: 20px">
      <el-pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
        :small="small" :disabled="disabled" :background="background" layout="total, sizes, prev, pager, next, jumper"
        :total="total" @change="pageChange" @current-change="pageChange" @size-change="pageChange" />
    </div>
    <el-dialog v-model="dialogVisible" title="信息修改">
      <el-form :model="editForm" :rules="baseRules2" ref="validForm2">
        <el-form-item label="商品名称" :label-width="formLabelWidth" prop="goodName">
          <el-input v-model="editForm.goodName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="商品编号" :label-width="formLabelWidth" prop="goodId">
          <el-input v-model="editForm.goodId" autocomplete="off" />
        </el-form-item>
        <el-form-item label="商品种类" :label-width="formLabelWidth" prop="categoryName">
          <el-input v-model="editForm.categoryName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="品牌名称" :label-width="formLabelWidth" prop="brandName">
          <el-input v-model="editForm.brandName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="价格" :label-width="formLabelWidth" prop="price">
          <el-input v-model="editForm.price" autocomplete="off" />
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
    <el-dialog v-model="dialogVisible2" title="添加商品">
      <el-form :model="addForm" :rules="baseRules" ref="validForm1">
        <el-form-item label="商品名称" :label-width="formLabelWidth" prop="goodName">
          <el-input v-model="addForm.goodName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="商品种类" :label-width="formLabelWidth" prop="categoryName">
          <el-input v-model="addForm.categoryName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="商品品牌" :label-width="formLabelWidth" prop="brandName">
          <el-input v-model="addForm.brandName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="价格" :label-width="formLabelWidth" prop="price">
          <el-input v-model="addForm.price" autocomplete="off" />
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
import { ref } from 'vue'
import NumRegion from '@/components/commom/NumRegion.vue'
//import axios from "axios";
import axios from "../../utils/axios";
import qs from "qs";
import { ElMessage } from "element-plus";

const goodSearch = ref({
  select: '商品名称',
  info: ''
})
const tableData = ref()
const currentRow = ref()
const currentIndex = ref()
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref()
const small = ref(true)
const background = ref(false)
const disabled = ref(false)
const dialogVisible = ref(false)
const dialogVisible2 = ref(false)
const formLabelWidth = '140px'
const editForm = ref()
const addForm = ref({
  goodName: '',
  brandName: '',
  categoryName: '',
  price: '',
  newBrandName: ''
})
const flag = ref(0)

const baseRules = ref({
  goodName: [{ required: true, message: "请输入", trigger: 'blur' }],
  brandName: [{ required: false, message: "请输入", trigger: 'blur' }],
  categoryName: [{ required: false, message: "请输入", trigger: 'blur' }],
  price: [{ required: false, pattern: /^(?:[1-9]\d*|0)(?:\.\d+)?$/, message: "请正确输入价格", trigger: 'blur' }],
})

const baseRules2 = ref({
  goodName: [{ required: true, message: "请输入", trigger: 'blur' }],
  goodId: [{ required: true, message: "请输入", trigger: 'blur' }],
  brandName: [{ required: false, message: "请输入", trigger: 'blur' }],
  categoryName: [{ required: false, message: "请输入", trigger: 'blur' }],
  price: [{ required: false, pattern: /^(?:[1-9]\d*|0)(?:\.\d+)?$/, message: "请正确输入价格", trigger: 'blur' }],
})
const validForm1 = ref("")
const validForm2 = ref("")

onMounted(() =>
  fetchData()
)
function indexMethod(index) {
  return index + 1 + (currentPage.value - 1) * pageSize.value;
}
function refresh() {
  goodSearch.info = ''
  flag.value = 0
  currentPage.value = 1
  fetchData()
}
function fetchData() {
  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/good/queryAll',
    data: qs.stringify({
      nowPage: currentPage.value,
      pageSize: pageSize.value,
    })
  }).then(function (response) {
    if (response.data.code == 200) {
      tableData.value = response.data.result.records,
        total.value = response.data.result.total
    } else {
      alert(response.data.message);
    }
  });
}

function download() {
  let data = axios.get('/admin/good/download', {
    responseType: 'blob'
  }).then(function (res) {
    const url = window.URL.createObjectURL(new Blob([res.data]))
    const link = document.createElement('a')
    link.style.display = 'none'
    link.href = url
    link.setAttribute('download', '商品信息表.xlsx')
    document.body.appendChild(link)
    link.click();
    document.body.removeChild(link)

  })
}
function openDialog(row) {
  dialogVisible.value = true
  //this.editForm = Object.assign({}, currentRow.value);
  editForm.value = Object.assign({}, row);
  //console.log(editForm.value);

}
function editAck() {
  validForm2.value.validate((valid) => {
    if (valid) {
      axios({
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        method: 'post',
        url: '/admin/good/update',
        data: qs.stringify({
          id: editForm.value.goodId,
          goodName: editForm.value.goodName,
          newCategoryName: editForm.value.categoryName,
          newBrandName: editForm.value.brandName,
          price: editForm.value.price
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
          //console.log(response)
          alert("未完成修改")
        }
      }).catch(function (error) {

      });
    }
  })
}
function search() {
  if (flag.value == 0) {
    currentPage.value = 1
  }
  flag.value = 1
  if (goodSearch.value.select == '商品编号') {
    searchId()
  }
  else if (goodSearch.value.select == '商品名称') {
    searchName()
  }
}
function set() {
  currentPage.value = 1
}
function searchId() {
  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/good/querySelect',
    data: {
      nowPage: currentPage.value,
      pageSize: pageSize.value,
      type: 0,
      input: goodSearch.value.info.valueOf(),
    }
  }).then(function (response) {
    if (response.data.code == 200) {
      tableData.value = response.data.result.records
      total.value = response.data.result.total
    } else {
      alert(response.data.message)
    }
  })
}
function searchName() {
  axios({
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    method: 'post',
    url: '/admin/good/querySelect',
    data: {
      nowPage: currentPage.value,
      pageSize: pageSize.value,
      type: 1,
      input: goodSearch.value.info.valueOf(),
    }
  }).then(function (response) {
    if (response.data.code == 200) {
      tableData.value = response.data.result.records,
        total.value = response.data.result.total
    } else {
      alert(response.data.message)
    }
  })
}
function getCurrentRow(row) {
  currentRow.value = row;
}
function addAck() {

  validForm1.value.validate((valid) => {
    if (valid) {
      axios({
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        method: 'post',
        url: '/admin/good/create',
        data: qs.stringify({
          goodName: addForm.value.goodName,
          newCategoryName: addForm.value.categoryName,
          newBrandName: addForm.value.brandName,
          price: addForm.value.price
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
          alert("添加失败")
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
    url: '/admin/good/deleteByNames',
    data: qs.stringify({
      goodNames: row.goodName,
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
.el-icon {
  margin-right: 5px;
}

.currentTable {
  margin-top: 20px;
}
</style>
