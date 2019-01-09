package com.appliance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appliance.mapper.DemoMapper;
import com.appliance.model.BaseResponse;
import com.appliance.pojo.DemoPojo;
import com.appliance.pojo.dto.DemoDto;
import com.appliance.service.DemoService;


@Service
public class DemoServiceImpl implements DemoService{
	@Autowired
	private DemoMapper demoMapper;
	
	@Override
	public BaseResponse<DemoPojo> demoLogin(DemoDto demoDto) {
		DemoPojo demoLogin = demoMapper.demoLogin(demoDto);
		if(demoLogin != null && demoDto.getPassword().equals(demoLogin.getPassword())) {
			BaseResponse<DemoPojo> result = new BaseResponse<>(); 
			result.setResponseData(demoLogin);
			result.setStatusCode(200);
			result.setStatusMsg("SUCCESS");
			return result;
		}else {
			BaseResponse<DemoPojo> result = new BaseResponse<>(); 
			result.setResponseData(demoLogin);
			result.setStatusCode(201);
			result.setStatusMsg("FAILED");
		return result;
		}
	}
}
