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
	private DemoMapper DemoMapper;
	
	@Override
	public int demoLogin(DemoDto DemoDto) {
		DemoPojo demoLogin = DemoMapper.demoLogin(DemoDto);
		if(DemoDto.getPassword().equals(demoLogin.getPassword())) {
			return 1;
		}
		return 0;
	}
}
