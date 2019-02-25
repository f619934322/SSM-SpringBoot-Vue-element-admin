package com.appliance.mapper;

import com.appliance.pojo.dto.DemandDto;

public interface DemandMapper {
	
	/**
	 * 采购需求新增
	 * @param demandDto
	 */
	public void insertNewDemand(DemandDto demandDto);

	/**
	 * 采购申请补充
	 * @param demandDto
	 */
	public void insertSupplementDemand(DemandDto demandDto);
}
