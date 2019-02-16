import { loginByUsername, logout, getUserInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'
import Cookies from 'js-cookie'
import { Message } from 'element-ui'

const user = {
  state: {
    user: '',
    status: '',
    code: '',
    token: getToken(),
    name: '',
    avatar: '',
    introduction: '',
    roles: [],
    setting: {
      articlePlatform: []
    }
  },

  mutations: {
    SET_CODE: (state, code) => {
      state.code = code
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_INTRODUCTION: (state, introduction) => {
      state.introduction = introduction
    },
    SET_SETTING: (state, setting) => {
      state.setting = setting
    },
    SET_STATUS: (state, status) => {
      state.status = status
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    }
  },

  actions: {
    // 用户名登录
    LoginByUsername({ commit }, userInfo) {
      const staffNo = userInfo.staffNo.trim()
      return new Promise((resolve, reject) => {
        loginByUsername(staffNo, userInfo.password).then(response => {
          const token = response.data.statusCode
          commit('SET_TOKEN', token) // 由于只需要前端携带Cookie中的JSESSIONID到后端验证，此处token改成statusCode
          setToken(token)
          Cookies.set('statusMsg', response.data.statusMsg)
          resolve()
        }).catch(error => {
          Message({
            message: '网络异常',
            type: 'error',
            duration: 5 * 1000
          })
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo().then(response => {
          // 由于mockjs 不支持自定义状态码只能这样hack
          if (!response) {
            Message({
              message: '网络异常',
              type: 'error',
              duration: 5 * 1000
            })
            reject('Verification failed, please login again.')
          }
          if (parseInt(response.data.statusCode) !== 200) {
            Message({
              message: '获取用户信息失败，检查用户是否登录',
              type: 'error',
              duration: 5 * 1000
            })
            console.warn('获取用户信息失败，检查用户是否登录。')
            reject('Verification failed, please login again.')
          }
          const data = response.data.responseData
          if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
            commit('SET_ROLES', data.roles)
          } else {
            reject('getInfo: roles must be a non-null array!')
          }
          var nameAndStaffNo = data.name + '(' + data.staffNo + ')'
          commit('SET_NAME', nameAndStaffNo)// 可以用store.getters获取这里赋的值,这里赋值是样式是：姓名（工号），后端不需要接收，只做前端展示
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout().then(() => { // 不需要前端传参数到后端验证用户再登出，只需要携带Cookie到后端即可
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          removeToken()
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 动态修改权限
    ChangeRoles({ commit, dispatch }, role) {
      return new Promise(resolve => {
        commit('SET_TOKEN', role)
        setToken(role)
        getUserInfo(role).then(response => {
          const data = response.data.responseData
          commit('SET_ROLES', data.roles)
          commit('SET_NAME', data.name)
          commit('SET_AVATAR', data.avatar)
          commit('SET_INTRODUCTION', data.introduction)
          dispatch('GenerateRoutes', data) // 动态修改权限后 重绘侧边菜单
          resolve()
        })
      })
    }
  }
}

export default user
