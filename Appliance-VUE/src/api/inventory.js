import request from '@/utils/request'

// 分页显示物品列表
export function pagination(data) {
  return request({
    url: '/appliance/inventory/inventoryList',
    method: 'post',
    data
  })
}

// 批量删除物品
export function bacthDeleteItem(data) {
  return request({
    url: '/appliance/inventory/batchDeleteInventory',
    method: 'post',
    data
  })
}

// 单选删除物品
export function deleteItem(data) {
  return request({
    url: '/appliance/inventory/deleteInventory',
    method: 'post',
    headers: { // 自定义Content-Type为json格式，否则此处为application/x-www-form-urlencoded，后端接收不了
      'Content-Type': 'application/json'
    },
    data
  })
}

// 编辑物品
export function updateItem(data) {
  return request({
    url: '/appliance/inventory/updateInventory',
    method: 'post',
    data
  })
}

// 获取库存单项详情
export function detailForInventory(data) {
  return request({
    url: '/appliance/inventory/inventoryDetail',
    method: 'post',
    headers: { // 自定义Content-Type为json格式，否则此处为application/x-www-form-urlencoded，后端接收不了
      'Content-Type': 'application/json'
    },
    data
  })
}

// 申请新采购
export function insertNewDemand(data) {
  return request({
    url: '/appliance/demand/insertNewDemand',
    method: 'post',
    data
  })
}

// 申请补充采购
export function supplementDemand(data) {
  return request({
    url: '/appliance/demand/insertSupplementDemand',
    method: 'post',
    data
  })
}

