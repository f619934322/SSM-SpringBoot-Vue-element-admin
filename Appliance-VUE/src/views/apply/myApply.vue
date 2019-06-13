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
          :picker-options="pickerOptions"
          clearable
          size="mini"
          value-format="yyyy-MM-dd"
          type="daterange"
          start-placeholder="开始日期"
          range-separator="至"
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
          style="margin-left: 6px;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)"
          icon="el-icon-search"
          @click="searchData"
        >搜索</el-button>
        <el-button class="filter-item" @click="clearSearchOptions">清空搜索选项</el-button>
      </div>
      <!-- /检索等顶部选项 -->
      <!-- 主表格 -->
      <el-table
        v-loading.body="listLoading"
        ref="multipleTable"
        :data="list"
        element-loading-text="拼命加载中"
        border
        stripe
        fit
        highlight-current-row
      >
        <el-table-column label="详情" type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="table-expand">
              <el-form-item label="申领人">
                <span>{{ props.row.creator }}</span>
              </el-form-item>
              <el-form-item label="申请原因">
                <span>{{ props.row.commit }}</span>
              </el-form-item>
              <el-form-item label="发起时间">
                <span>
                  <i class="el-icon-time"/>
                  {{ props.row.createTime }}
                </span>
              </el-form-item>
              <el-form-item label="审核人">
                <span>{{ props.row.reviewer }}</span>
              </el-form-item>
              <el-form-item label="审核时间">
                <span>
                  <i class="el-icon-time"/>
                  {{ props.row.reviewTime }}
                </span>
              </el-form-item>
              <el-form-item label="审核批注">
                <span>{{ props.row.reviewCommit }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="物品ID" min-width="120px;" sortable/>
        <el-table-column prop="itemName" label="物品名称" min-width="150px;" sortable/>
        <el-table-column prop="itemType" label="物品类型" min-width="120px;" sortable/>
        <el-table-column prop="itemCount" label="申领数量" min-width="120px;" sortable/>
        <el-table-column prop="status" label="审核状态" min-width="120px;" align="center" sortable>
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0">
              <el-tag type="info">未审核</el-tag>
            </span>
            <span v-if="scope.row.status === 1">
              <el-tag type="danger">驳回</el-tag>
            </span>
            <span v-if="scope.row.status === 2">
              <el-tag type="warning">通过但未领取</el-tag>
            </span>
            <span v-if="scope.row.status === 3">
              <el-tag type="success">领取完成</el-tag>
            </span>
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
import { paginationForMy } from '@/api/apply'
export default {
  data() {
    return {
      listLoading: true,
      list: null, // 这是分页list
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
      ],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      } // 这是时间选择的限制，设置为当前日期之后的不可选（大于号改小于号是当前日期之前不可选；-8.64e6不包括当前日期，-8.64e7包括当前日期）
    }
  },
  created() {
    // 这里是设置打开页面自动会调用的方法
    this.fetchData()
  },
  methods: {
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
    // 带检索条件去查询列表（带检索用参数）
    searchData() {
      this.currentPage = 1
      this.listLoading = true
      this.fetchData() // 跳回第一页，带条件参数去后端查询列表数据
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
      paginationForMy(listQuery).then(response => {
        const data = response.data.responseData
        this.list = data.list
        this.totalCount = data.total
        this.listLoading = false
      })
    }
  } // 这是方法末尾花括号
}
</script>
<style>
.table-expand {
  font-size: 0;
}
.table-expand label {
  width: 90px;
  color: #99a9bf;
}
.table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>
