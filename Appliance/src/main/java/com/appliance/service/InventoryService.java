package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.InventoryVo;
import com.github.pagehelper.PageInfo;

public interface InventoryService {

	
	/**
	 * 物品库存列表
	 * @param inventoryDto
	 * @return
	 */
	public BaseResponse<PageInfo<InventoryVo>> inventoryList(InventoryDto inventoryDto);
}
