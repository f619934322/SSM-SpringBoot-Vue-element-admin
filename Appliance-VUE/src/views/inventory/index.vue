<template>
  <!-- 检索选项 -->
  <div class="app-container">
    <div class="filter-container">
      <div class="container">
        <el-tooltip class="item" effect="light" content="物品名称" placement="top">
          <el-input
            v-model.trim="searchOptions.itemName"
            style="width: 200px;"
            class="filter-item"
            placeholder="物品名称"
          />
        </el-tooltip>
        <el-button
          class="filter-item"
          type="primary"
          style="margin-left: 6px;"
          icon="search"
          @click="searchData"
        >搜索</el-button>
        <el-button
          class="filter-item"
          align="right"
          style="margin-left: 6px;"
          type="primary"
          icon="edit"
        >申请新增采购</el-button>
        <el-button
          v-permission="['admin']"
          class="filter-item"
          type="danger"
          icon="delBacthClick"
          @click="delBacthClick"
        >批量删除</el-button>
      </div>
    </div>
    <!-- /检索选项 -->
    <!-- 物品批量删除弹窗 -->
    <el-dialog :visible.sync="dialogVisibleDelBatch" title="物品批量删除">
      <code>您确认要删除这些物品吗？ {{ multipleSelection }}</code>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleDelBatch = false;multipleSelection = null">取 消</el-button>
        <el-button type="danger" @click="bacthDeleteItemSubmit">确认删除</el-button>
      </div>
    </el-dialog>
    <!-- /物品批量删除弹窗 -->
    <!-- 物品单选删除弹窗 -->
    <el-dialog :visible.sync="dialogVisibleDel" title="物品删除">
      <code>您确认要删除此物品吗？ ID：{{ delItemId }}</code>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleDel = false;delItemId = null">取 消</el-button>
        <el-button type="danger" @click="itemDeleteSubmit">确认删除</el-button>
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
      <el-table-column prop="id" label="物品ID" min-width="120px;" sortable/>
      <el-table-column prop="itemName" label="物品名称" min-width="150px;" sortable/>
      <el-table-column prop="itemCount" label="物品总数" min-width="120px;" sortable/>
      <el-table-column prop="itemType" label="物品类型" min-width="120px;" sortable/>
      <el-table-column prop="commit" label="备注" min-width="150px;" sortable/>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button size="small" type="info" @click="deatil(scope.row.id);">详 情</el-button>
          <el-button size="small" type="primary" @click="supplement(scope.row.id);">申请补充</el-button>
          <el-button
            v-permission="['admin']"
            size="small"
            type="success"
            @click="updateItem(scope.row.id);"
          >编 辑</el-button>
          <el-button
            v-permission="['admin']"
            size="small"
            type="danger"
            @click="deleteItem(scope.row.id);"
          >删 除</el-button>
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
import { Message } from 'element-ui'
import permission from '@/directive/permission/index.js' // 权限判断指令
import { pagination, bacthDeleteItem, deleteItem } from '@/api/inventory'
export default {
  directives: { permission }, // 按钮权限判断，不符合权限的不显示按钮
  data() {
    return {
      listLoading: true,
      dialogVisibleDelBatch: false, // 这是批量删除的弹窗，默认false
      dialogVisibleDel: false, // 这是单选删除的弹窗，默认false
      multipleSelection: [], // 存放勾选对象的数组
      list: null,
      delItemId: null, // 这是单选删除的物品id
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      searchOptions: {
        // 这是传给后端的检索用参数
        itemName: null
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
    // 列表数据获取（默认不带检索用参数）
    fetchData() {
      this.listLoading = false
      const listQuery = {
        pageNum: this.currentPage, // 向后端传的页码
        pageSize: this.pagesize, // 向后端传的单页条数
        itemName: this.searchOptions.itemName // 以物品名称进行检索
      }
      pagination(listQuery).then(response => {
        const data = response.data.responseData
        this.list = data.list
        this.totalCount = data.total
        this.listLoading = false
      })
    },
    // 带检索条件去查询列表（带检索用参数）
    searchData() {
      this.currentPage = 1
      this.listLoading = true
      this.fetchData() // 跳回第一页，带条件参数去后端查询列表数据
    },
    // 批量删除确认
    delBacthClick() {
      if (this.multipleSelection === null || this.multipleSelection === '') {
        Message({
          message: '您还未勾选',
          type: 'error',
          duration: 5 * 1000
        })
        return
      }
      this.dialogVisibleDelBatch = true
    },
    // 批量删除
    bacthDeleteItemSubmit() {
      const ids = []
      this.multipleSelection.forEach(function(item) {
        ids.push(item.id)
      })

      this.listLoading = true
      this.dialogVisibleDelBatch = false
      bacthDeleteItem(ids)
        .then(response => {
          const data = response.data
          this.listLoading = false
          if (data.statusCode === 200) {
            this.multipleSelection = []
            Message({
              message: '批量删除成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.fetchData()
          } else {
            this.loading = false
            Message({
              message: '批量删除失败',
              type: 'error',
              duration: 5 * 1000
            })
          }
        })
        .catch(() => {
          this.loading = false
          Message({
            message: '批量删除失败',
            type: 'error',
            duration: 5 * 1000
          })
        })
    },
    // 单项删除确认弹窗显示
    deleteItem(id) {
      this.dialogVisibleDel = true
      this.delItemId = id
    },
    // 单项删除
    itemDeleteSubmit() {
      this.listLoading = true
      this.dialogVisibleDel = false
      const id = this.delItemId
      deleteItem(id)
        .then(response => {
          const data = response.data
          this.listLoading = false
          if (data.statusCode === 200) {
            this.delItemId = null
            Message({
              message: '删除成功',
              type: 'success',
              duration: 5 * 1000
            })
          } else {
            Message({
              message: '删除失败',
              type: 'error',
              duration: 5 * 1000
            })
          }
          this.fetchData()
        })
        .catch(() => {
          this.loading = false
          Message({
            message: '删除失败',
            type: 'error',
            duration: 5 * 1000
          })
        })
    }
  }
}
</script>
