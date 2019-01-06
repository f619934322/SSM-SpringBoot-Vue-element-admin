package com.appliance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appliance.mapper.DemoMapper;
import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;
import com.appliance.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService{
	@Autowired
	private DemoMapper demoMapper;
	
	@Override
	public int demoLogin(DemoDto demoDto) {
		DemoPojo demoLogin = demoMapper.demoLogin(demoDto);
		if(demoDto.getPassword().equals(demoLogin.getPassword())) {
			return 1;
		}
		return 0;
	}
}
