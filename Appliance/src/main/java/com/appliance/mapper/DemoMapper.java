package com.appliance.mapper;

import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;


public interface DemoMapper {

	/**
	 * 测试用Mapper
	 * @param demoDto
	 * @return
	 */
	@Deprecated
	DemoPojo demoLogin(DemoDto demoDto);
}
