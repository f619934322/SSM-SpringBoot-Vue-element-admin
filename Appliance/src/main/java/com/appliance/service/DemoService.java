package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;
import com.github.pagehelper.PageInfo;

public interface DemoService {

	/**
	 * 测试用service
	 * @param DemoDto
	 * @return
	 */
	@Deprecated
	public BaseResponse<DemoPojo> demoLogin(DemoDto demoDto);
	
	/**
	 * 测试分页
	 * @param demoDto
	 * @return
	 */
	public BaseResponse<PageInfo<DemoPojo>> getPageInfoDemo(DemoDto demoDto);
}
