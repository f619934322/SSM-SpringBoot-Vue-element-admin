package com.appliance.pojo.vo;

import com.appliance.model.PaginationBase;

import lombok.Data;

@Data
public class DemoVo extends PaginationBase{

	private String user;

	private String password;
	
}
