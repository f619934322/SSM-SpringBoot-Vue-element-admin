import request from '@/utils/request'

// 获取申领物品分页列表
export function pagination(data) {
  return request({
    url: '/appliance/apply/applyList',
    method: 'post',
    data
  })
}

// 审核操作（物品申领）
export function reviewApply(data) {
  return request({
    url: '/appliance/apply/reviewApply',
    method: 'post',
    data
  })
}

// 审核操作（物品申领）
export function insertNewApply(data) {
  return request({
    url: '/appliance/apply/insertApply',
    method: 'post',
    data
  })
}
