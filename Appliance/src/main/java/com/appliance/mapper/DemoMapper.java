package com.appliance.mapper;

import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;

public interface DemoMapper {

	DemoPojo demoLogin(DemoDto demoDto);
}
