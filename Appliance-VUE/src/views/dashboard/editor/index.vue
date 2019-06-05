<template>
  <!-- 当用户权限不是admin时，主页是这个 -->
  <div class="dashboard-editor-container">
    <h1>欢迎使用本采购系统</h1>
    <div class=" clearfix">
      <div class="info-container">
        <span class="display_name">{{ name }}</span>
        <span style="font-size:20px;padding-top:20px;display:inline-block;">Staff's Dashboard</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'DashboardEditor',
  data() {
    return {}
  },
  computed: {
    ...mapGetters(['name', 'roles'])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  background: url(../../../img/bg2.jpg); // 可以添加登录界面背景
  overflow: hidden;
  background-size: cover;
  min-height: 100%;
  width: 100%;
  background-color: #e3e3e3;
  min-height: 100vh;
  padding: 50px 60px 0px;
  .pan-info-roles {
    font-size: 12px;
    font-weight: 700;
    color: #333;
    display: block;
  }
  .info-container {
    position: relative;
    margin-left: 100px;
    height: 120px;
    line-height: 160px;
    .display_name {
      font-size: 36px;
      line-height: 38px;
      color: #212121;
      position: absolute;
      top: 25px;
    }
  }
}
</style>
