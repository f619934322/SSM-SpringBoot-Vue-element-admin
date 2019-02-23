package com.appliance.mapper;

import java.util.List;

import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.InventoryVo;

public interface InventoryMapper {

	/**
	 * 获取库存一览数据
	 * @param inventoryDto
	 * @return
	 */
	public List<InventoryVo> inventoryList(InventoryDto inventoryDto);
	
	/**
	 * 删除库存物品
	 * @param id
	 */
	public void deleteInventory(InventoryDto inventoryDto);
	
	/**
	 * 对库存表进行编辑
	 * @param inventoryDto
	 */
	public void updateInventory(InventoryDto inventoryDto);
}
