package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;

public interface DemandService {

	/**
	 * 采购需求新增
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<String> insertNewDemand(DemandDto demandDto);
	
	/**
	 * 采购申请补充
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<String> insertSupplementDemand(DemandDto demandDto);
}
