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

