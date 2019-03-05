<template>
<div class="app-container">
    <div class="filter-container">
    <!-- 用户新增弹窗 -->
    <el-dialog :visible.sync="dialogAdd" :before-close="handleCloseAdd" :rules="addRule"
      title="申请领取">
      <el-form
        ref="addForm"
        :rules="addRule"
        :model="userObj"
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="工号" prop="staffNo">
          <el-input type="count" v-model="userObj.staffNo" placeholder="请输入工号"/>
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input type="count" v-model="userObj.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="count" v-model="userObj.password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-input type="count" v-model="userObj.userType" placeholder="请输入人员类型"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAdd = false;$refs.addForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="insertNewUser('addForm')">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /用户新增弹窗 -->
   <!-- 主表格 -->
    <el-table
      v-loading.body="listLoading"
      ref="multipleTable"
      :data="list"
      element-loading-text="拼命加载中"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" align="center"/>
      <el-table-column prop="id" label="用户自增ID" min-width="120px;" sortable/>
      <el-table-column prop="staffNo" label="工号" min-width="150px;" sortable/>
      <el-table-column prop="name" label="姓名" min-width="120px;" sortable/>
      <el-table-column prop="userType" label="人员类型" min-width="120px;" sortable>
        <template slot-scope="scope">
            <span v-if="scope.row.userType === 1">普通人员</span>
            <span v-if="scope.row.userType === 2">管理员</span>
          </template>
          </el-table-column>
      <el-table-column align="center" label="操作" width="250">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary">
              操作选项
              <i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="success"
                  icon="el-icon-plus"
                  plain
                  @click="insertUserDialog(scope.row.id);"
                >添加用户</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="small"
                  type="warning"
                  icon="el-icon-edit"
                  plain
                  @click="updateUseDialogr(scope.row.id);"
                >用户编辑</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="small"
                  type="danger"
                  icon="el-icon-delete"
                  plain
                  @click="deleteUserDialog(scope.row.id);"
                >删除用户</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <!-- /主表格 -->
      <!-- 分页选项 -->
    <div align="center">
      <el-pagination
        :current-page="currentPage"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pagesize"
        :total="totalCount"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- /分页选项 -->
  </div>
 </div>
</template>
<script>
import { Message } from "element-ui";
import { pagination, insertNewUser } from "@/api/userManagement";
const userObj = {
  // 插入更新等对象在这初始化
  id: null,
  staffNo: null,
  name: null,
  password: null,
  userType: null
};
export default {
  data() {
    return {
      listLoading: true,
      dialogAdd: false, // 这是新增用户弹窗，默认false
      multipleSelection: [], // 存放勾选对象的数组
      list: null, // 这是库存一览的list，打开页面会去找接口获取数据并赋值，默认null
      // 这是编辑新增用的对象
      userObj: Object.assign({}, userObj),
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      addRule: {
        staffNo: [{ required: true, message: "请输入工号", trigger: "blur" }],
        name: [{ required: true, message: "请输入姓名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        userType: [
          { required: true, message: "请输入人员类型", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    // 这里是设置打开页面自动会调用的方法
    this.fetchData();
  },
  methods: {
    // ID数组的赋值
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 每页显示数据量变更
    handleSizeChange(val) {
      this.pagesize = val;
      this.fetchData(); // 每次选择一页显示几条的时候调用fetchData方法
    },
    // 页码变更
    handleCurrentChange(val) {
      this.currentPage = val;
      this.fetchData(); // 每次切换页码的时候调用fetchData方法
    },
    // 列表数据获取（默认不带检索用参数）
    fetchData() {
      this.listLoading = false;
      const listQuery = {
        pageNum: this.currentPage, // 向后端传的页码
        pageSize: this.pagesize // 向后端传的单页条数
      };
      pagination(listQuery).then(response => {
        const data = response.data.responseData;
        this.list = data.list;
        this.totalCount = data.total;
        this.listLoading = false;
      });
    },
    // 新增弹窗关闭
    handleCloseAdd() {
      this.dialogAdd = false;
      this.$refs.addForm.resetFields();
    },
    // 新增用户
    insertUserDialog(id) {
      this.userObj.id = id;
      this.dialogAdd = true;
    },
    // 新增确认
    insertNewUser(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          insertNewUser(this.userObj)
            .then(response => {
              const data = response.data;
              this.listLoading = false;
              if (data.statusCode === 200) {
                Message({
                  message: "新增编辑成功",
                  type: "success",
                  duration: 5 * 1000
                });
                this.$refs[formName].resetFields();
                this.dialogAdd = false;
                this.userObj = Object.assign({}, userObj); // 重新给修改用对象赋值初始化，userObj为全局const对象
                this.fetchData();
              } else {
                this.loading = false;
                Message({
                  message: "新增失败,原因："+data.statusMsg,
                  type: "error",
                  duration: 5 * 1000
                });
              }
            })
            .catch(() => {
              this.loading = false;
              Message({
                message: "新增失败",
                type: "error",
                duration: 5 * 1000
              });
            });
        }
      });
    },
    // 更新用户
    updateUseDialogr(id) {},
    // 单选删除弹窗
    deleteUserDialog(id) {}
  } // 这是方法末尾花括号
};
</script>