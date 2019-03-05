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
          size="mini"
          clearable
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
        <el-button class="filter-item" type="primary" icon="el-icon-download" @click="excelClick">导出</el-button>
      </div>
    <!-- /检索等顶部选项 -->
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
              <el-radio-group v-model="demandStatus" size="mini">
                <el-radio-button :disabled="demandObj.status === 2" label="1">驳回</el-radio-button>
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
            <el-input v-model.number="demandObj.itemCount" placeholder="请输入物品数量（如果驳回，请输入0）"/>
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
          <el-button @click="handleCloseReview()">取消</el-button>
          <el-button type="primary" @click="reviewDemand('reviewForm')">确 定</el-button>
        </div>
      </el-dialog>
      <!-- /审核弹窗 -->
      <!-- 导出弹窗 -->
      <el-dialog :visible.sync="dialogVisibleExeclForDemand" title="导出需求清单">
        <code>这将导出审核通过但为完成采购的需求清单。</code>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisibleExeclForDemand = false;">取 消</el-button>
          <el-button type="primary" @click="execlForDemand">确认导出</el-button>
        </div>
      </el-dialog>
      <!-- /导出弹窗 -->
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
        <el-table-column prop="status" label="审核状态" min-width="120px;" sortable>
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0">未审核</span>
            <span v-if="scope.row.status === 1">驳回</span>
            <span v-if="scope.row.status === 2">审核通过未采购</span>
            <span v-if="scope.row.status === 3">采购失败</span>
            <span v-if="scope.row.status === 4">采购完成</span>
          </template>
        </el-table-column>
        <el-table-column prop="addedFlag" label="需求标识" min-width="120px;" sortable>
          <template slot-scope="scope">
            <span v-if="scope.row.addedFlag === 0">需要补充</span>
            <span v-if="scope.row.addedFlag === 1">需要新增</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发起时间" min-width="120px;" sortable/>
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
                    :disabled="scope.row.status === 1 || scope.row.status === 3 || scope.row.status === 4"
                    size="mini"
                    type="warning"
                    icon="el-icon-info"
                    plain
                    @click="openDialogDemandReview(scope.row.id,scope.row.inventoryId,scope.row.purchasePrice,scope.row.itemCount,scope.row.addedFlag,scope.row.itemName,scope.row.itemType,scope.row.status,scope.row.reviewCommit);"
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
  </div>
