package com.appliance.service;

import com.appliance.model.BaseResponse;
import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;

public interface DemoService {

	/**
	 * 测试用service
	 * @param DemoDto
	 * @return
	 */
	@Deprecated
	public BaseResponse<DemoPojo> demoLogin(DemoDto demoDto);
}
