<template>
  <!-- 检索等顶部选项 -->
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
          style="margin-left: 6px;"
          icon="el-icon-search"
          @click="searchData"
        >搜索</el-button>
        <el-button
          class="filter-item"
          align="right"
          style="margin-left: 6px;"
          type="primary"
          icon="el-icon-circle-plus-outline"
          @click="openDialogNewDemand"
        >申请新增采购</el-button>
        <el-button
          v-permission="['admin']"
          class="filter-item"
          type="danger"
          icon="el-icon-delete"
          @click="delBacthClick"
        >批量删除</el-button>
      </div>
    </div>
    <!-- /检索等顶部选项 -->
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
    <!-- 库存编辑弹窗 -->
    <el-dialog
      :visible.sync="dialogItemUpdate"
      :before-close="handleCloseEdit"
      :rules="editRule"
      title="申请发起新采购"
    >
      <el-form
        ref="editForm"
        :model="itemUpdateObj"
        :rules="editRule"
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="id" style="display: none;">
          <el-input v-model="itemUpdateObj.id" disabled="disabled"/>
        </el-form-item>
        <el-form-item label="物品名称" prop="itemName">
          <el-input v-model="itemUpdateObj.itemName" placeholder="请输入物品名称"/>
        </el-form-item>
        <el-form-item label="物品总数" prop="itemCount">
          <el-input v-model="itemUpdateObj.itemCount" placeholder="请输入物品数量"/>
        </el-form-item>
        <el-form-item label="物品类型" prop="itemType">
          <el-select
            v-model="itemUpdateObj.itemType"
            class="filter-item"
            filterable
            placeholder="请选择物品类型"
          >
            <el-option v-for="item in itemTypeList" :key="item.key" :value="item.itemType"/>
          </el-select>
        </el-form-item>
        <el-form-item label="修改备注" prop="commit">
          <el-input
            v-model="itemUpdateObj.commit"
            :autosize="{ minRows: 3, maxRows: 5}"
            placeholder="请输入备注信息"
            type="textarea"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogItemUpdate = false;$refs.editForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="updateItemSubmit('editForm')">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /库存编辑弹窗 -->
    <!-- 新增采购弹窗 -->
    <el-dialog
      :visible.sync="dialogNewDemand"
      :before-close="handleCloseNewAdd"
      :rules="addRule"
      title="申请新采购"
    >
      <el-form
        ref="addForm"
        :model="demandObj"
        :rules="addRule"
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="物品名称" prop="itemName">
          <el-input v-model="demandObj.itemName" placeholder="请输入物品名称"/>
        </el-form-item>
        <el-form-item label="物品类型" prop="itemType">
          <el-select
            v-model="demandObj.itemType"
            class="filter-item"
            filterable
            placeholder="请选择物品类型"
          >
            <el-option v-for="item in itemTypeList" :key="item.key" :value="item.itemType"/>
          </el-select>
        </el-form-item>
        <el-form-item label="申请原因" prop="commit">
          <el-input
            v-model="demandObj.commit"
            :autosize="{ minRows: 3, maxRows: 5}"
            placeholder="请输入备注信息"
            type="textarea"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogNewDemand = false;$refs.addForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="insertNewDemand('addForm')">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /新增采购弹窗 -->
    <!-- 补充采购弹窗 -->
    <el-dialog
      :visible.sync="dialogSupplementDemand"
      :before-close="handleCloseSupplement"
      :rules="addRule"
      title="申请补充库存"
    >
      <el-form
        ref="supplementForm"
        :model="demandObj"
        :rules="addRule"
        class="small-space"
        label-position="left"
        label-width="80px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="申请原因" prop="commit">
          <el-input
            v-model="demandObj.commit"
            :autosize="{ minRows: 3, maxRows: 5}"
            placeholder="请输入备注信息"
            type="textarea"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSupplementDemand = false;$refs.supplementForm.resetFields()">取消</el-button>
        <el-button type="primary" @click="supplementDemand('supplementForm')">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /补充采购弹窗 -->
    <!-- 库存详情弹窗 -->
    <el-dialog :visible.sync="dialogVisibleDetail" title="库存详情">
      <el-table
        v-loading.body="listLoading"
        :data="tableDetail"
        height="300"
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="id" width="50"/>
        <el-table-column prop="itemName" label="物品名称" width="100"/>
        <el-table-column prop="itemCount" label="该次补充数量" width="120"/>
        <el-table-column prop="creator" label="申请人" width="120"/>
        <el-table-column prop="reviewer" label="审核人" width="120"/>
        <el-table-column prop="createTime" label="申请时间" width="120"/>
        <el-table-column prop="reviewTime" label="审核时间" width="120"/>
        <el-table-column prop="status" label="审核状态" width="120"/>
        <el-table-column prop="commit" label="备注" width="200"/>
      </el-table>
    </el-dialog>
    <!-- /库存详情弹窗 -->
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
                  type="primary"
                  icon="el-icon-circle-plus"
                  plain
                  @click="openDialogSupplementDemand(scope.row.id,scope.row.itemName,scope.row.itemType);"
                >申请补充</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="success"
                  icon="el-icon-plus"
                  plain
                  @click="apply(scope.row.id);"
                >申请领取</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="small"
                  type="info"
                  icon="el-icon-tickets"
                  plain
                  @click="detail(scope.row.id);"
                >采购详情</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  v-permission="['admin']"
                  size="small"
                  type="warning"
                  icon="el-icon-edit"
                  plain
                  @click="updateItem(scope.row.id,scope.row.itemName,scope.row.itemCount,scope.row.commit,scope.row.itemType);"
                >编辑物品</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  v-permission="['admin']"
                  size="small"
                  type="danger"
                  icon="el-icon-delete"
                  plain
                  @click="deleteItem(scope.row.id);"
                >删除物品</el-button>
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
import {
  pagination,
  bacthDeleteItem,
  deleteItem,
  updateItem,
  detailForInventory,
  insertNewDemand,
  supplementDemand
} from '@/api/inventory'
const inventoryObj = {
  // 插入更新等对象在这初始化
  id: null,
  itemName: null,
  itemCount: null,
  itemType: null,
  commit: null
}
// 采购对象初始化
const demandObj = {
  inventoryId: null,
  itemName: null,
  itemType: null,
  commit: null
}
export default {
  directives: { permission }, // 按钮权限判断，不符合权限的不显示按钮
  data() {
    return {
      listLoading: true,
      dialogVisibleDelBatch: false, // 这是批量删除的弹窗，默认false
      dialogVisibleDel: false, // 这是单选删除的弹窗，默认false
      dialogItemUpdate: false, // 这是编辑的弹窗，默认false
      dialogVisibleDetail: false, // 这是库存详情的弹窗，默认false
      dialogNewDemand: false, // 这是新增采购的弹窗，默认false
      dialogSupplementDemand: false, // 这是采购补充的弹窗，默认false
      multipleSelection: [], // 存放勾选对象的数组
      list: null, // 这是库存一览的list，打开页面会去找接口获取数据并赋值，默认null
      tableDetail: null, // 这是库存详情的list，默认null
      delItemId: null, // 这是单选删除的物品id
      itemTypeList: [{ key: 1, itemType: 'TS' }], // 这是编辑弹窗里的物品类型下拉框数据，默认写死
      // 这是编辑用的对象
      itemUpdateObj: Object.assign({}, inventoryObj),
      // 这是新增采购用对象
      demandObj: Object.assign({}, demandObj),
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      searchOptions: {
        // 这是传给后端的检索用参数
        itemName: null
      },
      // 编辑规则,在编辑时强制输入符合规则的内容
      editRule: {
        itemName: [
          { required: true, message: '请输入物品名称', trigger: 'blur' }
        ],
        itemCount: [
          { required: true, message: '请输入物品数量', trigger: 'blur' }
        ],
        itemType: [
          { required: true, message: '请选择物品类型', trigger: 'blur' }
        ],
        commit: [
          { required: true, message: '请输入本次修改的备注', trigger: 'blur' }
        ]
      },
      addRule: {
        itemName: [
          { required: true, message: '请输入物品名称', trigger: 'blur' }
        ],
        itemCount: [
          { required: true, message: '请输入物品数量', trigger: 'blur' }
        ],
        itemType: [
          { required: true, message: '请选择物品类型', trigger: 'blur' }
        ],
        commit: [
          { required: true, message: '请输入申请的备注', trigger: 'blur' }
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
      if (this.multipleSelection.length === 0) {
        // 数组判空
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
    },
    // 编辑弹窗关闭
    handleCloseEdit() {
      this.dialogItemUpdate = false
      this.$refs.editForm.resetFields()
    },
    // 编辑弹窗打开和内容赋值
    updateItem(id, itemName, itemCount, commit, itemType) {
      this.itemUpdateObj.id = id
      this.itemUpdateObj.itemName = itemName
      this.itemUpdateObj.itemCount = itemCount
      this.itemUpdateObj.commit = commit
      this.itemUpdateObj.itemType = itemType
      this.dialogItemUpdate = true
    },
    updateItemSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          updateItem(this.itemUpdateObj)
            .then(response => {
              const data = response.data
              this.listLoading = false
              if (data.statusCode === 200) {
                Message({
                  message: '编辑成功',
                  type: 'success',
                  duration: 5 * 1000
                })
                this.$refs[formName].resetFields()
                this.dialogItemUpdate = false
                this.itemUpdateObj = Object.assign({}, inventoryObj) // 重新给修改用对象赋值初始化，inventoryObj为全局const对象
                this.fetchData()
              } else {
                this.loading = false
                Message({
                  message: '编辑失败',
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            })
            .catch(() => {
              this.loading = false
              Message({
                message: '编辑失败',
                type: 'error',
                duration: 5 * 1000
              })
            })
        }
      })
    },
    // 库存物品详情弹窗显示
    detail(id) {
      // 先获取详情数据，再展示窗口
      detailForInventory(id)
        .then(response => {
          const data = response.data
          this.listLoading = false
          if (data.statusCode === 200) {
            this.tableDetail = data.responseData
            this.dialogVisibleDetail = true
          } else {
            Message({
              message: '获取详情失败',
              type: 'error',
              duration: 5 * 1000
            })
          }
        })
        .catch(() => {
          this.loading = false
          Message({
            message: '获取详情失败',
            type: 'error',
            duration: 5 * 1000
          })
        })
    },
    // 新增采购弹窗关闭
    handleCloseNewAdd() {
      this.dialogNewDemand = false
      this.$refs.addForm.resetFields()
    },
    // 打开采购新增表单
    openDialogNewDemand() {
      this.dialogNewDemand = true
    },
    // 新增采购申请
    insertNewDemand(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          insertNewDemand(this.demandObj)
            .then(response => {
              const data = response.data
              this.listLoading = false
              if (data.statusCode === 200) {
                Message({
                  message: '申请成功',
                  type: 'success',
                  duration: 5 * 1000
                })
              } else {
                Message({
                  message: '申请失败，检查网络',
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            })
            .catch(() => {
              this.loading = false
              Message({
                message: '申请失败，检查网络',
                type: 'error',
                duration: 5 * 1000
              })
            })
        }
      })
    }, // 补充采购弹窗关闭
    handleCloseSupplement() {
      this.dialogSupplementDemand = false
      this.$refs.supplementForm.resetFields()
    },
    // 打开采购新增表单
    openDialogSupplementDemand(id, itemName, itemType) {
      this.dialogSupplementDemand = true
      this.demandObj.inventoryId = id
      this.demandObj.itemName = itemName
      this.demandObj.itemType = itemType
    },
    supplementDemand(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          supplementDemand(this.demandObj)
            .then(response => {
              const data = response.data
              this.listLoading = false
              if (data.statusCode === 200) {
                this.dialogSupplementDemand = false
                this.demandObj = Object.assign({}, demandObj) // 重新给修改用对象赋值初始化，inventoryObj为全局const对象
                Message({
                  message: '申请成功',
                  type: 'success',
                  duration: 5 * 1000
                })
              } else {
                Message({
                  message: '申请失败，检查网络',
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            })
            .catch(() => {
              this.loading = false
              Message({
                message: '申请失败，检查网络',
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