</template>
<script>
import { Message } from "element-ui";
import permission from "@/directive/permission/index.js"; // 权限判断指令
import { pagination, reviewDemand } from "@/api/demand";
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
  commit: null,
  reviewCommit: null
};
export default {
  directives: { permission }, // 按钮权限判断，不符合权限的不显示按钮
  data() {
    return {
      listLoading: true,
      dialogDemandReview: false, // 这是审核操作窗口，默认false
      dialogVisibleExeclForDemand: false, // 这是导出弹窗，默认false
      list: null, // 这是申请一览的list，打开页面会去找接口获取数据并赋值，默认null
      demandObj: Object.assign({}, demandObj), // 这是审核用对象
      demandStatus: null, // 审核弹窗审核状态单选
      itemStatusList: [
        { key: 0, status: 0, statusName: "未审核" },
        { key: 1, status: 1, statusName: "驳回" },
        { key: 2, status: 2, statusName: "审核但未采购" },
        { key: 3, status: 3, statusName: "采购失败" },
        { key: 4, status: 4, statusName: "采购成功" }
      ], // 这是下拉框选项的审核状态,label绑定statusName，在下拉框中显示中文状态名称
      totalCount: 0,
      pagesize: 10,
      currentPage: 1,
      searchOptions: {
        // 这是传给后端的检索用参数
        itemName: null,
        createTimeBeginToEnd: [], // 这是时间的数组
        status: null // 这是下拉框的审核状态
      },
      // 审核表单逻辑验证
      editRule: {
        status: [
          { required: true, message: "请选择审核状态", trigger: "blur" }
        ],
        itemCount: [
          {
            required: true,
            message: "请输入物品数量（如果驳回，请输入0）",
            trigger: "blur"
          },
          {
            validator(rule, value, callback) {
              // 表单验证-正整数
              if (
                Number.isInteger(Number(value)) &&
                Number(value) > 0 &&
                Number(value) < 999
              ) {
                callback();
              } else {
                callback(new Error("请输入1-999的正整数"));
              }
            },
            trigger: "blur"
          }
        ],
        purchasePrice: [
          {
            required: true,
            message: "请输入采购价格（如果驳回，请输入0）",
            trigger: "blur"
          },
          {
            validator(rule, value, callback) {
              var reg = /^-?\d{1,5}(?:\.\d{1,3})?$/;
              if (reg.test(value)) {
                callback();
              } else {
                callback(new Error("请输入大于零小于十万不超过三位小数的数字"));
              }
            },
            trigger: "blur"
          }
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
        itemName: null,
        createTimeBeginToEnd: [], // 这是时间的数组
        status: null // 这是下拉框的审核状态
      };
    },
    // 带检索条件去查询列表（带检索用参数）
    searchData() {
      this.currentPage = 1;
      this.listLoading = true;
      this.fetchData(); // 跳回第一页，带条件参数去后端查询列表数据
    },
    // excel导出按钮方法
    excelClick() {
      this.dialogVisibleExeclForDemand = true;
    },
    execlForDemand() {
      const status = (this.demandObj.status = 2);
      const baseURL = "http://127.0.0.1:8011/";
      // 因为跨域，所以使用外链
      window.location.href =
        baseURL + "/appliance/demand/excelDemand?" + "status=" + status;
      this.demandObj = Object.assign({}, demandObj); // 重新给修改用对象赋值初始化，demandObj为全局const对象
      this.dialogVisibleExeclForDemand = false;
    },
    // 列表数据获取（默认不带检索用参数）
    fetchData() {
      this.listLoading = false;
      const listQuery = {
        pageNum: this.currentPage, // 向后端传的页码
        pageSize: this.pagesize, // 向后端传的单页条数
        itemName: this.searchOptions.itemName, // 以物品名称进行检索
        status: this.searchOptions.status, // 查出所有选择的审核状态数据
        createTimeBeginToEnd: this.searchOptions.createTimeBeginToEnd // 时间数组
      };
      if (listQuery.status === null || listQuery.status === "") {
        // 如果不按清空搜索选项直接打叉（clearable）会导致状态传""，所以这里做一次判断
        // 如果未选择下拉框的审核状态用于查询，也同样必须赋值给status
        listQuery.status = -1; // 因为后端status为int，前端如果传null，到后端就会变为默认值0，这样会导致mybtis不按逻辑执行，所以这里设置为-1
      }
      pagination(listQuery).then(response => {
        const data = response.data.responseData;
        this.list = data.list;
        this.totalCount = data.total;
        this.listLoading = false;
      });
    },
    // 打开审核窗口
    openDialogDemandReview(
      id,
      inventoryId,
      purchasePrice,
      itemCount,
      addedFlag,
      itemName,
      itemType,
      status,
      reviewCommit
    ) {
      // 这里获取列表中选中的采购需求id
      this.demandObj.id = id; // 然后赋值给对象
      this.demandObj.inventoryId = inventoryId;
      this.demandObj.purchasePrice = purchasePrice;
      this.demandObj.addedFlag = addedFlag;
      this.demandObj.itemName = itemName;
      this.demandObj.itemType = itemType;
      this.demandObj.status = status; // 获取该条的审核状态，用于判断在审核弹窗的审核状态选择的显示
      this.demandObj.reviewCommit = reviewCommit;
      if (itemCount === 0) {
        itemCount = null; // 由于后端物品数量类型为int，当数据库值为null时，后端会把他转变为0，这里转回null
      }
      this.demandStatus = status;
      this.demandObj.itemCount = itemCount;
      this.dialogDemandReview = true;
    },
    // 审核弹窗关闭
    handleCloseReview() {
      this.dialogDemandReview = false;
      this.$refs.reviewForm.resetFields();
    },
    // 审核
    reviewDemand(formName) {
      if (this.demandObj.status === this.demandStatus) {
        Message({
          message: "请选择下一状态！",
          type: "warning",
          duration: 5 * 1000
        });
        return;
      } else {
        this.demandObj.status = this.demandStatus; // 因为直接绑定this.demandObj.status会导致选择判断bug（页面展示上的），所以另外声明一个this.demandStatus来接收前端选择的状态
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.demandObj.status = parseInt(this.demandObj.status); // 状态码转为数字
          reviewDemand(this.demandObj)
            .then(response => {
              const data = response.data;
              this.listLoading = false;
              if (data.statusCode === 200) {
                Message({
                  message: "操作成功",
                  type: "success",
                  duration: 5 * 1000
                });
                this.$refs[formName].resetFields();
                this.dialogDemandReview = false;
                this.demandObj = Object.assign({}, demandObj); // 重新给修改用对象赋值初始化，demandObj为全局const对象
                this.fetchData();
              } else {
                this.loading = false;
                Message({
                  message: "操作失败",
                  type: "error",
                  duration: 5 * 1000
                });
              }
            })
            .catch(() => {
              this.loading = false;
              Message({
                message: "操作失败",
                type: "error",
                duration: 5 * 1000
              });
            });
        }
      });
    }
  } // 这是方法末尾花括号
};
</script>
