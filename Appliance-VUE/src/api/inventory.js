import request from '@/utils/request'

// 分页显示商品列表
export function pagination(data) {
  return request({
    url: '/appliance/inventory/inventoryList',
    method: 'post',
    data
  })
}
