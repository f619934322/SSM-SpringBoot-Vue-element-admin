import request from '@/utils/request'

// 用户列表
export function pagination(data) {
  return request({
    url: '/appliance/userManagement/userList',
    method: 'post',
    data
  })
}

// 新增用户
export function insertNewUser(data) {
  return request({
    url: '/appliance/userManagement/insertUser',
    method: 'post',
    data
  })
}

// 编辑用户
export function updateUser(data) {
  return request({
    url: '/appliance/userManagement/updateUser',
    method: 'post',
    data
  })
}

// 用户单选删除
export function userDelete(data) {
  return request({
    url: '/appliance/userManagement/deleteUser',
    method: 'post',
    headers: { // 自定义Content-Type为json格式，否则此处为application/x-www-form-urlencoded，后端接收不了
      'Content-Type': 'application/json'
    },
    data
  })
}

// 用户自行修改密码
export function changePassword(data) {
  return request({
    url: '/appliance/userManagement/passwordUpdate',
    method: 'post',
    headers: { // 自定义Content-Type为json格式，否则此处为application/x-www-form-urlencoded，后端接收不了
      'Content-Type': 'application/json'
    },
    data
  })
}

