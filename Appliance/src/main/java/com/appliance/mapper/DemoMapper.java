package com.appliance.mapper;

import java.util.List;

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
	
	/**
	 * 测试分页
	 * @param demoDto
	 * @return
	 */
	List<DemoPojo> getPageInfoDemo(DemoDto demoDto);
}
