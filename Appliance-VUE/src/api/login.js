import request from '@/utils/request'

export function loginByUsername(staffNo, password) {
  const data = {
    staffNo,
    password
  }
  return request({
    url: '/appliance/user/userLogin',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/appliance/user/userLogout',
    method: 'post'
  })
}

export function getUserInfo() {
  return request({
    url: '/appliance/user/getUserInfo',
    method: 'get'
  })
}

