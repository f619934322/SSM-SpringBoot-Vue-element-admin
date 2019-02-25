<template>
  <div>
    <!-- 审核弹窗 -->
    <el-dialog
      :visible.sync="dialogDemandReview"
      :before-close="handleCloseReview"
      :rules="editRule"
      title="审核管理"
    >
      <el-form
        ref="reviewForm"
        :model="demandObj"
        :rules="editRule"
        class="small-space"
        label-position="left"
        label-width="120px"
        style="width: 600px; margin-left:120px;"
      >
        <el-form-item label="选择审核状态" prop="status">
          <div align="left">
            <el-radio-group v-model="demandObj.status" size="mini">
              <el-radio-button label="1">驳回</el-radio-button>
              <el-radio-button label="2">通过未采购</el-radio-button>
              <el-radio-button label="3">采购失败</el-radio-button>
              <el-radio-button label="4">采购完成</el-radio-button>
            </el-radio-group>
          </div>
        </el-form-item>
        <el-form-item label="采购价格" prop="purchasePrice">
          <el-input v-model="demandObj.purchasePrice" placeholder="请输入物品价格（如果驳回，请输入0）"/>
        </el-form-item>
        <el-form-item label="采购数量" prop="itemCount">
          <el-input v-model="demandObj.itemCount" placeholder="请输入物品数量（如果驳回，请输入0）"/>
        </el-form-item>
        <el-form-item label="审核备注" prop="reviewCommit">
          <el-input
            v-model="demandObj.reviewCommit"
            :autosize="{ minRows: 3, maxRows: 5}"
            placeholder="请输入备注信息"
            type="textarea"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSupplementDemand = false;$refs.reviewForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="reviewDemand('reviewForm')">确 定</el-button>
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
      <el-table-column prop="status" label="审核状态" min-width="120px;" sortable/>
      <el-table-column prop="addedFlag" label="需求标识" min-width="120px;" sortable/>
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
                  size="mini"
                  type="warning"
                  icon="el-icon-info"
                  plain
                  @click="openDialogDemandReview(scope.row.id,scope.row.inventoryId,scope.row.purchasePrice,scope.row.itemCount,scope.row.addedFlag,scope.row.itemName,scope.row.itemType);"
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
</template>
<script>
import { Message } from 'element-ui'
import permission from '@/directive/permission/index.js' // 权限判断指令
import { pagination, reviewDemand } from '@/api/demand'
const demandObj = {
  // 插入更新等对象在这初始化
  id: null,
  inventoryId: null, // 库存表id
  itemName: null,
  itemCount: null,
  itemType: null,
  status: null, // 审核弹窗审核状态单选
  purchasePrice: null,
  addedFlag: null,
  commit: null
}
export default {
  directives: { permission }, // 按钮权限判断，不符合权限的不显示按钮
  data() {
    return {
      dialogDemandReview: false, // 这是审核操作窗口，默认false
      list: null, // 这是申请一览的list，打开页面会去找接口获取数据并赋值，默认null
      demandObj: Object.assign({}, demandObj), // 这是审核用对象
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      searchOptions: {
        // 这是传给后端的检索用参数
        itemName: null
      },
      // 审核表单逻辑验证
      editRule: {
        status: [
          { required: true, message: '请选择审核状态', trigger: 'blur' }
        ],
        itemCount: [
          {
            required: true,
            message: '请输入物品数量（如果驳回，请输入0）',
            trigger: 'blur'
          }
        ],
        purchasePrice: [
          {
            required: true,
            message: '请输入采购价格（如果驳回，请输入0）',
            trigger: 'blur'
          }
        ]
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
    // 打开审核窗口
    openDialogDemandReview(
      id,
      inventoryId,
      purchasePrice,
      itemCount,
      addedFlag,
      itemName,
      itemType
    ) {
      // 这里获取列表中选中的采购需求id
      this.demandObj.id = id // 然后赋值给对象
      this.demandObj.inventoryId = inventoryId
      this.demandObj.purchasePrice = purchasePrice
      this.demandObj.addedFlag = addedFlag
      this.demandObj.itemName = itemName
      this.demandObj.itemType = itemType
      if (itemCount === 0) {
        itemCount = null // 由于后端物品数量类型为int，当数据库值为null时，后端会把他转变为0，这里转回null
      }
      this.demandObj.itemCount = itemCount
      this.dialogDemandReview = true
    },
    // 审核弹窗关闭
    handleCloseReview() {
      this.dialogDemandReview = false
      this.$refs.reviewForm.resetFields()
    },
    // 审核
    reviewDemand(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.demandObj.status = parseInt(this.demandObj.status) // 状态码转为数字
          reviewDemand(this.demandObj)
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
                this.dialogDemandReview = false
                this.demandObj = Object.assign({}, demandObj) // 重新给修改用对象赋值初始化，demandObj为全局const对象
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
</script>
