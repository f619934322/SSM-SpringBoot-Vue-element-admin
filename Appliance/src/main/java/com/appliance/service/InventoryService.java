package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.InventoryVo;
import com.github.pagehelper.PageInfo;

public interface InventoryService {

	/**
	 * 物品库存列表
	 * 
	 * @param inventoryDto
	 * @return
	 */
	public BaseResponse<PageInfo<InventoryVo>> inventoryList(InventoryDto inventoryDto);

	/**
	 * 物品批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public BaseResponse<String> batchDeleteInventory(Long[] ids);

	/**
	 * 物品单选删除
	 * 
	 * @param id
	 * @return
	 */
	public BaseResponse<String> deleteInventory(Long id);

	/**
	 * 对库存表进行编辑
	 * @param inventoryDto
	 * @return
	 */
	public BaseResponse<String> updateInventory(InventoryDto inventoryDto);
}
