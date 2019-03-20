<template>
  <!-- 检索等顶部选项 -->
  <div class="app-container">
    <div class="filter-container">
      <div class="container">
        <el-tooltip class="item" effect="light" content="人员姓名" placement="top">
          <el-input
            v-model.trim="searchOptions.name"
            clearable
            style="width: 200px;"
            class="filter-item"
            placeholder="人员姓名"
          />
        </el-tooltip>
        <el-tooltip class="item" effect="light" content="工号" placement="top">
          <el-input
            v-model.trim="searchOptions.staffNo"
            clearable
            style="width: 200px;"
            class="filter-item"
            placeholder="工号"
          />
        </el-tooltip>
        <el-select
          v-model="searchOptions.userType"
          clearable
          class="filter-item"
          filterable
          placeholder="请选择用户类型"
        >
          <el-option
            v-for="item in userTypeList"
            :key="item.key"
            :label="item.label"
            :value="item.userType"
          />
        </el-select>
        <el-button
          class="filter-item"
          style="margin-left: 6px;"
          icon="el-icon-search"
          @click="searchData"
        >搜索</el-button>
        <el-button class="filter-item" @click="clearSearchOptions">清空搜索选项</el-button>
        <el-button
          class="filter-item"
          style="margin-left: 6px;"
          type="success"
          icon="el-icon-plus"
          @click="insertUserDialog"
        >添加用户</el-button>
      </div>
    </div>
    <!-- /检索等顶部选项 -->
    <!-- 用户新增弹窗 -->
    <el-dialog
      :visible.sync="dialogAdd"
      :before-close="handleCloseAdd"
      :rules="userRule"
      title="用户新增"
    >
      <el-form
        ref="addForm"
        :rules="userRule"
        :model="userObj"
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="工号" prop="staffNo">
          <el-input v-model="userObj.staffNo" type="count" placeholder="请输入工号"/>
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="userObj.name" type="count" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userObj.password" type="count" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="用户类型" prop="itemType">
          <el-select
            v-model="userObj.userType"
            class="filter-item"
            filterable
            placeholder="请选择人员类型"
          >
            <el-option
              v-for="item in userTypeList"
              :key="item.key"
              :value="item.userType"
              :label="item.label"
            >
              <span v-if="item.userType ===1 ">普通职工</span>
              <span v-if="item.userType ===2 ">管理员/采购人员</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAdd = false;$refs.addForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="insertNewUser('addForm')">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /用户新增弹窗 -->
    <!-- 用户编辑弹窗 -->
    <el-dialog
      :visible.sync="dialogUpdate"
      :before-close="handleCloseUpdate"
      :rules="userRule"
      title="用户编辑"
    >
      <el-form
        ref="updateForm"
        :rules="userRule"
        :model="userObj"
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="工号" prop="staffNo">
          <el-input v-model="userObj.staffNo" type="count" placeholder="请输入工号"/>
        </el-form-item>
        <el-form-item label="真实姓名" prop="name">
          <el-input v-model="userObj.name" type="count" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="userObj.password" type="count" placeholder="请输入密码（强制修改）"/>
        </el-form-item>
        <el-form-item label="用户类型" prop="itemType">
          <el-select
            v-model="userObj.userType"
            class="filter-item"
            filterable
            placeholder="请选择人员类型"
          >
            <el-option
              v-for="item in userTypeList"
              :key="item.key"
              :value="item.userType"
              :label="item.label"
            >
              <span v-if="item.userType ===1 ">普通职工</span>
              <span v-if="item.userType ===2 ">管理员/采购人员</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUpdate = false;$refs.updateForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="updateUser('updateForm')">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /用户编辑弹窗 -->
    <!-- 物品单选删除弹窗 -->
    <el-dialog :visible.sync="dialogVisibleDel" title="用户删除">
      <code>您确认要删除此用户吗？ ID:{{ userObj.id }}</code>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleDel = false;userObj.id = null">取 消</el-button>
        <el-button type="danger" @click="userDeleteSubmit">确认删除</el-button>
      </div>
    </el-dialog>
    <!-- /物品单选删除弹窗 -->
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
                  size="small"
                  type="warning"
                  icon="el-icon-edit"
                  plain
                  @click="updateUseDialogr(scope.row.id,scope.row.staffNo,scope.row.name,scope.row.userType);"
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
</template>
<script>
import { Message } from "element-ui";
import {
  pagination,
  insertNewUser,
  updateUser,
  userDelete
} from "@/api/userManagement";
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
      dialogUpdate: false, // 这是编辑用户的弹窗，默认false
      dialogVisibleDel: false, // 单选删除弹窗
      multipleSelection: [], // 存放勾选对象的数组
      list: null, // 这是库存一览的list，打开页面会去找接口获取数据并赋值，默认null
      userTypeList: [
        { key: 1, userType: 1, label: "普通职工" },
        { key: 2, userType: 2, label: "管理员/采购人员" }
      ], // 这是编辑弹窗里的用户类型下拉框数据，默认写死;label会最优先展示，其次到value，但是value是实际绑定的值，要传给后端的，是数字，所以不应用来展示
      // 这是编辑新增用的对象
      userObj: Object.assign({}, userObj),
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      searchOptions: {
        // 搜索用参数
        name: null,
        staffNo: null,
        userType: null
      },
      userRule: {
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
    // 清空搜索选项
    clearSearchOptions() {
      this.searchOptions = {
        // 此处用于重置搜索参数
        name: null,
        staffNo: null,
        userType: null
      };
    },
    // 列表数据获取（默认不带检索用参数）
    fetchData() {
      this.listLoading = false;
      const listQuery = {
        pageNum: this.currentPage, // 向后端传的页码
        pageSize: this.pagesize, // 向后端传的单页条数
        name: this.searchOptions.name, // 这三个用于查询用户姓名，工号，用户类型
        staffNo: this.searchOptions.staffNo,
        userType: this.searchOptions.userType
      };
      pagination(listQuery).then(response => {
        const data = response.data.responseData;
        this.list = data.list;
        this.totalCount = data.total;
        this.listLoading = false;
      });
    },
    // 带检索条件去查询列表（带检索用参数）
    searchData() {
      this.currentPage = 1;
      this.listLoading = true;
      this.fetchData(); // 跳回第一页，带条件参数去后端查询列表数据
    },
    // 新增弹窗关闭
    handleCloseAdd() {
      this.dialogAdd = false;
      this.$refs.addForm.resetFields();
    },
    // 新增用户
    insertUserDialog() {
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
                  message: "新增成功",
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
                  message: "新增失败,原因：" + data.statusMsg,
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
    // 编辑弹窗关闭
    handleCloseUpdate() {
      this.dialogUpdate = false;
      this.$refs.updateForm.resetFields();
    },
    // 更新用户
    updateUseDialogr(id, staffNo, name, userType) {
      this.userObj.id = id;
      this.userObj.staffNo = staffNo;
      this.userObj.name = name;
      this.userObj.userType = userType;
      this.dialogUpdate = true;
    },
    // 更新用户确认
    updateUser(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          updateUser(this.userObj)
            .then(response => {
              const data = response.data;
              this.listLoading = false;
              if (data.statusCode === 200) {
                Message({
                  message: "编辑成功",
                  type: "success",
                  duration: 5 * 1000
                });
                this.$refs[formName].resetFields();
                this.dialogUpdate = false;
                this.userObj = Object.assign({}, userObj); // 重新给修改用对象赋值初始化，userObj为全局const对象
                this.fetchData();
              } else {
                this.loading = false;
                Message({
                  message: "编辑失败",
                  type: "error",
                  duration: 5 * 1000
                });
              }
            })
            .catch(() => {
              this.loading = false;
              Message({
                message: "编辑失败",
                type: "error",
                duration: 5 * 1000
              });
            });
        }
      });
    },
    // 单选删除弹窗
    deleteUserDialog(id) {
      this.userObj.id = id;
      this.dialogVisibleDel = true;
    },
    // 单项删除
    userDeleteSubmit() {
      this.listLoading = true;
      this.dialogVisibleDel = false;
      const id = this.userObj.id;
      userDelete(id)
        .then(response => {
          const data = response.data;
          this.listLoading = false;
          if (data.statusCode === 200) {
            this.delItemId = null;
            Message({
              message: "删除成功",
              type: "success",
              duration: 5 * 1000
            });
          } else {
            Message({
              message: "删除失败",
              type: "error",
              duration: 5 * 1000
            });
          }
          this.fetchData();
        })
        .catch(() => {
          this.loading = false;
          Message({
            message: "删除失败",
            type: "error",
            duration: 5 * 1000
          });
        });
    }
  } // 这是方法末尾花括号
};
</script>
