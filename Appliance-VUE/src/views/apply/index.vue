<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- 检索等顶部选项 -->
      <div class="container">
        <el-tooltip class="item" effect="light" content="物品名称" placement="top">
          <el-input
            v-model.trim="searchOptions.itemName"
            clearable
            style="width: 200px;"
            class="filter-item"
            placeholder="物品名称"
          />
        </el-tooltip>
        <el-date-picker
          v-model="searchOptions.createTimeBeginToEnd"
          clearable
          size="mini"
          value-format="yyyy-MM-dd"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
        <el-select
          v-model="searchOptions.status"
          clearable
          class="filter-item"
          filterable
          placeholder="请选择审核状态"
        >
          <el-option
            v-for="item in itemStatusList"
            :key="item.key"
            :label="item.statusName"
            :value="item.status"
          />
        </el-select>
        <el-button
          class="filter-item"
          style="margin-left: 6px;"
          icon="el-icon-search"
          @click="searchData"
        >搜索</el-button>
        <el-button class="filter-item" @click="clearSearchOptions">清空搜索选项</el-button>
      </div>
      <!-- /检索等顶部选项 -->
      <!-- 审核弹窗 -->
      <el-dialog
        :visible.sync="dialogApplyReview"
        :before-close="handleCloseApply"
        :rules="editRule"
        title="审核管理"
      >
        <el-form
          ref="applyForm"
          :model="applyObj"
          :rules="editRule"
          class="small-space"
          label-position="left"
          label-width="120px"
          style="width: 600px; margin-left:120px;"
        >
          <el-form-item label="选择审核状态" prop="status">
            <div align="left">
              <el-radio-group v-model="applyStatus" size="mini">
                <el-radio-button :disabled="applyObj.status === 2" label="1">驳回</el-radio-button>
                <el-radio-button label="2">通过未领取</el-radio-button>
                <el-radio-button label="3">已领取</el-radio-button>
              </el-radio-group>
            </div>
          </el-form-item>
          <el-form-item label="审核备注" prop="reviewCommit">
            <el-input
              v-model="applyObj.reviewCommit"
              :autosize="{ minRows: 3, maxRows: 5}"
              placeholder="请输入备注信息"
              type="textarea"
            />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="handleCloseApply()">取消</el-button>
          <el-button type="primary" @click="reviewApply('applyForm')">确 定</el-button>
        </div>
      </el-dialog>
      <!-- /审核弹窗 -->
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
        <el-table-column prop="id" label="物品ID" min-width="120px;" sortable/>
        <el-table-column prop="itemName" label="物品名称" min-width="150px;" sortable/>
        <el-table-column prop="itemType" label="物品类型" min-width="120px;" sortable/>
        <el-table-column prop="itemCount" label="申领数量" min-width="120px;" sortable/>
        <el-table-column prop="status" label="审核状态" min-width="120px;" sortable>
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0">未审核</span>
            <span v-if="scope.row.status === 1">驳回</span>
            <span v-if="scope.row.status === 2">通过但未领取</span>
            <span v-if="scope.row.status === 3">领取完成</span>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="申领人" min-width="120px;" sortable/>
        <el-table-column prop="createTime" label="发起时间" min-width="120px;" sortable/>
        <el-table-column prop="reviewer" label="审核人" min-width="120px;" sortable/>
        <el-table-column prop="reviewTime" label="审核时间" min-width="120px;" sortable/>
        <el-table-column prop="commit" label="备注" min-width="150px;" sortable/>
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
                    v-permission="['admin']"
                    :disabled="scope.row.status === 1 || scope.row.status === 3"
                    size="mini"
                    type="warning"
                    icon="el-icon-info"
                    plain
                    @click="openDialogApplyReview(scope.row.id,scope.row.inventoryId,scope.row.status,scope.row.itemCount);"
                  >进行审核</el-button>
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
import { Message } from 'element-ui'
import permission from '@/directive/permission/index.js' // 权限判断指令
import { pagination, reviewApply } from '@/api/apply'
const applyObj = {
  id: null,
  inventoryId: null,
  status: null,
  itemCount: null,
  reviewCommit: null
}
export default {
  directives: { permission }, // 按钮权限判断，不符合权限的不显示按钮
  data() {
    return {
      listLoading: true,
      dialogApplyReview: false, // 审核弹窗，默认false
      list: null, // 这是分页list
      applyStatus: null, // 这是审核弹窗的状态
      applyObj: Object.assign({}, applyObj), // 这是审核用对象
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      searchOptions: {
        // 这是传给后端的检索用参数
        itemName: null,
        createTimeBeginToEnd: [], // 这是时间的数组
        status: null // 这是下拉框的审核状态
      },
      itemStatusList: [
        { key: 0, status: 0, statusName: '未审核' },
        { key: 1, status: 1, statusName: '驳回' },
        { key: 2, status: 2, statusName: '审核但未领取' },
        { key: 3, status: 3, statusName: '领取完毕' }
      ], // 这是下拉框选项的审核状态,label绑定statusName，在下拉框中显示中文状态名称
      // 审核表单逻辑验证
      editRule: {
        status: [{ required: true, message: '请选择审核状态', trigger: 'blur' }]
      }
    }
  },
  created() {
    // 这里是设置打开页面自动会调用的方法
    this.fetchData()
  },
  methods: {
    // ID数组的赋值
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 每页显示数据量变更
    handleSizeChange(val) {
      this.pagesize = val
      this.fetchData() // 每次选择一页显示几条的时候调用fetchData方法
    },
    // 页码变更
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchData() // 每次切换页码的时候调用fetchData方法
    },
    // 清空搜索选项
    clearSearchOptions() {
      this.searchOptions = {
        // 此处用于重置搜索参数
        itemName: null,
        createTimeBeginToEnd: [], // 这是时间的数组
        status: null // 这是下拉框的审核状态
      }
    },
    // 带检索条件去查询列表（带检索用参数）
    searchData() {
      this.currentPage = 1
      this.listLoading = true
      this.fetchData() // 跳回第一页，带条件参数去后端查询列表数据
    },
    // 列表数据获取（默认不带检索用参数）
    fetchData() {
      this.listLoading = false
      const listQuery = {
        pageNum: this.currentPage, // 向后端传的页码
        pageSize: this.pagesize, // 向后端传的单页条数
        itemName: this.searchOptions.itemName, // 以物品名称进行检索
        status: this.searchOptions.status, // 查出所有选择的审核状态数据
        createTimeBeginToEnd: this.searchOptions.createTimeBeginToEnd // 时间数组
      }
      if (listQuery.status === null || listQuery.status === '') {
        // 如果不按清空搜索选项直接打叉（clearable）会导致状态传""，所以这里做一次判断
        // 如果未选择下拉框的审核状态用于查询，也同样必须赋值给status
        listQuery.status = -1 // 因为后端status为int，前端如果传null，到后端就会变为默认值0，这样会导致mybtis不按逻辑执行，所以这里设置为-1
      }
      pagination(listQuery).then(response => {
        const data = response.data.responseData
        this.list = data.list
        this.totalCount = data.total
        this.listLoading = false
      })
    },
    // 审核弹窗关闭
    handleCloseApply() {
      this.dialogApplyReview = false
      this.$refs.applyForm.resetFields()
    },
    // 审核弹窗打开
    openDialogApplyReview(applyId, inventoryId, status, itemCount) {
      // 获取到该条数据的参数，传值给对象最后会将这个对象给后端
      this.applyObj.id = applyId
      this.applyObj.inventoryId = inventoryId
      this.applyObj.status = status
      this.applyStatus = status
      this.applyObj.itemCount = itemCount
      this.dialogApplyReview = true
    },
    // 进行审核
    reviewApply(formName) {
      if (parseInt(this.applyObj.status) === parseInt(this.applyStatus)) {
        // 必须采用转换，因为两者类型可能会不一样导致业务逻辑失效
        Message({
          message: '请选择下一状态！',
          type: 'warning',
          duration: 5 * 1000
        })
        return
      } else {
        this.applyObj.status = this.applyStatus // 因为直接绑定this.applyObj.status会导致选择判断bug（页面展示上）
        this.$refs[formName].validate(valid => {
          if (valid) {
            this.applyObj.status = parseInt(this.applyObj.status) // 状态码转为数字
            reviewApply(this.applyObj)
              .then(response => {
                const data = response.data
                this.listLoading = false
                if (data.statusCode === 200) {
                  Message({
                    message: '操作成功',
                    type: 'success',
                    duration: 5 * 1000
                  })
                  this.$refs[formName].resetFields()
                  this.dialogApplyReview = false
                  this.applyObj = Object.assign({}, applyObj) // 重新给修改用对象赋值初始化，applyObj为全局const对象
                  this.fetchData()
                } else {
                  this.loading = false
                  Message({
                    message: '操作失败',
                    type: 'error',
                    duration: 5 * 1000
                  })
                }
              })
              .catch(() => {
                this.loading = false
                Message({
                  message: '操作失败',
                  type: 'error',
                  duration: 5 * 1000
                })
              })
          }
        })
      }
    } // 这是方法末尾花括号
  }
}
</script>
