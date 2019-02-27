package com.appliance.mapper;

import java.util.List;

import com.appliance.pojo.dto.DemandDto;
import com.appliance.pojo.vo.DemandVo;

public interface DemandMapper {

	/**
	 * 采购需求新增
	 * 
	 * @param demandDto
	 */
	public void insertNewDemand(DemandDto demandDto);

	/**
	 * 采购申请补充
	 * 
	 * @param demandDto
	 */
	public void insertSupplementDemand(DemandDto demandDto);

	/**
	 * 获取审核清单
	 * 
	 * @param demandDto
	 * @return
	 */
	public List<DemandVo> demandList(DemandDto demandDto);

	/**
	 * 采购审核操作
	 * 
	 * @param demandDto
	 */
	public void reviewDemand(DemandDto demandDto);

	/**
	 * 对初次采购完成的需求表进行更新，添加与其对应的库存表ID，以用于库存表详情查看
	 * 
	 * @param inventoryId
	 */
	public void updateDemandInventoryId(DemandDto demandDto);
}
