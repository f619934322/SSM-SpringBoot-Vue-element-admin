import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/** note: Submenu only appear when children.length>=1
 *  detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 **/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']    will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if true, the page will no be cached(default is false)
    breadcrumb: false            if false, the item will hidden in breadcrumb(default is true)
  }
**/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  // 首页
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: '首页',
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
      }
    ]
  },
  // 首页/
  // 库存
  {
    path: '/inventory',
    component: Layout,
    redirect: 'inventory',
    children: [
      {
        path: 'inventoryIndex',
        component: () => import('@/views/inventory/index'),
        name: '教学用品物品一览',
        meta: { title: 'inventoryInfo', icon: 'table', noCache: true }
      }
    ]
  },
  // 库存/
  // 关于我的
  {
    path: '/aboutMe',
    component: Layout,
    redirect: '/aboutMe',
    meta: {
      title: 'aboutMe',
      icon: 'table'
    },
    children: [
      {
        path: 'myApply',
        component: () => import('@/views/apply/myApply'),
        name: '我的申请-领取',
        meta: { title: 'myApply', noCache: true }
      },
      {
        path: 'myDemand',
        component: () => import('@/views/demand/myDemand'),
        name: '我的申请-采购',
        meta: { title: 'myDemand', noCache: true }
      }
    ]
  }
  // 关于我的/
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

// 以下是异步路由，通过判断meta内的roles来控制是否显示左侧某项路由
export const asyncRouterMap = [
  // 管理员权限页
  {
    path: '/reviewForAdmin',
    component: Layout,
    redirect: '/reviewForAdmin/index',
    alwaysShow: true, // will always show the root menu
    meta: {
      title: 'pageForAdmin',
      icon: 'lock',
      roles: ['admin'] // you can set roles in root nav;['admin', 'editor']
    },
    children: [
      {
        path: 'reviewDemand',
        component: () => import('@/views/demand/index'),
        name: '教学用品物品申请采购审核清单',
        meta: {
          title: 'reviewDemand',
          roles: ['admin'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'reviewApply',
        component: () => import('@/views/apply/index'),
        name: '教学用品物品申请领取审核清单',
        meta: {
          title: 'reviewApply',
          roles: ['admin'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'userManagement',
        component: () => import('@/views/user/userManagement'),
        name: '用户管理',
        meta: {
          title: 'userManagement',
          roles: ['admin'] // or you can only set roles in sub nav
        }
      }
    ]
  },
  // 管理员权限页/
  { path: '*', redirect: '/404', hidden: true }
]
