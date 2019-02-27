package com.appliance.mapper;

import java.util.List;

import com.appliance.pojo.dto.InventoryDto;
import com.appliance.pojo.vo.DemandVo;
import com.appliance.pojo.vo.InventoryVo;

public interface InventoryMapper {

	/**
	 * 获取库存一览数据
	 * 
	 * @param inventoryDto
	 * @return
	 */
	public List<InventoryVo> inventoryList(InventoryDto inventoryDto);

	/**
	 * 删除库存物品
	 * 
	 * @param id
	 */
	public void deleteInventory(InventoryDto inventoryDto);

	/**
	 * 对库存表进行编辑
	 * 
	 * @param inventoryDto
	 */
	public void updateInventory(InventoryDto inventoryDto);

	/**
	 * 库存物品的详情
	 * 
	 * @param id
	 * @return
	 */
	public List<DemandVo> inventoryDetail(Long id);

	/**
	 * 新增库存
	 * 
	 * @param inventoryDto
	 */
	public void insertNewInventory(InventoryDto inventoryDto);

	/**
	 * 根据需求表ID查出一条对应的库存表数据
	 * 
	 * @param demandId
	 * @return
	 */
	public InventoryVo selectInventoryByDemandId(Long demandId);

	/**
	 * 需求标识为0（补充）时，直接更新库存表数量
	 * 
	 * @param inventoryDto
	 */
	public void updateInventoryForitemCount(InventoryDto inventoryDto);

	/**
	 * 根据库存表id查出一条唯一数据
	 * 
	 * @param id
	 * @return
	 */
	public InventoryVo selectInventoryById(Long id);
}
