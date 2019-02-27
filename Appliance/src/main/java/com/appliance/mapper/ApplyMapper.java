package com.appliance.mapper;

import java.util.List;

import com.appliance.pojo.dto.ApplyDto;
import com.appliance.pojo.vo.ApplyVo;

public interface ApplyMapper {

	/**
	 * 获取申领表数据
	 * 
	 * @param applyDto
	 * @return
	 */
	public List<ApplyVo> applyList(ApplyDto applyDto);

	/**
	 * 新增申领物品
	 * 
	 * @param applyDto
	 */
	public void insertApply(ApplyDto applyDto);

	/**
	 * 物品申领的审核操作
	 * 
	 * @param applyDto
	 */
	public void reviewApply(ApplyDto applyDto);
	
	/**
	 * 去数据库中查出唯一一条申领数据
	 * @param id
	 * @return
	 */
	public ApplyVo selectApplyById(Long id);

}
