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

