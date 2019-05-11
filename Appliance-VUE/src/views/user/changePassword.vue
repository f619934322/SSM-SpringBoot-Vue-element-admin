<template>
  <div class="background">
    <el-card class="box-card" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.4)">
      <div class="from">
        <el-form ref="changePasswordForm" :model="changePasswordObj" :rules="changePasswordRule">
          <h1 align="center">密码修改</h1>
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input
              v-model="changePasswordObj.oldPassword"
              :type="passwordType"
              auto-complete="new-password"
              placeholder="请输入旧密码"
            />
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input
              v-model="changePasswordObj.newPassword"
              :type="passwordType"
              auto-complete="new-password"
              placeholder="请输入新密码"
            />
          </el-form-item>
          <el-form-item label="确认新密码" prop="newPassword2">
            <el-input
              v-model="changePasswordObj.newPassword2"
              :type="passwordType"
              auto-complete="new-password"
              placeholder="确认新密码"
            />
          </el-form-item>
          <div align="right">
            <el-button type="primary" @click="changePassword('changePasswordForm')">提交更改</el-button>
            <el-button>
              <router-link to="/">取消并返回主页</router-link>
            </el-button>
          </div>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<script>
import { Message } from 'element-ui'
import { changePassword } from '@/api/userManagement'
const changePasswordObj = {
  // 插入更新等对象在这初始化
  oldPassword: null,
  newPassword: null,
  newPassword2: null,
  password: null
}
export default {
  data() {
    var validatePassword = (rule, value, callback) => {
      if (value === null) {
        callback(new Error('请输入密码'))
      } else if (value.length < 6) {
        callback(new Error('密码不得少于6位'))
      } else {
        callback()
      }
    }
    var validatePassword2 = (rule, value, callback) => {
      if (value === null) {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.changePasswordObj.newPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      changePasswordObj: Object.assign({}, changePasswordObj), // 双向绑定密码对象
      passwordType: 'password', // 密码类型，可以隐藏密码
      changePasswordRule: {
        oldPassword: [
          {
            required: true,
            message: '请输入您的旧密码',
            trigger: 'blur',
            validator: validatePassword
          }
        ],
        newPassword: [
          {
            required: true,
            message: '请输入新密码（最少6位）',
            validator: validatePassword,
            trigger: 'blur'
          }
        ],
        newPassword2: [
          {
            required: true,
            message: '两次密码必须一样！',
            validator: validatePassword2,
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    // 修改密码
    changePassword(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          changePassword(this.changePasswordObj)
            .then(response => {
              const data = response.data
              this.listLoading = false
              if (data.statusCode === 200) {
                this.$alert('密码修改成功，即将登出', '提示', {
                  confirmButtonText: '确定',
                  callback: action => {
                    this.$message({
                      type: 'success',
                      message: `密码修改成功，即将登出`
                    })
                    this.$refs[formName].resetFields()
                    this.changePasswordObj = Object.assign(
                      {},
                      changePasswordObj
                    ) // 重新给修改用对象赋值初始化，changePasswordObj为全局const对象
                    this.$router.push('/') // 修改成功后跳转到主页
                    this.logout() // 然后登出
                  }
                })
              } else {
                this.loading = false
                Message({
                  message: '密码修改失败：' + data.statusMsg,
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            })
            .catch(() => {
              this.loading = false
              Message({
                message: '密码修改失败',
                type: 'error',
                duration: 5 * 1000
              })
            })
        }
      })
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // In order to re-instantiate the vue-router object to avoid bugs
      })
    }
  } // 这是方法末尾花括号
}
</script>
<style>
.background {
  background: url(../../img/bg4.jpg);
  overflow: hidden;
  background-size: cover;
  min-height: 100%;
  width: 100%;
  background-color: #e3e3e3;
  min-height: 100vh;
  padding: 50px 60px 0px;
}
.box-card {
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  -moz-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  -o-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
.from {
  margin: 0 center;
  height: auto;
  width: auto;
}
</style>
