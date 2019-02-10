import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'// progress bar style
import { getToken } from '@/utils/auth' // getToken from cookie
import Cookies from 'js-cookie'

NProgress.configure({ showSpinner: false })// NProgress Configuration

// permission judge function
function hasPermission(roles, permissionRoles) {
  if (roles.indexOf('admin') >= 0) return true // admin permission passed directly
  if (!permissionRoles) return true
  return roles.some(role => permissionRoles.indexOf(role) >= 0)
}

const whiteList = ['/login', '/auth-redirect']// no redirect whitelist

router.beforeEach((to, from, next) => {
  console.info('从' + from.path + '跳转到:' + to.path)
  NProgress.start() // start progress bar
  // 这里由于只需要前端携带cookie的JSESSIONID去后端验证，所以此处的token为状态码;假如假如后端并未存储登录状态，前端刷新后将跳转到登录界面，因为statusCode没有值
  const statusCode = getToken()
  if (statusCode === '200') { // determine if there has token
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      if (store.getters.roles.length === 0) { // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetUserInfo').then(res => { // 拉取user_info，这里将去后端查询权限字段，并返回
          console.info('拉取用户信息返回的状态码：' + res.data.statusCode)
          if (parseInt(res.data.statusCode) !== 200) {
            console.warn('获取用户信息失败，检查用户是否登录。')
            store.dispatch('FedLogOut').then(() => {
              next({ path: '/' })
            })
          }
          const roles = res.data.responseData.roles // note: roles must be a array! such as: ['editor','develop']
          store.dispatch('GenerateRoutes', { roles }).then(() => { // 根据roles权限生成可访问的路由表
            router.addRoutes(store.getters.addRouters) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
          })
        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error(err)
            next({ path: '/' })
          })
        })
      } else {
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        if (hasPermission(store.getters.roles, to.meta.roles)) {
          next()
        } else {
          next({ path: '/401', replace: true, query: { noGoBack: true }})
        }
        // 可删 ↑
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) { // 在免登录白名单，直接进入
      next()
    } else if (statusCode != null && statusCode !== '200') { // 这里判断是否有状态码保存在Cookie中，若没有则意味着从未调用后端接口登录
      Message({
        message: Cookies.get('statusMsg'),
        type: 'error',
        duration: 5 * 1000
      })
      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    } else {
      Message({
        message: '登出完成',
        type: 'info',
        duration: 5 * 1000
      })
      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
