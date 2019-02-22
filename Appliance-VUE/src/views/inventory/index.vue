<template>
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
        >申请采购</el-button>
      </div>
    </div>
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
      <el-table-column align="center" prop="goodsId" label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="small" type="info" @click="deatil(scope.row.id);">详情</el-button>
          <el-button size="small" type="success" @click="update(scope.row.id);">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
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
  </div>
</template>
<script>
import { pagination } from '@/api/inventory'
export default {
  data() {
    return {
      listLoading: true,
      multipleSelection: [], // 存放勾选ID的数组
      list: null,
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
      console.info(val)
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
      (this.currentPage = 1)((this.listLoading = true))
      this.fetchData() // 跳回第一页，带条件参数去后端查询列表数据
    }
  }
}
</script>
