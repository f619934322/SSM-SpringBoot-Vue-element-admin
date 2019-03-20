package com.appliance.service;

import java.util.List;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.DemandDto;
import com.appliance.pojo.vo.DemandVo;
import com.github.pagehelper.PageInfo;

public interface DemandService {

	/**
	 * 采购需求新增
	 * 
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<String> insertNewDemand(DemandDto demandDto);

	/**
	 * 采购申请补充
	 * 
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<String> insertSupplementDemand(DemandDto demandDto);

	/**
	 * 获取审核清单
	 * 
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<PageInfo<DemandVo>> demandList(DemandDto demandDto);

	/**
	 * 采购审核操作
	 * 
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<String> reviewDemand(DemandDto demandDto);

	/**
	 * 导出需求表
	 * 
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<List<DemandVo>> excelDemand(DemandDto demandDto);

	/**
	 * 我的采购
	 * 
	 * @param demandDto
	 * @return
	 */
	public BaseResponse<PageInfo<DemandVo>> myDemand(DemandDto demandDto);
}
