import request from '@/utils/request'

// 分页显示申请列表
export function pagination(data) {
  return request({
    url: '/appliance/demand/demandList',
    method: 'post',
    data
  })
}

// 审核操作
export function reviewDemand(data) {
  return request({
    url: '/appliance/demand/reviewDemand',
    method: 'post',
    data
  })
}

// 我的采购
export function paginationForMy(data) {
  return request({
    url: '/appliance/demand/myDemand',
    method: 'post',
    data
  })
}

