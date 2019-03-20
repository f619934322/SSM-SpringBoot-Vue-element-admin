package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.dto.ApplyDto;
import com.appliance.pojo.vo.ApplyVo;
import com.github.pagehelper.PageInfo;

public interface ApplyService {

	/**
	 * 获取申领表数据
	 * 
	 * @param applyDto
	 * @return
	 */
	public BaseResponse<PageInfo<ApplyVo>> applyList(ApplyDto applyDto);

	/**
	 * 新增申领物品
	 * 
	 * @param applyDto
	 * @return
	 */
	public BaseResponse<String> insertApply(ApplyDto applyDto);

	/**
	 * 审核申领
	 * 
	 * @param applyDto
	 * @return
	 */
	public BaseResponse<String> reviewApply(ApplyDto applyDto);

	/**
	 * 我的申领
	 * 
	 * @param applyDto
	 * @return
	 */
	public BaseResponse<PageInfo<ApplyVo>> myApply(ApplyDto applyDto);
}
